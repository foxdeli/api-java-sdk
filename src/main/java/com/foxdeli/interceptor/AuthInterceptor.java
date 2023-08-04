package com.foxdeli.interceptor;

import com.foxdeli.helper.AuthHelper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * The `AuthInterceptor` class implements the OkHttp Interceptor interface to handle API authentication.
 * It intercepts outgoing HTTP requests and adds the access token obtained from the `AuthHelper` class
 * as the "Authorization" header in the request before proceeding with the request.
 * This ensures that the API requests are authenticated with the proper access token.
 */
@Slf4j
public class AuthInterceptor implements Interceptor {

    /**
     * Intercepts the outgoing HTTP request and adds the access token to the "Authorization" header.
     *
     * @param chain The interceptor chain to proceed with the modified request.
     * @return The response received from the API after proceeding with the authenticated request.
     * @throws IOException If an I/O error occurs during the API call.
     */
    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        log.trace("Authenticating for request: " + request);
        Request authorizedRequest = request.newBuilder()
                .header("Authorization", AuthHelper.getAccessToken())
                .build();
        return chain.proceed(authorizedRequest);
    }
}

