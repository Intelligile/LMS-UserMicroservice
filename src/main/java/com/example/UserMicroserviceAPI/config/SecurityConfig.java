
// SecurityConfig.java
package com.example.UserMicroserviceAPI.config;

import com.example.UserMicroserviceAPI.jwt.AuthEntryPointJwt;
import com.example.UserMicroserviceAPI.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    DataSource dataSource;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select `username`, `password`,`enabled` from user where `username`=?")
                .authoritiesByUsernameQuery(
                        "select u.username, a.authority " +
                                "from user u " +
                                "join user_authorities ua on u.id = ua.user_id " +
                                "join authority a on ua.authority_id = a.id " +
                                "where u.username = ?"
                )
                .passwordEncoder(passwordEncoder());
    }


    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
        .cors(cors -> cors // Enable CORS
        .configurationSource(request -> {
            var corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowedOrigins(List.of("http://localhost", "http://127.0.0.1")); // Accept any localhost
            corsConfiguration.setAllowedOriginPatterns(List.of("http://localhost:*", "http://127.0.0.1:*")); // Wildcard support for localhost with any port
            corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            corsConfiguration.setAllowedHeaders(List.of("*"));
            corsConfiguration.setAllowCredentials(true);
            return corsConfiguration;
        }))
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .anyRequest().authenticated())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .headers(headers -> {
                headers.frameOptions(frameOptions -> frameOptions.sameOrigin());
                headers.contentSecurityPolicy("default-src 'self'");
                headers.cacheControl(cacheControl -> cacheControl.disable());
                headers.xssProtection(xss -> xss.disable()); // Consider enabling XSS protection
                headers.httpStrictTransportSecurity(hsts -> hsts.disable()); // Consider enabling HSTS if using HTTPS
            })
            .csrf(csrf -> csrf.disable())
            .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    
        return http.build();
    }
    


    @Bean
    public UserDetailsService userDetailsService() {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager();
        manager.setDataSource(dataSource);
        manager.setUsersByUsernameQuery("select `username`, `password`,`enabled` from user where `username`=?");
        manager.setAuthoritiesByUsernameQuery(
                "select u.username, a.authority " +
                        "from user u " +
                        "join user_authorities ua on u.id = ua.user_id " +
                        "join authority a on ua.authority_id = a.id " +
                        "where u.username = ?"
        );
        return manager;
    }

    // @Bean
    // public CommandLineRunner initData(UserDetailsService userDetailsService) {
    //     return args -> {
    //         JdbcUserDetailsManager manager = (JdbcUserDetailsManager) userDetailsService;
    //         UserDetails user1 = User.withUsername("fadelzohbi").password(passwordEncoder().encode("12345")).roles("USER").build();
    //         UserDetails admin = User.withUsername("oussamaziadeh").password(passwordEncoder().encode("adminPass")).roles("ADMIN").build();
    //          manager.createUser(user1);
    //          manager.createUser(admin);
    //     };
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
}
