package com.es.modle.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response<T> {
    private Status status;
    private Integer code;
    private T payload;
    private Object errors;
    private Object metadata;
    public static final Random random = new Random();

    public static <T> Response<T> create(Status status, T payload) {
        Response response = new Response<T>();
        response.setStatus(status);
        response.setCode(status.ordinal());
        response.setPayload(payload);
        return response;
    }

    public static Response badRequest() {
        return create(Status.BAD_REQUEST, null);
    }

    public static Response ok() {
        return create(Status.OK, null);
    }

    @Deprecated
    public static <T> Response<T> ok(T payload) {
        return create(Status.OK, payload);
    }

    public static <T> Response<T> ok(Optional<T> payload) {
        return ok(payload.get());
    }


    public static <T> Response<List<T>> listModel(List<T> r) {
        return ok(r);
    }

    public static Response unauthorized() {
        return create(Status.UNAUTHORIZED, null);
    }

    public static Response validationException() {
        return create(Status.VALIDATION_EXCEPTION, null);
    }

    public static Response wrongCredentials() {
        return create(Status.WRONG_CREDENTIALS, null);
    }

    public static Response accessDenied() {
        return create(Status.ACCESS_DENIED, null);
    }

    public static Response grouponCompletedException(String msg) {
        return create(Status.GROUP_COMPLETED_EXCEPTION, null);
    }

    public static Response notFound() {
        return create(Status.NOT_FOUND, null);
    }

    public static Response duplicateEntity() {
        return create(Status.DUPLICATE_ENTITY, null);
    }

    public static <T> Response<T> exception(String msg, Integer code) {
        Response response = create(Status.EXCEPTION, null);
        Optional.ofNullable(code).ifPresent(x -> response.setCode(x));
        response.setErrors(msg);
        return response;
    }

    @Deprecated
    public static <T> Response<T> exception(String msg) {
        return exception(msg, null);
    }

    public static <T> Response<T> exception(Exception e) {
        log.error("Response:[exception]", e);
        return exception(e.getMessage() == null ? e.toString() : e.getMessage());
    }

    public static <T> Response<T> exception() {
        return exception(null, null);
    }


    public enum Status {
        OK,
        BAD_REQUEST,
        UNAUTHORIZED,
        VALIDATION_EXCEPTION,
        EXCEPTION,
        WRONG_CREDENTIALS,
        ACCESS_DENIED,
        NOT_FOUND,
        DUPLICATE_ENTITY,
        /**
         * 拼团状态
         */
        GROUPON,
        /**
         * 拼团完成，错误
         */
        GROUP_COMPLETED_EXCEPTION
    }
}
