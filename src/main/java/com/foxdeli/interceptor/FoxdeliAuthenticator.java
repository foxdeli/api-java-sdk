package com.foxdeli.interceptor;

import com.foxdeli.exception.FoxdeliAuthenticationException;
import com.foxdeli.helper.AuthHelper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The `FoxdeliAuthenticator` class implements the OkHttp Authenticator interface to handle API authentication challenges.
 * It is responsible for automatically authenticating API requests by intercepting HTTP responses that require reauthentication.
 * When a response is received with an authentication challenge, the authenticator checks the validity of the access and refresh tokens.
 * If the access token has expired, it refreshes the token. If the refresh token has expired, it reauthorizes using the username and password.
 * The class ensures that the API requests are always authenticated with valid tokens.
 */
@Slf4j
public class FoxdeliAuthenticator implements Authenticator {

    /**
     * Authenticates API requests by intercepting HTTP responses with authentication challenges.
     * It automatically handles the reauthorization process by refreshing the access token or reauthorizing with the username and password
     * based on the validity of the access and refresh tokens.
     *
     * @param route    The route of the HTTP request, if available (not used in this implementation).
     * @param response The HTTP response received from the API server.
     * @return A new request with the updated "Authorization" header for the API call.
     * @throws FoxdeliAuthenticationException If the authentication fails with a valid token, indicating a potential issue.
     */
    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @NotNull Response response) {
        // Check if the refresh token has expired. If so, reauthorize with username and password.
        if (AuthHelper.isRefreshTokenExpired()) {
            log.trace("Refresh token expired, authorizing with username + password");
            AuthHelper.authorize();
        }
        // If the access token has expired, refresh it.
        else if (AuthHelper.isAccessTokenExpired()) {
            log.trace("Access token expired, refreshing token");
            AuthHelper.refreshToken();
        }
        // If the response already has an "Authorization" header, and access token and refresh token are not expired,
        // it indicates an issue with the current token.
        // Throw an exception to indicate that authentication failed with a valid token.
        else if (response.request().header("Authorization") != null) {
            throw new FoxdeliAuthenticationException("Authentication failed with valid token. Please try 'Foxdeli.init()' again.");
        }
        log.trace("Authenticating for response: " + response);
        log.trace("Challenges: " + response.challenges());

        // Create a new request with the updated "Authorization" header and return it.
        return response.request().newBuilder()
                .header("Authorization", AuthHelper.getAccessToken())
                .build();
    }
}
