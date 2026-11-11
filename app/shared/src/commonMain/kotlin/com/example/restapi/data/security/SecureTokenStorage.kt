package com.example.restapi.data.security

import com.example.restapi.domain.auth.AuthTokens
import com.example.restapi.domain.auth.TokenStorage
import eu.anifantakis.lib.ksafe.KSafe

/**
 * Keeps the tokens in KSafe: values are encrypted (AES-GCM, key in Android Keystore / iOS Keychain /
 * a protected file on desktop) before they reach the disk.
 */
internal class SecureTokenStorage(private val ksafe: KSafe) : TokenStorage {

    override suspend fun getTokens(): AuthTokens? {
        val access = ksafe.get(ACCESS_TOKEN_KEY, "")
        val refresh = ksafe.get(REFRESH_TOKEN_KEY, "")
        return if (access.isNotEmpty() && refresh.isNotEmpty()) AuthTokens(access, refresh) else null
    }

    override suspend fun saveTokens(tokens: AuthTokens) {
        ksafe.put(ACCESS_TOKEN_KEY, tokens.accessToken)
        ksafe.put(REFRESH_TOKEN_KEY, tokens.refreshToken)
    }

    override suspend fun clear() {
        ksafe.put(ACCESS_TOKEN_KEY, "")
        ksafe.put(REFRESH_TOKEN_KEY, "")
    }

    private companion object {
        const val ACCESS_TOKEN_KEY = "auth_access_token"
        const val REFRESH_TOKEN_KEY = "auth_refresh_token"
    }
}
