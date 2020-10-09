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
import com.kalela.innovexsupervisor.base.BaseApplication
import com.kalela.innovexsupervisor.databinding.FragmentHomeBinding
import com.kalela.innovexsupervisor.ui.clock.AnalogClock
import com.kalela.innovexsupervisor.viewmodel.HomeFragmentViewModel
import com.kalela.innovexsupervisor.viewmodel.factory.HomeFragmentViewModelFactory
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Home page fragment. The first fragment on application load.
 */
class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"
    private lateinit var binding : FragmentHomeBinding
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var viewModelFactory: HomeFragmentViewModelFactory
    @Inject
    lateinit var retrofit : Retrofit

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

        val layerDrawable : LayerDrawable = resources.getDrawable(R.drawable.app_background) as LayerDrawable

        viewModel.initializeClock()
        viewModel.checkBackendTasks()

        binding.getReportButton.setOnClickListener {
            val layer : ColorDrawable = layerDrawable.getDrawable(1) as ColorDrawable
            Log.d(TAG, "the layer is ${layer}, color is ${layer.color}")
            layer.color = 0xff999999.toInt()
            layer.alpha = 200
            layer.invalidateSelf()

            it.findNavController().navigate(R.id.action_homeFragment_to_reportFragment)

        }

        viewModel.message.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
            }
        })

        binding.root.background = layerDrawable

        return binding.root
    }

}