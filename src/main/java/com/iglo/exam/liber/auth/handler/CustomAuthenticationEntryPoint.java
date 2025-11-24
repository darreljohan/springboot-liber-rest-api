package com.iglo.exam.liber.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iglo.exam.liber.error.ErrorMessageResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("delegatedAuthenticationEntryPoint")
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException)
        throws IOException, ServletException{
            System.out.println("CustomAuthenticationEntryPoint: " + authException.getMessage());

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            ErrorMessageResponse<Object> errorMessageResponse = ErrorMessageResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED)
                    .message("Username or password is wrong")
                    .errors(authException.getMessage())
                    .build();

            response.getWriter().write(objectMapper.writeValueAsString(errorMessageResponse));
        }

}
