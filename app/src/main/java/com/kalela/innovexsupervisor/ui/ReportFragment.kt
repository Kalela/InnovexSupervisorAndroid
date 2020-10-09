package com.kalela.innovexsupervisor.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.kalela.innovexsupervisor.R
import com.kalela.innovexsupervisor.base.BaseApplication
import com.kalela.innovexsupervisor.databinding.FragmentHomeBinding
import com.kalela.innovexsupervisor.databinding.FragmentReportBinding
import com.kalela.innovexsupervisor.viewmodel.HomeFragmentViewModel
import com.kalela.innovexsupervisor.viewmodel.factory.HomeFragmentViewModelFactory
import retrofit2.Retrofit
import javax.inject.Inject


class ReportFragment : Fragment() {

    private lateinit var binding : FragmentReportBinding
//    private lateinit var viewModel: HomeFragmentViewModel
//    private lateinit var viewModelFactory: HomeFragmentViewModelFactory
    @Inject
    lateinit var retrofit : Retrofit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report, container, false)
        (activity?.application as BaseApplication).apiComponent.injectReportFragment(this)


        return binding.root
    }
}