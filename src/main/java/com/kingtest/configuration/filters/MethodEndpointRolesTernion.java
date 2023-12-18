package com.kingtest.configuration.filters;

import java.util.List;

public record MethodEndpointRolesTernion(String method, String endpoint, List<String> roles) {}