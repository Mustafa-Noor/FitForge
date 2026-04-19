package com.example.fitforge.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.fitforge.R
import com.example.fitforge.data.models.Badge

class BadgeAdapter(
    private val badgeList: List<Badge>
) : RecyclerView.Adapter<BadgeAdapter.BadgeViewHolder>() {

    class BadgeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvEmoji: TextView = itemView.findViewById(R.id.tvEmoji)
        val tvBadgeName: TextView = itemView.findViewById(R.id.tvBadgeName)
        val tvBadgeDescription: TextView = itemView.findViewById(R.id.tvBadgeDescription)
        val tvHowToGet: TextView = itemView.findViewById(R.id.tvHowToGet)
        val ivLockStatus: ImageView = itemView.findViewById(R.id.ivLockStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_badge, parent, false)
        return BadgeViewHolder(view)
    }

    override fun onBindViewHolder(holder: BadgeViewHolder, position: Int) {
        val badge = badgeList[position]
        holder.tvEmoji.text = badge.emoji
        holder.tvBadgeName.text = badge.name
        holder.tvBadgeDescription.text = badge.description
        holder.tvHowToGet.text = "Goal: ${badge.unlockCondition}"
        
        if (badge.isUnlocked) {
            holder.ivLockStatus.visibility = View.GONE
            holder.itemView.alpha = 1.0f
        } else {
            holder.ivLockStatus.visibility = View.VISIBLE
            holder.itemView.alpha = 0.6f
        }

        holder.itemView.setOnClickListener {
            val status = if (badge.isUnlocked) "✅ Unlocked!" else "🔒 Locked"
            Toast.makeText(holder.itemView.context, 
                "${badge.emoji} ${badge.name}: ${badge.description}\nStatus: $status", 
                Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = badgeList.size
}
