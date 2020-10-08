package com.kalela.innovexsupervisor.ui

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.kalela.innovexsupervisor.R
import com.kalela.innovexsupervisor.databinding.FragmentHomeBinding
import com.kalela.innovexsupervisor.viewmodel.HomeFragmentViewModel
import com.kalela.innovexsupervisor.viewmodel.factory.HomeFragmentViewModelFactory


class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"
    private lateinit var binding : FragmentHomeBinding
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var viewModelFactory: HomeFragmentViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        viewModelFactory = HomeFragmentViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeFragmentViewModel::class.java)
        binding.myViewModel = viewModel
        binding.lifecycleOwner = this

        val layerDrawable : LayerDrawable = resources.getDrawable(R.drawable.app_background) as LayerDrawable
        Log.d(TAG, "number of background layers = ${layerDrawable.numberOfLayers}")

        binding.getReportButton.setOnClickListener {
            Log.d(TAG, "onCreate: On click")
            val layer : ColorDrawable = layerDrawable.getDrawable(1) as ColorDrawable
            Log.d(TAG, "the layer is ${layer}, color is ${layer.color}")
            layer.color = 0xff999999.toInt()
            layer.alpha = 100
            layer.invalidateSelf()

            it.findNavController().navigate(R.id.action_homeFragment_to_reportFragment)

        }

        viewModel.subscribeToRealtimeUpdates()
        viewModel.message.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
            }
        })

        binding.root.background = layerDrawable

        return binding.root
    }

}