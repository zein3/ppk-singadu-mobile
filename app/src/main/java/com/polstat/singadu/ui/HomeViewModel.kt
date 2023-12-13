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
import com.polstat.singadu.data.ReportRepository
import com.polstat.singadu.data.UserPreferencesRepository
import com.polstat.singadu.data.UserRepository
import com.polstat.singadu.data.UserState
import com.polstat.singadu.model.Report
import com.polstat.singadu.model.User
import kotlinx.coroutines.launch

private const val TAG = "HomeViewModel"

class HomeViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userRepository: UserRepository,
    private val reportRepository: ReportRepository
) : ViewModel() {

    private lateinit var reports: List<Report>
    private lateinit var userState: UserState
    private lateinit var user: User
    var reportsUiState: ReportsUiState by mutableStateOf(ReportsUiState.Loading)
        private set

    init {
        viewModelScope.launch {
            userPreferencesRepository.user.collect {
                userState = it
                try {
                    user = userRepository.getProfile(it.token)
                    getAllReports()
                } catch (e: Exception) {
                    Log.e(TAG, e.stackTraceToString())
                }
            }
        }
    }

    private suspend fun getAllReports() {
        try {
            if (userState.isAdmin) {
                reports = reportRepository.getAllReports(userState.token)
            } else if (userState.isEnumerator) {
                reports = reportRepository.getReportsByUser(userState.token, user.id!!)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
            return
        }

        Log.d(TAG, "reports: $reports")
        reportsUiState = ReportsUiState.Success(reports)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SingaduApplication)
                HomeViewModel(
                    userPreferencesRepository = application.userPreferenceRepository,
                    userRepository = application.container.userRepository,
                    reportRepository = application.container.reportRepository
                )
            }
        }
    }

}

sealed interface ReportsUiState {
    data class Success(val reports: List<Report>): ReportsUiState
    object Error: ReportsUiState
    object Loading: ReportsUiState
}