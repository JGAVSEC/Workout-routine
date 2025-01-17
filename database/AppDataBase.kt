package com.example.myworkoutapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myworkoutapp.data.dao.SavedExerciseDao
import com.example.myworkoutapp.data.dao.UserDao
import com.example.myworkoutapp.data.dao.WorkoutDao
import com.example.myworkoutapp.data.models.SavedExercise
import com.example.myworkoutapp.data.models.User
import com.example.myworkoutapp.data.models.Workout
import com.example.myworkoutapp.data.models.WorkoutExercise

@Database(
    entities = [
        SavedExercise::class,
        Workout::class,
        WorkoutExercise::class,
        User::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun savedExerciseDao(): SavedExerciseDao
    abstract fun workoutDao(): WorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "workout_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}