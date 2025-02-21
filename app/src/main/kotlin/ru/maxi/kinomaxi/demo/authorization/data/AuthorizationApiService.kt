package ru.maxi.kinomaxi.demo.authorization.data

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Интерфейс взаимодействия с REST API для авторизации
 */
interface AuthorizationApiService {

    /**
     * Получение токена
     */
    @GET("authentication/token/new")
    suspend fun getRequestToken(): TokenResponse

    /**
     * Вход с помощью логина
     */
    @POST("authentication/token/validate_with_login")
    suspend fun validateWithLogin(@Body loginRequest: LoginRequest): TokenResponse

    /**
     * Получить идентификатор сессии
     */
    @POST("authentication/session/new")
    suspend fun createSession(@Body sessionRequest: SessionRequest): SessionResponse

}