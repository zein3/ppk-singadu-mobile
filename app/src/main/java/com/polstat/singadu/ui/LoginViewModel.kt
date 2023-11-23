package com.polstat.singadu.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.polstat.singadu.SingaduApplication
import com.polstat.singadu.data.UserPreferencesRepository
import com.polstat.singadu.data.UserRepository
import com.polstat.singadu.model.LoginForm
import retrofit2.HttpException

private const val TAG = "LoginViewModel"

class LoginViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    var emailField by mutableStateOf("")
        private set

    var passwordField by mutableStateOf("")
        private set

    fun updateEmail(email: String) {
        emailField = email
    }

    fun updatePassword(password: String) {
        passwordField = password
    }

    suspend fun attemptLogin(): LoginResult {
        try {
            val loginResponse = userRepository.login(LoginForm(emailField, passwordField))
            Log.d(TAG, "accessToken: ${loginResponse.accessToken}")

            userPreferencesRepository.saveToken(loginResponse.accessToken)

            val user = userRepository.getProfile(loginResponse.accessToken)
            val isAdmin = user.roles?.any { role -> role.name == "ROLE_ADMIN" }
            val isSupervisor = user.roles?.any { role -> role.name == "ROLE_PENGAWAS" }
            val isEnumerator = user.roles?.any { role -> role.name == "ROLE_PENCACAH" }

            Log.d(TAG, "name: ${user.name}")
            Log.d(TAG, "email: ${user.email}")
            Log.d(TAG, "isAdmin: $isAdmin")
            Log.d(TAG, "isSupervisor: $isSupervisor")
            Log.d(TAG, "isEnumerator: $isEnumerator")

            userPreferencesRepository.saveName(user.name)
            userPreferencesRepository.saveEmail(user.email)
            userPreferencesRepository.saveIsAdmin(isAdmin ?: false)
            userPreferencesRepository.saveIsSupervisor(isSupervisor ?: false)
            userPreferencesRepository.saveIsEnumerator(isEnumerator ?: false)
        } catch(e: HttpException) {
            when (e.code()) {
                400 -> {
                    Log.d(TAG, "bad input")
                    return LoginResult.BadInput
                }
                401 -> {
                    Log.d(TAG, "Wrong email or password")
                    return LoginResult.WrongEmailOrPassword
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Can't login: (${e.javaClass}) ${e.message}")
            return LoginResult.NetworkError
        }

        return LoginResult.Success
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SingaduApplication)
                val userRepository = application.container.userRepository
                LoginViewModel(
                    userPreferencesRepository = application.userPreferenceRepository,
                    userRepository = userRepository
                )
            }
        }
    }

}

enum class LoginResult {
    Success,
    BadInput,
    WrongEmailOrPassword,
    NetworkError
}