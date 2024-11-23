package com.example.myworkoutapp
import android.widget.Button
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.Toast

class BrowseWorkoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_workout)
        
        // Find the back button by its ID
        val backButton: Button = findViewById(R.id.backButton)

        // Set an OnClickListener for the back button
        backButton.setOnClickListener {
            finish() // Finish the current activity and go back to MainActivity
        }
    }
}