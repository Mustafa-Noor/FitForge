package com.example.fitforge.activities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fitforge.R

class SuccessAwardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_award)

        val points = intent.getIntExtra("points_earned", 0)
        val title = intent.getStringExtra("success_title") ?: "BUILT DIFFERENT"
        val message = intent.getStringExtra("success_message") ?: "You absolute unit. Keep grinding."
        val emoji = intent.getStringExtra("success_emoji") ?: "🏆"

        findViewById<TextView>(R.id.tvPointsEarned).text = "+$points"
        findViewById<TextView>(R.id.tvSuccessTitle).text = title
        findViewById<TextView>(R.id.tvSuccessMessage).text = message
        findViewById<TextView>(R.id.tvMainEmoji).text = emoji

        findViewById<Button>(R.id.btnContinue).setOnClickListener {
            finish()
        }
    }
}
