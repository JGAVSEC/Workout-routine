package com.example.myworkoutapp

import android.app.AlertDialog
import android.os.Bundle
import android.content.Intent
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.myworkoutapp.data.database.AppDatabase
import com.example.myworkoutapp.data.repository.WorkoutRepository
import com.example.myworkoutapp.data.models.Workout
import com.example.myworkoutapp.data.models.WorkoutExercise
import com.example.myworkoutapp.data.models.ExerciseSuggestion
import com.example.myworkoutapp.data.models.ExerciseData
import com.example.myworkoutapp.data.models.ExerciseResponse
import com.example.myworkoutapp.api.RetrofitClient
import com.squareup.picasso.Picasso
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager


class NewWorkoutActivity : ComponentActivity() {
    private lateinit var workoutRepository: WorkoutRepository
    private var currentWorkoutId: Int? = null
    private var currentFilter: String? = null
    private val TAG = "NewWorkoutActivity"
    private val selectedExercises = mutableListOf<ExerciseData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_workout)

        val database = AppDatabase.getDatabase(this)
        workoutRepository = WorkoutRepository(database.workoutDao())

        val workoutNameInput = findViewById<EditText>(R.id.workoutNameInput)
        val container = findViewById<LinearLayout>(R.id.exercisesContainer)

        workoutNameInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val workoutName = workoutNameInput.text.toString()
                if (workoutName.isNotEmpty()) {
                    hideKeyboard()
                    lifecycleScope.launch {
                        currentWorkoutId = workoutRepository.createWorkout(workoutName)
                        loadExercises()
                    }
                }
                true
            } else false
        }

        findViewById<Button>(R.id.backButton).setOnClickListener {
            finish()
        }

        findViewById<ImageButton>(R.id.filterButton).setOnClickListener {
            showFilterDialog()
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private val exerciseCategories = listOf(
        "shoulder", "biceps", "triceps", "forearm", "chest", "back", "leg"
    )
    
    private fun showFilterDialog() {
        val categories = exerciseCategories.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("Filter by Category")
            .setItems(categories) { _, which ->
                currentFilter = categories[which]
                lifecycleScope.launch {
                    loadExercises()
                }
            }
            .setNeutralButton("Clear Filter") { _, _ ->
                currentFilter = null
                lifecycleScope.launch {
                    loadExercises()
                }
            }
            .show()
    }

    private suspend fun loadExercises() {
        try {
            val container = findViewById<LinearLayout>(R.id.exercisesContainer)
            container.removeAllViews()
            val allExercises = mutableListOf<ExerciseSuggestion>()
            
            // Use filter or all categories
            val categoriesToFetch = currentFilter?.let { listOf(it) } ?: exerciseCategories

            categoriesToFetch.forEach { category ->
                val response = RetrofitClient.api.searchExercises(
                    term = category,
                    language = 2
                )
                allExercises.addAll(response.suggestions)
            }

            // Filter unique exercises with images
            val exercises = allExercises
                .filter { it.data.image != null }
                .distinctBy { it.data.id }
                .shuffled()
                .take(15)
    
            exercises.forEach { suggestion ->
                val exercise = suggestion.data
                val exerciseView = layoutInflater.inflate(
                    R.layout.item_new_workout_exercise,
                    container,
                    false
                )
                exerciseView.findViewById<TextView>(R.id.exerciseName).text = exercise.name
        
                val imageView = exerciseView.findViewById<ImageView>(R.id.exerciseImage)
                Picasso.get()
                    .load("https://wger.de${exercise.image}")
                    .into(imageView)
        
    
                    exerciseView.findViewById<Button>(R.id.addToWorkoutButton).setOnClickListener {
                        currentWorkoutId?.let { workoutId ->
                            lifecycleScope.launch {
                                val workoutExercise = WorkoutExercise(
                                    workoutId = workoutId,
                                    name = exercise.name,
                                    category = exercise.category,
                                    imageUrl = "https://wger.de${exercise.image}",
                                    order = selectedExercises.size
                                )
                                try {
                                    workoutRepository.addExerciseToWorkout(workoutId, workoutExercise)
                                    addExerciseToWorkout(exercise)
                                    Toast.makeText(
                                        this@NewWorkoutActivity,
                                        "Exercise added to workout!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } catch (e: Exception) {
                                    Log.e(TAG, "Error adding exercise: ${e.message}")
                                    Toast.makeText(
                                        this@NewWorkoutActivity,
                                        "Error adding exercise",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
    
                container.addView(exerciseView)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading exercises: ${e.message}")
        }
    }

    private fun addExerciseToWorkout(exercise: ExerciseData) {
        val selectedContainer = findViewById<LinearLayout>(R.id.selectedExercisesContainer)
        
        val thumbnailView = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(120, LinearLayout.LayoutParams.MATCH_PARENT).apply {
                marginEnd = 8
            }
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
    
        Picasso.get()
            .load("https://wger.de${exercise.image}")
            .into(thumbnailView)
    
        selectedContainer.addView(thumbnailView)
        selectedExercises.add(exercise)
    
        thumbnailView.setOnClickListener {
            AlertDialog.Builder(this@NewWorkoutActivity)
                .setTitle("Remove Exercise")
                .setMessage("Are you sure you want to remove ${exercise.name} from your workout?")
                .setPositiveButton("Yes") { _, _ ->
                    lifecycleScope.launch {
                        try {
                            currentWorkoutId?.let { workoutId ->
                                val workoutExercise = WorkoutExercise(
                                    workoutId = workoutId,
                                    name = exercise.name,
                                    category = exercise.category,
                                    imageUrl = "https://wger.de${exercise.image}",
                                    order = selectedExercises.indexOf(exercise)
                                )
                                workoutRepository.removeExerciseFromWorkout(workoutExercise)
                                
                                selectedContainer.removeView(thumbnailView)
                                selectedExercises.remove(exercise)
                                
                                Log.d(TAG, "Exercise removed: ${exercise.name}")
                                Toast.makeText(
                                    this@NewWorkoutActivity,
                                    "Exercise removed from workout",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error removing exercise: ${e.message}")
                        }
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }
    }
}