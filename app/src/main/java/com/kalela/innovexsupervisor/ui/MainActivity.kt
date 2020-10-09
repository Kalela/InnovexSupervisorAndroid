package com.kalela.innovexsupervisor.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.kalela.innovexsupervisor.R
import com.kalela.innovexsupervisor.base.BaseApplication
import com.kalela.innovexsupervisor.data.model.Task
import com.kalela.innovexsupervisor.databinding.ActivityMainBinding
import com.kalela.innovexsupervisor.injection.retrofit.TasksService
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Our applications main activity/ Entry point.
 */
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    @Inject lateinit var retrofit : Retrofit

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        (application as BaseApplication).apiComponent.injectMainActivity(this)

//        checkBackendTasks()
    }

    fun checkBackendTasks() {
        val tasksService : TasksService = retrofit.create(
            TasksService::class.java)

        val responseLiveData : LiveData<Response<List<Task>>> = liveData {
            val response = tasksService.getAllTasks()
            emit(response)
        }

        responseLiveData.observe(this, Observer {
            Log.d(TAG, "checkBackendTasks: ${it.body()}")
        })

    }
}