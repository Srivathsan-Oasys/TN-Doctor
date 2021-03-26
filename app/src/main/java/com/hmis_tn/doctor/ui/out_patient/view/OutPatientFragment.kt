package com.hmis_tn.doctor.ui.out_patient.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.databinding.FragmentAdmissionBinding

class OutPatientFragment : Fragment() {
    private var binding: FragmentAdmissionBinding? = null
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
        replaceFragment(OutPatientChildFragment())


        return binding!!.root
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.addToBackStack("home")
        fragmentTransaction.commit()
    }
}

