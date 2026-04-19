package com.example.fitforge.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fitforge.R
import com.example.fitforge.data.models.Exercise

class ExerciseAdapter(
    private val exerciseList: MutableList<Exercise>,
    private val listener: OnExerciseClickListener
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    interface OnExerciseClickListener {
        fun onExerciseClick(exercise: Exercise)
    }

    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvExerciseName: TextView = itemView.findViewById(R.id.tvExerciseName)
        val tvMuscleType:   TextView = itemView.findViewById(R.id.tvMuscleType)
        val tvExerciseLevel: TextView = itemView.findViewById(R.id.tvExerciseLevel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exerciseList[position]
        holder.tvExerciseName.text = exercise.name
        holder.tvMuscleType.text   = exercise.muscleGroup
        holder.tvExerciseLevel.text = exercise.level
        
        // Color the level text
        val context = holder.itemView.context
        val colorRes = when (exercise.level) {
            "Beginner" -> R.color.fit_success
            "Intermediate" -> R.color.fit_warning
            "Advanced" -> R.color.fit_error
            else -> R.color.fit_text_secondary
        }
        holder.tvExerciseLevel.setTextColor(ContextCompat.getColor(context, colorRes))

        holder.itemView.setOnClickListener { listener.onExerciseClick(exercise) }
    }

    override fun getItemCount(): Int = exerciseList.size
}
