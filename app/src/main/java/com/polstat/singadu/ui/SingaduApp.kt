package com.polstat.singadu.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.polstat.singadu.R
import com.polstat.singadu.ui.theme.SingaduTheme
import kotlinx.coroutines.launch

enum class SingaduScreen {
    Login,
    Register,
    Home
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingaduApp(
    navController: NavHostController = rememberNavController(),
    singaduAppViewModel: SingaduAppViewModel = viewModel(factory = SingaduAppViewModel.Factory)
) {
    val loggedInUser = singaduAppViewModel.userState.collectAsState().value

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var showProgressDialog by rememberSaveable { mutableStateOf(false) }
    var showMessageDialog by rememberSaveable { mutableStateOf(false) }
    var messageTitle by rememberSaveable { mutableIntStateOf(R.string.error) }
    var messageBody by rememberSaveable { mutableIntStateOf(R.string.error) }

    val showSpinner: () -> Unit = {
        showProgressDialog = true
    }

    val showMessage: (Int, Int) -> Unit = { newMessageTitle, newMessageBody ->
        messageTitle = newMessageTitle
        messageBody = newMessageBody
        showProgressDialog = false
        showMessageDialog = true
    }

    val showTopBar = when (navBackStackEntry?.destination?.route) {
        SingaduScreen.Login.name, SingaduScreen.Register.name -> false
        else -> true
    }

    if (showProgressDialog) {
        ProgressDialog(onDismissRequest = { showProgressDialog = false })
    }
    if (showMessageDialog) {
        MessageDialog(
            onDismissRequest = { showMessageDialog = !showMessageDialog },
            onClose = { showMessageDialog = !showMessageDialog },
            title = messageTitle,
            message = messageBody
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                SingaduDrawer(
                    navController = navController,
                    closeDrawer = {
                        scope.launch {
                            drawerState.apply {
                                close()
                            }
                        }
                    }
                )
            }
        }
    ) {

        Scaffold(
            topBar = {
                if (showTopBar) {
                    SingaduAppBar(
                        onMenuClicked = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = SingaduScreen.Login.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = SingaduScreen.Login.name) {
                    LoginScreen(
                        onLoginSuccess = {
                            showProgressDialog = false
                            navController.navigate(SingaduScreen.Home.name)
                        },
                        onRegisterButtonClicked = { navController.navigate(SingaduScreen.Register.name) },
                        showSpinner = showSpinner,
                        showMessage = showMessage
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

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingaduAppBar(
    modifier: Modifier = Modifier,
    onMenuClicked: () -> Unit = {}
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
            IconButton(onClick = onMenuClicked) {
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

@Composable
fun SingaduDrawer(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    closeDrawer: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = closeDrawer) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.kembali)
                )
            }
            
            Text(
                text = stringResource(id = R.string.menu)
            )
        }

        DrawerNavigationItem(
            Icons.Filled.Home,
            text = R.string.menu_beranda
        ) {
            closeDrawer()
        }
        DrawerNavigationItem(
            Icons.Filled.Face,
            text = R.string.menu_manajemen_user
        ) {
            closeDrawer()
        }
        DrawerNavigationItem(
            icons = Icons.Filled.MailOutline,
            text = R.string.menu_manajemen_jenis_masalah
        ) {
            closeDrawer()
        }
        DrawerNavigationItem(
            Icons.Filled.Settings,
            text = R.string.menu_pengaturan
        ) {
            closeDrawer()
        }
        DrawerNavigationItem(
            icons = Icons.Filled.ExitToApp,
            text = R.string.logout
        ) {
            navController.navigate(SingaduScreen.Login.name)
            closeDrawer()
        }
    }
}

@Composable
fun DrawerNavigationItem(
    icons: ImageVector,
    @StringRes text: Int,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Icon(
            icons,
            contentDescription = null
        )

        Spacer(modifier = Modifier.padding(4.dp))

        Text(
            text = stringResource(id = text)
        )
    }
}

@Preview
@Composable
fun SingaduAppBarPreview() {
    SingaduTheme {
        SingaduAppBar()
    }
}

@Preview
@Composable
fun SingaduDrawerPreview() {
    SingaduTheme {
        SingaduDrawer(
            navController = rememberNavController()
        )
    }
}