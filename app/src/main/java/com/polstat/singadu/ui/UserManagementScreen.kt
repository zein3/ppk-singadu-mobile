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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.polstat.singadu.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManagementScreen(
    modifier: Modifier = Modifier,
    userManagementViewModel: UserManagementViewModel = viewModel(factory = UserManagementViewModel.Factory)
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(5.dp)
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
            userManagementUiState = userManagementViewModel.userManagementUiState
        )
    }
}

@Composable
fun UserList(
    userManagementUiState: UserManagementUiState
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
                        description = user.email
                    )
                }
            }
        }
    }
}