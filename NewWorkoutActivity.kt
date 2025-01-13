package com.example.myworkoutapp

import android.content.Intent
import android.widget.Button
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.Toast

class NewWorkoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_workout)

        findViewById<Button>(R.id.backButton).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.searchButton).setOnClickListener {
            startActivity(Intent(this, BrowseWorkoutActivity::class.java))
        }

        findViewById<Button>(R.id.savedButton).setOnClickListener {
            startActivity(Intent(this, SavedWorkoutActivity::class.java))
        }
    }
}