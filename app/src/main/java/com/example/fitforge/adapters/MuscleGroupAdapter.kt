package com.example.fitforge.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitforge.R

class MuscleGroupAdapter(
    private val muscleGroups: List<Pair<String, String>>,
    private val listener: (String) -> Unit
) : RecyclerView.Adapter<MuscleGroupAdapter.MuscleViewHolder>() {

    class MuscleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvEmoji: TextView = itemView.findViewById(R.id.tvMuscleEmoji)
        val tvName: TextView = itemView.findViewById(R.id.tvMuscleName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MuscleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_muscle_group, parent, false)
        return MuscleViewHolder(view)
    }

    override fun onBindViewHolder(holder: MuscleViewHolder, position: Int) {
        val (name, emoji) = muscleGroups[position]
        holder.tvName.text = name
        holder.tvEmoji.text = emoji
        holder.itemView.setOnClickListener { listener(name) }
    }

    override fun getItemCount() = muscleGroups.size
}
