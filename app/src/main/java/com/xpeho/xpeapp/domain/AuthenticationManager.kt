package com.xpeho.xpeapp.domain

import android.util.Log
import androidx.annotation.VisibleForTesting
import com.xpeho.xpeapp.data.DatastorePref
import com.xpeho.xpeapp.data.entity.AuthentificationBody
import com.xpeho.xpeapp.data.model.AuthResult
import com.xpeho.xpeapp.data.model.WordpressToken
import com.xpeho.xpeapp.data.service.FirebaseService
import com.xpeho.xpeapp.data.service.WordpressRepository
import com.xpeho.xpeapp.di.TokenProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking

/**
 * Singleton responsible for keeping track of the authentication state,
 * logging the user in and logging the user out.
 * @param wordpressRepo: Repository for wordpress authentication
 * @param datastorePref: Wrapper for the DataStore of Preferences,
 *  for storing the authentication data
 */
class AuthenticationManager(
    val tokenProvider: TokenProvider,
    val wordpressRepo: WordpressRepository,
    val datastorePref: DatastorePref,
    val firebaseService: FirebaseService
) {

    companion object {
        private const val TOKEN_VALIDITY_DAYS = 5
        private const val DAYS_TO_MILLISECONDS = 24 * 60 * 60 * 1000L
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val _authState: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Unauthenticated)

    val authState = _authState.asStateFlow()

    fun restoreAuthStateFromStorage() = runBlocking {
        datastorePref.getAuthData()?.let { authData ->
            // Verify if the token has expired (5 days)
            if (isTokenExpired(authData)) {
                // The token has expired, perform logout
                logout()
            } else {
                // The token is still valid, restore the authenticated state
                _authState.value = AuthState.Authenticated(authData)
                tokenProvider.set("Bearer ${authData.token.token}")
            }
        }
    }

    suspend fun isAuthValid(): Boolean {
        return when (val authState = this.authState.value) {
            is AuthState.Unauthenticated -> false
            is AuthState.Authenticated -> {
                // Verify if the token has expired (5 days)
                if (isTokenExpired(authState.authData)) {
                    logout()
                    return false
                }
                
                // Note(loucas): Order of operations here is important,
                // lazy `&&` evalutation makes this faster
                firebaseService.isAuthenticated()
                        && wordpressRepo.validateToken(authState.authData.token) is AuthResult.Success
            }
        }
    }

    /**
     * Verify if the token has expired.
     * A token is considered expired if it is older than 5 days.
     * @param authData: The authentication data containing the token and its saved timestamp.
     * @return True if the token has expired, false otherwise.
     */
    private fun isTokenExpired(authData: AuthData): Boolean {
        val currentTime = System.currentTimeMillis()
        val tokenAge = currentTime - authData.tokenSavedTimestamp
        val tokenValidityInMillis = TOKEN_VALIDITY_DAYS * DAYS_TO_MILLISECONDS
        
        val ageInDays = tokenAge / DAYS_TO_MILLISECONDS
        Log.d("AuthenticationManager", "Token age: $ageInDays days")
        
        return tokenAge > tokenValidityInMillis
    }

    fun getAuthData(): AuthData? {
        return when (val authState = this.authState.value) {
            is AuthState.Authenticated -> authState.authData
            else -> null
        }
    }

    suspend fun login(username: String, password: String): AuthResult<WordpressToken> = coroutineScope {
        val wpDefRes = async {
            wordpressRepo.authenticate(AuthentificationBody(username, password))
        }
        val fbDefRes = async {
            wordpressRepo.handleServiceExceptions(
                tryBody = {
                    firebaseService.authenticate()
                    return@async AuthResult.Success(Unit)
                },
                catchBody = { e ->
                    Log.e("AuthenticationManager: login", "Network error: ${e.message}")
                    return@async AuthResult.NetworkError
                }
            )
        }

        val result = when (val wpRes = wpDefRes.await()) {
            is AuthResult.NetworkError -> AuthResult.NetworkError
            is AuthResult.Unauthorized -> AuthResult.Unauthorized
            else -> {
                val fbRes = fbDefRes.await()
                when (fbRes) {
                    is AuthResult.NetworkError -> AuthResult.NetworkError
                    is AuthResult.Unauthorized -> AuthResult.Unauthorized
                    else -> {
                        val authData = AuthData(username, (wpRes as AuthResult.Success).data)
                        writeAuthentication(authData)
                        _authState.value = AuthState.Authenticated(authData)
                        tokenProvider.set("Bearer ${authData.token.token}")
                        wpRes
                    }
                }
            }
        }
        return@coroutineScope result
    }

    private suspend fun writeAuthentication(authData: AuthData) {
        val username = authData.username
        val wordpressUid = wordpressRepo.getUserId(username)
        datastorePref.setAuthData(authData)
        datastorePref.setIsConnectedLeastOneTime(true)
        datastorePref.setWasConnectedLastTime(true)
        datastorePref.setLastEmail(username)
        wordpressUid?.let { datastorePref.setUserId(it) }
    }

    suspend fun logout() {
        firebaseService.signOut()
        datastorePref.clearAuthData()
        datastorePref.setWasConnectedLastTime(false)
        _authState.value = AuthState.Unauthenticated
    }
}

sealed interface AuthState {
    object Unauthenticated : AuthState
    data class Authenticated(val authData: AuthData) : AuthState
}

data class AuthData(
    val username: String, 
    val token: WordpressToken,
    val tokenSavedTimestamp: Long = System.currentTimeMillis()
)