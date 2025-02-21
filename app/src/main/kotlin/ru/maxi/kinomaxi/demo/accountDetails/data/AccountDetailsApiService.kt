package ru.maxi.kinomaxi.demo.accountDetails.data

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Интерфейс взаимодействия с REST API для получения информации об аккаунте
 */
interface AccountDetailsApiService {

    /**
     * Получить информации об аккаунте
     */
    @GET("account")
    suspend fun getAccountDetails(@Query("session_id") sessionId: String): AccountDetailsResponse

    /**
     * Удалить сессию
     */
    @DELETE("authentication/session")
    suspend fun deleteSession(@Query("session_id") sessionId: String): Response<Void>
}