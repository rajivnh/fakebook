package com.tutorial.config;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tutorial.filter.AuthFilter; 

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
	@Autowired
	AuthFilter authFilter;
	
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) -> authz
                .requestMatchers("/authenticate/**").permitAll()
                .requestMatchers(toH2Console()).permitAll()     
                .anyRequest().authenticated());
        
        http.sessionManagement((session) ->  session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
        http.csrf(csrf -> csrf.disable());
        http.headers(header -> header.frameOptions(config -> config.sameOrigin()));
        
        return http.build();
    }
    
    @Bean
    public WebSecurityCustomizer apiStaticResources() {
        return (web) -> web.ignoring()
        		.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
        		.and().ignoring()
        		.requestMatchers("/*.html");
    }
}