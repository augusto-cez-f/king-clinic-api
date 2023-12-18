package com.kingtest.entities.enums;

public enum ERole {
    R("R"),
    M("M"),
    C("C");

    private String code;

    ERole(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}