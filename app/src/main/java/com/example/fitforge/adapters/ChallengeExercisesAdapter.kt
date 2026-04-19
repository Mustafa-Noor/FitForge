package com.example.fitforge.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitforge.R
import com.example.fitforge.data.models.ChallengeExercise

class ChallengeExercisesAdapter(
    private val exercises: List<ChallengeExercise>
) : RecyclerView.Adapter<ChallengeExercisesAdapter.ExerciseViewHolder>() {

    class ExerciseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvExerciseName)
        val tvDetails: TextView = view.findViewById(R.id.tvExerciseDetails)
        val tvTimer: TextView = view.findViewById(R.id.tvTimer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_challenge_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.tvName.text = exercise.exerciseName
        
        if (exercise.durationSeconds > 0) {
            holder.tvDetails.text = "Duration: ${exercise.durationSeconds}s"
            holder.tvTimer.visibility = View.VISIBLE
            holder.tvTimer.text = "⏱️ ${exercise.durationSeconds}s"
        } else {
            holder.tvDetails.text = "${exercise.sets} sets x ${exercise.reps} reps"
            holder.tvTimer.visibility = View.GONE
        }
    }

    override fun getItemCount() = exercises.size
}
