package com.example.UserMicroserviceAPI.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//CUSTOM HANDLING OF UNAUTHORIZED REQUEST
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        handleException(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", authException.getMessage(), request.getServletPath());
    }

    public void handleIllegalArgumentException(HttpServletResponse response, String message, String path) throws IOException {
        handleException(response, HttpServletResponse.SC_BAD_REQUEST, "Bad Request", message, path);
    }

    private void handleException(HttpServletResponse response, int status, String error, String message, String path) throws IOException {
        logger.error("{} error: {}", error, message);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status);

        final Map<String, Object> body = new HashMap<>();
        body.put("status", status);
        body.put("error", error);
        body.put("message", message);
        body.put("path", path);

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
