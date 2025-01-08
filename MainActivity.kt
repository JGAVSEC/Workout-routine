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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("MyWorkoutApp", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        if (!isLoggedIn) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val logoutButton: Button = findViewById(R.id.logoutButton)
        
        logoutButton.setOnClickListener {
            logout()
        }

        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)
        val infoButton: Button = findViewById(R.id.infoButton)

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
        
        infoButton.setOnClickListener {
            onInfoButtonClick()
        }
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

