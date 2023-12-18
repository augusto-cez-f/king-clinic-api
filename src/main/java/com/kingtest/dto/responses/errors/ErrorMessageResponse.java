package com.kingtest.dto.responses.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ErrorMessageResponse {
    @Getter
    private String message; 
}
