package com.example.myworkoutapp.data.models
import com.example.myworkoutapp.SavedWorkoutActivity 

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_exercises")
data class SavedExercise(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val category: String,
    val imageUrl: String?,
    val savedDate: Long = System.currentTimeMillis()
)