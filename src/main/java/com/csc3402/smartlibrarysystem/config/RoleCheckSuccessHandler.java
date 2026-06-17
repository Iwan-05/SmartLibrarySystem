package com.csc3402.smartlibrarysystem.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RoleCheckSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String selectedRole = request.getParameter("role");

        boolean roleMatches = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(actualRole -> actualRole.equals("ROLE_" + selectedRole));

        if (!roleMatches) {
            request.getSession().invalidate();
            response.sendRedirect("/login?roleError=true");
            return;
        }

        if ("LIBRARIAN".equals(selectedRole)) {
            response.sendRedirect("/admin");
        } else {
            response.sendRedirect("/dashboard");
        }
    }
}