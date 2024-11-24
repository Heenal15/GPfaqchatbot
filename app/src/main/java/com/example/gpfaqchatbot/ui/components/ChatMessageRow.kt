package com.example.gpfaqchatbot.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gpfaqchatbot.ui.ChatMessage

@Composable
fun ChatMessageRow(chatMessage: ChatMessage) {
    val isUser = chatMessage.isUser

    // Define colors
    val bubbleColor = if (isUser) Color(0xFFD8F3DC) else Color(0xFFD8EFFF) // Light blue for bot
    val textColor = Color(0xFF000000) // Black text for bot

    // Outer Box for alignment
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .wrapContentSize(if (isUser) Alignment.CenterEnd else Alignment.CenterStart) // Aligns messages
    ) {
        // Inner Box for bubble styling
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isUser) 16.dp else 0.dp,
                        bottomEnd = if (isUser) 0.dp else 16.dp
                    )
                )
                .background(bubbleColor) // Different color for bot vs user
                .padding(12.dp)
        ) {
            // Message text
            Text(
                text = chatMessage.message,
                color = textColor,
                style = TextStyle(fontSize = 16.sp)
            )
        }
    }
}

