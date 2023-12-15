package com.polstat.singadu.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.polstat.singadu.SingaduApplication
import com.polstat.singadu.data.ProblemTypeRepository
import com.polstat.singadu.data.ReportRepository
import com.polstat.singadu.data.UserPreferencesRepository
import com.polstat.singadu.model.ProblemType
import com.polstat.singadu.model.Report
import kotlinx.coroutines.launch
import java.time.LocalDate

private const val TAG = "EditReportViewModel"

class EditReportViewModel(
    savedStateHandle: SavedStateHandle,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val reportRepository: ReportRepository,
    private val problemTypeRepository: ProblemTypeRepository
) : ViewModel() {
    var allProblemTypes: List<ProblemType> = listOf()
    var report by mutableStateOf(
        Report(
            id = null,
            description = "",
            reporter = null,
            problemType = ProblemType(id = null, name = ""),
            reportedDate = "",
            createdOn = null,
            updatedOn = null
        )
    )

    private lateinit var token: String
    private val reportId: Long = checkNotNull(savedStateHandle["reportId"])

    var deskripsi by mutableStateOf("")
        private set
    var reportedDate by mutableStateOf(LocalDate.now().toString())
        private set
    var selectedProblemTypeId by mutableLongStateOf(0)
        private set
    var selectedProblemTypeName by mutableStateOf("")
        private set

    init {
        viewModelScope.launch {
            userPreferencesRepository.user.collect { user ->
                token = user.token
                try {
                    report = reportRepository.getReportById(user.token, reportId)
                    allProblemTypes = problemTypeRepository.getProblemTypes(user.token)

                    deskripsi = report.description
                    reportedDate = report.reportedDate
                    selectedProblemTypeId = report.problemType.id!!
                    selectedProblemTypeName = report.problemType.name
                } catch (e: Exception) {
                    Log.e(TAG, "Error: ${e.message}")
                }
            }
        }
    }

    fun updateDeskripsi(newDeskripsi: String) {
        deskripsi = newDeskripsi
    }

    fun updateReportedDate(newDate: String) {
        reportedDate = newDate
    }

    fun setSelectedProblemType(ptype: ProblemType) {
        selectedProblemTypeId = ptype.id ?: 0
        selectedProblemTypeName = ptype.name
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SingaduApplication)
                EditReportViewModel(
                    this.createSavedStateHandle(),
                    userPreferencesRepository = application.userPreferenceRepository,
                    reportRepository = application.container.reportRepository,
                    problemTypeRepository = application.container.problemTypeRepository
                )
            }
        }
    }

}