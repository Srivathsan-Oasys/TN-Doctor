package com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.component.extention.isTablet
import com.oasys.digihealth.doctor.databinding.FragmentLabBinding
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui.LabChildFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui.LabChildFragmentNew

class LabFragment : Fragment() {
    private var binding: FragmentLabBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_lab,
                container,
                false
            )

        return binding!!.root
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
        if (isTablet(requireContext())) {
            replaceFragment(LabChildFragment())

        } else {
            replaceFragment(LabChildFragmentNew())
        }
    }
}

