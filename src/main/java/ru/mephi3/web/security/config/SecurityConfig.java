package ru.mephi3.web.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
			.antMatchers("/private/**").authenticated()//.hasAnyRole("ADMIN", "USER")
//			.anyRequest().authenticated()
			.and()
		.httpBasic()
			.and()
		.csrf()
			.disable()
		.cors()
			.and().sessionManagement()
//			.invalidSessionUrl("/expired")
			.maximumSessions(1).expiredUrl("/expired")
			.maxSessionsPreventsLogin(false)
			.sessionRegistry(getSessionRegistry()).and()
			.and()
//		.exceptionHandling().accessDeniedPage("/accessdenied")
//			.and()
		.logout().deleteCookies("JSESSIONID").and()
		.formLogin()
			.loginPage("/login")
			.permitAll()
			.defaultSuccessUrl("/private/main");
	}
	
	
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().withUser("admin").password(passwordEncoder().encode("secret")).roles("USER","ADMIN");
//	}
	
	@Bean
	public SessionRegistry getSessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
