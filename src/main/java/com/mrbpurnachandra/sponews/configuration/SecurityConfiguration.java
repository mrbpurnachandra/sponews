package com.mrbpurnachandra.sponews.configuration;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorize -> {
            authorize.requestMatchers("/article/create").authenticated();
            authorize.requestMatchers("/", "/article/{articleId}", "/author/show").permitAll();
            authorize.anyRequest().authenticated(); 
        })
        .oauth2Login(customizer -> {
            customizer.loginPage("/");  
            customizer.defaultSuccessUrl("/"); 
        })
        .logout(customizer -> customizer.logoutSuccessUrl("/"))
        .build(); 
    }
}