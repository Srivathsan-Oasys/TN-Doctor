package com.hmis_tn.doctor.ui.emr_workflow.prescription.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.databinding.FragmentPrescriptionBinding

class PrescriptionFragment : Fragment() {
    private var binding: FragmentPrescriptionBinding? = null

    private var fragment: PrescriptionChildFragment? = null

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_prescription,
                container,
                false
            )

        val args = arguments
        if (args == null) {

        } else {

            var type = args.getInt(AppConstants.RESPONSETYPE)

            fragment = PrescriptionChildFragment()

            val bundle = Bundle()
            bundle.putInt(AppConstants.RESPONSETYPE, type)
            fragment!!.arguments = bundle

            replaceFragment(fragment!!)

        }

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


    fun refreshPage() {


        val prescription = PrescriptionFragment()
        val bundle = Bundle()
        bundle.putInt(AppConstants.RESPONSETYPE, 0)
        prescription.arguments = bundle

        replaceFragment(prescription)
    }

}

