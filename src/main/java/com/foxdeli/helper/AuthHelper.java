package com.foxdeli.helper;

import com.auth0.jwt.JWT;
import com.foxdeli.exception.FoxdeliAuthenticationException;
import com.foxdeli.token.ApiException;
import com.foxdeli.token.api.TokenApi;
import com.foxdeli.token.api.model.Authorization;
import com.foxdeli.token.api.model.TokenPair;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * The `AuthHelper` class provides utility methods for handling authentication and authorization with the Foxdeli API.
 * It manages the authentication credentials, access token, and refresh token used for API calls.
 * The class offers methods to set the username and password, authorize the API client, and refresh tokens when needed.
 * It also includes methods to check if the access token and refresh token have expired.
 */
@Slf4j
public class AuthHelper {

    /**
     * Private constructor to prevent instantiation of the `AuthHelper` class.
     * This class only contains static utility methods, and it should not be instantiated directly.
     */
    private AuthHelper() {
    }

    /**
     * The username used for API authentication.
     */
    private static String username;

    /**
     * The password used for API authentication.
     */
    private static String password;

    /**
     * The current access token obtained after successful authorization.
     */
    @Getter
    private static String accessToken;

    /**
     * The current refresh token obtained after successful authorization.
     */
    private static String refreshToken;

    /**
     * The API client for handling token-related operations.
     */
    private static final TokenApi tokenApi = new TokenApi();

    /**
     * Sets the username for API authentication.
     * If the provided username is different from the current one, the existing tokens are invalidated.
     *
     * @param username The username to authenticate with the Foxdeli API.
     * @throws FoxdeliAuthenticationException If the provided username is null.
     */
    public static void setUsername(String username) {
        if (username == null) {
            throw new FoxdeliAuthenticationException("Username can not be null");
        } else {
            if (!username.equals(AuthHelper.username)) {
                invalidate();
            }
            AuthHelper.username = username;
        }
    }

    /**
     * Sets the password for API authentication.
     * If the provided password is different from the current one, the existing tokens are invalidated.
     *
     * @param password The password to authenticate with the Foxdeli API.
     * @throws FoxdeliAuthenticationException If the provided password is null.
     */
    public static void setPassword(String password) {
        if (password == null) {
            throw new FoxdeliAuthenticationException("Password can not be null");
        } else {
            if (!password.equals(AuthHelper.password)) {
                invalidate();
            }
            AuthHelper.password = password;
        }
    }

    /**
     * Authorizes the API client by obtaining access and refresh tokens using the provided username and password.
     *
     * @throws FoxdeliAuthenticationException If the API call for authorization fails.
     */
    public static void authorize() {
        final TokenPair tokenPair;
        try {
            tokenPair = tokenApi.authorize(new Authorization().email(username).password(password));
        } catch (ApiException e) {
            throw new FoxdeliAuthenticationException("Authorization failed with code " + e.getCode() + " and response body: " + e.getResponseBody());
        }
        accessToken = tokenPair.getToken();
        refreshToken = tokenPair.getRefreshToken();
    }

    /**
     * Refreshes the access token using the current refresh token.
     *
     * @throws FoxdeliAuthenticationException If the API call for refreshing the token fails.
     */
    public static void refreshToken() {
        final TokenPair tokenPair;
        try {
            tokenPair = tokenApi.refresh(refreshToken, null);
        } catch (ApiException e) {
            throw new FoxdeliAuthenticationException("Refresh token failed with code " + e.getCode() + " and response body: " + e.getResponseBody());
        }
        accessToken = tokenPair.getToken();
        refreshToken = tokenPair.getRefreshToken();
    }

    /**
     * Checks if the current access token has expired.
     *
     * @return `true` if the access token has expired, `false` otherwise.
     */
    public static boolean isAccessTokenExpired() {
        try {
            return JWT.decode(accessToken).getExpiresAt().before(new Date());
        } catch (Exception e) {
            log.warn("Failed to parse accessToken: {}", e.getMessage());
            return true;
        }
    }

    /**
     * Checks if the current refresh token has expired.
     *
     * @return `true` if the refresh token has expired, `false` otherwise.
     */
    public static boolean isRefreshTokenExpired() {
        try {
            return JWT.decode(refreshToken).getExpiresAt().before(new Date());
        } catch (Exception e) {
            log.warn("Failed to parse refreshToken: {}", e.getMessage());
            return true;
        }
    }

    /**
     * Invalidates the current access and refresh tokens.
     * This method is called when the username or password is changed to ensure new tokens are obtained on the next authorization.
     */
    private static void invalidate() {
        accessToken = null;
        refreshToken = null;
    }
}
