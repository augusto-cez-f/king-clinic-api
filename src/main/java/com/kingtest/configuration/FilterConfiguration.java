package com.kingtest.configuration;

import java.util.List;
import java.util.Set;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.kingtest.configuration.filters.AuthorizationRequestValidation;
import com.kingtest.configuration.filters.MethodEndpointRolesTernion;
import com.kingtest.configuration.filters.RequestResponseLoggingFilter;

@Configuration
public class FilterConfiguration {
    /**
     * /consultas     -> Allowed to all; M can't post
     * /medicos       -> Allowed to all; M can't post
     * /recepcionista -> C only
     * /pacientes     -> Allowed to all; M can't post
     * /signin        -> Doesn't require authorization
     */


    private List<String> mRole = List.of("M");
    private List<String> mrRole = List.of("M", "R");
    private List<String> endpointAllowedToAll = List.of("/medicos", "/medicos/*", 
        "/pacientes", "/pacientes/*", "/consultas", "/consultas/*", 
        "/recepcionistas", "/recepcionistas/*", "/pacientes", "/pacientes/*");

    private Set<MethodEndpointRolesTernion> blockedMethodEndpointRolesTernion = Set.of(
        new MethodEndpointRolesTernion(HttpMethod.POST.toString(), "/medicos", mRole),
        new MethodEndpointRolesTernion(HttpMethod.POST.toString(), "/pacientes", mRole),
        new MethodEndpointRolesTernion(HttpMethod.POST.toString(), "/consultas", mRole),
        new MethodEndpointRolesTernion(HttpMethod.GET.toString(), "/recepcionistas/*", mrRole),
        new MethodEndpointRolesTernion(HttpMethod.POST.toString(), "/recepcionistas", mrRole)
    );


    @Bean
    public FilterRegistrationBean<RequestResponseLoggingFilter> requestResponseFilter() {
        FilterRegistrationBean<RequestResponseLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestResponseLoggingFilter()); // Provide parameter values
        registrationBean.addUrlPatterns("/*"); // Specify URL patterns to filter
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<AuthorizationRequestValidation> authorizationFilterForAll() {
        FilterRegistrationBean<AuthorizationRequestValidation> registrationBean = new FilterRegistrationBean<>();
        AuthorizationRequestValidation authorizationFilter = new AuthorizationRequestValidation(blockedMethodEndpointRolesTernion);
        registrationBean.setFilter(authorizationFilter); // Provide parameter values
        registrationBean.addUrlPatterns(endpointAllowedToAll.toArray(new String[0])); // Specify URL patterns to filter
        return registrationBean;
    }

    // @Bean
    // public FilterRegistrationBean<AuthorizationRequestValidation> authorizationFilterForMedico() {
    //     FilterRegistrationBean<AuthorizationRequestValidation> registrationBean = new FilterRegistrationBean<>();
    //     AuthorizationRequestValidation authorizationFilter = new AuthorizationRequestValidation(List.of("M"));
    //     authorizationFilter.addBlockedMethodEndpointPair("POST", "/medicos");

    //     registrationBean.setFilter(authorizationFilter); // Provide parameter values
    //     // registrationBean.addUrlPatterns("/medicos", "/medicos/*", "/pacientes", "/pacientes/*"); // Specify URL patterns to filter
    //     registrationBean.addUrlPatterns(endpointAllowedToM.toArray(new String[0])); // Specify URL patterns to filter
    //     return registrationBean;
    // }

    // @Bean
    // public FilterRegistrationBean<AuthorizationRequestValidation> authorizationFilterForRecepcionista() {
    //     FilterRegistrationBean<AuthorizationRequestValidation> registrationBean = new FilterRegistrationBean<>();
    //     registrationBean.setFilter(new AuthorizationRequestValidation( List.of("R") )); // Provide parameter values
    //     registrationBean.addUrlPatterns("/medicos", "/medicos/*"); // Specify URL patterns to filter
    //     return registrationBean;
    // }

    /**
    @Bean
    public FilterRegistrationBean<MyCustomFilter1> filter1() {
        FilterRegistrationBean<MyCustomFilter1> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new MyCustomFilter1());
        
        // Specify the URL patterns for the first filter
        registrationBean.addUrlPatterns("/admin/*"); // Apply to /admin, /admin/data, /admin/profile

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<MyCustomFilter2> filter2() {
        FilterRegistrationBean<MyCustomFilter2> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new MyCustomFilter2());
        
        // Specify the URL patterns for the second filter
        registrationBean.addUrlPatterns("/client/*", "/general"); // Apply to /client, /client/data, and /general

        return registrationBean;
    }
     */
}
