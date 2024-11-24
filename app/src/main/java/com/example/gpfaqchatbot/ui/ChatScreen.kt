package com.example.gpfaqchatbot.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.gpfaqchatbot.ui.ChatMessage
import com.example.gpfaqchatbot.FAQ
import com.example.gpfaqchatbot.ui.components.ChatMessageRow
import com.example.gpfaqchatbot.ui.components.QuickReplySection
import com.example.gpfaqchatbot.ui.components.InputSection
import com.example.gpfaqchatbot.util.readFAQsFromCSV
import info.debatty.java.stringsimilarity.JaroWinkler


@Composable
fun ChatScreen() {
    val context = LocalContext.current
    val faqList = remember { readFAQsFromCSV(context, "faq_data.csv") }
    LaunchedEffect(faqList) {
        println("Loaded FAQ List: $faqList")
    }

    val messages = remember { mutableStateListOf<ChatMessage>() }
    var userInput by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        // Centered Top App Bar
        TopAppBar(
            backgroundColor = Color(0xFF00008B),
            contentColor = MaterialTheme.colors.onPrimary,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center // Centers the content within the TopAppBar
            ) {
                Text(
                    text = "GP FAQ Chatbot",
                    style = MaterialTheme.typography.h6 // You can customize the style as needed
                )
            }
        }

        // Chat messages list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            contentPadding = PaddingValues(8.dp),
        ) {
            items(messages) { message ->
                ChatMessageRow(chatMessage = message)
            }
        }

        // Quick Replies
        QuickReplySection(
            options = listOf("How do I book an appointment?", "What are your opening hours?"),
            onOptionSelected = { option ->
                messages.add(ChatMessage(option, isUser = true))
                messages.add(
                    ChatMessage(
                        generateBotResponse(option, faqList),
                        isUser = false
                    )
                )
            }
        )

        // Input Section
        InputSection(
            userInput = userInput,
            onUserInputChange = { userInput = it },
            onSend = {
                if (userInput.text.isNotBlank()) {
                    messages.add(ChatMessage(userInput.text, isUser = true))
                    messages.add(
                        ChatMessage(
                            generateBotResponse(userInput.text, faqList),
                            isUser = false
                        )
                    )
                    userInput = TextFieldValue("")
                }
            }
        )
    }
}


fun generateBotResponse(userMessage: String, faqList: List<FAQ>): String {
    val similarity = JaroWinkler()
    val threshold = 0.85 // Adjust threshold for stricter or looser matching

    // Normalize user input
    val normalizedInput = userMessage.trim().lowercase()

    // Find the most similar question in the FAQ list
    val bestMatch = faqList.maxByOrNull {
        similarity.similarity(it.question.lowercase(), normalizedInput)
    }

    // Check if the similarity score meets the threshold
    return if (bestMatch != null && similarity.similarity(bestMatch.question.lowercase(), normalizedInput) >= threshold) {
        bestMatch.answer // Return the matched answer
    } else {
        "Sorry, I’m not sure how to help with that. Please contact our reception for more assistance."
    }
}



