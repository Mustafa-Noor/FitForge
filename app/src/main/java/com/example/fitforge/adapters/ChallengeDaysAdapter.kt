package com.example.fitforge.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitforge.R
import com.example.fitforge.data.models.ChallengeDay

class ChallengeDaysAdapter(
    private val days: List<ChallengeDay>,
    private val currentDayIndex: Int,
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
        
        val isLocked = position > currentDayIndex
        holder.ivLocked.visibility = if (isLocked) View.VISIBLE else View.GONE
        holder.tvDay.alpha = if (isLocked) 0.5f else 1.0f
        holder.tvDayLabel.alpha = if (isLocked) 0.5f else 1.0f
        
        holder.itemView.setOnClickListener {
            if (!isLocked) onDayClick(day)
        }
    }

    override fun getItemCount() = days.size
}
