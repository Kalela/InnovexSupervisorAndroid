package com.kalela.innovexsupervisor.viewmodel

import android.util.Log
import androidx.databinding.Observable
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.kalela.innovexsupervisor.data.model.Task
import com.kalela.innovexsupervisor.injection.retrofit.TasksService
import com.kalela.innovexsupervisor.ui.clock.AnalogClock
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

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage
    private var tasks = MutableLiveData<List<Task>>()
    val allTasks: LiveData<List<Task>>
        get() = tasks

    fun checkBackendTasks() {
        val tasksService: TasksService = retrofit.create(
            TasksService::class.java
        )

        val responseLiveData: LiveData<Response<List<Task>>> = liveData {
            val response = tasksService.getAllTasks()
            emit(response)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                responseLiveData.observe(viewLifecycleOwner, Observer {
                    tasks.value = it.body()
                })
            }
        }


    }

    fun initializeClock() {
        checkBackend() // start backend checl on start
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
            checkBackend()
        }, 1000, 1000)

    }

    private fun checkBackend() {
        if (seconds % 30 == 0 || seconds == 0) {
            checkBackendTasks()
        }
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }


}