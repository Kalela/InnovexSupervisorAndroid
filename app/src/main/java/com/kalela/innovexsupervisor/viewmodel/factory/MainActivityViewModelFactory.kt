package com.kalela.innovexsupervisor.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kalela.innovexsupervisor.viewmodel.MainActivityViewModel

class MainActivityViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return MainActivityViewModel() as T
        }

        throw IllegalArgumentException("Unknown View Model class")
    }
}