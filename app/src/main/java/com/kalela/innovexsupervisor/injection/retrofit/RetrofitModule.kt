package com.kalela.innovexsupervisor.injection.retrofit

import com.kalela.innovexsupervisor.util.retrofit.RetrofitInstance
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Retrofit module that provides a singleton retrofit instance.
 */
@Module
class RetrofitModule {
    @Singleton
    @Provides
    fun provideRetrofitInstance(): Retrofit {
        return RetrofitInstance.getRetrofitInstance()
    }
}