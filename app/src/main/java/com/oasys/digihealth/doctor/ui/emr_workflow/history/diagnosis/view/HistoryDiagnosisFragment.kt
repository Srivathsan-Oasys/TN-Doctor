package com.oasys.digihealth.doctor.ui.emr_workflow.history.diagnosis.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.databinding.FragmentDiagnosisBinding

class HistoryDiagnosisFragment : Fragment() {

    var binding: FragmentDiagnosisBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_diagnosis, container, false)


        return binding!!.root
    }

    private fun replaceFragment(fragment: Fragment) {

        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.diagnosis_fragment_Container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()


    }

    override fun onResume() {
        super.onResume()
        replaceFragment(HistoryDiagnosisChildFragment())
    }
}