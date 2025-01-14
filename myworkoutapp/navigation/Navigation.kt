package com.example.myworkoutapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myworkoutapp.screens.*

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Main.route) {
            MainScreen(navController)
        }
        composable(Screen.NewWorkout.route) {
            NewWorkoutScreen(navController)
        }
        composable(Screen.SavedWorkouts.route) {
            SavedWorkoutsScreen(navController)
        }
        composable(Screen.BrowseWorkouts.route) {
            BrowseWorkoutsScreen(navController)
        }
        composable(Screen.Progress.route) {
            ProgressScreen(navController)
        }
        composable(Screen.MyWorkouts.route) {
            MyWorkoutsScreen(navController)
        }
        composable(Screen.Info.route) {
            InfoScreen(navController)
        }

    }
}

