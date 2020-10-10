package com.kalela.innovexsupervisor.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kalela.innovexsupervisor.R
import com.kalela.innovexsupervisor.base.BaseApplication
import com.kalela.innovexsupervisor.data.model.Task
import com.kalela.innovexsupervisor.databinding.FragmentHomeBinding
import com.kalela.innovexsupervisor.databinding.FragmentReportBinding
import com.kalela.innovexsupervisor.ui.adapter.TaskReportRecyclerViewAdapter
import com.kalela.innovexsupervisor.viewmodel.HomeFragmentViewModel
import com.kalela.innovexsupervisor.viewmodel.ReportFragmentViewModel
import com.kalela.innovexsupervisor.viewmodel.factory.HomeFragmentViewModelFactory
import com.kalela.innovexsupervisor.viewmodel.factory.ReportFragmentViewModelFactory
import retrofit2.Retrofit
import javax.inject.Inject


class ReportFragment : Fragment() {

    private lateinit var binding: FragmentReportBinding
    private lateinit var viewModel: ReportFragmentViewModel
    private lateinit var viewModelFactory: ReportFragmentViewModelFactory
    private lateinit var adapter: TaskReportRecyclerViewAdapter

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

        initRecyclerView()

        return binding.root
    }

    private fun initRecyclerView() {
        binding.reportRecyclerView.layoutManager =
            LinearLayoutManager(requireActivity().applicationContext)
        adapter = TaskReportRecyclerViewAdapter()
        binding.reportRecyclerView.adapter = adapter
        displayTaskList()
    }

    private fun displayTaskList() {
        viewModel.getAllTasks()
        viewModel.allTasks.observe(viewLifecycleOwner, Observer {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })

    }
}