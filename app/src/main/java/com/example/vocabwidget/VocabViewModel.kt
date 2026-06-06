package com.example.vocabwidget

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

class VocabViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPrefs = application.getSharedPreferences("vocab_prefs", Context.MODE_PRIVATE)

    // Observable states for UI
    val favorites = mutableStateListOf<String>().apply {
        addAll(sharedPrefs.getStringSet("favorites", emptySet()) ?: emptySet())
    }
    val learnedWords = mutableStateListOf<String>().apply {
        addAll(sharedPrefs.getStringSet("learned", emptySet()) ?: emptySet())
    }
    val masteredWords = mutableStateListOf<String>().apply {
        addAll(sharedPrefs.getStringSet("mastered", emptySet()) ?: emptySet())
    }
    val searchHistory = mutableStateListOf<String>().apply {
        addAll(sharedPrefs.getStringSet("history", emptySet()) ?: emptySet())
    }
    
    val wordNotes = mutableStateMapOf<String, String>().apply {
        sharedPrefs.all.filterKeys { it.startsWith("note_") }.forEach { (k, v) ->
            if (v is String) put(k.removePrefix("note_"), v)
        }
    }

    val xp = mutableIntStateOf(sharedPrefs.getInt("xp", 0))
    val streak = mutableIntStateOf(sharedPrefs.getInt("streak", 0))
    val isDarkMode = mutableStateOf(sharedPrefs.getBoolean("dark_mode", false))
    val speechRate = mutableStateOf(sharedPrefs.getFloat("speech_rate", 1.0f))

    private val _selectedWord = MutableStateFlow<Word?>(null)
    val selectedWord = _selectedWord.asStateFlow()

    init {
        checkAndResetStreak()
    }

    fun selectWord(word: Word) {
        _selectedWord.value = word
        addToHistory(word.term)
    }

    fun toggleDarkMode() {
        isDarkMode.value = !isDarkMode.value
        sharedPrefs.edit().putBoolean("dark_mode", isDarkMode.value).apply()
    }

    fun toggleFavorite(term: String) {
        if (favorites.contains(term)) {
            favorites.remove(term)
        } else {
            favorites.add(term)
        }
        sharedPrefs.edit().putStringSet("favorites", favorites.toSet()).apply()
    }

    fun markAsLearned(term: String) {
        if (!learnedWords.contains(term) && !masteredWords.contains(term)) {
            learnedWords.add(term)
            addXP(25)
            updateLearnedDate()
            sharedPrefs.edit().putStringSet("learned", learnedWords.toSet()).apply()
        }
    }

    fun markAsMastered(term: String) {
        if (!masteredWords.contains(term)) {
            masteredWords.add(term)
            if (learnedWords.contains(term)) {
                learnedWords.remove(term)
            }
            addXP(50)
            updateLearnedDate()
            sharedPrefs.edit().putStringSet("mastered", masteredWords.toSet()).apply()
            sharedPrefs.edit().putStringSet("learned", learnedWords.toSet()).apply()
        }
    }

    fun saveNote(term: String, note: String) {
        wordNotes[term] = note
        sharedPrefs.edit().putString("note_$term", note).apply()
    }

    fun addXP(amount: Int) {
        xp.intValue += amount
        sharedPrefs.edit().putInt("xp", xp.intValue).apply()
    }

    private fun updateLearnedDate() {
        val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val lastDate = sharedPrefs.getString("last_learned_date", "")
        
        if (lastDate != today) {
            val yesterday = Calendar.getInstance().apply { add(Calendar.DATE, -1) }.time
            val yesterdayStr = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(yesterday)
            
            streak.intValue = if (lastDate == yesterdayStr) streak.intValue + 1 else 1
            sharedPrefs.edit()
                .putInt("streak", streak.intValue)
                .putString("last_learned_date", today)
                .apply()
        }
    }

    private fun checkAndResetStreak() {
        val lastDate = sharedPrefs.getString("last_learned_date", "")
        if (lastDate.isNullOrEmpty()) return
        
        val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val yesterday = Calendar.getInstance().apply { add(Calendar.DATE, -1) }.time
        val yesterdayStr = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(yesterday)
        
        if (lastDate != today && lastDate != yesterdayStr) {
            streak.intValue = 0
            sharedPrefs.edit().putInt("streak", 0).apply()
        }
    }

    private fun addToHistory(term: String) {
        if (searchHistory.contains(term)) {
            searchHistory.remove(term)
        }
        searchHistory.add(0, term)
        sharedPrefs.edit().putStringSet("history", searchHistory.toSet()).apply()
    }

    fun clearHistory() {
        searchHistory.clear()
        sharedPrefs.edit().putStringSet("history", emptySet()).apply()
    }

    fun updateSpeechRate(rate: Float) {
        speechRate.value = rate
        sharedPrefs.edit().putFloat("speech_rate", rate).apply()
    }

    fun resetData() {
        favorites.clear()
        learnedWords.clear()
        masteredWords.clear()
        searchHistory.clear()
        wordNotes.clear()
        xp.intValue = 0
        streak.intValue = 0
        sharedPrefs.edit().clear().apply()
    }
}
