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
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1

    interface OnWorkoutActionListener {
        fun onEditClick(workout: Workout, position: Int)
        fun onDeleteClick(workout: Workout, position: Int)
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHeaderDate: TextView = itemView.findViewById(R.id.tvHeaderDate)
    }

    class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvWorkoutName:    TextView = itemView.findViewById(R.id.tvWorkoutName)
        val tvWorkoutDetails: TextView = itemView.findViewById(R.id.tvWorkoutDetails)
        val tvWorkoutDate:    TextView = itemView.findViewById(R.id.tvWorkoutDate)
        val btnEdit:          ImageButton = itemView.findViewById(R.id.btnEditWorkout)
        val btnDelete:        ImageButton = itemView.findViewById(R.id.btnDeleteWorkout)
    }

    private var itemsWithHeaders = mutableListOf<Any>()

    init {
        updateItems()
    }

    private fun updateItems() {
        itemsWithHeaders.clear()
        if (workoutList.isEmpty()) return

        var lastDate = ""
        workoutList.forEach { workout ->
            val currentDate = DateUtils.formatHistoryDate(workout.dateMillis)
            if (currentDate != lastDate) {
                itemsWithHeaders.add(currentDate)
                lastDate = currentDate
            }
            itemsWithHeaders.add(workout)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemsWithHeaders[position] is String) TYPE_HEADER else TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_workout_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_workout, parent, false)
            WorkoutViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemsWithHeaders[position]
        if (holder is HeaderViewHolder && item is String) {
            holder.tvHeaderDate.text = item.uppercase()
        } else if (holder is WorkoutViewHolder && item is Workout) {
            holder.tvWorkoutName.text    = item.exerciseName
            holder.tvWorkoutDetails.text = "${item.muscleGroup}  •  ${item.sets} sets × ${item.reps} reps"
            holder.tvWorkoutDate.text    = DateUtils.formatHistoryDate(item.dateMillis)
            
            holder.btnEdit.setOnClickListener { listener.onEditClick(item, position) }
            holder.btnDelete.setOnClickListener { listener.onDeleteClick(item, position) }
        }
    }

    override fun getItemCount(): Int = itemsWithHeaders.size

    fun refreshData() {
        updateItems()
        notifyDataSetChanged()
    }

    // This ensures that when we notifyDataSetChanged in HomeActivity/HistoryActivity,
    // the internal itemsWithHeaders list is also updated.
    fun setData(newList: List<Workout>) {
        workoutList.clear()
        workoutList.addAll(newList)
        updateItems()
        notifyDataSetChanged()
    }
}
