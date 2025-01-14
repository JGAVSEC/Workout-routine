package com.example.myworkoutapp.data.repository

import com.example.myworkoutapp.data.dao.UserDao
import com.example.myworkoutapp.data.models.User

class UserRepository(private val userDao: UserDao) {
    suspend fun createUser(name: String, email: String, password: String) {
        userDao.insertUser(User(
            name = name,
            email = email,
            password = password
        ))
    }
    suspend fun validateUser(email: String, password: String): Boolean {
        return userDao.validateUser(email, password)
    }

    suspend fun userExists(email: String): Boolean {
        return userDao.getUserByEmail(email) != null
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }
}