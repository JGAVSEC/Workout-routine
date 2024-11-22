package com.example.samotest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Button 1: Start Workout
        findViewById<Button>(R.id.btn_start_workout).setOnClickListener {
            startActivity(Intent(this, StartWorkoutActivity::class.java))
        }

        // Button 2: View Routines
        findViewById<Button>(R.id.btn_view_routines).setOnClickListener {
            startActivity(Intent(this, ViewRoutinesActivity::class.java))
        }

        // Button 3: Track Progress
        findViewById<Button>(R.id.btn_track_progress).setOnClickListener {
            startActivity(Intent(this, TrackProgressActivity::class.java))
        }

        // Button 4: Settings
        findViewById<Button>(R.id.btn_settings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}
