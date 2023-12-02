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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.polstat.singadu.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManagementScreen(
    modifier: Modifier = Modifier,
    userManagementViewModel: UserManagementViewModel = viewModel(factory = UserManagementViewModel.Factory),
    showSpinner: () -> Unit = {},
    showMessage: (Int, Int) -> Unit = { _, _ -> }
) {
    val scope = rememberCoroutineScope()
    var showConfirmDialog by rememberSaveable { mutableStateOf(false) }

    if (showConfirmDialog) {
        ConfirmDialog(
            onConfirmRequest = {
                showConfirmDialog = false
                showSpinner()
                scope.launch {
                    when (userManagementViewModel.deleteUser()) {
                        DeleteUserResult.Success -> {
                            showMessage(R.string.sukses, R.string.berhasil_hapus_akun_terpilih)
                            userManagementViewModel.getAllUsers()
                            userManagementViewModel.filterUsers(userManagementViewModel.searchQuery)
                        }
                        DeleteUserResult.Error -> {
                            showMessage(R.string.error, R.string.network_error)
                        }
                    }
                }
            },
            onDismissRequest = { showConfirmDialog = false },
            message = R.string.hapus_akun
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        TextField(
            value = userManagementViewModel.searchQuery,
            onValueChange = { userManagementViewModel.filterUsers(it) },
            singleLine = true,
            placeholder = {
                Text(text = stringResource(R.string.cari))
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(5.dp))

        UserList(
            userManagementUiState = userManagementViewModel.userManagementUiState,
            onDeleteClicked = { selectedUserId ->
                userManagementViewModel.selectedUserId = selectedUserId
                showConfirmDialog = true
            }
        )
    }
}

@Composable
fun UserList(
    userManagementUiState: UserManagementUiState,
    onDeleteClicked: (Long) -> Unit = {}
) {
    when(userManagementUiState) {
        is UserManagementUiState.Error -> {
            Text(text = stringResource(id = R.string.error))
        }
        is UserManagementUiState.Loading -> {
            Text(text = "Loading")
        }
        is UserManagementUiState.Success -> {
            val users = userManagementUiState.users
            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                items(items = users) { user ->
                    ItemCard(
                        title = user.name,
                        description = user.email,
                        options = {
                            Column {
                                DrawerNavigationItem(
                                    icons = Icons.Filled.Edit,
                                    text = R.string.edit_user
                                )
                                DrawerNavigationItem(
                                    icons = Icons.Filled.Delete,
                                    text = R.string.hapus_akun,
                                    onClick = { user.id?.let { onDeleteClicked(it) } }
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}