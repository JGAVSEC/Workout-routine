package com.example.myworkoutapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myworkoutapp.data.database.AppDatabase
import com.example.myworkoutapp.data.models.Workout
import com.example.myworkoutapp.data.models.WorkoutWithExercises
import com.example.myworkoutapp.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.LinearLayoutManager


class MyWorkoutsActivity : ComponentActivity() {
    private lateinit var workoutRepository: WorkoutRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_workouts)

        val backToLoginButton = findViewById<ImageButton>(R.id.backButton)
        val database = AppDatabase.getDatabase(this)
        workoutRepository = WorkoutRepository(database.workoutDao())
        
        val recyclerView = findViewById<RecyclerView>(R.id.workoutsRecyclerView)
        val adapter = WorkoutAdapter()
        
        backToLoginButton.setOnClickListener {
            finish() // This will return to LoginActivity
        }

        adapter.onWorkoutClick = { workoutWithExercises ->
            val intent = Intent(this, WorkoutDetailActivity::class.java).apply {
                putExtra("WORKOUT_ID", workoutWithExercises.workout.id)
            }
            startActivity(intent)
        }

        adapter.onDeleteClick = { workoutWithExercises ->
            lifecycleScope.launch {
                workoutRepository.deleteWorkout(workoutWithExercises.workout)
            }
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.VERTICAL
        }

        lifecycleScope.launch {
            Log.d(TAG, "Starting to collect workouts")
            workoutRepository.getAllWorkoutsWithExercises()
                .map { workouts -> 
                    workouts.filter { it.exercises.isNotEmpty() }
                }
                .collect { workouts ->
                    Log.d(TAG, "Received ${workouts.size} workouts")
                    adapter.updateWorkouts(workouts)
                }
        }
    }
}

class WorkoutAdapter : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {
    private var workouts = listOf<WorkoutWithExercises>()
    var onWorkoutClick: ((WorkoutWithExercises) -> Unit)? = null
    var onDeleteClick: ((WorkoutWithExercises) -> Unit)? = null

    class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val workoutName: Button = itemView.findViewById(R.id.workoutName)
        val deleteWorkout: ImageButton = itemView.findViewById(R.id.deleteWorkout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout_grid, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = workouts[position]
        holder.workoutName.text = workout.workout.name
        
        holder.workoutName.setOnClickListener {
            onWorkoutClick?.invoke(workout)
        }
        
        holder.deleteWorkout.setOnClickListener {
            onDeleteClick?.invoke(workout)
        }
    }

    override fun getItemCount() = workouts.size

    fun updateWorkouts(newWorkouts: List<WorkoutWithExercises>) {
        workouts = newWorkouts
        notifyDataSetChanged()
    }
}

