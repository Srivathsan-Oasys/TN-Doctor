package com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.databinding.FragmentDocumentBinding
import com.hmis_tn.doctor.ui.emr_workflow.documents.ui.DocumentChildFragment

class DocumentFragment : Fragment() {

    private var binding: FragmentDocumentBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_document,
                container,
                false
            )
        replaceFragment(DocumentChildFragment())
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
}

