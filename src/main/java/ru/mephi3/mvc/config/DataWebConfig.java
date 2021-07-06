package ru.mephi3.mvc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
public class DataWebConfig implements WebMvcConfigurer {

//	@Bean
//	public StringToUserConverter stringToUserConverter() {
//		return new StringToUserConverter();
//	}

}
