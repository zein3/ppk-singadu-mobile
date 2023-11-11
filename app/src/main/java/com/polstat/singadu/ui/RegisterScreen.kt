package com.polstat.singadu.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polstat.singadu.R
import com.polstat.singadu.ui.theme.SingaduTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBackButtonClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Card {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier.padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp)
            ) {

                Image(painter = painterResource(
                    id = R.drawable.bps),
                    contentDescription = stringResource(id = R.string.logo_bps),
                    modifier = Modifier.size(128.dp)
                )
                
                Spacer(modifier = Modifier.padding(8.dp))

                Text(
                    text = stringResource(id = R.string.register),
                    style = TextStyle(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 26.sp
                    )
                )

                Spacer(modifier = Modifier.padding(12.dp))

                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text(text = stringResource(id = R.string.nama)) }
                )

                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text(text = stringResource(id = R.string.email)) }
                )

                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text(text = stringResource(id = R.string.password)) },
                )

                Spacer(modifier = Modifier.padding(top = 24.dp))

                Button(
                    onClick = { /*TODO*/ }
                ) {
                    Text(text = stringResource(id = R.string.register))
                }
                
                Spacer(modifier = Modifier.padding(8.dp))

                Text(
                    text = stringResource(id = R.string.kembali),
                    style = TextStyle(
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp,
                        textDecoration = TextDecoration.Underline,
                        color = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier.clickable { onBackButtonClicked() }
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RegisterScreenPreview() {
    SingaduTheme {
        RegisterScreen()
    }
}