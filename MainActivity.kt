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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the buttons by their IDs
        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)

        // Set OnClickListeners for the buttons
        button1.setOnClickListener {
            onNewWorkoutButtonClick()
        }

        button2.setOnClickListener {
            onSavedWorkoutsButtonClick()
        }

        button3.setOnClickListener {
            onBrowseWorkoutsButtonClick()
        }

        button4.setOnClickListener {
            onProgressButtonClick()
        }
    }

    private fun onNewWorkoutButtonClick() {
        Toast.makeText(this, "New Workout button clicked", Toast.LENGTH_SHORT).show()
    }

    private fun onSavedWorkoutsButtonClick() {
        Toast.makeText(this, "Saved Workouts button clicked", Toast.LENGTH_SHORT).show()
    }

    private fun onBrowseWorkoutsButtonClick() {
        Toast.makeText(this, "Browse Workouts button clicked", Toast.LENGTH_SHORT).show()
    }

    private fun onProgressButtonClick() {
        Toast.makeText(this, "Progress button clicked", Toast.LENGTH_SHORT).show()
        }
}

