package com.kingtest.configuration.filters;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.kingtest.crypto.JwtService;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Order(2)
@Slf4j
public class AuthorizationRequestValidation implements Filter {
    private JwtService jwtService;

    private List<String> acceptableRoles;
    private final static String BEARER = "Bearer ";
    private Set<MethodEndpointRolesTernion> blockedRequestTernions = new HashSet<>();

    public AuthorizationRequestValidation(List<String> acceptableRoles) {
        this.acceptableRoles = acceptableRoles;
    }

    public AuthorizationRequestValidation(Set<MethodEndpointRolesTernion> blockedRequestTernions) {
        this.blockedRequestTernions = blockedRequestTernions;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Retrieve the Spring application context from the filter configuration
        ApplicationContext context = WebApplicationContextUtils
                .getRequiredWebApplicationContext(filterConfig.getServletContext());
        // Inject the ClientService bean from the application context
        jwtService = context.getBean(JwtService.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String authorization = req.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization)) {
            setupUnauthorizedErrorResponse(httpResponse);
            return;
        }

        authorization = authorization.replace(BEARER, "");
        try {
            if (!jwtService.doesJwtHasRequiredRole(authorization, acceptableRoles)) {
                setupUnauthorizedErrorResponse(httpResponse);
                return;
            }

            if (requestIsBlockedByMethod(req, authorization)) {
                setupUnauthorizedErrorResponse(httpResponse);
                return;
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            setupUnauthorizedErrorResponse(httpResponse);
            return;
        }
      
        chain.doFilter(request, response);
    }

    private void setupUnauthorizedErrorResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Usuário não tem permissão para acessar esse endpoint."); // Your error message
    }

    private boolean requestIsBlockedByMethod(HttpServletRequest httpRequest, String authorization) {
        String requestURI = httpRequest.getRequestURI();
        String requestMethod = httpRequest.getMethod();

        for (MethodEndpointRolesTernion blockerTernion : blockedRequestTernions) {
            if(methodIsForbiddenForGivenEndpoint(requestURI, requestMethod, blockerTernion)) {
                return true;
            }
        }

        return false;
    }

    private Boolean methodIsForbiddenForGivenEndpoint(String requestURI, String requestMethod, MethodEndpointRolesTernion blockerTernion){
        return requestURI.startsWith(blockerTernion.endpoint()) && 
                blockerTernion.method().equals(requestMethod) &&
                jwtService.doesJwtHasRequiredRole(requestMethod, acceptableRoles);
    }
}
