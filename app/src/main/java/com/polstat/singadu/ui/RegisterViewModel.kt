package com.polstat.singadu.ui

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.polstat.singadu.SingaduApplication
import com.polstat.singadu.data.UserRepository
import com.polstat.singadu.model.RegisterForm
import kotlinx.coroutines.launch

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

    fun register(onDone: () -> Unit) {
        if (passwordField != confirmPasswordField) {
            // TODO: add error message
            onDone()
            return
        }
        viewModelScope.launch {
            // TODO: add error handler
            userRepository.register(RegisterForm(nameField, emailField, passwordField))

            onDone()
        }
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