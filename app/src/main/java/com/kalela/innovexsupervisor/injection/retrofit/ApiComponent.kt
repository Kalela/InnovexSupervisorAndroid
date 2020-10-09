package com.kalela.innovexsupervisor.injection.retrofit

import com.kalela.innovexsupervisor.ui.HomeFragment
import com.kalela.innovexsupervisor.ui.MainActivity
import com.kalela.innovexsupervisor.ui.ReportFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitModule::class])
interface ApiComponent {
    fun injectHomeFragment(homeFragment: HomeFragment)
    fun injectMainActivity(mainActivity: MainActivity)
    fun injectReportFragment(reportFragment: ReportFragment)
}