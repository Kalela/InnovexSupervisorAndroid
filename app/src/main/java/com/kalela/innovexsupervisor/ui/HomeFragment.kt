package com.kalela.innovexsupervisor.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kalela.innovexsupervisor.R
import com.kalela.innovexsupervisor.base.BaseApplication
import com.kalela.innovexsupervisor.data.model.AnalogTime
import com.kalela.innovexsupervisor.databinding.FragmentHomeBinding
import com.kalela.innovexsupervisor.util.NetworkUtil
import com.kalela.innovexsupervisor.util.showLoading
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
    private lateinit var snackbar: Snackbar

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

        checkNetworkConnectivity()
        showLoading(true, binding.progressBar)
        stopTasksClickListener()
        observeTimerChanges();
        handleColorChanges()
        toastHandler()
        snackHandler()
        prepareReportsFragment()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        if (!viewModel.clockInitialized) {
            viewModel.startRunningTasks()
            viewModel.initializeClock() // Ensure initialize clock and tasks is only done once
        }
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDetach() {
        viewModel.disposeClock()
        super.onDetach()
    }

    /**
     * Observe the timer values and populate the Analog clock view
     */
    private fun observeTimerChanges() {
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
     * Stop all running tasks
     */
    private fun stopTasksClickListener() {
        binding.stopTasksButton.setOnClickListener {
            viewModel.stopAllTasks()
        }
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
            showLoading(false, binding.progressBar)
            when (it.name) {
                "START" -> { // Change color of the wall
                    layer.color = it.color
                    layer.alpha = 200
                    layer.invalidateSelf()
                }
                "STOP" -> {
                    binding.analogClock.backboardColor = it.color
                }
                "REPORT" -> {
                    binding.analogClock.numbersColor = it.color
                }
            }

        })
    }

    /**
     * Handle toast message for this fragment in the activity
     */
    private fun toastHandler() {
        viewModel.message.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
            }
        })
    }

    /**
     * Handle snackbar message for this fragment in the activity
     */
    private fun snackHandler() {
        viewModel.snackMessage.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                snackbar = Snackbar.make(binding.root, it,
                    Snackbar.LENGTH_INDEFINITE).setAction("Exit", View.OnClickListener {
                    activity?.finish();
                })
                snackbar.setActionTextColor(Color.BLUE)
                val snackbarView = snackbar.view
                snackbarView.setBackgroundColor(Color.LTGRAY)
                val textView =
                    snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                textView.setTextColor(Color.BLACK)
                textView.textSize = 14f
                showLoading(false, binding.progressBar) // Only works because snackbar message only handles one error occurrnce
                snackbar.show()
            }
        })
    }

    /**
     * Check if phone is connected to the internet
     */
    private fun checkNetworkConnectivity() {
        if (NetworkUtil.isOnline(requireActivity().applicationContext)) {
            viewModel.checkBackend() // Run first check if network available
            viewModel.isConnected = true
        } else {
            viewModel.isConnected = false
        }
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