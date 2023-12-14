package com.polstat.singadu.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    var query by rememberSaveable { mutableStateOf("") }

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
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = stringResource(id = R.string.cari))
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.padding(5.dp))

        ReportsList(
            reportsUiState = homeViewModel.reportsUiState
        )
    }
}

@Composable
fun ReportsList(
    reportsUiState: ReportsUiState
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
                        status = if (report.solved) stringResource(id = R.string.selesai) else stringResource(id = R.string.belum_selesai)
                    )
                }
            }
        }
    }
}