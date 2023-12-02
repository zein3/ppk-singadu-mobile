package com.polstat.singadu.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polstat.singadu.ui.theme.SingaduTheme

@Composable
fun ItemCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)) {
        Card(
            modifier = modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 15.dp)
            ) {
                // three button

                Text(
                    text = title,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = description,
                )
            }
        }
    }
}

@Preview
@Composable
fun ItemCardPreview() {
    SingaduTheme {
        ItemCard(title = "title", description = "description")
    }
}