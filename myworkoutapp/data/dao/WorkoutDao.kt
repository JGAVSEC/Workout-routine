package com.example.myworkoutapp.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.myworkoutapp.data.models.Workout
import com.example.myworkoutapp.data.models.WorkoutExercise
import com.example.myworkoutapp.data.models.WorkoutWithExercises

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insertWorkout(workout: Workout): Long

    @Insert
    suspend fun insertWorkoutExercise(exercise: WorkoutExercise)

    @Query("SELECT * FROM workouts ORDER BY createdDate DESC")
    fun getAllWorkouts(): Flow<List<Workout>>

    @Query("SELECT * FROM workout_exercises WHERE workoutId = :workoutId ORDER BY `order`")
    fun getWorkoutExercises(workoutId: Int): Flow<List<WorkoutExercise>>

    @Delete
    suspend fun deleteWorkout(workout: Workout)

    @Delete
    suspend fun deleteWorkoutExercise(exercise: WorkoutExercise)

    @Query("UPDATE workouts SET name = :newName WHERE id = :workoutId")
    suspend fun updateWorkoutName(workoutId: Int, newName: String)

    @Transaction
    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    fun getWorkoutWithExercises(workoutId: Int): Flow<WorkoutWithExercises>
}