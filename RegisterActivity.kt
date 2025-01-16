package com.example.myworkoutapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.myworkoutapp.data.database.AppDatabase
import com.example.myworkoutapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterActivity : ComponentActivity() {
    private lateinit var userRepository: UserRepository
    private val TAG = "RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        try {
            val database = AppDatabase.getDatabase(this)
            userRepository = UserRepository(database.userDao())

            val backToLoginButton = findViewById<Button>(R.id.backToLoginButton)
            val nameInput = findViewById<EditText>(R.id.nameInput)
            val emailInput = findViewById<EditText>(R.id.emailInput)
            val passwordInput = findViewById<EditText>(R.id.passwordInput)
            val registerButton = findViewById<Button>(R.id.registerButton)
            
            backToLoginButton.setOnClickListener {
                finish() // This will return to LoginActivity
            }

            registerButton.setOnClickListener {
                val name = nameInput.text.toString()
                val email = emailInput.text.toString()
                val password = passwordInput.text.toString()

                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }


                lifecycleScope.launch {
                    try {
                        if (userRepository.userExists(email)) {
                            Toast.makeText(this@RegisterActivity, "Email already registered", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d(TAG, "Attempting to create user: $email")
                            userRepository.createUser(name, email, password)
                            Log.d(TAG, "User created successfully")
                            Toast.makeText(this@RegisterActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Registration failed: ${e.message}", e)
                        Toast.makeText(this@RegisterActivity, "Registration failed: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing: ${e.message}", e)
            Toast.makeText(this, "Error initializing app", Toast.LENGTH_LONG).show()
        }
    }
}