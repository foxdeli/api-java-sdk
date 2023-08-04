package com.foxdeli.interceptor;

import com.foxdeli.exception.FoxdeliConnectionException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http2.ConnectionShutdownException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * The `ErrorHandlingInterceptor` class implements the OkHttp Interceptor interface to handle API error responses.
 * It intercepts outgoing HTTP requests and catches potential exceptions that might occur during the API call.
 * If an exception is caught, it translates the exception into a more user-friendly error message and throws a
 * custom `FoxdeliConnectionException` with the appropriate error message.
 */
public class ErrorHandlingInterceptor implements Interceptor {

    /**
     * Intercepts the outgoing HTTP request and handles potential exceptions that might occur during the API call.
     * If an exception is caught, it translates the exception into a more user-friendly error message and throws a
     * `FoxdeliConnectionException` with the appropriate error message.
     *
     * @param chain The interceptor chain to proceed with the original request.
     * @return The response received from the API if the request proceeds successfully.
     * @throws FoxdeliConnectionException If an exception occurs during the API call, it is wrapped in this custom exception.
     */
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) {
        Request request = chain.request();
        try {
            return chain.proceed(request);
        } catch (Exception e) {
            final String message;
            if (e instanceof SocketTimeoutException) {
                message = "Timeout - Please check your internet connection";
            } else if (e instanceof UnknownHostException) {
                message = "Unable to make a connection. Please check your internet";
            } else if (e instanceof ConnectionShutdownException) {
                message = "Connection shutdown. Please check your internet";
            } else if (e instanceof IOException) {
                message = "Server is unreachable, please try again later.";
            } else {
                message = e.getMessage();
            }
            throw new FoxdeliConnectionException(message, e);
        }
    }
}
