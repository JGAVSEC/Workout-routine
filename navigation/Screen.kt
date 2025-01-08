package com.example.myworkoutapp.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Login : Screen("login")
    object NewWorkout : Screen("new_workout")
    object SavedWorkouts : Screen("saved_workouts")
    object BrowseWorkouts : Screen("browse_workouts")
    object Progress : Screen("progress")
    object Info : Screen("info")
}