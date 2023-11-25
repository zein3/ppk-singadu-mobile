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
import com.polstat.singadu.data.UserRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    var nameField by mutableStateOf("")
        private set

    var oldPasswordField by mutableStateOf("")
        private set

    var newPasswordField by mutableStateOf("")
        private set

    var confirmPasswordField by mutableStateOf("")
        private set

    init {
        // TODO: atur nameField
    }

    fun updateNameField(name: String) {
        nameField = name
    }

    fun updateOldPasswordField(password: String) {
        oldPasswordField = password
    }

    fun updateNewPasswordField(password: String) {
        newPasswordField = password
    }

    fun updateConfirmPasswordField(password: String) {
        confirmPasswordField = password
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SingaduApplication)
                ProfileViewModel(
                    userPreferencesRepository = application.userPreferenceRepository,
                    userRepository = application.container.userRepository
                )
            }
        }
    }
}