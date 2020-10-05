package com.kalela.innovexsupervisor.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kalela.innovexsupervisor.R
import com.kalela.innovexsupervisor.databinding.ActivityMainBinding

/**
 * Our applications main activity/ Entry point.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.root.setBackgroundResource(R.drawable.app_background)

    }
}