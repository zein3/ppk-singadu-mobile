package com.polstat.singadu.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun EditUserScreen(
    modifier: Modifier = Modifier,
    editUserViewModel: EditUserViewModel = viewModel(factory = EditUserViewModel.Factory)
) {
    Text(text = "User id: ${editUserViewModel.itemId.toString()}")
}