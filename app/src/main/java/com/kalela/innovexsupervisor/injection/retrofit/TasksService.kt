package com.kalela.innovexsupervisor.injection.retrofit

import com.kalela.innovexsupervisor.data.model.Task
import retrofit2.Response
import retrofit2.http.GET

interface TasksService {
    @GET("tasks/report")
    suspend fun getAllTasks() : Response<List<Task>>

    @GET("tasks/running")
    suspend fun getRunningTasks() : Response<List<Task>>

    @GET("tasks/stop")
    suspend fun stopTasks() : Response<HashMap<String, String>>

    @GET("tasks/start")
    suspend fun startTasks() : Response<HashMap<String, String>>

    @GET("tasks/clear")
    suspend fun clearTasks() : Response<HashMap<String, String>>
}