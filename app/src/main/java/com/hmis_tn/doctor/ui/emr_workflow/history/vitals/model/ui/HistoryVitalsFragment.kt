package com.hmis_tn.doctor.ui.emr_workflow.history.vitals.model.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.databinding.FragmentHistoryVitalsBinding

class HistoryVitalsFragment : Fragment() {
    private var binding: FragmentHistoryVitalsBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_history_vitals,
                container,
                false
            )


        return binding!!.root
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onResume() {
        super.onResume()
        replaceFragment(HistoryVitalsChildFragment())
    }
}

