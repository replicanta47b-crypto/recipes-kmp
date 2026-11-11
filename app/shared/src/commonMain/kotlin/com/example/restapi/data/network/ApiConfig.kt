package com.example.restapi.data.network

internal object ApiConfig {
    const val BASE_URL = "https://dummyjson.com"

    const val LOGIN_PATH = "/auth/login"
    const val REFRESH_PATH = "/auth/refresh"
    const val ME_PATH = "/auth/me"

    /**
     * Lifetime of the access token requested from the server.
     * Set it to 1 to see the automatic refresh in action after a minute.
     */
    const val ACCESS_TOKEN_LIFETIME_MINS = 30

    const val REQUEST_TIMEOUT_MS = 15_000L
}
