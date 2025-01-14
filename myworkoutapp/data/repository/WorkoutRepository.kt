package com.example.myworkoutapp.data.repository

import com.example.myworkoutapp.data.dao.WorkoutDao
import com.example.myworkoutapp.data.models.Workout
import com.example.myworkoutapp.data.models.WorkoutExercise
import com.example.myworkoutapp.data.models.WorkoutWithExercises
import kotlinx.coroutines.flow.Flow

class WorkoutRepository(private val workoutDao: WorkoutDao) {
    suspend fun createWorkout(name: String): Int {
        val workout = Workout(name = name)
        return workoutDao.insertWorkout(workout).toInt()
    }

    suspend fun addExerciseToWorkout(workoutId: Int, exercise: WorkoutExercise) {
        workoutDao.insertWorkoutExercise(exercise)
    }

    fun getAllWorkouts(): Flow<List<Workout>> = 
        workoutDao.getAllWorkouts()

    fun getWorkoutExercises(workoutId: Int): Flow<List<WorkoutExercise>> = 
        workoutDao.getWorkoutExercises(workoutId)

    suspend fun deleteWorkout(workout: Workout) {
        workoutDao.deleteWorkout(workout)
    }
    
    fun getWorkoutWithExercises(workoutId: Int): Flow<WorkoutWithExercises> {
        return workoutDao.getWorkoutWithExercises(workoutId)
    }

    suspend fun removeExerciseFromWorkout(exercise: WorkoutExercise) {
        workoutDao.deleteWorkoutExercise(exercise)
    }

    suspend fun updateWorkoutName(workoutId: Int, newName: String) {
        workoutDao.updateWorkoutName(workoutId, newName)
    }

}