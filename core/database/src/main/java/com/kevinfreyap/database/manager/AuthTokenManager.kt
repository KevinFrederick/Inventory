package com.kevinfreyap.database.manager

import android.content.Context
import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.kevinfreyap.domain.manager.IAuthTokenManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.jvm.java

class AuthTokenManager @Inject constructor(
    @ApplicationContext context: Context,
    private val dataStore: DataStore<Preferences>
): IAuthTokenManager {

    private val accessTokenKey = stringPreferencesKey("access_token")
    private val refreshTokenKey = stringPreferencesKey("refresh_token")
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _tokenState = MutableStateFlow<String?>(null)
    override val tokenState: StateFlow<String?> = _tokenState.asStateFlow()

    private val _refreshTokenState = MutableStateFlow<String?>(null)

    private val aead: Aead

    init {
        AeadConfig.register()

        val keysetHandle = AndroidKeysetManager.Builder()
            .withSharedPref(context, "tink_keyset", "tink_prefs")
            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
            .withMasterKeyUri("android-keystore://master_key")
            .build()
            .keysetHandle

        aead = keysetHandle.getPrimitive(RegistryConfiguration.get(), Aead::class.java)

        scope.launch {
            val prefs = dataStore.data.first()
            val encryptedAccess = prefs[accessTokenKey]
            val encryptedRefresh = prefs[refreshTokenKey]

            _tokenState.value = encryptedAccess?.let { decrypt(it) }
            _refreshTokenState.value = encryptedRefresh?.let { decrypt(it) }
        }
    }

    override fun saveToken(accessToken: String, refreshToken: String) {
        scope.launch {
            val encryptedAccess = encrypt(accessToken)
            val encryptedRefresh = encrypt(refreshToken)
            dataStore.edit { preferences ->
                preferences[accessTokenKey] = encryptedAccess
                preferences[refreshTokenKey] = encryptedRefresh
            }
            _tokenState.value = accessToken
            _refreshTokenState.value = refreshToken
        }
    }

    override fun clearToken() {
        scope.launch {
            dataStore.edit { preferences ->
                preferences.remove(accessTokenKey)
                preferences.remove(refreshTokenKey)
            }
            _tokenState.value = null
            _refreshTokenState.value = null
        }
    }

    override fun fetchAccessToken(): String? = _tokenState.value

    override fun fetchRefreshToken(): String? = _refreshTokenState.value

    private fun encrypt(plaintext: String): String {
        val ciphertext = aead.encrypt(plaintext.toByteArray(Charsets.UTF_8), null)
        return Base64.encodeToString(ciphertext, Base64.DEFAULT)
    }

    private fun decrypt(base64Ciphertext: String): String? {
        return try {
            val ciphertext = Base64.decode(base64Ciphertext, Base64.DEFAULT)
            val plaintext = aead.decrypt(ciphertext, null)
            String(plaintext, Charsets.UTF_8)
        } catch (_: Exception) {
            null
        }
    }
}