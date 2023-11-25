package com.polstat.singadu.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.polstat.singadu.SingaduApplication
import com.polstat.singadu.data.UserRepository
import com.polstat.singadu.model.RegisterForm

enum class RegisterResult {
    Success,
    EmptyField,
    PasswordMismatch,
    NetworkError
}

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    var nameField by mutableStateOf("")
        private set
    var emailField by mutableStateOf("")
        private set
    var passwordField by mutableStateOf("")
        private set
    var confirmPasswordField by mutableStateOf("")
        private set

    fun updateNameField(name: String) {
        nameField = name
    }

    fun updateEmailField(email: String) {
        emailField = email
    }

    fun updatePasswordField(password: String) {
        passwordField = password
    }

    fun updateConfirmPasswordField(password: String) {
        confirmPasswordField = password
    }

    suspend fun register(): RegisterResult {
        if (nameField == "" || emailField == "" || passwordField == "") {
            return RegisterResult.EmptyField
        }
        if (passwordField != confirmPasswordField) {
            return RegisterResult.PasswordMismatch
        }

        try {
            userRepository.register(RegisterForm(nameField, emailField, passwordField))
        } catch (e: Exception) {
            return RegisterResult.NetworkError
        }

        return RegisterResult.Success
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SingaduApplication)
                val userRepository = application.container.userRepository
                RegisterViewModel(userRepository = userRepository)
            }
        }
    }

}