package com.example.fitforge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fitforge.data.Workout
import com.example.fitforge.databinding.ItemWorkoutBinding

class WorkoutAdapter(private val workouts: List<Workout>) : 
    RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

    inner class WorkoutViewHolder(private val binding: ItemWorkoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(workout: Workout) {
            binding.tvExerciseName.text = workout.exerciseName
            binding.tvSetsReps.text = "${workout.sets} sets × ${workout.reps} reps"
            binding.tvWeight.text = "Weight: ${workout.weight}"
            binding.tvDate.text = workout.date
            binding.tvDuration.text = "Duration: ${workout.duration} min"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val binding = ItemWorkoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(workouts[position])
    }

    override fun getItemCount() = workouts.size
}
