package com.example.myworkoutapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myworkoutapp.ui.theme.MyWorkoutAppTheme
import android.widget.Button
import android.widget.Toast
import android.content.Intent
import android.content.Context
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import com.example.myworkoutapp.data.database.AppDatabase
import com.example.myworkoutapp.data.repository.UserRepository
import kotlinx.coroutines.launch


class LoginActivity : ComponentActivity() {
    private lateinit var userRepository: UserRepository
    private val TAG = "LoginActivity"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        try {
            val database = AppDatabase.getDatabase(this)
            userRepository = UserRepository(database.userDao())
            
            // Match these IDs with XML
            val emailInput = findViewById<EditText>(R.id.emailInput)
            val passwordInput = findViewById<EditText>(R.id.passwordInput)
            val loginButton = findViewById<Button>(R.id.loginButton)
            val createAccountButton = findViewById<Button>(R.id.createAccountButton)

            loginButton.setOnClickListener {
                handleLogin(emailInput.text.toString(), passwordInput.text.toString())
            }

            createAccountButton.setOnClickListener {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing: ${e.message}")
            Toast.makeText(this, "Error initializing app", Toast.LENGTH_LONG).show()
        }
    }

    private fun handleLogin(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                if (userRepository.validateUser(email, password)) {
                    val sharedPref = getSharedPreferences("MyWorkoutApp", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putBoolean("isLoggedIn", true)
                        apply()
                    }
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Login error: ${e.message}")
                Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}