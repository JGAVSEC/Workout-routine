package com.example.myworkoutapp
import android.widget.Button
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.Toast

class SavedWorkoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_workout)

        val backButton: Button = findViewById(R.id.backButton)

        backButton.setOnClickListener {
            finish()
        }
    }
}