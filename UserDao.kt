package com.example.myworkoutapp.data.dao

import androidx.room.*
import com.example.myworkoutapp.data.models.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT EXISTS(SELECT * FROM users WHERE email = :email AND password = :password)")
    suspend fun validateUser(email: String, password: String): Boolean
}