package com.kalela.innovexsupervisor.base

import android.app.Application
import com.kalela.innovexsupervisor.injection.retrofit.ApiComponent
import com.kalela.innovexsupervisor.injection.retrofit.DaggerApiComponent
import com.kalela.innovexsupervisor.injection.retrofit.RetrofitModule

/**
 * Contains dependencies that are required to be instantiated before the application begins.
 */
class BaseApplication : Application() {
    lateinit var apiComponent: ApiComponent

    override fun onCreate() {
        apiComponent = initDagger()
        super.onCreate()
    }

    private fun initDagger(): ApiComponent = DaggerApiComponent
        .builder()
        .retrofitModule(RetrofitModule())
        .build()
}