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
import com.polstat.singadu.data.ProblemTypeRepository
import com.polstat.singadu.data.UserPreferencesRepository
import com.polstat.singadu.model.ProblemType
import kotlinx.coroutines.launch

private const val TAG = "CreateProblemTypeViewModel"

class CreateProblemTypeViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val problemTypeRepository: ProblemTypeRepository
) : ViewModel() {
    private lateinit var token: String
    var nameField by mutableStateOf("")
        private set

    init {
        viewModelScope.launch {
            userPreferencesRepository.user.collect { user ->
                token = user.token
            }
        }
    }

    suspend fun createProblemType(): CreateProblemTypeResult {
        try {
            problemTypeRepository.createProblemType(token, ProblemType(
                id = null,
                name = nameField
            ))
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
            return CreateProblemTypeResult.Error
        }

        return CreateProblemTypeResult.Success
    }

    fun updateNameField(newName: String) {
        nameField = newName
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SingaduApplication)
                val problemTypeRepository = application.container.problemTypeRepository
                CreateProblemTypeViewModel(
                    userPreferencesRepository = application.userPreferenceRepository,
                    problemTypeRepository = problemTypeRepository
                )
            }
        }
    }
}

enum class CreateProblemTypeResult {
    Success,
    Error
}