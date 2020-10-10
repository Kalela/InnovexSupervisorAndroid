package com.kalela.innovexsupervisor.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.kalela.innovexsupervisor.R
import com.kalela.innovexsupervisor.data.model.Task
import kotlinx.android.synthetic.main.report_list_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Task reports recyclerview
 */
class TaskReportRecyclerViewAdapter() :
    RecyclerView.Adapter<MyViewHolder>() {
    private val taskList = ArrayList<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.report_list_item, parent, false)

        return MyViewHolder(listItem)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    fun setList(subscribers : List<Task>) {
        taskList.clear()
        taskList.addAll(subscribers)
    }

}

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(task: Task) {
        view.name_text_view.text = "Event : ${task.name}"
        view.application_time_text_view.text = "Application time : ${getDateTime(task.application_time)}"
        view.task_time_text_view.text = "Actual time : ${getDateTime(task.actual_time)}"
        view.running_text_view.text = "Task is running : ${task.isRunning}"
        view.message_text_view.text = "${getDateTime(task.application_time)}  ${task.task_definition}"
    }

    private fun getDateTime(time:Timestamp): String? {
        return time.toDate().toString()
    }
}
