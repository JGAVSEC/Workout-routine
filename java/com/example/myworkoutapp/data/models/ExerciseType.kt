package com.example.myworkoutapp.data
import com.example.myworkoutapp.SavedWorkoutActivity 

enum class ExerciseType(val searchTerm: String) {
    LEGS("legs"),
    BACK("back"),
    CHEST("chest"),
    ARMS("arms"),
    TRICEPS("triceps"),
    BICEPS("biceps"),
    QUADS("quads"),
    ABS("abs"),
    CORE("core")
}