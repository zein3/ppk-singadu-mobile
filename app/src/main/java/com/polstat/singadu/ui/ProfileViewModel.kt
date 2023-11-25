package com.polstat.singadu.ui

import android.util.Log
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
import com.polstat.singadu.model.User
import kotlinx.coroutines.launch

private const val TAG = "ProfileViewModel"

class ProfileViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private lateinit var token: String
    private lateinit var email: String

    var nameField by mutableStateOf("")
        private set

    var oldPasswordField by mutableStateOf("")
        private set

    var newPasswordField by mutableStateOf("")
        private set

    var confirmPasswordField by mutableStateOf("")
        private set

    init {
        viewModelScope.launch {
            userPreferencesRepository.user.collect { user ->
                token = user.token
                nameField = user.name
                email = user.email
            }
        }
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

    suspend fun updateProfile(): UpdateProfileResult {
        try {
            userRepository.updateProfile(
                token = token,
                user = User(
                    id = null,
                    name = nameField,
                    email = email,
                    password = "password ga dicek",
                    roles = null,
                    supervisor = null
                )
            )

            userPreferencesRepository.saveName(nameField)
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
            return UpdateProfileResult.Error
        }

        return UpdateProfileResult.Success
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

enum class UpdateProfileResult {
    Success,
    Error
}