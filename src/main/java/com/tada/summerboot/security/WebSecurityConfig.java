package com.tada.summerboot.security;

import com.tada.summerboot.component.SuccessHandler;
import com.tada.summerboot.service.ProductServiceImpl;
import com.tada.summerboot.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private SuccessHandler successHandler;

	@Autowired
	UserServiceImpl user_service_implementation;

	@Override
	public void configure(WebSecurity web) {
		// DO NOT EDIT
		// do not authenticate these APIs
		web.ignoring()
				.antMatchers("/assets/**")

				.antMatchers("/css/**")
				.antMatchers("/js/**")
				.antMatchers("/images/**")
				.antMatchers("/user-photos/**")
				.antMatchers("/products/**")
				.antMatchers("/posts/**")
				.antMatchers("/products/json/**") // is this necessary?
				.antMatchers("/users/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// DO NOT EDIT
		http.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/register").permitAll()
				.antMatchers("/contact").permitAll()
				.antMatchers("/about").permitAll()
				.antMatchers("/delivery").permitAll()
				.antMatchers("/advertising").permitAll()
				.antMatchers("/affiliates").permitAll()
				.antMatchers("/checkout_success").permitAll()
				.antMatchers("/exchanges_returns").permitAll()
				.antMatchers("/faq").permitAll()
				.antMatchers("/payment").permitAll()
				.antMatchers("/privacy_policy").permitAll()
				.antMatchers("/shopping_cart").permitAll()
				.antMatchers("/terms_and_conditions").permitAll()


				.antMatchers("/every-users").permitAll()
				.antMatchers("/shop_all").permitAll()
				.antMatchers(HttpMethod.POST, "/user/new").permitAll()
				.antMatchers("/product").hasRole("ADMIN")
				.anyRequest()
				.authenticated().and().formLogin().loginPage("/login").permitAll().successHandler(successHandler).and()
				.logout().permitAll();
	}

	@Bean
	@Override
	public UserDetailsService userDetailsService() {

		// Get all the users in database
		// Use the service instead of repo.
		List<com.tada.summerboot.model.User> users = user_service_implementation.getAllUsers();

		// Prepare an ArrayList for the InMemoryUserDetailsManager method at the end of
		// this function
		ArrayList<UserDetails> list = new ArrayList<UserDetails>();

		// Iterate (go through one by one) and build a UserDetails for this app
		for (int i = 0; i < users.size(); i++) {

			// Create a UserDetails instance but set it based on the user in database
			UserDetails user = User.withDefaultPasswordEncoder().username(users.get(i).getUsername())
					.password(users.get(i).getPassword()).roles("ADMIN").build();
			// Add that instance to the list
			list.add(user);
		}

		// Have at least one admin user for developer to login
		UserDetails admin = User.withDefaultPasswordEncoder().username("admin").password("admin").roles("ADMIN")
				.build();
		list.add(admin);

		System.out.println(list);
		return new InMemoryUserDetailsManager(list);
	}
}
