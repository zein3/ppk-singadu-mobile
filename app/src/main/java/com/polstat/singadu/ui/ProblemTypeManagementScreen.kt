package com.polstat.singadu.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.polstat.singadu.R
import kotlinx.coroutines.launch

@Composable
fun ProblemTypeManagementScreen(
    modifier: Modifier = Modifier,
    problemTypeViewModel: ProblemTypeManagementViewModel = viewModel(factory = ProblemTypeManagementViewModel.Factory),
    showMessage: (Int, Int) -> Unit = { _, _ -> },
    showSpinner: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    var showConfirmDialog by rememberSaveable { mutableStateOf(false) }

    if (showConfirmDialog) {
        ConfirmDialog(
            onConfirmRequest = {
                showConfirmDialog = false
                showSpinner()
                scope.launch {
                    when (problemTypeViewModel.deleteProblemType()) {
                        DeleteProblemTypeResult.Success -> {
                            showMessage(R.string.sukses, R.string.berhasil_hapus_jenis_masalah)
                            problemTypeViewModel.getProblemTypes()
                        }
                        DeleteProblemTypeResult.Error -> showMessage(R.string.error, R.string.network_error)
                    }
                }
            },
            onDismissRequest = { showConfirmDialog = false },
            message = R.string.hapus_jenis_masalah
        )
    }

    ProblemTypeManagementScreenContent(
        problemTypeUiState = problemTypeViewModel.problemTypeUiState,
        onDeleteClicked = { id ->
            problemTypeViewModel.selectedId = id
            showConfirmDialog = true
        }
    )
}

@Composable
private fun ProblemTypeManagementScreenContent(
    problemTypeUiState: ProblemTypeUiState,
    modifier: Modifier = Modifier,
    onDeleteClicked: (Long) -> Unit = {}
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
                    ItemCard(
                        title = problemType.name,
                        description = "",
                        options = {
                            Column {
                                DrawerNavigationItem(
                                    icons = Icons.Filled.Delete,
                                    text = R.string.hapus_jenis_masalah,
                                    onClick = { problemType.id?.let { onDeleteClicked(it) } }
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}