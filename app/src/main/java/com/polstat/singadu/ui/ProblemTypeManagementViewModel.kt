package com.polstat.singadu.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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
import java.io.IOException

private const val TAG = "ProblemTypeManagementViewModel"

class ProblemTypeManagementViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val problemTypeRepository: ProblemTypeRepository
) : ViewModel() {

    private lateinit var token: String

    var problemTypeUiState: ProblemTypeUiState by mutableStateOf(ProblemTypeUiState.Loading)
        private set

    var selectedId: Long by mutableLongStateOf(0)

    init {
        viewModelScope.launch {
            userPreferencesRepository.user.collect { user ->
                token = user.token
            }
        }
        getProblemTypes()
    }

    fun getProblemTypes() {
        viewModelScope.launch {
            problemTypeUiState = ProblemTypeUiState.Loading
            problemTypeUiState = try {
                ProblemTypeUiState.Success(problemTypeRepository.getProblemTypes(token))
            } catch(e: IOException) {
                Log.e(TAG, "IOException: ${e.message}")
                ProblemTypeUiState.Error
            } catch (e: Exception) {
                Log.e(TAG, "Exception: ${e.message}")
                ProblemTypeUiState.Error
            }
        }
    }

    suspend fun deleteProblemType(): DeleteProblemTypeResult {
        try {
            problemTypeRepository.deleteProblemType(token, selectedId)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
            return DeleteProblemTypeResult.Error
        }

        return DeleteProblemTypeResult.Success
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SingaduApplication)
                val problemTypeRepository = application.container.problemTypeRepository
                ProblemTypeManagementViewModel(
                    application.userPreferenceRepository,
                    problemTypeRepository
                )
            }
        }
    }

}

sealed interface ProblemTypeUiState {
    data class Success(val problemTypes: List<ProblemType>): ProblemTypeUiState
    object Error: ProblemTypeUiState
    object Loading: ProblemTypeUiState
}

enum class DeleteProblemTypeResult {
    Success,
    Error
}