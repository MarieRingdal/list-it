package com.example.listit

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.listit.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private var TAG:String = "LoginActivity.kt"
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        setSupportActionBar(loginToolbar)
        supportActionBar?.title = "Login"


        binding.registerNewUserTextView.setOnClickListener {
            startActivity(Intent(this, RegisterNewUserActivity::class.java))
        }

        binding.loginButton.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val userEmail = loginEmailInput.text.toString().trim()
        val userPassword = loginPasswordInput.text.toString().trim()

        when {
            userEmail.isEmpty() -> Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            userPassword.isEmpty() -> Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
            else -> {
                auth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this, TodoListActivity::class.java),
                                ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
                            Log.d(TAG, "signInWithEmail:success")
                        } else {
                            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                        }

                    }
            }
        }
    }
}