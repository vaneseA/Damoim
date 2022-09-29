package com.example.damoim

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import com.example.damoim.auth.LoginActivity
import com.example.damoim.utils.FirebaseAuthUtils

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
//      statusBarColor 색상변경
//        window.statusBarColor = ContextCompat.getColor(this, R.color.maincolor)
        val uid = FirebaseAuthUtils.getUid()

        if (uid == "null") {

            Handler().postDelayed({
                val intent = Intent(baseContext, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, 1800)
        } else {
            Handler().postDelayed({
                val intent = Intent(baseContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 1800)
        }
    }
}