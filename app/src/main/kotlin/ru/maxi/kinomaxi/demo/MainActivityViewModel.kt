package ru.maxi.kinomaxi.demo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.maxi.kinomaxi.demo.data.DataStoreRepository
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow< MainActivityViewState>( MainActivityViewState.Unauthorized)

    /**
     * Cостояние главной активности
     * */
    val viewState = _viewState.asStateFlow().onStart {
        checkSession()
        observeUserPreferences()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue =  MainActivityViewState.Unauthorized
    )

    private fun observeUserPreferences() {
        viewModelScope.launch {
            dataStoreRepository.userPreferencesFlow.collect { userPreferences ->
                if (userPreferences?.sessionId.isNullOrEmpty()) {
                    _viewState.value = MainActivityViewState.Unauthorized
                } else {
                    _viewState.value = MainActivityViewState.Authorized(userPreferences)
                }
            }
        }
    }

    /**
     * Проверка сессии
     */
    fun checkSession() {
        viewModelScope.launch {
            try {
                dataStoreRepository.checkSession()
            }catch (e: Exception){
                Log.e("checkSession: ", e.toString())
            }
        }
    }
}