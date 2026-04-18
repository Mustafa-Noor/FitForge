package com.example.fitforge.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.example.fitforge.R
import com.example.fitforge.data.SharedPreferencesManager

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val prefs = SharedPreferencesManager(this)

        findViewById<Button>(R.id.btnGetStarted).setOnClickListener {
            prefs.setFirstLaunchComplete()
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}
