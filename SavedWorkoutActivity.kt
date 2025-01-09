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

        try {
            val database = AppDatabase.getDatabase(this)
            repository = SavedExerciseRepository(database.savedExerciseDao())

            findViewById<Button>(R.id.backButton).setOnClickListener {
                finish()
            }

            val container = findViewById<LinearLayout>(R.id.exercisesContainer)

            lifecycleScope.launch {
                try {
                    repository.getAllExercises().collect { exercises ->
                        Log.d(TAG, "Loading ${exercises.size} exercises")
                        container.removeAllViews()
                        
                        exercises.forEach { exercise ->
                            val view = layoutInflater.inflate(R.layout.item_exercise, container, false)
                            view.findViewById<TextView>(R.id.exerciseName).text = exercise.name
                            
                            val imageView = view.findViewById<ImageView>(R.id.exerciseImage)
                            Picasso.get()
                                .load(exercise.imageUrl)
                                .into(imageView)
                            
                            container.addView(view)
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error loading exercises", e)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate", e)
        }
    }
}