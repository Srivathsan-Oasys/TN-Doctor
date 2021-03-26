package com.hmis_tn.doctor.ui.emr_workflow.history.familyhistory.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.databinding.FragmentFamilyHistoryBinding


class HistoryFamilyFragment : Fragment() {
    private var binding: FragmentFamilyHistoryBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_family_history,
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
        replaceFragment(HistoryFamilyChildFragment())
    }

}