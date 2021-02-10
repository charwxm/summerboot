package com.tada.summerboot.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	public void configure(WebSecurity web) {
		//DO NOT EDIT
		//do not authenticate these APIs
		web.ignoring()
				.antMatchers("/assets/**")
				.antMatchers("/**")

				.antMatchers("/products/**")
				.antMatchers("/posts/**")
				.antMatchers("/products/json/**") //is this necessary?
				.antMatchers("/users/**");
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//DO NOT EDIT
		http
				.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/product").hasRole("ADMIN")
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
				.logout()
				.permitAll();
	}

	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		UserDetails user =
				User.withDefaultPasswordEncoder()
						.username("admin")
						.password("admin")
						.roles("ADMIN")
						.build();

		return new InMemoryUserDetailsManager(user);
	}
}
