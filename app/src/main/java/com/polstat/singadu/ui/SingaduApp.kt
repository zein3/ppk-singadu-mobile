package com.polstat.singadu.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.polstat.singadu.R
import com.polstat.singadu.ui.theme.SingaduTheme

enum class SingaduScreen() {
    Login,
    Register,
    Home
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingaduApp(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val showTopBar = when (navBackStackEntry?.destination?.route) {
        SingaduScreen.Login.name, SingaduScreen.Register.name -> false
        else -> true
    }

    Scaffold(
        topBar = {
            if (showTopBar) {
                SingaduAppBar()
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = SingaduScreen.Login.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = SingaduScreen.Login.name) {
                LoginScreen(
                    onLoginButtonClicked = { navController.navigate(SingaduScreen.Home.name) },
                    onRegisterButtonClicked = { navController.navigate(SingaduScreen.Register.name) }
                )
            }

            composable(route = SingaduScreen.Register.name) {
                RegisterScreen(
                    onBackButtonClicked = { navController.navigate(SingaduScreen.Login.name) }
                )
            }

            composable(route = SingaduScreen.Home.name) {
                HomeScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingaduAppBar(
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bps),
                    contentDescription = stringResource(id = R.string.logo_bps),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.padding(6.dp))
                Text(text = stringResource(id = R.string.app_name))
            }
        },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = stringResource(id = R.string.menu)
                )
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = stringResource(id = R.string.profile)
                )
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
fun SingaduAppBarPreview() {
    SingaduTheme {
        SingaduAppBar()
    }
}