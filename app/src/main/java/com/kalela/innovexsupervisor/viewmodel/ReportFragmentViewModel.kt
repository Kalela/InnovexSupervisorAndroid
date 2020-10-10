package com.kalela.innovexsupervisor.viewmodel

import androidx.databinding.Observable
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.kalela.innovexsupervisor.data.model.Task
import com.kalela.innovexsupervisor.injection.retrofit.TasksService
import com.kalela.innovexsupervisor.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*
import kotlin.concurrent.timerTask

class ReportFragmentViewModel(
    val retrofit: Retrofit,
    private val viewLifecycleOwner: LifecycleOwner
) : ViewModel(), Observable {

    private var tasks = MutableLiveData<List<Task>>()
    val allTasks: LiveData<List<Task>>
        get() = tasks

    fun getAllTasks() {
        val tasksService: TasksService = retrofit.create(
            TasksService::class.java
        )
        val response: LiveData<Response<List<Task>>> = liveData {
            val response = tasksService.getAllTasks()
            emit(response)
        }

        response.observe(viewLifecycleOwner, Observer {
            tasks.value = it.body()
        })
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}