package com.kalela.innovexsupervisor.ui

import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kalela.innovexsupervisor.R
import com.kalela.innovexsupervisor.databinding.ActivityMainBinding
import com.kalela.innovexsupervisor.viewmodel.MainActivityViewModel
import com.kalela.innovexsupervisor.viewmodel.factory.MainActivityViewModelFactory

/**
 * Our applications main activity/ Entry point.
 */
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var viewModelFactory: MainActivityViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelFactory = MainActivityViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.myViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.subscribeToRealtimeUpdates()
        viewModel.message.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
            }
        })


        val layerDrawable : LayerDrawable = resources.getDrawable(R.drawable.app_background) as LayerDrawable
        Log.d(TAG, "number of background layers = ${layerDrawable.numberOfLayers}")

        binding.getReportButton.setOnClickListener {
            Log.d(TAG, "onCreate: On click")
            val layer : ColorDrawable = layerDrawable.getDrawable(1) as ColorDrawable
            Log.d(TAG, "the layer is ${layer}, color is ${layer.color}")
            layer.color = 0xff999999.toInt()
            layer.alpha = 100
            layer.invalidateSelf()

        }

        binding.root.background = layerDrawable

    }
}