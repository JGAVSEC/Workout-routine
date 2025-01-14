package com.example.myworkoutapp.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Main : Screen("main")
    object NewWorkout : Screen("new_workout")
    object SavedWorkouts : Screen("saved_workouts")
    object BrowseWorkouts : Screen("browse_workouts")
    object Progress : Screen("progress")
    object Info : Screen("info")
    object MyWorkouts : Screen("my_workouts")
}
