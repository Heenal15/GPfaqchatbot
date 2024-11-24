package com.example.gpfaqchatbot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import com.example.gpfaqchatbot.ui.*
import com.example.gpfaqchatbot.ui.theme.GPFaqChatBotTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GPFaqChatBotTheme {
                Surface(color = MaterialTheme.colors.background) {
                    ChatScreen()
                }
            }
        }
    }
}