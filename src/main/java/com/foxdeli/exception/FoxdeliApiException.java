package com.foxdeli.exception;

import com.foxdeli.orders.ApiException;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class FoxdeliApiException extends RuntimeException {

    private final int code;
    private final Map<String, List<String>> responseHeaders;
    private final String responseBody;

    public FoxdeliApiException(String message, ApiException e) {
        super(message, e);
        this.code = e.getCode();
        this.responseHeaders = e.getResponseHeaders();
        this.responseBody = e.getResponseBody();
    }

    public String getMessage() {
        return String.format("Message: %s%nHTTP response code: %s%nHTTP response body: %s%nHTTP response headers: %s",
                super.getMessage(), this.getCode(), this.getResponseBody(), this.getResponseHeaders());
    }
}
