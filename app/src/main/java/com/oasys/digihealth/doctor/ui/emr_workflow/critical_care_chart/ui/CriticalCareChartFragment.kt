package com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.databinding.FragmentCriticalCareChartBinding

class CriticalCareChartFragment : Fragment() {

    private var binding: FragmentCriticalCareChartBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_critical_care_chart,
            container,
            false
        )

        return binding?.root!!
    }

    private fun replaceFragment(
        fragment: Fragment
    ) {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onResume() {
        super.onResume()
        replaceFragment(CriticalCareChartChildFragment())
    }
}
