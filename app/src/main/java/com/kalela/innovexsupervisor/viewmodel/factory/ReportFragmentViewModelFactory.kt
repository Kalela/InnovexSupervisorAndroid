package com.kalela.innovexsupervisor.viewmodel.factory

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kalela.innovexsupervisor.viewmodel.ReportFragmentViewModel
import retrofit2.Retrofit

class ReportFragmentViewModelFactory(
    val retrofit: Retrofit,
    private val viewLifecycleOwner: LifecycleOwner
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReportFragmentViewModel::class.java)) {
            return ReportFragmentViewModel(
                retrofit = retrofit,
                viewLifecycleOwner = viewLifecycleOwner
            ) as T
        }

        throw IllegalArgumentException("Unknown View Model class")
    }
}