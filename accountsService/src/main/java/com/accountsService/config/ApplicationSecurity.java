package com.accountsService.config;

import com.accountsService.jwt.JwtTokenFilter;
import com.accountsService.model.ERole;
import com.accountsService.model.User;
import com.accountsService.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;


@Configuration
public class ApplicationSecurity {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationSecurity.class);

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {

            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                if(email.contains("user")) return new User(1,"user@user.com","$2a$12$/f3bTQhL.MGgo0u03yJbp.fokMl6Mb1TTvXsf5H6S.kTRU/Zr/AJ2",ERole.CLIENTE);
                else return new User(2,"admin@admin.com","$2a$12$/f3bTQhL.MGgo0u03yJbp.fokMl6Mb1TTvXsf5H6S.kTRU/Zr/AJ2",ERole.GESTOR);
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig
    ) throws Exception {
        logger.info("Entra authenticationManager!!!!");
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // http.authenticationProvider(authProvider()); // can be commented

        http
                .authorizeHttpRequests((requests) -> requests
                        .antMatchers("/auth/login",
                                "/docs/**",
                                "/users",
                                "/h2-ui/**",
                                "/configuration/ui",
                                "/swagger-resources/**",
                                "/configuration/security",
                                "/swagger-ui.html",
                                "/webjars/**"
                        ).permitAll() // HABILITAR ESPACIOS LIBRES
//                        .antMatchers("/**").permitAll() // BARRA LIBRE
//                        .antMatchers("/products/**").hasAuthority(ERole.USER.name())
                        .antMatchers(HttpMethod.PUT, "/accouts/addBalance/**").hasAnyAuthority(ERole.CLIENTE.name())
                                .antMatchers(HttpMethod.PUT, "/accouts/withdrawBalance/**").hasAnyAuthority(ERole.CLIENTE.name())
                        .antMatchers(HttpMethod.DELETE,"/accounts/delete/**").hasAnyAuthority(ERole.GESTOR.name())
                        .antMatchers(HttpMethod.POST,"/accounts/**").hasAnyAuthority(ERole.GESTOR.name())
                        .anyRequest().authenticated()
                );

        http.headers(headers ->
                headers.frameOptions(frameOptionsConfig -> frameOptionsConfig.sameOrigin())
        );

        http.exceptionHandling((exception) -> exception.authenticationEntryPoint(
                (request, response, ex) -> {
                    response.sendError(
                            HttpServletResponse.SC_UNAUTHORIZED,
                            ex.getMessage()
                    );
                }
        ));

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}