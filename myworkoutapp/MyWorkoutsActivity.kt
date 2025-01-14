package com.example.myworkoutapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myworkoutapp.data.database.AppDatabase
import com.example.myworkoutapp.data.models.Workout
import com.example.myworkoutapp.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MyWorkoutsActivity : ComponentActivity() {
    private lateinit var workoutRepository: WorkoutRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_workouts)

        val database = AppDatabase.getDatabase(this)
        workoutRepository = WorkoutRepository(database.workoutDao())
        
        val recyclerView = findViewById<RecyclerView>(R.id.workoutsRecyclerView)
        val adapter = WorkoutAdapter()
        
        adapter.onWorkoutClick = { workout ->
            val intent = Intent(this, WorkoutDetailActivity::class.java).apply {
                putExtra("WORKOUT_ID", workout.id)
            }
            startActivity(intent)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        
        lifecycleScope.launch {
            workoutRepository.getAllWorkouts().collect { workouts ->
                adapter.updateWorkouts(workouts)
            }
        }
    }
}

class WorkoutAdapter : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {
    private var workouts = listOf<Workout>()
    var onWorkoutClick: ((Workout) -> Unit)? = null

    class WorkoutViewHolder(val button: Button) : RecyclerView.ViewHolder(button)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout_grid, parent, false) as Button
        return WorkoutViewHolder(view)
    }
    

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = workouts[position]
        holder.button.text = workout.name
        holder.button.setOnClickListener {
            onWorkoutClick?.invoke(workout)
        }
    }

    override fun getItemCount() = workouts.size

    fun updateWorkouts(newWorkouts: List<Workout>) {
        workouts = newWorkouts
        notifyDataSetChanged()
    }
}