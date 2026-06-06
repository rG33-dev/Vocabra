package com.example.vocabwidget.ui

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Home : Screen("home")
    object Detail : Screen("detail")
    object Quiz : Screen("quiz")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
    object History : Screen("history")
    object Settings : Screen("settings")
    object Flashcards : Screen("flashcards")
}
