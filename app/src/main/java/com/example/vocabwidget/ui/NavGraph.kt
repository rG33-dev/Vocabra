package com.example.vocabwidget.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vocabwidget.VocabViewModel
import com.example.vocabwidget.Word

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: VocabViewModel,
    onSpeak: (String) -> Unit,
    onShare: (Word) -> Unit,
    onExport: (List<String>) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(onStart = { 
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Welcome.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onWordClick = { word ->
                    viewModel.selectWord(word)
                    navController.navigate(Screen.Detail.route)
                },
                onNavigateToQuiz = { navController.navigate(Screen.Quiz.route) },
                onNavigateToFlashcards = { navController.navigate(Screen.Flashcards.route) },
                onNavigateToFavorites = { navController.navigate(Screen.Favorites.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
            )
        }
        composable(Screen.Detail.route) {
            val word by viewModel.selectedWord.collectAsState()
            word?.let {
                DetailScreen(
                    viewModel = viewModel,
                    word = it,
                    onBack = { navController.popBackStack() },
                    onSpeak = onSpeak,
                    onShare = onShare
                )
            }
        }
        composable(Screen.Quiz.route) {
            QuizScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Flashcards.route) {
            FlashcardScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(
                favorites = viewModel.favorites,
                onWordClick = { word ->
                    viewModel.selectWord(word)
                    navController.navigate(Screen.Detail.route)
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToHistory = { navController.navigate(Screen.History.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onExport = { onExport(viewModel.masteredWords.toList()) }
            )
        }
        composable(Screen.History.route) {
            HistoryScreen(
                history = viewModel.searchHistory,
                onWordClick = { word ->
                    viewModel.selectWord(word)
                    navController.navigate(Screen.Detail.route)
                },
                onBack = { navController.popBackStack() },
                onClear = { viewModel.clearHistory() }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                speechRate = viewModel.speechRate.value,
                onSpeechRateChange = { viewModel.updateSpeechRate(it) },
                onBack = { navController.popBackStack() },
                onReset = { viewModel.resetData() }
            )
        }
    }
}
