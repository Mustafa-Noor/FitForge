package com.example.fitforge.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitforge.R

class MuscleGroupAdapter(
    private val muscleGroups: List<Triple<String, Int, String>>, // Name, IconRes, Triple
    private val listener: (String) -> Unit
) : RecyclerView.Adapter<MuscleGroupAdapter.MuscleViewHolder>() {

    class MuscleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivIcon: ImageView = itemView.findViewById(R.id.ivMuscleIcon)
        val tvName: TextView = itemView.findViewById(R.id.tvMuscleName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MuscleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_muscle_group, parent, false)
        return MuscleViewHolder(view)
    }

    override fun onBindViewHolder(holder: MuscleViewHolder, position: Int) {
        val (name, iconRes, _) = muscleGroups[position]
        holder.tvName.text = name
        holder.ivIcon.setImageResource(iconRes)
        holder.itemView.setOnClickListener { listener(name) }
    }

    override fun getItemCount() = muscleGroups.size
}
