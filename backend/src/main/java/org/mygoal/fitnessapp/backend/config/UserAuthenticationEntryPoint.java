package org.mygoal.fitnessapp.backend.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.mygoal.fitnessapp.backend.dto.ErrorDto;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles unauthorized access attempts by returning a JSON response with an error message.
 */
@Component
public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Invoked when an unauthenticated user attempts to access a secured resource.
     *
     * @param request       the {@link HttpServletRequest} that resulted in an authentication exception
     * @param response      the {@link HttpServletResponse} to send the error response
     * @param authException the {@link AuthenticationException} that was thrown
     * @throws IOException      if an input or output exception occurs
     * @throws ServletException if a servlet-specific error occurs
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        OBJECT_MAPPER.writeValue(response.getOutputStream(), new ErrorDto("Unauthorized path"));
    }
}
