package com.hmis_tn.doctor.ui.emr_workflow.investigation_result.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.databinding.InvestigationResultFragmentBinding

class InvestigationResultFragment : Fragment() {
    private var binding: InvestigationResultFragmentBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.investigation_result_fragment,
                container,
                false
            )
        replaceFragment(InvestigationResultListViewFragment())
        return binding!!.root
    }

    private fun replaceFragment(
        fragment: Fragment
    ) {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainers, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}

