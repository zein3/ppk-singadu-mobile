package com.polstat.singadu.ui

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.polstat.singadu.SingaduApplication
import com.polstat.singadu.data.UserPreferencesRepository
import com.polstat.singadu.data.UserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update


class SingaduAppViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel(){

    private val _uiState = MutableStateFlow(SingaduAppUiState())
    val uiState: StateFlow<SingaduAppUiState> = _uiState.asStateFlow()

    val userState: StateFlow<UserState> = userPreferencesRepository.user.map { user ->
        user
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UserState(
            "",
            "",
            "",
            isAdmin = false,
            isSupervisor = false,
            isEnumerator = false
        )
    )

    fun showSpinner() {
        _uiState.update { currentState ->
            currentState.copy(
                showProgressDialog = true
            )
        }
    }

    fun dismissSpinner() {
        _uiState.update { currentState ->
            currentState.copy(
                showProgressDialog = false
            )
        }
    }

    fun showMessageDialog(@StringRes title: Int, @StringRes body: Int) {
        _uiState.update {  currentState ->
            currentState.copy(
                showProgressDialog = false,
                showMessageDialog = true,
                messageTitle = title,
                messageBody = body
            )
        }
    }

    fun dismissMessageDialog() {
        _uiState.update {  currentState ->
            currentState.copy(
                showMessageDialog = false
            )
        }
    }

    suspend fun logout() {
        userPreferencesRepository.saveToken("")
        userPreferencesRepository.saveName("")
        userPreferencesRepository.saveEmail("")
        userPreferencesRepository.saveIsAdmin(false)
        userPreferencesRepository.saveIsSupervisor(false)
        userPreferencesRepository.saveIsEnumerator(false)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SingaduApplication)
                SingaduAppViewModel(
                    userPreferencesRepository = application.userPreferenceRepository
                )
            }
        }
    }
}

data class SingaduAppUiState(
    val showProgressDialog: Boolean = false,
    val showMessageDialog: Boolean = false,
    @StringRes val messageTitle: Int = 0,
    @StringRes val messageBody: Int = 0
)