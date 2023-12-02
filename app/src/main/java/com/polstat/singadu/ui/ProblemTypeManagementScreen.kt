package com.polstat.singadu.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.polstat.singadu.R

@Composable
fun ProblemTypeManagementScreen(
    modifier: Modifier = Modifier,
    problemTypeViewModel: ProblemTypeManagementViewModel = viewModel(factory = ProblemTypeManagementViewModel.Factory)
) {
    ProblemTypeManagementScreenContent(
        problemTypeUiState = problemTypeViewModel.problemTypeUiState
    )
}

@Composable
private fun ProblemTypeManagementScreenContent(
    problemTypeUiState: ProblemTypeUiState,
    modifier: Modifier = Modifier
) {
    when(problemTypeUiState) {
        is ProblemTypeUiState.Error -> {
            Text(text = "Error")
        }
        is ProblemTypeUiState.Loading -> {
            Text(text = "Loading")
        }
        is ProblemTypeUiState.Success -> {
            val problemTypes = problemTypeUiState.problemTypes
            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                items(items = problemTypes) { problemType ->
                    ItemCard(title = problemType.name, description = "")
                }
            }
        }
    }
}