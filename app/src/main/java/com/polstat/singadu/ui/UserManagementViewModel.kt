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

private const val TAG = "UserManagementViewModel"

class UserManagementViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private lateinit var token: String

    private lateinit var users: List<User>
    var userManagementUiState: UserManagementUiState by mutableStateOf(UserManagementUiState.Loading)
        private set
    var searchQuery: String by mutableStateOf("")
        private set
    var selectedUserId: Long by mutableStateOf(0)

    init {
        viewModelScope.launch {
            userPreferencesRepository.user.collect { user ->
                token = user.token
            }
        }
        getAllUsers()
    }

    fun filterUsers(newQuery: String) {
        searchQuery = newQuery

        if (userManagementUiState is UserManagementUiState.Success) {
            val results = users.filter { user ->
                user.name.contains(searchQuery.trim(), true) ||
                user.email.contains(searchQuery.trim(), true)
            }
            userManagementUiState = UserManagementUiState.Success(results)
        }
    }

    fun getAllUsers() {
        viewModelScope.launch {
            userManagementUiState = UserManagementUiState.Loading
            try {
                users = userRepository.getAllUsers(token)
                userManagementUiState = UserManagementUiState.Success(users)
            } catch(e: Exception) {
                Log.e(TAG, "Exception: ${e.message}")
                userManagementUiState = UserManagementUiState.Error
            }
        }
    }

    suspend fun deleteUser(): DeleteUserResult {
        try {
            userRepository.deleteUser(token, selectedUserId)
        } catch (e: Exception) {
            Log.e(TAG, "exception: ${e.message}")
            return DeleteUserResult.Error
        }

        return DeleteUserResult.Success
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SingaduApplication)
                UserManagementViewModel(
                    userPreferencesRepository = application.userPreferenceRepository,
                    userRepository = application.container.userRepository
                )
            }
        }
    }
}

sealed interface UserManagementUiState {
    data class Success(val users: List<User>): UserManagementUiState
    object Error: UserManagementUiState
    object Loading: UserManagementUiState
}

enum class DeleteUserResult {
    Success,
    Error
}