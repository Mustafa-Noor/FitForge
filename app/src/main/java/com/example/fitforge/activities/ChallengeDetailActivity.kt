package com.example.fitforge.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitforge.R
import com.example.fitforge.adapters.ChallengeDaysAdapter
import com.example.fitforge.data.ChallengeData
import com.example.fitforge.data.SharedPreferencesManager

class ChallengeDetailActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferencesManager
    private lateinit var challengeId: String
    private lateinit var adapter: ChallengeDaysAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge_detail)

        prefs = SharedPreferencesManager(this)
        challengeId = intent.getStringExtra("challenge_id") ?: ""
        val challenge = ChallengeData.challenges.find { it.id == challengeId } ?: return

        findViewById<TextView>(R.id.tvChallengeTitle).text = challenge.title
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewDays)
        // Change to GridLayoutManager with 4 columns for a better grid view
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        
        loadDays(challenge)
    }

    private fun loadDays(challenge: com.example.fitforge.data.models.Challenge) {
        val currentProgress = prefs.getChallengeProgress(challengeId)
        adapter = ChallengeDaysAdapter(challenge.days, currentProgress) { day ->
            val intent = Intent(this, DayExercisesActivity::class.java)
            intent.putExtra("challenge_id", challengeId)
            intent.putExtra("day_number", day.dayNumber)
            startActivity(intent)
        }
        findViewById<RecyclerView>(R.id.recyclerViewDays).adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        // Refresh progress when returning from exercise screen
        val challenge = ChallengeData.challenges.find { it.id == challengeId }
        challenge?.let { loadDays(it) }
    }
}
