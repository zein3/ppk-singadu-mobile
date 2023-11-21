package com.polstat.singadu.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.polstat.singadu.SingaduApplication
import com.polstat.singadu.data.UserPreferencesRepository
import com.polstat.singadu.data.UserState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class SingaduAppViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel(){

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

    fun attemptLogin() {
        // TESTING
        viewModelScope.launch {
            userPreferencesRepository.saveToken("adsf")
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SingaduApplication)
                SingaduAppViewModel(application.userPreferenceRepository)
            }
        }
    }
}