package com.example.myworkoutapp.ui.main

data class MainUiState(
    val isLoggedIn: Boolean = false,
    val username: String = "",
    val workoutCount: Int = 0,
    val isLoading: Boolean = false
)