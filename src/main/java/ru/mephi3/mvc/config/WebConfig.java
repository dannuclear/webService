package ru.mephi3.mvc.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.RequiredArgsConstructor;
import ru.mephi3.reposotory.PermissionRepository;
import ru.mephi3.reposotory.RoleRepository;
import ru.mephi3.service.EquipmentTypeService;
import ru.mephi3.web.method.support.DataTablesRequestArgumentResolver;
import ru.mephi3.webService.formatter.EquipmentTypeFormatter;
import ru.mephi3.webService.formatter.ObjectClassToJSTreeNodeConverter;

@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackages = {"ru.mephi3.mvc.controller", "ru.mephi3.rest.controller", "ru.mephi3.rest.hateoas"})
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {// implements WebFluxConfigurer{
    @Value("${rest.guide.default-page-size}")
    private Integer defaultPageSize;
    private final WebMvcProperties mvcProperties;

    @Bean
    public ObjectMapper objectMapper() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(mvcProperties.getFormat().getDate());
        DateTimeFormatter localDataTimeFormatter = DateTimeFormatter.ofPattern(mvcProperties.getFormat().getDateTime());

        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dtf));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dtf));

        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(localDataTimeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(localDataTimeFormatter));
        mapper.registerModule(javaTimeModule);

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public RouterFunction<ServerResponse> apiV1Routing(PermissionRepository permissionRepository, RoleRepository roleRepository) {
        return RouterFunctions.route().path("/api/v1", apiBuilder -> apiBuilder.path("/permissions", b -> b.GET("", req -> {
            return ServerResponse.ok().body(permissionRepository.findAll());
        }).GET("/{permissionId}", req -> {
            Integer permissionId = Integer.valueOf(req.pathVariable("permissionId"));
            return ServerResponse.ok().body(permissionRepository.findById(permissionId).get());
        })).path("/roles", b -> b.GET("", req -> {
            return ServerResponse.ok().body(roleRepository.findAll());
        }).GET("/{roleId}", req -> {
            Integer roleId = Integer.valueOf(req.pathVariable("roleId"));
            return ServerResponse.ok().body(roleRepository.findById(roleId).get());
        }))).build();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//		PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
//		resolver.setFallbackPageable(PageRequest.of(0, defaultPageSize));
//		resolvers.add(resolver);
        DataTablesRequestArgumentResolver dtResolver = new DataTablesRequestArgumentResolver();
        resolvers.add(dtResolver);
    }

    //@Bean
//	public ConfigurableWebBindingInitializer getConfigurableWebBindingInitializer(
//			FormattingConversionService mvcConversionService, Validator mvcValidator) {
//
//		ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
//		initializer.setConversionService(mvcConversionService);
//		initializer.setValidator(mvcValidator);
//		initializer.setAutoGrowNestedPaths(false);
//		MessageCodesResolver messageCodesResolver = getMessageCodesResolver();
//		if (messageCodesResolver != null) {
//			initializer.setMessageCodesResolver(messageCodesResolver);
//		}
//		return initializer;
//	}

    @Bean
    public ObjectClassToJSTreeNodeConverter ObjectClassToJSTreeNodeConverter() {
        return new ObjectClassToJSTreeNodeConverter();
    }

    @Bean
    public EquipmentTypeFormatter equipmentTypeFormatter(EquipmentTypeService equipmentTypeService) {
        return new EquipmentTypeFormatter(equipmentTypeService);
    }
}
