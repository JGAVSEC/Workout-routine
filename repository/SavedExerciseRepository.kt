package com.example.myworkoutapp.data.repository

import android.util.Log
import com.example.myworkoutapp.data.dao.SavedExerciseDao
import com.example.myworkoutapp.data.models.SavedExercise
import kotlinx.coroutines.flow.Flow

class SavedExerciseRepository(private val savedExerciseDao: SavedExerciseDao) {
    private val TAG = "SavedExerciseRepository"

    fun getAllExercises(): Flow<List<SavedExercise>> {
        Log.d(TAG, "Getting all exercises")
        return savedExerciseDao.getAllExercises()
    }

    suspend fun insert(exercise: SavedExercise) {
        Log.d(TAG, "Inserting exercise: ${exercise.name}")
        savedExerciseDao.insertExercise(exercise)
    }
}