package com.example.myworkoutapp

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.example.myworkoutapp.data.database.AppDatabase
import com.example.myworkoutapp.data.repository.WorkoutRepository
import com.example.myworkoutapp.data.models.WorkoutWithExercises
import kotlinx.coroutines.flow.Flow

class WorkoutDetailActivity : ComponentActivity() {
    private lateinit var workoutRepository: WorkoutRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_create) 

        findViewById<Button>(R.id.backButton).setOnClickListener {
            finish()  // or onBackPressed() for older Android versions
        }

        val workoutId = intent.getIntExtra("WORKOUT_ID", -1)
        if (workoutId == -1) {
            finish()
            return
        }

        val database = AppDatabase.getDatabase(this)
        workoutRepository = WorkoutRepository(database.workoutDao())

        lifecycleScope.launch {
            val workoutFlow: Flow<WorkoutWithExercises> = workoutRepository.getWorkoutWithExercises(workoutId)
            workoutFlow.collectLatest { workoutWithExercises ->
                displayWorkout(workoutWithExercises)
            }
        }
    }
    private fun displayWorkout(workoutWithExercises: WorkoutWithExercises) {
        val titleText = findViewById<TextView>(R.id.workoutTitleText)
        titleText.text = workoutWithExercises.workout.name

        val container = findViewById<LinearLayout>(R.id.exercisesContainer)
        container.removeAllViews()
        
        workoutWithExercises.exercises.forEach { exercise ->
            val exerciseView = layoutInflater.inflate(
                R.layout.item_my_workout,
                container,
                false
            )
            exerciseView.findViewById<TextView>(R.id.exerciseName).text = exercise.name
            container.addView(exerciseView)
        }
    }
}