package com.iglo.exam.liber.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iglo.exam.liber.error.ErrorMessageResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        HttpStatus httpStatus = HttpStatus.FORBIDDEN;

        ErrorMessageResponse<Object> errorMessageResponse = ErrorMessageResponse.builder()
                .status(httpStatus)
                .message("Access Denied")
                .errors("You don't have permission to access this resource")
                .build();

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(errorMessageResponse));
    }
}

