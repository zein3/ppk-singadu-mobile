package com.polstat.singadu.ui

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

class CreateReportViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val problemTypeRepository: ProblemTypeRepository
) : ViewModel() {

    private lateinit var token: String
    var allProblemTypes: List<ProblemType> = listOf()
    var deskripsi by mutableStateOf("")
        private set
    var selectedProblemTypeId by mutableLongStateOf(0)
        private set
    var selectedProblemTypeName by mutableStateOf("")
        private set

    init {
        viewModelScope.launch {
            userPreferencesRepository.user.collect { user ->
                token = user.token
                allProblemTypes = problemTypeRepository.getProblemTypes(token)
            }
        }
    }

    fun updateDeskripsi(newDeskripsi: String) {
        deskripsi = newDeskripsi
    }

    fun setSelectedProblemType(ptype: ProblemType) {
        selectedProblemTypeId = ptype.id ?: 0
        selectedProblemTypeName = ptype.name
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SingaduApplication)
                CreateReportViewModel(
                    userPreferencesRepository = application.userPreferenceRepository,
                    problemTypeRepository = application.container.problemTypeRepository
                )
            }
        }
    }

}