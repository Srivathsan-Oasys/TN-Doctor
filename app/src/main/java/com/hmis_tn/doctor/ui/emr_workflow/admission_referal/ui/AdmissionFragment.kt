package com.hmis_tn.doctor.ui.emr_workflow.admission_referal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.databinding.FragmentAdmissionBinding

class AdmissionFragment : Fragment() {
    private var binding: FragmentAdmissionBinding? = null
    private var flow: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            flow = this.getString("flow", flow)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_admission,
                container,
                false
            )
        replaceFragment(AdmissionChildFragment())


        return binding!!.root
    }

    private fun replaceFragment(fragment: Fragment) {
        val bundle = Bundle()
        bundle.putString("flow", flow)
        fragment.arguments = bundle
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}

