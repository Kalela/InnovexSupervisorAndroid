package com.kalela.innovexsupervisor.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kalela.innovexsupervisor.R
import com.kalela.innovexsupervisor.data.model.Task
import kotlinx.android.synthetic.main.report_list_item.view.*

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
        view.name_text_view.text = task.name
    }
}