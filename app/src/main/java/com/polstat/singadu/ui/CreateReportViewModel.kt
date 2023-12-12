package com.polstat.singadu.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import com.polstat.singadu.data.NetworkReportRepository
import com.polstat.singadu.data.ProblemTypeRepository
import com.polstat.singadu.data.ReportRepository
import com.polstat.singadu.data.UserPreferencesRepository
import com.polstat.singadu.model.CreateReportForm
import com.polstat.singadu.model.ProblemType
import com.polstat.singadu.model.Report
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

private const val TAG = "CreateReportViewModel"

class CreateReportViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val problemTypeRepository: ProblemTypeRepository,
    private val reportRepository: ReportRepository
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

    suspend fun createReport(): CreateReportResult {
        if (selectedProblemTypeId == 0L || deskripsi == "") {
            return CreateReportResult.BadInput
        }

        try {
            val report = CreateReportForm(
                description = deskripsi,
                reportedDate = LocalDateTime.now().toString(),
                solved = false,
                problemType = ProblemType(
                    id = selectedProblemTypeId,
                    name = selectedProblemTypeName
                )
            )
            Log.d(TAG, report.toString())
            reportRepository.createReport(token, report)
        } catch (e: Exception) {
            Log.e(TAG, e.stackTraceToString())
            return CreateReportResult.Error
        }

        return CreateReportResult.Success
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SingaduApplication)
                CreateReportViewModel(
                    userPreferencesRepository = application.userPreferenceRepository,
                    problemTypeRepository = application.container.problemTypeRepository,
                    reportRepository = application.container.reportRepository
                )
            }
        }
    }

}

enum class CreateReportResult {
    Success,
    BadInput,
    Error
}