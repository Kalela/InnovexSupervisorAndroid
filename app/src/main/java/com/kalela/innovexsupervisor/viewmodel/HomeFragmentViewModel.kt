package com.kalela.innovexsupervisor.viewmodel

import androidx.databinding.Observable
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.kalela.innovexsupervisor.data.model.AnalogTime
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

class HomeFragmentViewModel(
    val retrofit: Retrofit,
    private val viewLifecycleOwner: LifecycleOwner
) : ViewModel(), Observable {
    private val TAG = "HomeFragmentViewModel"

    private var mHourTracked = 0
    var seconds: Int = 0
    var minutes: Int = 0
    val analogTime = MutableLiveData<AnalogTime>()
    var isConnected: Boolean = true
    private val statusMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>
        get() = statusMessage
    private var task = MutableLiveData<Task>()
    val dueTask: LiveData<Task>
        get() = task


    private fun checkBackendTasks() {
        val tasksService: TasksService = retrofit.create(
            TasksService::class.java
        )

        val responseDueTask: LiveData<Response<List<Task>>> = liveData {
            val response = tasksService.getRunningTasks()
            emit(response)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                responseDueTask.observe(viewLifecycleOwner, Observer {
                    when (it.body()?.get(0)?.name) {
                        "START" -> {
                            task.value = Task(name = "STOP")
                        }
                        "STOP" -> {
                            task.value = Task(name = "REPORT")
                        }
                        "REPORT" -> {
                            task.value = Task(name = "START")
                        }
                    }
                })
            }
        }


    }

    /**
     * Stop all running tasks
     */
    fun stopAllTasks() {
        val tasksService: TasksService = retrofit.create(
            TasksService::class.java
        )

        val response: LiveData<Response<HashMap<String, String>>> = liveData {
            val response = tasksService.stopTasks()
            emit(response)
        }

        response.observe(viewLifecycleOwner, Observer {
            if (it.message() == "OK") {
                statusMessage.value = Event("Tasks stopped successfully")
            }
        })

    }

    /**
     * Delete all stored tasks
     */
    fun deleteAllTasks() {
        val tasksService: TasksService = retrofit.create(
            TasksService::class.java
        )

        val response: LiveData<Response<HashMap<String, String>>> = liveData {
            val response = tasksService.stopTasks()
            emit(response)
        }

        response.observe(viewLifecycleOwner, Observer {
            if (it.message() == "OK") {
                statusMessage.value = Event("Tasks stopped successfully")
            }
        })
    }

    /**
     * Set up recurring calls to simulate time.
     */
    fun initializeClock() {
        Timer().scheduleAtFixedRate(timerTask {
            seconds += 1
            if (seconds % 60 == 0) {
                minutes += 1
            }
            if (minutes % 60 == 0) {
                mHourTracked += 1
                mHourTracked =
                    if (mHourTracked > 12) mHourTracked - 12 else mHourTracked // Convert to 12 hour
            }
            viewLifecycleOwner.lifecycleScope.launch {
                withContext(Dispatchers.Main) {
                    analogTime.value = getTime(seconds, minutes)
                }
            }

            checkBackend()
        }, 1000, 1000)

    }

    private fun getTime(seconds: Int, minutes: Int): AnalogTime {
        return AnalogTime(seconds, minutes)
    }

    /**
     * Check running tasks if 30 seconds have elapsed
     */
    fun checkBackend() {
        if (seconds % 10 == 0) {
            checkBackendTasks()
        }
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}