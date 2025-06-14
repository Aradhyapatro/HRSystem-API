package com.HRSystem.Project.Config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.HRSystem.Project.Service.CustomUserDetailsService;
import com.HRSystem.Project.Service.JwtService;
import jakarta.servlet.http.HttpServletRequest;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	@Autowired
    private JwtService jwtService;
	@Autowired
    private CustomUserDetailsService userDetailsService;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    JwtFilter jwtFilter = new JwtFilter(jwtService, userDetailsService);

	    http.csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers(
	                    "/api/auth/**",                         // your public APIs
	                    "/swagger-ui/**",                       // Swagger UI
	                    "/v3/api-docs/**",                      // OpenAPI JSON docs
	                    "/swagger-ui.html",                     // Swagger entry point
	                    "/api-docs/**" 
	                ).permitAll()
	            .anyRequest().authenticated()
	        )
	        .sessionManagement(session -> session
	            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	        )
	        .addFilterBefore(jwtFilter, BasicAuthenticationFilter.class)
	        .cors().configurationSource(new CorsConfigurationSource() {
	            @Override
	            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
	                CorsConfiguration cfg = new CorsConfiguration();
	                cfg.setAllowedOrigins(Arrays.asList("*"));
	                cfg.setAllowedMethods(Collections.singletonList("*"));
	                cfg.setAllowCredentials(true);
	                cfg.setAllowedHeaders(Collections.singletonList("*"));
	                cfg.setExposedHeaders(Arrays.asList("Authorization", "Content-Disposition"));
	                cfg.setMaxAge(3600L);
	                return cfg;
	            }
	        });

	    return http.build();
	}

	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }
}