package com.polstat.singadu.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polstat.singadu.R
import com.polstat.singadu.model.Report
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    showSpinner: () -> Unit = {},
    showMessage: (Int, Int) -> Unit = { _, _ -> },
    navigateToEditScreen: (Long) -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.daftar_laporan),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        TextField(
            value = query,
            onValueChange = {
                query = it
                homeViewModel.filterReports(query)
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
            label = {
                Text(text = stringResource(id = R.string.cari))
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.padding(5.dp))

        ReportsList(
            reportsUiState = homeViewModel.reportsUiState,
            isAdmin = homeViewModel.isAdmin(),
            isSupervisor = homeViewModel.isSupervisor(),
            onDeleteClicked = { report ->
                showSpinner()
                scope.launch {
                    when (homeViewModel.deleteReport(report)) {
                        ReportOperationResult.Success -> {
                            showMessage(R.string.sukses, R.string.berhasil_hapus_laporan)
                            homeViewModel.getAllReports()
                        }
                        ReportOperationResult.Error -> showMessage(R.string.error, R.string.network_error)
                    }
                }
            },
            onChangeStatusClicked = { report ->
                showSpinner()
                scope.launch {
                    when (homeViewModel.changeReportStatus(report)) {
                        ReportOperationResult.Success -> {
                            showMessage(R.string.sukses, R.string.berhasil_ubah_status)
                            homeViewModel.getAllReports()
                        }
                        ReportOperationResult.Error -> showMessage(R.string.error, R.string.network_error)
                    }
                }
            },
            onEditClicked = { report ->
                report.id?.let { navigateToEditScreen(it) }
            }
        )
    }
}

@Composable
fun ReportsList(
    reportsUiState: ReportsUiState,
    isAdmin: Boolean,
    isSupervisor: Boolean,
    onDeleteClicked: (Report) -> Unit = {},
    onChangeStatusClicked: (Report) -> Unit = {},
    onEditClicked: (Report) -> Unit = {}
) {
    when (reportsUiState) {
        is ReportsUiState.Error -> {
            Text(text = stringResource(id = R.string.error))
        }
        is ReportsUiState.Loading -> {
            Text(text = "Loading")
        }
        is ReportsUiState.Success -> {
            val reports = reportsUiState.reports
            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                items(items = reports) { report ->
                    ReportItemCard(
                        description = report.description,
                        problemType = report.problemType.name,
                        reporter = report.reporter!!.name,
                        reportedDate = toLocalFormat(report.reportedDate),
                        status = if (report.solved) stringResource(id = R.string.selesai) else stringResource(id = R.string.belum_selesai),
                        options = {
                            Column {
                                if (isAdmin) {
                                    DrawerNavigationItem(
                                        icons = Icons.Filled.Delete,
                                        text = R.string.hapus_laporan,
                                        onClick = { onDeleteClicked(report) }
                                    )
                                } else if (isSupervisor) {
                                    DrawerNavigationItem(
                                        icons = Icons.Filled.Info,
                                        text = R.string.ubah_status,
                                        onClick = { onChangeStatusClicked(report) }
                                    )
                                } else {
                                    DrawerNavigationItem(
                                        icons = Icons.Filled.Edit,
                                        text = R.string.edit_laporan,
                                        onClick = { onEditClicked(report) }
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

fun toLocalFormat(dateString: String): String {
    val date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
    return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(Locale.getDefault()))
}