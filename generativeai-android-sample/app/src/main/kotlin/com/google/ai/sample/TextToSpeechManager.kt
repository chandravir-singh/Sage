package com.google.ai.sample

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

object TextToSpeechManager {
    private lateinit var textToSpeech: TextToSpeech
    private var isTextToSpeechInitialized = false
    //here you can give male if you want to select male voice.

    fun initialize(context: Context) {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isTextToSpeechInitialized = true
                for (voice in textToSpeech.getVoices()) {
                    if (voice.name == "en-us-x-sfg#male_1-local") {
                        // Set male voice
                        textToSpeech.setVoice(voice)
                        break
                    }
                }
                textToSpeech.setSpeechRate(0.8f);
            }
        }
    }

    fun speak(text: String) {
        if (isTextToSpeechInitialized) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    fun stop() {
        if (isTextToSpeechInitialized) {
            textToSpeech.stop()
        }
    }

    fun shutdown() {
        if (isTextToSpeechInitialized) {
            textToSpeech.shutdown()
            isTextToSpeechInitialized = false
        }
    }
}

@Composable
fun TextToSpeechWrapper(
    speakText: String,
    speakEnable: Boolean
) {
    LaunchedEffect(key1 = speakEnable) {
        if (speakEnable) {
            TextToSpeechManager.speak(speakText)
        } else {
            TextToSpeechManager.shutdown()
        }
    }
}
