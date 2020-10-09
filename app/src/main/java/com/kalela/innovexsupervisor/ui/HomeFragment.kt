package com.kalela.innovexsupervisor.ui

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.kalela.innovexsupervisor.R
import com.kalela.innovexsupervisor.base.BaseApplication
import com.kalela.innovexsupervisor.data.model.AnalogTime
import com.kalela.innovexsupervisor.databinding.FragmentHomeBinding
import com.kalela.innovexsupervisor.util.NetworkUtil
import com.kalela.innovexsupervisor.viewmodel.HomeFragmentViewModel
import com.kalela.innovexsupervisor.viewmodel.factory.HomeFragmentViewModelFactory
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Home page fragment. The first fragment on application load.
 */
class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var viewModelFactory: HomeFragmentViewModelFactory

    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        (activity?.application as BaseApplication).apiComponent.injectHomeFragment(this)
        viewModelFactory = HomeFragmentViewModelFactory(retrofit, viewLifecycleOwner)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeFragmentViewModel::class.java)
        binding.myViewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.initializeClock()
        checkNetworkConnectivity()

        handleColorChanges()
        toastHandler()
        prepareReportsFragment()
        return binding.root
    }

    /**
     * Handle color changes in UI
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun handleColorChanges() {
        val layerDrawable: LayerDrawable =
            resources.getDrawable(R.drawable.app_background) as LayerDrawable
        binding.root.background = layerDrawable
        val layer: ColorDrawable = layerDrawable.getDrawable(1) as ColorDrawable

        viewModel.dueTask.observe(viewLifecycleOwner, Observer {
            binding.taskName.text = it.name
            when (it.name) {
                "START" -> { // Change color of the wall
                    layer.color = it.color
                    layer.alpha = 200
                    layer.invalidateSelf()
                }
                "STOP" -> {
                    binding.analogClock.backboardColor = it.color
                }
                "STOP" -> {
                    binding.analogClock.numbersColor = it.color
                }
            }

        })
    }

    /**
     * Handle toast message for this activity
     */
    private fun toastHandler() {
        viewModel.message.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
            }
        })
    }

    /**
     * Check if phone is connected to the internet
     */
    fun checkNetworkConnectivity() {
        if (NetworkUtil.isOnline(requireActivity().applicationContext)) {
            viewModel.checkBackend() // Run first check if network available
            viewModel.isConnected = true
        } else {
            viewModel.isConnected = false
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.analogTime.observe(
            viewLifecycleOwner,
            Observer(function = fun(time: AnalogTime?) {
                time.let {
                    if (time != null) {
                        binding.analogClock.mMinute = time.minutes
                    }
                    if (time != null) {
                        binding.analogClock.mSecond = time.seconds
                    }
                }
            })
        )
    }

    /**
     * Set up on click listener that opens reports fragment
     */
    private fun prepareReportsFragment() {
        binding.getReportButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_reportFragment)

        }
    }

}