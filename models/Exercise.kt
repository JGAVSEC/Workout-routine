package com.example.myworkoutapp.data.models
import com.example.myworkoutapp.SavedWorkoutActivity 

data class ExerciseData(
    val id: Int,
    val base_id: Int,
    val name: String,
    val category: String,
    val image: String?,
    val image_thumbnail: String?
)

data class ExerciseSuggestion(
    val value: String,
    val data: ExerciseData
)

data class ExerciseResponse(
    val suggestions: List<ExerciseSuggestion>
)