package com.example.myworkoutapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.myworkoutapp.api.RetrofitClient
import com.squareup.picasso.Picasso

class ExerciseDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_detail)

        findViewById<Button>(R.id.backButton).setOnClickListener {
            finish()
        }

        val exerciseType = intent.getStringExtra("EXERCISE_TYPE") ?: return
        val exercisesContainer = findViewById<LinearLayout>(R.id.exercisesContainer)
        
        lifecycleScope.launch {
            try {
                val categoryHeader = findViewById<TextView>(R.id.categoryHeader)
                val exercisesContainer = findViewById<LinearLayout>(R.id.exercisesContainer)
                var headerText = exerciseType.uppercase()
                
                val primaryResponse = RetrofitClient.api.searchExercises(term = exerciseType)
                var exercises = primaryResponse.suggestions
                    .filter { it.data.image != null }
                    .distinctBy { it.data.id }
                    .shuffled()
                    .take(7)
        
                if (exercises.size < 5) {
                    val fallbackType = when (exerciseType.lowercase()) {
                        "triceps", "biceps" -> "arms"
                        "quads" -> "legs"
                        "core" -> "abs"
                        else -> "chest"
                    }
                    
                    if (fallbackType != exerciseType.lowercase()) {
                        headerText = "$headerText & ${fallbackType.uppercase()}"
                        
                        val fallbackResponse = RetrofitClient.api.searchExercises(term = fallbackType)
                        val fallbackExercises = fallbackResponse.suggestions
                            .filter { it.data.image != null }
                            .distinctBy { it.data.id }
                        
                        exercises = (exercises + fallbackExercises)
                            .distinctBy { it.data.id }
                            .shuffled()
                            .take((5..7).random())
                    }
                }
        
                categoryHeader.text = headerText
                exercisesContainer.removeAllViews()
        
                exercises.forEach { suggestion ->
                    val exercise = suggestion.data
                    val exerciseView = layoutInflater.inflate(R.layout.item_exercise, exercisesContainer, false)
                    
                    exerciseView.findViewById<TextView>(R.id.exerciseName).text = exercise.name
                    
                    val imageView = exerciseView.findViewById<ImageView>(R.id.exerciseImage)
                    Picasso.get()
                        .load("https://wger.de${exercise.image}")
                        .into(imageView)
                    
                    exercisesContainer.addView(exerciseView)
                }
            } catch (e: Exception) {
                Toast.makeText(this@ExerciseDetailActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}