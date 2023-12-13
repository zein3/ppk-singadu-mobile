package com.polstat.singadu.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
    modifier: Modifier = Modifier,
    options: @Composable (() -> Unit) = {}
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)) {
        Card(
            modifier = modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 15.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = { expanded = true },
                        modifier = Modifier.size(25.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = null
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            options()
                        }
                    }
                }

                Text(
                    text = description,
                )
            }
        }
    }
}

@Composable
fun ReportItemCard(
    description: String,
    problemType: String,
    reporter: String,
    status: String,
    modifier: Modifier = Modifier,
    options: @Composable (() -> Unit) = {}
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)) {
        Card(
            modifier = modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 15.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "$problemType - $reporter",
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = { expanded = true },
                        modifier = Modifier.size(25.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = null
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            options()
                        }
                    }
                }
                
                Spacer(modifier = Modifier.padding(5.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = description,
                    )

                    Text(
                        text = status,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                    )
                }
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