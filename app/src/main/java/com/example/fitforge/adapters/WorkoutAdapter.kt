package com.example.fitforge.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitforge.R
import com.example.fitforge.data.models.Workout
import com.example.fitforge.utils.DateUtils

class WorkoutAdapter(
    private val workoutList: MutableList<Workout>,
    private val listener: OnWorkoutActionListener
) : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

    interface OnWorkoutActionListener {
        fun onEditClick(workout: Workout, position: Int)
        fun onDeleteClick(workout: Workout, position: Int)
    }

    class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvWorkoutName:    TextView = itemView.findViewById(R.id.tvWorkoutName)
        val tvWorkoutDetails: TextView = itemView.findViewById(R.id.tvWorkoutDetails)
        val tvWorkoutDate:    TextView = itemView.findViewById(R.id.tvWorkoutDate)
        val btnEdit:          ImageButton = itemView.findViewById(R.id.btnEditWorkout)
        val btnDelete:        ImageButton = itemView.findViewById(R.id.btnDeleteWorkout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = workoutList[position]
        holder.tvWorkoutName.text    = workout.exerciseName
        holder.tvWorkoutDetails.text = "${workout.muscleGroup}  •  ${workout.sets} sets × ${workout.reps} reps"
        holder.tvWorkoutDate.text    = DateUtils.formatHistoryDate(workout.dateMillis)
        
        holder.btnEdit.setOnClickListener { listener.onEditClick(workout, position) }
        holder.btnDelete.setOnClickListener { listener.onDeleteClick(workout, position) }
    }

    override fun getItemCount(): Int = workoutList.size

    fun removeItem(position: Int) {
        workoutList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, workoutList.size)
    }
}
