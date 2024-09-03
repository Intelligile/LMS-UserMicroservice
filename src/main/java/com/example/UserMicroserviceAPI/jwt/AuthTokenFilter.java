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
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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
    private UserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    //custom filter
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.debug("AuthTokenFi1ter called for URI : {}", request.getRequestURI());
        try {
            //extract jwt token
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                //token
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());//userDetails.getAuthorities() the roles
                logger.debug("Roles from JWT : {}" , userDetails.getAuthorities());
                //enhancing the authentication object (token) with additional details from the request (session id ,,,)
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //setting the security context , authenticating the user for the duration of the request
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }catch (Exception e ){
            logger.error("cannot set user authentication :{}",e);
        }
        //continue the filter chain(since we've added our own custom filter) as usual
        filterChain.doFilter(request,response);
    }

    private String parseJwt(HttpServletRequest request){
        String jwt= jwtUtils.getJwtFromHeader(request);
        logger.debug("AuthTokenFilter java:{}",jwt);
        return jwt;
    }
}