package ru.maxi.kinomaxi.demo.authorization.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.maxi.kinomaxi.demo.accountDetails.data.AccountDetailsApiService
import ru.maxi.kinomaxi.demo.authorization.data.AuthorizationApiService
import ru.maxi.kinomaxi.demo.authorization.data.LoginRequest
import ru.maxi.kinomaxi.demo.authorization.data.SessionRequest
import ru.maxi.kinomaxi.demo.data.DataStoreRepository
import ru.maxi.kinomaxi.demo.data.UserPreferences
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val apiService: AuthorizationApiService,
    private val accountDetailsApiService: AccountDetailsApiService,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _viewState =
        MutableStateFlow<AuthorizationViewState>(AuthorizationViewState.Loading)
    val viewState: StateFlow<AuthorizationViewState> get() = _viewState

    /**
     * Авторизация
     * */
    fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            try {
                val requestToken = getRequestToken()
                val validatedToken = validateWithLogin(username, password, requestToken)
                val sessionId = createSession(validatedToken)
                val accountDetails = accountDetailsApiService.getAccountDetails(sessionId)
                val userPreferences = UserPreferences(
                    sessionId = sessionId,
                    avatarUrl = accountDetails.avatar?.gravatar?.avatarUrl,
                    username = accountDetails.username,
                    name = accountDetails.name
                )
                dataStoreRepository.saveUserPreference(userPreferences)
                _viewState.value = AuthorizationViewState.Success(sessionId)
            } catch (e: Exception) {
                _viewState.value = AuthorizationViewState.Error(e.message ?: "Authorization failed")
            }
        }
    }


    private suspend fun getRequestToken(): String {
        val response = apiService.getRequestToken()
        if (response.success) {
            return response.requestToken
        } else {
            throw Exception()
        }
    }

    private suspend fun validateWithLogin(
        username: String,
        password: String,
        requestToken: String
    ): String {
        val loginRequest = LoginRequest(username, password, requestToken)
        val response = apiService.validateWithLogin(loginRequest)
        if (response.success) {
            return response.requestToken
        } else {
            throw Exception()
        }
    }

    private suspend fun createSession(requestToken: String): String {
        val sessionRequest = SessionRequest(requestToken)
        val response = apiService.createSession(sessionRequest)
        if (response.success) {
            return response.sessionId
        } else {
            throw Exception()
        }
    }

}