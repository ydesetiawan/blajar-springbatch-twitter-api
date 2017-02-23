package com.yd.config;

import javax.servlet.http.HttpSessionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.yd.security.CustomAuthenticationSuccessHandler;

/**
 * @author edys
 * @version 1.0, Jan 20, 2017
 * @since
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService customUserDetailsService;

	@Autowired
	private SessionRegistry sessionRegistry;

	@Value("${login.maximum_sessions}")
	private int maximumSessions;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(customUserDetailsService).passwordEncoder(
				passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
				.antMatchers("/data/**", "/dist/**", "/less/**", "/vendor/**")
				.permitAll();

		http.formLogin().loginPage("/login").defaultSuccessUrl("/")
				.failureUrl("/login?error=failure").permitAll().and()
				.authorizeRequests().antMatchers("/login").permitAll();
		http.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.permitAll();
		http.authorizeRequests().antMatchers("/support/switchUser")
        .hasAnyRole("USER");
		http.authorizeRequests().antMatchers("/", "/**").hasRole("USER");
		http.addFilter(customSwitchUserFilter());

		http.sessionManagement().maximumSessions(maximumSessions)
				.expiredUrl("/login?error=expired")
				.sessionRegistry(sessionRegistry).and()
				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
	}

	@Bean
	public SwitchUserFilter customSwitchUserFilter() {
		SwitchUserFilter switchUserFilter = new SwitchUserFilter();
		switchUserFilter.setUserDetailsService(customUserDetailsService);
		switchUserFilter.setSwitchUserUrl("/support/switchUser");
		switchUserFilter.setExitUserUrl("/support/switchUserExit");
		switchUserFilter
				.setSuccessHandler(customAuthenticationSuccessHandler());
		switchUserFilter.setUsernameParameter("username");
		return switchUserFilter;
	}

	@Bean
	public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
		return new CustomAuthenticationSuccessHandler();
	}

	@Bean
	HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher() {
			@Override
			public void sessionCreated(HttpSessionEvent event) {
				super.sessionCreated(event);
			}

			@Override
			public void sessionDestroyed(HttpSessionEvent event) {
				super.sessionDestroyed(event);
			}
		};
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();

	}
}