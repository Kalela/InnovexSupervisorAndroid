package com.kalela.innovexsupervisor.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kalela.innovexsupervisor.R
import com.kalela.innovexsupervisor.databinding.ActivityMainBinding
import com.kalela.innovexsupervisor.viewmodel.HomeFragmentViewModel
import com.kalela.innovexsupervisor.viewmodel.factory.HomeFragmentViewModelFactory

/**
 * Our applications main activity/ Entry point.
 */
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }
}