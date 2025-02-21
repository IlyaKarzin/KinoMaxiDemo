package ru.maxi.kinomaxi.demo

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ResourceProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /**
    * Вернуть строку из ресурсов
     */
    fun getString(resId: Int): String {
        return context.getString(resId)
    }
}