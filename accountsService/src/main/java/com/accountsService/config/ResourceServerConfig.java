package com.accountsService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class ResourceServerConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/accounts/withdrawBalance/**").authenticated()
                .mvcMatchers("/accounts/addBalance/**").authenticated()
                .mvcMatchers("/accounts/addBalance/**").hasAuthority("SCOPE_accounts.client")
                .mvcMatchers("/accounts/withdrawBalance/**").hasAuthority("SCOPE_accounts.client")
                .and()
                .oauth2ResourceServer()
                .jwt();

        return http.build();
    }
}