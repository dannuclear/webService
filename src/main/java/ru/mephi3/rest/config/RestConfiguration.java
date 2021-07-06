package ru.mephi3.rest.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

//@Configuration
//@EnableWebMvc
//@EnableSpringDataWebSupport
//@Import(value = { SpringDataWebConfiguration.class })
//@Import(value = { RepositoryRestMvcConfiguration.class })
public class RestConfiguration implements WebMvcConfigurer /* implements RepositoryRestConfigurer */ {
//	@Value("${rest.guide.default-page-size}")
//	private Integer defaultPageSize;
//
//	@Bean
//	public ObjectMapper objectMapper() {
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.registerModule(new JavaTimeModule());
//		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//		return mapper;
//	}
//
//	@Override
//	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//		PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
//		resolver.setFallbackPageable(PageRequest.of(0, defaultPageSize));
//		resolvers.add(resolver);
//		WebMvcConfigurer.super.addArgumentResolvers(resolvers);
//	}
}
