package com.example.UserMicroserviceAPI.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.UserMicroserviceAPI.service.CustomUserDetailsService;

import java.io.IOException;

//CUSTOM FILTER
//AUTO INTERCEPT ANY REQUEST TO CHECK IF ITS AUTHENTICATED

//OncePerRequestFilter to make sure that this executes once per request
@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    //field injection, automatically autowiring jwtUtils , userDetailsService instances
    @Autowired
    private JwtUtils jwtUtils;
        @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    //custom filter
    @Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    logger.debug("AuthTokenFilter called for URI: {}", request.getRequestURI());
    try {
        // Extract JWT token
        String jwt = parseJwt(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            logger.debug("Extracted username from JWT: {}", username);
            
            // Determine if the user is a licensor using the new method
            boolean isLicensor = customUserDetailsService.isLicensorUser(username); // Call isLicensorUser
            logger.debug("Extracted user type from function: {}", isLicensor);
            // Load user details based on the type
            UserDetails userDetails = customUserDetailsService.loadUserByUsernameBasedOnType(username, isLicensor);
            if (userDetails == null) {
                logger.warn("UserDetails not found for username: {}", username);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
                return; // Stop processing if user not found
            }

            // Create authentication token
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
            
            logger.debug("Roles from JWT: {}", userDetails.getAuthorities());

            // Enhance the authentication object with additional details from the request
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // Set the security context, authenticating the user for the duration of the request
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("Authentication set successfully for user: {}", username);
        }

    } catch (Exception e) {
        logger.error("Cannot set user authentication: {}", e.getMessage(), e);
    }
    // Continue the filter chain as usual
    filterChain.doFilter(request, response);
}

   

    private String parseJwt(HttpServletRequest request){
        String jwt= jwtUtils.getJwtFromHeader(request);
        logger.debug("AuthTokenFilter java:{}",jwt);
        return jwt;
    }
}