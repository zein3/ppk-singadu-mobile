package com.polstat.singadu.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.unit.dp
import com.polstat.singadu.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserScreen(
    editUserViewModel: EditUserViewModel,
    modifier: Modifier = Modifier,
    showSpinner: () -> Unit = {},
    showMessage: (Int, Int) -> Unit = { _, _ -> }
) {
    val userUiState = editUserViewModel.userUiState
    var supervisorDropdownExpanded by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        TextField(
            value = userUiState.name,
            onValueChange = {},
            enabled = false,
            label = {
                Text(text = stringResource(id = R.string.nama))
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(5.dp))

        TextField(
            value = userUiState.email,
            onValueChange = {},
            enabled = false,
            label = {
                Text(text = stringResource(id = R.string.email))
            },
            modifier = Modifier.fillMaxWidth()
        )

        EditUserRoleCheckbox(
            checked = editUserViewModel.userHasRole("ROLE_ADMIN"),
            onCheckedChange = { isAddingRole ->
                showSpinner()
                scope.launch {
                    when (editUserViewModel.updateUserRole("ROLE_ADMIN", isAddingRole)) {
                        UpdateUserRoleResult.Success -> showMessage(R.string.sukses, R.string.berhasil_ubah_role)
                        UpdateUserRoleResult.Error -> showMessage(R.string.error, R.string.network_error)
                    }
                }
            },
            text = stringResource(id = R.string.role_admin)
        )
        EditUserRoleCheckbox(
            checked = editUserViewModel.userHasRole("ROLE_PENGAWAS"),
            onCheckedChange = { isAddingRole ->
                showSpinner()
                scope.launch {
                    when (editUserViewModel.updateUserRole("ROLE_PENGAWAS", isAddingRole)) {
                        UpdateUserRoleResult.Success -> showMessage(R.string.sukses, R.string.berhasil_ubah_role)
                        UpdateUserRoleResult.Error -> showMessage(R.string.error, R.string.network_error)
                    }
                }
            },
            text = stringResource(id = R.string.role_pengawas)
        )
        EditUserRoleCheckbox(
            checked = editUserViewModel.userHasRole("ROLE_PENCACAH"),
            onCheckedChange = {isAddingRole ->
                showSpinner()
                scope.launch {
                    when (editUserViewModel.updateUserRole("ROLE_PENCACAH", isAddingRole)) {
                        UpdateUserRoleResult.Success -> showMessage(R.string.sukses, R.string.berhasil_ubah_role)
                        UpdateUserRoleResult.Error -> showMessage(R.string.error, R.string.network_error)
                    }
                }
            },
            text = stringResource(id = R.string.role_pencacah)
        )

        if (editUserViewModel.userHasRole("ROLE_PENCACAH")) {
            ExposedDropdownMenuBox(
                expanded = supervisorDropdownExpanded,
                onExpandedChange = { supervisorDropdownExpanded = !supervisorDropdownExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = editUserViewModel.supervisorName,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = supervisorDropdownExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    label = {
                        Text(text = stringResource(id = R.string.supervisor))
                    }
                )

                ExposedDropdownMenu(
                    expanded = supervisorDropdownExpanded,
                    onDismissRequest = { supervisorDropdownExpanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    editUserViewModel.allSupervisors.forEach { supervisor ->
                        DropdownMenuItem(
                            text = { Text(text = supervisor.name) },
                            onClick = {
                                editUserViewModel.setSelectedSupervisor(supervisor)
                                supervisorDropdownExpanded = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EditUserRoleCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)

        Text(text = text)
    }
}