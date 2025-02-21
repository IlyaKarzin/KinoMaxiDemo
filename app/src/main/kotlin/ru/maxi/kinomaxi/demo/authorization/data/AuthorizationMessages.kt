package ru.maxi.kinomaxi.demo.authorization.data

import kotlinx.serialization.SerialName

import kotlinx.serialization.Serializable


/**
 * Ответ на запрос получения токена
 */
@Serializable
data class TokenResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("expires_at") val expiresAt: String,
    @SerialName("request_token") val requestToken: String
)

/**
 * Данные для входа с помощью логина
 */
@Serializable
data class LoginRequest(
    @SerialName("username") val username: String,
    @SerialName("password") val password: String,
    @SerialName("request_token") val requestToken: String
)

/**
 * Данные для получения идентификатора сессии
 */
@Serializable
data class SessionRequest(
    @SerialName("request_token") val requestToken: String
)

/**
 * Ответ на получение идентификатора сессии
 */
@Serializable
data class SessionResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("session_id") val sessionId: String
)