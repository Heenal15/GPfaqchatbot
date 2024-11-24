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

    val messages = remember { mutableStateListOf(ChatMessage("Welcome to GP FAQ Chatbot! How can I assist you today?", isUser = false)) }
    var userInput by remember { mutableStateOf(TextFieldValue("")) }
    var relatedSuggestions by remember { mutableStateOf<List<String>>(emptyList()) } // List of related suggestions

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        // Top App Bar
        TopAppBar(
            backgroundColor = Color(0xFF00008B),
            contentColor = MaterialTheme.colors.onPrimary,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "GP FAQ Chatbot",
                    style = MaterialTheme.typography.h6
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

        // Related suggestions
        if (relatedSuggestions.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(relatedSuggestions) { suggestion ->
                    Button(
                        onClick = {
                            messages.add(ChatMessage(suggestion, isUser = true))
                            messages.add(
                                ChatMessage(
                                    generateBotResponse(suggestion, faqList),
                                    isUser = false
                                )
                            )
                            relatedSuggestions = emptyList() // Clear suggestions after selection
                        },
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                    ) {
                        Text(text = suggestion)
                    }
                }
            }
        }

        // Input Section
        InputSection(
            userInput = userInput,
            onUserInputChange = { userInput = it },
            onSend = {
                if (userInput.text.isNotBlank()) {
                    val userText = userInput.text
                    messages.add(ChatMessage(userText, isUser = true))

                    val botResponse = generateBotResponse(userText, faqList)
                    if (botResponse.startsWith("Sorry")) {
                        // Fetch related suggestions from FAQ list
                        val suggestions = getSpecificKeywordSuggestions(userText, faqList)
                        if (suggestions.isNotEmpty()) {
                            relatedSuggestions = suggestions
                        } else {
                            messages.add(ChatMessage(botResponse, isUser = false))
                            relatedSuggestions = emptyList()
                        }
                    } else {
                        messages.add(ChatMessage(botResponse, isUser = false))
                        relatedSuggestions = emptyList()
                    }

                    userInput = TextFieldValue("")
                }
            }
        )
    }
}

// Function to fetch FAQs containing the specific keyword
fun getSpecificKeywordSuggestions(userMessage: String, faqList: List<FAQ>): List<String> {
    val keywords = mapOf(
        "appointment" to "appointment",
        "prescription" to "prescription",
        "medical records" to "medical records",
        "sick note" to "sick note"
    )

    // Find the keyword in the user message
    val detectedKeyword = keywords.keys.firstOrNull { userMessage.contains(it, ignoreCase = true) }

    // If a keyword is detected, filter FAQ questions that contain the keyword
    return if (detectedKeyword != null) {
        faqList.filter { faq ->
            faq.question.contains(keywords[detectedKeyword]!!, ignoreCase = true)
        }.map { it.question }.take(3) // Limit to 3 questions
    } else {
        emptyList() // No keyword found
    }
}

fun generateBotResponse(userMessage: String, faqList: List<FAQ>): String {
    val similarity = JaroWinkler()
    val threshold = 0.85 // Adjust threshold for stricter or looser matching

    // Normalize user input
    val normalizedInput = userMessage.trim().lowercase()

    // Handle specific user greetings
    if (isGreeting(normalizedInput)) {
        return "Hi there! I'm doing great, thank you. How can I assist you today?"
    }

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

// Function to detect user greetings
fun isGreeting(message: String): Boolean {
    val greetings = listOf("hi", "hello", "hey", "how are you", "hi there", "hello there")
    return greetings.any { greeting ->
        message.contains(greeting)
    }
}



