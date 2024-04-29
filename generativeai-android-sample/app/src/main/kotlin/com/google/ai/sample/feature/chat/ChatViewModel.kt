/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.ai.sample.feature.chat

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.asTextOrNull
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import com.google.ai.sample.TextToSpeechWrapper

class ChatViewModel(
    generativeModel: GenerativeModel
) : ViewModel() {


    private val chat = generativeModel.startChat(
        history = listOf(
            content(role = "user") { text("Your name is Doc Sage(male),  you are a mental health therapist,  your nature is uplifting, motivating, empathic, cheerful. You assist mainly in giving suggestion remedies and solutions and diagnosing mental health patients having ADHD, anxiety, depression, and other mental distress. give a concise answer like you are talking with the patients(users).You also have to create a brief questionnaire which consists of 10 multiple choice questions which help you to identify the mental disorder out of ADHD, anxiety and depression that the user is suffering from.  Don't write all the questions in a single response write them one by one. Always start with a greeting stating your name.Have a interactive feel with the user and don't try to write monologs STRICTLY always answer in less than 50 words.") },
            content(role = "model") { text("Hi, I'm Doc Sage ready to help you") }
        )
    )
    val history2 = listOf(
        content(role = "model") { text("Hi, I'm Doc Sage ready to help you") }
    )

    private val _uiState: MutableStateFlow<ChatUiState> =
        MutableStateFlow(ChatUiState(history2.map { content ->
            // Map the initial messages
            ChatMessage(
                text = content.parts.first().asTextOrNull() ?: "",
                participant = if (content.role == "user") Participant.USER else Participant.SAGE,
                isPending = false
            )
        }))
    val uiState: StateFlow<ChatUiState> =
        _uiState.asStateFlow()


    fun sendMessage(userMessage: String) {
        // Add a pending message
        _uiState.value.addMessage(
            ChatMessage(
                text = userMessage,
                participant = Participant.USER,
                isPending = true
            )
        )

        viewModelScope.launch {
            try {
                val response = chat.sendMessage(userMessage)
                _uiState.value.replaceLastPendingMessage()

                response.text?.let { modelResponse ->
                    _uiState.value.addMessage(
                        ChatMessage(
                            text = modelResponse,
                            participant = Participant.SAGE,
                            isPending = false
                        )
                    )

//                    textToSpeech.speak(modelResponse, TextToSpeech.QUEUE_FLUSH, null, null)

                }
            } catch (e: Exception) {
                _uiState.value.replaceLastPendingMessage()
                _uiState.value.addMessage(
                    ChatMessage(
                        text = e.localizedMessage,
                        participant = Participant.ERROR
                    )
                )
            }
        }
    }

//    override fun onCleared() {
//        super.onCleared()
//        textToSpeech.stop()
//        textToSpeech.shutdown()
//    }
}
