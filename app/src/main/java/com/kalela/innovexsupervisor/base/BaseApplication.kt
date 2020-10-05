package com.kalela.innovexsupervisor.base

import android.app.Application

/**
 * Contains dependencies that are required to be instantiated before the application begins.
 */
class BaseApplication : Application() {
//    lateinit var smartPhoneComponent: SmartPhoneComponent
//
//    override fun onCreate() {
//        smartPhoneComponent = initDagger()
//        super.onCreate()
//    }
//
//    private fun initDagger(): SmartPhoneComponent = DaggerSmartPhoneComponent
//        .builder()
//        .memoryCardModule(MemoryCardModule(1000))
//        .build()
}