package com.mindbowser.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApiMsgResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private int status;
    private String message;
    private List<String> errors;
    private Object response;

    public ApiMsgResponse(int status, String message, List<String> errors, Object responseObject) {
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.response = responseObject;
    }

    public ApiMsgResponse(int status, String message, Object responseObject) {
        this.status = status;
        this.message = message;
        this.response = responseObject;
    }

    public ApiMsgResponse(int status_code, String message, List<String> errors) {
        this.status = status_code;
        this.message = message;
        this.errors = errors;
    }

    public ApiMsgResponse() {}
}
