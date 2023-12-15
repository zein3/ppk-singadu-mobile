package com.polstat.singadu.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditReportScreen(
    editReportViewModel: EditReportViewModel,
    showSpinner: () -> Unit,
    showMessage: (Int, Int) -> Unit,
    navigateBack: () -> Unit
) {
    var jenisMasalahExpanded by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Text(
            text = stringResource(id = R.string.form_edit_laporan),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.padding(5.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp, vertical = 10.dp)
            ) {
                TextField(
                    value = editReportViewModel.deskripsi,
                    onValueChange = { editReportViewModel.updateDeskripsi(it) },
                    label = {
                        Text(text = stringResource(id = R.string.deskripsi_masalah))
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = jenisMasalahExpanded,
                    onExpandedChange = { jenisMasalahExpanded = !jenisMasalahExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = editReportViewModel.selectedProblemTypeName,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = jenisMasalahExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        label = {
                            Text(text = stringResource(id = R.string.jenis_masalah))
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = jenisMasalahExpanded,
                        onDismissRequest = { jenisMasalahExpanded = false }
                    ) {
                        editReportViewModel.allProblemTypes.forEach { ptype ->
                            DropdownMenuItem(
                                text = { Text(text = ptype.name) },
                                onClick = {
                                    editReportViewModel.setSelectedProblemType(ptype)
                                    jenisMasalahExpanded = false
                                }
                            )
                        }
                    }
                }

                DatePicker(
                    label = stringResource(id = R.string.tanggal_kejadian),
                    value = editReportViewModel.reportedDate,
                    onValueChange = { editReportViewModel.updateReportedDate(it) }
                )

                Spacer(modifier = Modifier.padding(10.dp))

                Button(
                    onClick = {
                        showSpinner()
                        scope.launch {
                            when (editReportViewModel.editReport()) {
                                EditReportResult.Success -> {
                                    showMessage(R.string.sukses, R.string.berhasil_ubah_laporan)
                                    navigateBack()
                                }
                                EditReportResult.Error -> showMessage(R.string.error, R.string.network_error)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.edit_laporan))
                }
            }
        }
    }
}

