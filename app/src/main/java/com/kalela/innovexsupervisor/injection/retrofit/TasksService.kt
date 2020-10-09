package com.kalela.innovexsupervisor.injection.retrofit

import com.kalela.innovexsupervisor.data.model.Task
import retrofit2.Response
import retrofit2.http.GET

interface TasksService {
    @GET("tasks/report")
    suspend fun getAllTasks() : Response<List<Task>>

    @GET("tasks/running")
    suspend fun getRunningTasks() : Response<List<Task>>
}