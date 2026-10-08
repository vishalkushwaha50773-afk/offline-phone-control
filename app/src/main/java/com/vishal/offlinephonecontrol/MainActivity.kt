package com.vishal.offlinephonecontrol

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.RecognitionListener
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.util.Locale

class MainActivity : Activity(), TextToSpeech.OnInitListener {

    private lateinit var statusText: TextView
    private lateinit var speakButton: Button
    private lateinit var stopButton: Button
    private lateinit var tts: TextToSpeech

    private var speechRecognizer: SpeechRecognizer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.txtStatus)
        speakButton = findViewById(R.id.btnStart)
        stopButton = findViewById(R.id.btnStop)

        tts = TextToSpeech(this, this)

        speakButton.setOnClickListener {
            startListening()
        }

        stopButton.setOnClickListener {
            speechRecognizer?.stopListening()
            statusText.text = "Assistant is Stopped"
            Toast.makeText(this, "Assistant Stopped", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale("hi", "IN")
        }
    }

    private fun startListening() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            statusText.text = "Speech recognition unavailable"
            Toast.makeText(
                this,
                "Speech recognition is not available",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        speechRecognizer?.destroy()
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                statusText.text = "Listening..."
            }

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                statusText.text = "Processing..."
            }

            override fun onError(error: Int) {
                statusText.text = "Try again"
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(
                    SpeechRecognizer.RESULTS_RECOGNITION
                )

                val command = matches?.firstOrNull()?.lowercase(Locale.ROOT) ?: ""

                statusText.text = "You said: $command"
                handleCommand(command)
            }

            override fun onPartialResults(partialResults: Bundle?) {}

            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN")
        }

        speechRecognizer?.startListening(intent)
    }

    private fun handleCommand(command: String) {
        when {
            command.contains("नमस्ते") || command.contains("hello") -> {
                speak("नमस्ते! मैं आपका असिस्टेंट हूँ।")
            }

            command.contains("समय") || command.contains("time") -> {
                val currentTime = java.text.SimpleDateFormat(
                    "hh:mm a",
                    Locale.getDefault()
                ).format(java.util.Date())

                speak("अभी समय है $currentTime")
            }

            command.contains("बंद") || command.contains("stop") -> {
                statusText.text = "Assistant is Stopped"
                speak("असिस्टेंट बंद किया जा रहा है।")
            }

            else -> {
                speak("माफ कीजिए, यह कमांड अभी मुझे समझ नहीं आई।")
            }
        }
    }

    private fun speak(message: String) {
        if (::tts.isInitialized) {
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, "assistant_reply")
        }
    }

    override fun onDestroy() {
        speechRecognizer?.destroy()

        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }

        super.onDestroy()
    }
}
