package com.oasys.digihealth.doctor.ui.emr_workflow.history.prescription.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.oasys.digihealth.doctor.R

import com.oasys.digihealth.doctor.databinding.FragmentHistoryPrescriptionBinding


class HistoryPrescriptionFragment : Fragment() {
    private var binding: FragmentHistoryPrescriptionBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_history_prescription,
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
        replaceFragment(HistoryPrescriptionChildFragment())
    }

}