package com.example.semestralka

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.semestralka.database.ProjectEntity

class ProjectItemAdapter: RecyclerView.Adapter<ProjectItemAdapter.ViewHolder>() {
    var data = listOf<ProjectEntity>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val projectName: TextView = itemView.findViewById(R.id.projectNameRecycler)
        val workedHours: TextView = itemView.findViewById(R.id.workedHoursRecycler)
        val recommendedHours: TextView = itemView.findViewById(R.id.recommendedHoursRecycler)

        fun bind(item: ProjectEntity) {
            val res = itemView.context.resources
            projectName.text = res.getString(R.string.project_name_recycler) + item.projectName
            workedHours.text = res.getString(R.string.workedHours) + getTimeStringFromIntInSecond(item.workedHours)
            recommendedHours.text = res.getString(R.string.RecommendedHours) + item.recommendedHours.toString() + " Hod."
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.project_item, parent, false)
                return ViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

}

