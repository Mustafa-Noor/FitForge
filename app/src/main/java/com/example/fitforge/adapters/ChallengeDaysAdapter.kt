package com.example.fitforge.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.fitforge.R
import com.example.fitforge.data.models.ChallengeDay

class ChallengeDaysAdapter(
    private val days: List<ChallengeDay>,
    private val currentDayIndex: Int,
    private val hasCompletedToday: Boolean,
    private val onDayClick: (ChallengeDay) -> Unit
) : RecyclerView.Adapter<ChallengeDaysAdapter.DayViewHolder>() {

    class DayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDay: TextView = view.findViewById(R.id.tvDayNumber)
        val ivLocked: ImageView = view.findViewById(R.id.ivLocked)
        val tvDayLabel: TextView = view.findViewById(R.id.tvDayLabel)
        val frame: View = view.findViewById(R.id.frameDayCircle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_challenge_day, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = days[position]
        holder.tvDay.text = day.dayNumber.toString()
        holder.tvDayLabel.text = "Day ${day.dayNumber}"
        
        val isFutureDay = position > currentDayIndex
        // A day is "locked" if it's in the future OR if the user already did a day today and this is the next one
        val isLocked = isFutureDay || (hasCompletedToday && position == currentDayIndex)
        val isCompleted = position < currentDayIndex

        holder.ivLocked.visibility = if (isLocked && !isCompleted) View.VISIBLE else View.GONE
        
        // Visual feedback for different states
        when {
            isCompleted -> {
                holder.tvDay.alpha = 1.0f
                holder.tvDayLabel.alpha = 1.0f
                holder.tvDay.text = "✅"
            }
            isLocked -> {
                holder.tvDay.alpha = 0.4f
                holder.tvDayLabel.alpha = 0.4f
            }
            else -> {
                holder.tvDay.alpha = 1.0f
                holder.tvDayLabel.alpha = 1.0f
            }
        }
        
        holder.itemView.setOnClickListener {
            if (isCompleted) {
                Toast.makeText(holder.itemView.context, "Already crushed this day! 💪", Toast.LENGTH_SHORT).show()
            } else if (isLocked) {
                if (hasCompletedToday && position == currentDayIndex) {
                    Toast.makeText(holder.itemView.context, "One day at a time, champ! Come back tomorrow. ⏳", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(holder.itemView.context, "Complete previous days first! 🔒", Toast.LENGTH_SHORT).show()
                }
            } else {
                onDayClick(day)
            }
        }
    }

    override fun getItemCount() = days.size
}
