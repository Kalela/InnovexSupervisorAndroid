package com.kalela.innovexsupervisor.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.liveData
import com.kalela.innovexsupervisor.R
import com.kalela.innovexsupervisor.base.BaseApplication
import com.kalela.innovexsupervisor.data.model.Task
import com.kalela.innovexsupervisor.databinding.ActivityMainBinding
import com.kalela.innovexsupervisor.injection.retrofit.TasksService
import com.kalela.innovexsupervisor.util.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import java.util.HashMap
import javax.inject.Inject

/**
 * Our applications main activity/ Entry point.
 */
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    @Inject
    lateinit var retrofit: Retrofit

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        (application as BaseApplication).apiComponent.injectMainActivity(this)
    }

    /**
     * Delete all tasks before exiting the application
     */
    override fun onDestroy() {
        CoroutineScope(Dispatchers.Main).launch {

            val tasksService: TasksService = retrofit.create(
                TasksService::class.java
            )

            val result = async(Dispatchers.IO) {
                tasksService.stopTasks()
            }

            result.await()
            Toast.makeText(this@MainActivity, "All tasks stopped.", Toast.LENGTH_SHORT).show()

        }
        super.onDestroy()
    }

}