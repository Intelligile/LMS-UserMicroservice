// SecurityConfig.java
package com.example.UserMicroserviceAPI.config;

import com.example.UserMicroserviceAPI.jwt.AuthEntryPointJwt;
import com.example.UserMicroserviceAPI.jwt.AuthTokenFilter;
import com.example.UserMicroserviceAPI.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    DataSource dataSource;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
    @Bean
    public JdbcUserDetailsManager licensorUserDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager licensorUserDetailsManager = new JdbcUserDetailsManager();
        licensorUserDetailsManager.setDataSource(dataSource);
    
        // Custom queries for LicensorUser table
        String userByUsernameQuery = "SELECT username, password, enabled FROM LicensorUser WHERE username = ?";
        String authoritiesByUsernameQuery = "SELECT u.username, a.authority FROM LicensorUser u " +
                                            "JOIN LicensorUser_LicensorAuthorities ua ON u.id = ua.user_id " +
                                            "JOIN LicensorAuthority a ON ua.authority_id = a.id " +
                                            "WHERE u.username = ?";

        // Override default queries
        licensorUserDetailsManager.setUsersByUsernameQuery(userByUsernameQuery);
        licensorUserDetailsManager.setAuthoritiesByUsernameQuery(authoritiesByUsernameQuery);

        return licensorUserDetailsManager;
    }

    @Bean
public JdbcUserDetailsManager userDetailsManager(DataSource dataSource) {
    JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager();
    userDetailsManager.setDataSource(dataSource);

    // Queries for the default user table (assuming the standard Spring Security schema)
    String userByUsernameQuery = "SELECT username, password, enabled FROM user WHERE username = ?";
    String authoritiesByUsernameQuery = "SELECT username, authority FROM authorities WHERE username = ?";

    userDetailsManager.setUsersByUsernameQuery(userByUsernameQuery);
    userDetailsManager.setAuthoritiesByUsernameQuery(authoritiesByUsernameQuery);

    return userDetailsManager;
}

    
   


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors // Enable CORS
                .configurationSource(request -> {
                    var corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(List.of("http://localhost", "http://127.0.0.1"));
                    corsConfiguration.setAllowedOriginPatterns(List.of("http://localhost:*", "http://127.0.0.1:*"));
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/licensor/api/auth/**", "/api/auth/**" , "/api/products/**","/api/regions/**" , "/licensor/api/authorities/**").permitAll()
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .anyRequest().authenticated())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .headers(headers -> {
                headers.frameOptions(frameOptions -> frameOptions.sameOrigin());
                headers.contentSecurityPolicy("default-src 'self'");
                headers.cacheControl(cacheControl -> cacheControl.disable());
                headers.xssProtection(xss -> xss.disable());
                headers.httpStrictTransportSecurity(hsts -> hsts.disable());
            })
            .csrf(csrf -> csrf.disable())
            .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
            

        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedHandler() {
        return (request, response, authException) -> {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }

  
}
