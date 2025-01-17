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
import com.example.myworkoutapp.data.repository.SavedExerciseRepository
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
    private lateinit var savedExerciseRepository: SavedExerciseRepository
    private var currentWorkoutId: Int? = null
    private var currentFilter: String? = null
    private var showingSavedExercises = false
    private val TAG = "NewWorkoutActivity"
    private val selectedExercises = mutableListOf<ExerciseData>()

    private suspend fun createWorkout(name: String): Int {
        Log.d(TAG, "Creating new workout with name: $name")
        val workoutId = workoutRepository.createWorkout(name)
        Log.d(TAG, "Created workout with ID: $workoutId")
        return workoutId
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_workout)

        val database = AppDatabase.getDatabase(this)
        workoutRepository = WorkoutRepository(database.workoutDao())
        savedExerciseRepository = SavedExerciseRepository(database.savedExerciseDao())

        val workoutNameInput = findViewById<EditText>(R.id.workoutNameInput)
        val container = findViewById<LinearLayout>(R.id.exercisesContainer)

        findViewById<Button>(R.id.toggleSourceButton).setOnClickListener {
            showingSavedExercises = !showingSavedExercises
            val buttonText = if (showingSavedExercises) "Show API Exercises" else "Show Saved Exercises"
            (it as Button).text = buttonText
            lifecycleScope.launch {
                loadExercises()
            }
        }

        workoutNameInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val workoutName = workoutNameInput.text.toString()
                if (workoutName.isNotEmpty()) {
                    hideKeyboard()
                    lifecycleScope.launch {
                        // Only create if no currentWorkoutId
                        if (currentWorkoutId == null) {
                            currentWorkoutId = workoutRepository.createWorkout(workoutName)
                        }
                        loadExercises()
                    }
                }
                true
            } else false
        }

        // workoutNameInput.setOnEditorActionListener { _, actionId, _ ->
        //     if (actionId == EditorInfo.IME_ACTION_DONE) {
        //         val workoutName = workoutNameInput.text.toString()
        //         if (workoutName.isNotEmpty()) {
        //             hideKeyboard()
        //             lifecycleScope.launch {
        //                 currentWorkoutId = workoutRepository.createWorkout(workoutName)
        //                 loadExercises()
        //             }
        //         }
        //         true
        //     } else false
        // }

        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish()
        }

        findViewById<ImageButton>(R.id.filterButton).setOnClickListener {
            showFilterDialog()
        }

        findViewById<Button>(R.id.saveWorkoutButton).setOnClickListener {
            val workoutName = workoutNameInput.text.toString()
            if (workoutName.isNotEmpty()) {
                lifecycleScope.launch {
                    try {
                        currentWorkoutId?.let { workoutId ->
                            workoutRepository.updateWorkoutName(workoutId, workoutName)
                            Toast.makeText(
                                this@NewWorkoutActivity,
                                "Workout saved!",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error saving workout: ${e.message}")
                        Toast.makeText(
                            this@NewWorkoutActivity,
                            "Error saving workout",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    this@NewWorkoutActivity,
                    "Please enter a workout name",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // findViewById<Button>(R.id.saveWorkoutButton).setOnClickListener {
        //     val workoutName = workoutNameInput.text.toString()
        //     if (workoutName.isNotEmpty()) {
        //         lifecycleScope.launch {
        //             try {
        //                 currentWorkoutId?.let { workoutId ->
        //                     workoutRepository.updateWorkoutName(workoutId, workoutName)
        //                     Toast.makeText(
        //                         this@NewWorkoutActivity,
        //                         "Workout saved!",
        //                         Toast.LENGTH_SHORT
        //                     ).show()
        //                     finish()
        //                 }
        //             } catch (e: Exception) {
        //                 Log.e(TAG, "Error saving workout: ${e.message}")
        //                 Toast.makeText(
        //                     this@NewWorkoutActivity,
        //                     "Error saving workout",
        //                     Toast.LENGTH_SHORT
        //                 ).show()
        //             }
        //         }
        //     } else {
        //         Toast.makeText(
        //             this@NewWorkoutActivity,
        //             "Please enter a workout name",
        //             Toast.LENGTH_SHORT
        //         ).show()
        //     }
        // }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
    //Mogoce UPPER CASE
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

            if (showingSavedExercises) {
                // Load saved exercises
                savedExerciseRepository.getAllExercises().collect { savedExercises ->
                    val filteredExercises = if (currentFilter != null) {
                        savedExercises.filter { it.category.equals(currentFilter, ignoreCase = true) }
                    } else {
                        savedExercises
                    }

                    filteredExercises.forEach { savedExercise ->
                        val exerciseView = layoutInflater.inflate(
                            R.layout.item_new_workout_exercise,
                            container,
                            false
                        )
                        
                        exerciseView.findViewById<TextView>(R.id.exerciseName).text = savedExercise.name
                        
                        val imageView = exerciseView.findViewById<ImageView>(R.id.exerciseImage)
                        Picasso.get()
                            .load(savedExercise.imageUrl)
                            .into(imageView)

                        exerciseView.findViewById<Button>(R.id.addToWorkoutButton).setOnClickListener {
                            currentWorkoutId?.let { workoutId ->
                                lifecycleScope.launch {
                                    try {
                                        val workoutExercise = WorkoutExercise(
                                            workoutId = workoutId,
                                            name = savedExercise.name,
                                            category = savedExercise.category,
                                            imageUrl = savedExercise.imageUrl,
                                            order = selectedExercises.size
                                        )
                                        workoutRepository.addExerciseToWorkout(workoutId, workoutExercise)
                                        addExerciseToWorkout(ExerciseData(
                                            id = 0,
                                            base_id = 0,
                                            name = savedExercise.name,
                                            category = savedExercise.category,
                                            image = savedExercise.imageUrl,
                                            image_thumbnail = savedExercise.imageUrl
                                        ))
                                        Toast.makeText(this@NewWorkoutActivity, 
                                            "Exercise added to workout!", 
                                            Toast.LENGTH_SHORT).show()
                                    } catch (e: Exception) {
                                        Log.e(TAG, "Error adding exercise: ${e.message}")
                                    }
                                }
                            }
                        }
                        container.addView(exerciseView)
                    }
                }
            } else {
                // Load API exercises
                val allExercises = mutableListOf<ExerciseSuggestion>()
                val categoriesToFetch = currentFilter?.let { listOf(it) } ?: exerciseCategories

                categoriesToFetch.forEach { category ->
                    val response = RetrofitClient.api.searchExercises(
                        term = category,
                        language = 2
                    )
                    allExercises.addAll(response.suggestions)
                }

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
                                try {
                                    // Check if exercise already exists before adding
                                    if (selectedExercises.any { it.name == exercise.name }) {
                                        Toast.makeText(
                                            this@NewWorkoutActivity,
                                            "Exercise already added to workout",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@launch
                                    }
                    
                                    val workoutExercise = WorkoutExercise(
                                        workoutId = workoutId,
                                        name = exercise.name,
                                        category = exercise.category,
                                        imageUrl = "https://wger.de${exercise.image}",
                                        order = selectedExercises.size
                                    )
                                    
                                    // Add to database first
                                    workoutRepository.addExerciseToWorkout(workoutId, workoutExercise)
                                    // Then update UI
                                    addExerciseToWorkout(exercise)
                                    
                                    Toast.makeText(
                                        this@NewWorkoutActivity,
                                        "Exercise added to workout!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } catch (e: Exception) {
                                    Log.e(TAG, "Error adding exercise: ${e.message}")
                                }
                            }
                        }
                    }
                    container.addView(exerciseView)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading exercises: ${e.message}")
        }
    }

    private fun addExerciseToWorkout(exercise: ExerciseData) {
        val selectedContainer = findViewById<LinearLayout>(R.id.selectedExercisesContainer)
        
        if (selectedExercises.any { it.name == exercise.name }) {
            Toast.makeText(
                this,
                "Exercise already added to workout",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val thumbnailView = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(120, LinearLayout.LayoutParams.MATCH_PARENT).apply {
                marginEnd = 8
            }
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
    
        // Handle both API and saved exercise image URLs
        val imageUrl = if (exercise.image?.startsWith("http") == true) {
            exercise.image
        } else {
            "https://wger.de${exercise.image}"
        }

        // Picasso.get()
        //     .load("https://wger.de${exercise.image}")
        //     .into(thumbnailView)

        
        Picasso.get()
        .load(imageUrl)  // Use the processed imageUrl
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
                                    imageUrl = imageUrl,  // Use the same processed imageUrl
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
