package com.example.myworkoutapp.screens

import android.widget.Button
import android.view.LayoutInflater
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.myworkoutapp.R
import com.example.myworkoutapp.navigation.Screen
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import androidx.compose.runtime.LaunchedEffect
import com.example.myworkoutapp.BrowseWorkoutActivity
import com.example.myworkoutapp.NewWorkoutActivity
import com.example.myworkoutapp.SavedWorkoutActivity
import com.example.myworkoutapp.ProgressWorkoutActivity
import com.example.myworkoutapp.MyWorkoutActivity
import com.example.myworkoutapp.InfoActivity
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.myworkoutapp.data.database.AppDatabase
import com.example.myworkoutapp.data.models.Workout
import com.example.myworkoutapp.data.repository.WorkoutRepository
import com.example.myworkoutapp.LoginActivity


@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        context.startActivity(Intent(context, LoginActivity::class.java))
        navController.navigateUp()
    }
}

@Composable
fun MainScreen(navController: NavController) {
    AndroidView(
        factory = { context ->
            val view = LayoutInflater.from(context).inflate(R.layout.activity_main, null)
            
            // NEW WORKOUT button
            view.findViewById<Button>(R.id.button1).setOnClickListener {
                context.startActivity(Intent(context, NewWorkoutActivity::class.java))
            }
            
            // SAVED WORKOUTS button
            view.findViewById<Button>(R.id.button2).setOnClickListener {
                context.startActivity(Intent(context, SavedWorkoutActivity::class.java))
            }
            
            // BROWSE WORKOUTS button
            view.findViewById<Button>(R.id.button3).setOnClickListener {
                context.startActivity(Intent(context, BrowseWorkoutActivity::class.java))
            }
            
            // PROGRESS button
            view.findViewById<Button>(R.id.button5).setOnClickListener {
                context.startActivity(Intent(context, MyWorkoutActivity::class.java))
            }

            // PROGRESS button
            view.findViewById<Button>(R.id.button4).setOnClickListener {
                context.startActivity(Intent(context, ProgressWorkoutActivity::class.java))
            }
            
            // INFO button
            view.findViewById<Button>(R.id.infoButton).setOnClickListener {
                context.startActivity(Intent(context, InfoActivity::class.java))
            }
            
            // LOGOUT button
            view.findViewById<Button>(R.id.logoutButton)?.setOnClickListener {
                val sharedPref = context.getSharedPreferences("MyWorkoutApp", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putBoolean("isLoggedIn", false)
                    apply()
                }
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Main.route) { inclusive = true }
                }
            }
            
            view
        },
        modifier = Modifier.fillMaxSize()
    )
}


@Composable
fun NewWorkoutScreen(navController: NavController) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        context.startActivity(Intent(context, NewWorkoutActivity::class.java))
        navController.navigateUp()
    }
}

@Composable
fun SavedWorkoutsScreen(navController: NavController) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        context.startActivity(Intent(context, SavedWorkoutActivity::class.java))
        navController.navigateUp()
    }
}

@Composable
fun ProgressScreen(navController: NavController) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        context.startActivity(Intent(context, ProgressWorkoutActivity::class.java))
        navController.navigateUp()
    }
}

@Composable
fun InfoScreen(navController: NavController) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        context.startActivity(Intent(context, InfoActivity::class.java))
        navController.navigateUp()
    }
}
@Composable
fun BrowseWorkoutsScreen(navController: NavController) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        context.startActivity(Intent(context, BrowseWorkoutActivity::class.java))
        navController.navigateUp()
    }
}

@Composable
private fun WorkoutCard(workout: Workout) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .aspectRatio(1f)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF34495E)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = workout.name,
                color = Color.White,
                fontSize = 20.sp
            )
        }
    }
}


@Composable
fun MyWorkoutsScreen(navController: NavController) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        context.startActivity(Intent(context, MyWorkoutActivity::class.java))
        navController.navigateUp()
    }
}

