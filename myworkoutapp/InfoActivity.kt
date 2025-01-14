package com.example.myworkoutapp
import android.widget.Button
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.Toast

class InfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val backButton: Button = findViewById(R.id.backButton)

        backButton.setOnClickListener {
            finish()
        }
    }
}