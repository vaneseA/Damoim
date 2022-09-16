package com.example.damoim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signUpBtn.setOnClickListener {
            val email = emailEdt.text.toString()
            val password = passwordEdt.text.toString()
            val nickname = nickEdt.text.toString()

            Log.d("SignupActivity","Email is: " + email)
            Log.d("SignupActivity","Password: $password")
        }
        }
}