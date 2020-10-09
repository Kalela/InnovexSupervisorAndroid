package com.kalela.innovexsupervisor.viewmodel.factory

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kalela.innovexsupervisor.viewmodel.HomeFragmentViewModel
import retrofit2.Retrofit

class HomeFragmentViewModelFactory(
    val retrofit: Retrofit,
    private val viewLifecycleOwner: LifecycleOwner
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeFragmentViewModel::class.java)) {
            return HomeFragmentViewModel(
                retrofit = retrofit,
                viewLifecycleOwner = viewLifecycleOwner
            ) as T
        }

        throw IllegalArgumentException("Unknown View Model class")
    }
}