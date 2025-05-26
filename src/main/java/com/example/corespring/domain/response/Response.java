package com.example.corespring.domain.response;

import com.example.corespring.constants.Constants;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Map;

/**
 * Created by NhanNguyen on 1/18/2021
 *
 * @author : NhanNguyen
 * @since : 1/18/2021
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Response {
    String requestId;
    String type;
    String code;
    String message;
    Object data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Object> fieldError;

    public Response(String type, String code, String message, Object data) {
        this.type = type;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Response(String type, String code, String message) {
        this.type = type;
        this.code = code;
        this.message = message;
    }

    public Response(String type, String code, Object data) {
        this.type = type;
        this.code = code;
        this.data = data;
    }

    public Response(String type, String code) {
        this.type = type;
        this.code = code;
    }

    public Response(String type) {
        this.type = type;
    }

    public Response() {
    }
    /**
     * @param data the data to set
     */
    public Response withData(Object data) {
        this.data = data;
        return this;
    }

    public static Response success(String code) {
        return new Response(Constants.ResponseType.SUCCESS, code);
    }

    public static Response success() {
        return new Response(Constants.ResponseType.SUCCESS);
    }

    public static Response error(String code) {
        return new Response(Constants.ResponseType.ERROR, code);
    }

    public static Response error(String type, String code) {
        return new Response(type, code);
    }

    public static Response warning(String code) {
        return new Response(Constants.ResponseType.WARNING, code);
    }

    public static Response invalidPermission() {
        return new Response(Constants.ResponseType.ERROR, "invalidPermission");
    }

    public static Response confirm(String code, String callback, Object data) {
        return new Response(Constants.ResponseType.CONFIRM, code, callback, data);
    }

    public static Response confirm(String code, String callback) {
        return new Response(Constants.ResponseType.CONFIRM, code, callback, null);
    }

    public static Response success(String code, String message) {
        return new Response(Constants.ResponseType.SUCCESS, code, message);
    }

    public static Response warning(String code, String message) {
        return new Response(Constants.ResponseType.WARNING, code, message);
    }
}
