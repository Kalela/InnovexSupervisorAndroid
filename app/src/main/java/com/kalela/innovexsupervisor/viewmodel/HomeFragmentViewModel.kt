package com.kalela.innovexsupervisor.viewmodel

import android.util.Log
import androidx.databinding.Observable
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.kalela.innovexsupervisor.data.model.AnalogTime
import com.kalela.innovexsupervisor.data.model.Task
import com.kalela.innovexsupervisor.injection.retrofit.TasksService
import com.kalela.innovexsupervisor.util.Event
import kotlinx.coroutines.CoroutineScope
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
    private val snackStatusMessage = MutableLiveData<Event<String>>()
    val snackMessage: LiveData<Event<String>>
        get() = snackStatusMessage

    private var task = MutableLiveData<Task>()
    val dueTask: LiveData<Task>
        get() = task

    private var tasksStopped: Boolean = false
    var clockInitialized: Boolean = false
    val timer: Timer = Timer()


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
                    if (it.body()?.get(0)?.error != "") {
                        snackStatusMessage.value = Event(
                            "An error occurred on our end. Please try again later."
                        )
                        stopAllTasks()
                    } else {
                        when (it.body()?.get(0)?.name) {
                            "START" -> {
                                task.value =
                                    it.body()?.get(0)?.color?.let { it1 ->
                                        Task(
                                            name = "STOP",
                                            color = it1
                                        )
                                    }
                            }
                            "STOP" -> {
                                task.value =
                                    it.body()?.get(0)?.color?.let { it1 ->
                                        Task(
                                            name = "REPORT",
                                            color = it1
                                        )
                                    }
                            }
                            "REPORT" -> {
                                task.value =
                                    it.body()?.get(0)?.color?.let { it1 ->
                                        Task(
                                            name = "START",
                                            color = it1
                                        )
                                    }
                            }
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
        tasksStopped = true;
        response.observe(viewLifecycleOwner, Observer {
            statusMessage.value = Event("Tasks stopped successfully")
        })

    }

    /**
     * Start running tasks
     */
    fun startRunningTasks() {
        val tasksService: TasksService = retrofit.create(
            TasksService::class.java
        )

        val response: LiveData<Response<HashMap<String, String>>> = liveData {
            val response = tasksService.startTasks()
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
        clockInitialized = true
        timer.scheduleAtFixedRate(timerTask {
            Log.d(TAG, "initializeClock: seconds is $seconds, minutes is $minutes")
            seconds += 1
            if (seconds % 60 == 0) {
                minutes += 1
            }
            if (minutes % 60 == 0) {
                mHourTracked += 1
                mHourTracked =
                    if (mHourTracked > 12) mHourTracked - 12 else mHourTracked // Convert to 12 hour
            }


            analogTime.postValue(AnalogTime(seconds, minutes))


            checkBackend()
        }, 1000, 1000)

    }

    /**
     * Dispose timer
     */
    fun disposeClock() {
        timer.cancel()
    }

    /**
     * Check running tasks if 30 seconds have elapsed
     */
    fun checkBackend() {
        if (seconds % 10 == 0 && !tasksStopped && seconds > 0) {
            checkBackendTasks()
        }
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}