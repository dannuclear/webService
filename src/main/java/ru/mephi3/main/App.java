package ru.mephi3.main;

import java.io.IOException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@SpringBootApplication
@EnableJpaRepositories("ru.mephi3.reposotory")
@EntityScan("ru.mephi3.domain")
@ComponentScan(basePackages = { "ru.mephi3.service.impl", "ru.mephi3.main", "ru.mephi3.mvc.config",
		"ru.mephi3.web.security.config" })
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableHypermediaSupport(type = HypermediaType.HAL_FORMS)
@ConfigurationPropertiesScan
public class App extends SpringBootServletInitializer // AbstractReactiveWebInitializer //
{
	
//	@Override
//	public void onStartup(ServletContext servletContext) throws ServletException {
//		servletContext.addListener(HttpSessionEventPublisher.class);
//		super.onStartup(servletContext);
//	}
	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
	    return new HttpSessionEventPublisher();
	}

	public static void main(String[] args) throws IOException {
		System.out.println("Hello World!" + Thread.currentThread());

//		org.springframework.web.SpringServletContainerInitializer
//        Flux<Integer> publisher = Flux.range(1, 1000).delayElements(Duration.ofSeconds(3));
//        Mono.just(25).subscribe(e->{
//        	System.out.println(Thread.currentThread().toString() + e);
//        });
//        publisher.log().subscribe(e->{
//        	System.out.println(Thread.currentThread().toString() + e);
//        });
//        
//        publisher.log().subscribe(e->{
//        	System.out.println(Thread.currentThread().toString() + e);
//        });
//        
//        System.out.println("end");
//        System.in.read();
	}

//	@Override
//	protected Class<?>[] getConfigClasses() {
//		return new Class[] { WebConfig.class };
//	}

}
