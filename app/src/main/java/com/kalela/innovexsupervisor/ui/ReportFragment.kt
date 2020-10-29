package com.kalela.innovexsupervisor.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kalela.innovexsupervisor.R
import com.kalela.innovexsupervisor.base.BaseApplication
import com.kalela.innovexsupervisor.databinding.FragmentReportBinding
import com.kalela.innovexsupervisor.ui.adapter.TaskReportRecyclerViewAdapter
import com.kalela.innovexsupervisor.util.showLoading
import com.kalela.innovexsupervisor.viewmodel.ReportFragmentViewModel
import com.kalela.innovexsupervisor.viewmodel.factory.ReportFragmentViewModelFactory
import retrofit2.Retrofit
import javax.inject.Inject


class ReportFragment : Fragment() {

    private lateinit var binding: FragmentReportBinding
    private lateinit var viewModel: ReportFragmentViewModel
    private lateinit var viewModelFactory: ReportFragmentViewModelFactory
    private lateinit var adapter: TaskReportRecyclerViewAdapter
    private lateinit var snackbar: Snackbar

    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report, container, false)
        (activity?.application as BaseApplication).apiComponent.injectReportFragment(this)
        viewModelFactory = ReportFragmentViewModelFactory(retrofit, viewLifecycleOwner)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(ReportFragmentViewModel::class.java)
        showLoading(true, binding.progressBar)
        initRecyclerView()
        initSwipeRefresh()
        snackHandler()


        return binding.root
    }

    /**
     * Initialise the swipe refresh layout
     */
    private fun initSwipeRefresh() {
        binding.reportSwipeRefreshLayout.setOnRefreshListener {
            initRecyclerView()
        }
    }


    /**
     * Initialise the recycler view
     */
    private fun initRecyclerView() {
        binding.reportRecyclerView.layoutManager =
            LinearLayoutManager(requireActivity().applicationContext)
        adapter = TaskReportRecyclerViewAdapter()
        binding.reportRecyclerView.adapter = adapter
        displayTaskList()
    }

    /**
     * Populate recyclerview adapter with data
     */
    private fun displayTaskList() {
        showLoading(true, binding.progressBar)
        viewModel.getAllTasks()
        viewModel.allTasks.observe(viewLifecycleOwner, Observer {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
            showLoading(false, binding.progressBar)
            binding.reportSwipeRefreshLayout.isRefreshing = false
        })
    }

    /**
     * Handle snackbar message for this fragment in the activity
     */
    private fun snackHandler() {
        viewModel.snackMessage.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let { str ->
                snackbar = Snackbar.make(
                    binding.root, str,
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("Dismiss", View.OnClickListener {  })
                snackbar.setActionTextColor(Color.BLUE)
                val snackbarView = snackbar.view
                snackbarView.setBackgroundColor(Color.LTGRAY)
                val textView =
                    snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                textView.setTextColor(Color.BLACK)
                textView.textSize = 14f
                showLoading(false, binding.progressBar)
                binding.reportSwipeRefreshLayout.isRefreshing = false
                snackbar.show()
            }
        })
    }
}