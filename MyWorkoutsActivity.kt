package com.example.myworkoutapp

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

class MyWorkoutActivity : ComponentActivity() {
    private lateinit var workoutRepository: WorkoutRepository
    private lateinit var adapter: WorkoutAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_workouts)

        val database = AppDatabase.getDatabase(this)
        workoutRepository = WorkoutRepository(database.workoutDao())

        findViewById<Button>(R.id.backButton).setOnClickListener {
            finish()
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.workoutsRecyclerView)
        adapter = WorkoutAdapter()
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

    class WorkoutViewHolder(val view: TextView) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false) as TextView
        return WorkoutViewHolder(textView)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.view.text = workouts[position].name
    }

    override fun getItemCount() = workouts.size

    fun updateWorkouts(newWorkouts: List<Workout>) {
        workouts = newWorkouts
        notifyDataSetChanged()
    }
}