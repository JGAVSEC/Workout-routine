package com.example.myworkoutapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myworkoutapp.ui.theme.MyWorkoutAppTheme
import android.widget.Button
import android.widget.Toast
import android.content.Intent
import android.content.Context
import android.util.Log
import com.example.myworkoutapp.ui.main.MainViewModel
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myworkoutapp.navigation.SetupNavGraph
import com.example.myworkoutapp.navigation.Screen

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val sharedPref = getSharedPreferences("MyWorkoutApp", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
    
        if (!isLoggedIn) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            setContent {
                val navController = rememberNavController()
                SetupNavGraph(
                    navController = navController,
                    startDestination = Screen.Main.route
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart Called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart Called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume Called")
        Toast.makeText(this, "Welcome back to SAMZYZZ!", Toast.LENGTH_SHORT).show()
        //Refresh the data

    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy Called")
    }

    private fun onNewWorkoutButtonClick() {
        val intent = Intent(this, NewWorkoutActivity::class.java)
        startActivity(intent)
    }

    private fun onSavedWorkoutsButtonClick() {
        val intent = Intent(this, SavedWorkoutActivity::class.java)
        startActivity(intent)
    }

    private fun onBrowseWorkoutsButtonClick() {
        val intent = Intent(this, BrowseWorkoutActivity::class.java)
        startActivity(intent)}

    private fun onProgressButtonClick() {
        val intent = Intent(this, ProgressWorkoutActivity::class.java)
        startActivity(intent)
    }

    private fun onInfoButtonClick() {
        val intent = Intent(this, InfoActivity::class.java)
        startActivity(intent)
    }
    
    private fun onMyWorkoutButtonClick() {
        val intent = Intent(this, MyWorkoutActivity::class.java)
        startActivity(intent)
    }

    private fun logout() {
        val sharedPref = getSharedPreferences("MyWorkoutApp", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("isLoggedIn", false)
            apply()
        }
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}

