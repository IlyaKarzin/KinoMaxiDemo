package ru.maxi.kinomaxi.demo.accountDetails.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.maxi.kinomaxi.demo.AppConfig


/**
 * Ответ на запрос информации об аккаунте
 */
@Serializable
data class AccountDetailsResponse(
    @SerialName("username") val username: String,
    @SerialName("avatar") val avatar: AvatarDetails? = null,
    @SerialName("name") val name: String,
)

/**
 * Детали аватара
 */
@Serializable
data class AvatarDetails(
    @SerialName("tmdb") val gravatar: TmdbObject? = null
)

/**
 * Объект TMDb, содержащий путь к аватару
 */
@Serializable
data class TmdbObject(
    @SerialName("avatar_path") val avatarPath: String? = null
){
    /**
     * URL аватара, сформированный на основе avatarPath
     */
    val avatarUrl: String?
        get() = avatarPath?.let {
            "${AppConfig.IMAGE_BASE_URL}${AppConfig.POSTER_PREVIEW_SIZE}$it"
        }
}