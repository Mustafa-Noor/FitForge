package com.example.fitforge

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.fitforge.activities.HomeActivity
import com.example.fitforge.activities.WelcomeActivity
import com.example.fitforge.data.SharedPreferencesManager
import com.example.fitforge.notifications.FitNotificationManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FitNotificationManager.createChannels(this)

        val prefs = SharedPreferencesManager(this)

        Handler(Looper.getMainLooper()).postDelayed({
            val destination = if (prefs.isFirstLaunch()) WelcomeActivity::class.java
                              else HomeActivity::class.java
            startActivity(Intent(this, destination))
            finish()
        }, 2000)
    }
}
