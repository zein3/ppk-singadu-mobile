package com.polstat.singadu.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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