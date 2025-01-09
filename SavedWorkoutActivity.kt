package com.example.myworkoutapp

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.myworkoutapp.data.database.AppDatabase
import com.example.myworkoutapp.data.repository.SavedExerciseRepository
import com.squareup.picasso.Picasso



class SavedWorkoutActivity : ComponentActivity() {
    private lateinit var repository: SavedExerciseRepository
    private val TAG = "SavedWorkoutActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_workout)

        val database = AppDatabase.getDatabase(this)
        repository = SavedExerciseRepository(database.savedExerciseDao())

        findViewById<Button>(R.id.backButton).setOnClickListener {
             finish()
        }

        lifecycleScope.launch {
            try {
                repository.getAllExercises().collect { exercises ->
                    Log.d(TAG, "Received exercises: ${exercises.size}")
                    val grid = findViewById<GridLayout>(R.id.savedWorkoutsGrid)
                    grid.removeAllViews()
                    
                    exercises.forEach { exercise ->
                        val exerciseView = layoutInflater.inflate(
                            R.layout.item_exercise,
                            grid,
                            false
                        )
                        exerciseView.findViewById<TextView>(R.id.exerciseName).text = exercise.name
                        
                        val imageView = exerciseView.findViewById<ImageView>(R.id.exerciseImage)
                        Picasso.get()
                            .load(exercise.imageUrl)
                            .into(imageView)
                        
                        grid.addView(exerciseView)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error: ${e.message}")
                Toast.makeText(this@SavedWorkoutActivity, 
                    "Error loading exercises", Toast.LENGTH_SHORT).show()
            }
        }
    }
}