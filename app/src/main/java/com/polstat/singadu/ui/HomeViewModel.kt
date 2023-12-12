package com.polstat.singadu.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.polstat.singadu.SingaduApplication
import com.polstat.singadu.data.UserPreferencesRepository
import com.polstat.singadu.data.UserState
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private var userData by mutableStateOf(UserState(token = "", name = "", email = "", isAdmin = false, isSupervisor = false, isEnumerator = false))

    init {
        viewModelScope.launch {
            userPreferencesRepository.user.collect { user ->
                userData = user
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SingaduApplication)
                HomeViewModel(
                    userPreferencesRepository = application.userPreferenceRepository
                )
            }
        }
    }

}