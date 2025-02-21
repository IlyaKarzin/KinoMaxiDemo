package ru.maxi.kinomaxi.demo.data

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val SESSION_ID = stringPreferencesKey("session_id")
    val USERNAME = stringPreferencesKey("username")
    val AVATAR_URL = stringPreferencesKey("avatar-url")
}