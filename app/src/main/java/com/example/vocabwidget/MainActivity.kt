package com.example.vocabwidget

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.vocabwidget.ui.NavGraph
import com.example.vocabwidget.ui.theme.VocabWidgetTheme
import java.util.Locale

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(this, this)
        enableEdgeToEdge()
        setContent {
            val vocabViewModel: VocabViewModel = viewModel()
            val navController = rememberNavController()

            VocabWidgetTheme(darkTheme = vocabViewModel.isDarkMode.value) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(
                        navController = navController,
                        viewModel = vocabViewModel,
                        onSpeak = { speak(it) },
                        onShare = { shareWord(it) },
                        onExport = { exportVocabulary(it) }
                    )
                }
            }
        }
    }

    private fun speak(text: String) {
        tts?.setSpeechRate(getSharedPreferences("vocab_prefs", MODE_PRIVATE).getFloat("speech_rate", 1.0f))
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun shareWord(word: Word) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Word of the Day: '${word.term}'\nDefinition: ${word.definition}")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, null))
    }

    private fun exportVocabulary(mastered: List<String>) {
        val summary = "My Vocabulary Master List:\n\n" + mastered.joinToString("\n") { term ->
            val word = VocabRepository.getAllWords().find { it.term == term }
            "✓ $term: ${word?.definition ?: ""}"
        }
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, summary)
        }
        startActivity(Intent.createChooser(intent, "Export My Words"))
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) tts?.language = Locale.US
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}
