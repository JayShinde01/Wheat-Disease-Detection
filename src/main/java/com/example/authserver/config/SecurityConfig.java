package com.example.authserver.config;

import com.example.authserver.jwt.JwtAuthenticationFilter;
import com.example.authserver.oauth.OAuth2SuccessHandler;
import java.net.HttpRetryException;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.w3c.dom.css.CSSFontFaceRule;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final OAuth2SuccessHandler OAuth2SuccessHandler;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	 @Bean
	    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
	            throws Exception {

	        return configuration.getAuthenticationManager();
	    }

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http)
	        throws Exception {

	    http

	            .csrf(csrf -> csrf.disable())

	            .authorizeHttpRequests(auth -> auth

	                    .requestMatchers(
	                    		"/api/auth/**",
	    						"/api/swagger-ui/index.html",
	    						"/api/swagger-ui/**"
	                    ).permitAll()

	                    .anyRequest().authenticated()

	            )

	            .oauth2Login(oauth -> oauth

	                    .successHandler(OAuth2SuccessHandler)

	            )
	            .addFilterBefore(
	                    jwtAuthenticationFilter,
	                    UsernamePasswordAuthenticationFilter.class
	            )
	            .httpBasic(httpBasic -> httpBasic.disable());

	    return http.build();

	}
}
