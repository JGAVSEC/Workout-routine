package com.example.myworkoutapp.data.dao

import android.util.Log
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.myworkoutapp.data.models.Workout
import com.example.myworkoutapp.data.models.WorkoutExercise
import com.example.myworkoutapp.data.models.WorkoutWithExercises
@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: Workout): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWorkoutExercise(exercise: WorkoutExercise)

    // @Query("SELECT * FROM workouts")
    // fun getAllWorkouts(): Flow<List<Workout>>

    // @Query("SELECT * FROM workout_exercises WHERE workoutId = :workoutId")
    // fun getWorkoutExercises(workoutId: Int): Flow<List<WorkoutExercise>>

    // @Transaction
    // @Query("SELECT * FROM workouts GROUP BY id")
    // fun getAllWorkoutsWithExercises(): Flow<List<WorkoutWithExercises>>
    @Transaction
    @Query("SELECT DISTINCT w.* FROM workouts w")
    fun getAllWorkoutsWithExercises(): Flow<List<WorkoutWithExercises>>


    @Transaction
    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    fun getWorkoutWithExercises(workoutId: Int): Flow<WorkoutWithExercises>

    @Delete
    suspend fun deleteWorkout(workout: Workout)

    @Delete
    suspend fun deleteWorkoutExercise(exercise: WorkoutExercise)

    @Query("UPDATE workouts SET name = :newName WHERE id = :workoutId")
    suspend fun updateWorkoutName(workoutId: Int, newName: String)

}