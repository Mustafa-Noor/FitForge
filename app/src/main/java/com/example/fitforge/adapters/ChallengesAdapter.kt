package com.example.fitforge.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitforge.R
import com.example.fitforge.data.models.Challenge

class ChallengesAdapter(
    private val challenges: List<Challenge>,
    private val onChallengeClick: (Challenge) -> Unit
) : RecyclerView.Adapter<ChallengesAdapter.ChallengeViewHolder>() {

    class ChallengeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivIcon: ImageView = view.findViewById(R.id.ivChallengeIcon)
        val tvTitle: TextView = view.findViewById(R.id.tvChallengeTitle)
        val tvDesc: TextView = view.findViewById(R.id.tvChallengeDesc)
        val tvDays: TextView = view.findViewById(R.id.tvChallengeDays)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_challenge_card, parent, false)
        return ChallengeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val challenge = challenges[position]
        holder.ivIcon.setImageResource(challenge.imageResId)
        holder.tvTitle.text = challenge.title
        holder.tvDesc.text = challenge.description
        holder.tvDays.text = "${challenge.totalDays} Days"
        
        holder.itemView.setOnClickListener { onChallengeClick(challenge) }
    }

    override fun getItemCount() = challenges.size
}
