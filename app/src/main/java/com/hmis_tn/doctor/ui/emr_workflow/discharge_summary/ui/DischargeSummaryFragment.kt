package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.databinding.FragmentDischargeSummaryBinding

class DischargeSummaryFragment : Fragment() {

    private lateinit var binding: FragmentDischargeSummaryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_discharge_summary, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        childFragmentManager.beginTransaction().let {
            it.replace(R.id.fragmentContainer, DischargeSummaryChildFragment())
            it.addToBackStack(null)
            it.commit()
        }
    }

}
