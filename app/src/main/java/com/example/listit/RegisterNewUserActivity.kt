package com.example.listit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import android.widget.Toolbar
import com.example.listit.databinding.ActivityRegisterAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register_account.*

class RegisterNewUserActivity : AppCompatActivity() {

    private var TAG:String = "RegisterAccountActivity.kt"
    private lateinit var binding: ActivityRegisterAccountBinding
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val toolbar = binding.registerUserToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = ("Register")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.registerNewUserButton.setOnClickListener{
            registerUser()
        }

        binding.alreadyAUserTextView.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun registerUser(){
        val newUserEmail = registerEmailInput.text.toString().trim()
        val newUserPassword = registerPasswordInput.text.toString().trim()
        val confirmNewUserPassword = confirmPasswordInput.text.toString().trim()

        when {
            newUserEmail.isEmpty() -> Toast.makeText(this, "Please enter an email address", Toast.LENGTH_SHORT).show()
            !Patterns.EMAIL_ADDRESS.matcher(newUserEmail).matches() -> Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            newUserPassword.isEmpty() -> Toast.makeText(this, "Please enter a valid password", Toast.LENGTH_SHORT).show()
            confirmNewUserPassword != newUserPassword -> Toast.makeText(this, "Passwords does not match", Toast.LENGTH_SHORT).show()
        else -> {
            auth.createUserWithEmailAndPassword(newUserEmail, newUserPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this, ToDoListActivity::class.java))
                        finish()
                        Log.d(TAG, "createUserWithEmail:success")
                    } else {
                        Toast.makeText(baseContext, "Registration failed. Try again later.",
                            Toast.LENGTH_SHORT).show()
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    }
                }
        }
    }
}}