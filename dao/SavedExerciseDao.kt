package com.example.myworkoutapp.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.myworkoutapp.data.models.SavedExercise

@Dao
interface SavedExerciseDao {
    @Query("SELECT * FROM saved_exercises ORDER BY savedDate DESC")
    fun getAllExercises(): Flow<List<SavedExercise>>

    @Insert
    suspend fun insertExercise(exercise: SavedExercise)

    @Delete
    suspend fun deleteExercise(exercise: SavedExercise)
}