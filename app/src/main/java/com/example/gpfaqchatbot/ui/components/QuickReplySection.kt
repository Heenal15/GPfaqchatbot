package com.example.gpfaqchatbot.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuickReplySection(options: List<String>, onOptionSelected: (String) -> Unit) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(options) { option ->
            Button(
                onClick = { onOptionSelected(option) },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
            ) {
                Text(option, color = MaterialTheme.colors.onSecondary)
            }
        }
    }
}
