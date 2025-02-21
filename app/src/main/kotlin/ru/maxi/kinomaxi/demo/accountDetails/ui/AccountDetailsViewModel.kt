package ru.maxi.kinomaxi.demo.accountDetails.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.maxi.kinomaxi.demo.R
import ru.maxi.kinomaxi.demo.ResourceProvider
import ru.maxi.kinomaxi.demo.data.DataStoreRepository
import javax.inject.Inject

@HiltViewModel
class AccountDetailsViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val _viewState = MutableStateFlow<AccountDetailsViewState>(AccountDetailsViewState.Loading)

    /**
     * Cостояние страницы информации об аккаунте
     * */
    val viewState = _viewState.asStateFlow().onStart {
        loadData()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AccountDetailsViewState.Loading
    )

    private fun loadData(){
        try {
            viewModelScope.launch {
                dataStoreRepository.checkSession()
                val userPreferences = dataStoreRepository.userPreferencesFlow.firstOrNull()
                if (userPreferences!=null && !userPreferences.sessionId.isNullOrEmpty()){
                    _viewState.value = AccountDetailsViewState.Authorized(userPreferences)
                }else{
                    _viewState.value = AccountDetailsViewState.Error(resourceProvider.getString(R.string.log_out_message))
                }
            }
        }catch (e:Exception){
            _viewState.value = AccountDetailsViewState.Error(resourceProvider.getString(R.string.log_out_message))
        }
    }

    /**
     * Выйти из аккаунта и удалить сессию
     * */
    fun logout(){
        viewModelScope.launch {
            try {
                dataStoreRepository.clearUserPreferences()
                _viewState.value = AccountDetailsViewState.Error(resourceProvider.getString(R.string.log_out_message))
            } catch (e: Exception) {
                _viewState.value = AccountDetailsViewState.Error(resourceProvider.getString(R.string.log_out_error_message))
            }
        }
    }

    /**
     * Проверка сессии
     * */
    fun checkSession() {
        viewModelScope.launch {
                dataStoreRepository.checkSession()
        }
    }


}