package com.mindbowser.entity;

public enum ErrorCode {
    UNKONWN(-1, "Unknow Error"), SUCCESS(0, "Success"), BAD_REQUEST(1, "Bad Request"), RESOURCE(2, "Resource Not Found"), INTERNAL_SERVER(3,
            "Internal Server"), UNAUTHORIZED(4, "UnAuthorized access"), CONSTRAINT_VIOLATION(4, "Constraint Violation");

    private final int code;
    private final String description;

    private ErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }
}
