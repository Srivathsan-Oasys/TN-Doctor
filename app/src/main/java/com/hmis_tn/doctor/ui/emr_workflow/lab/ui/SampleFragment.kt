package com.hmis_tn.doctor.ui.emr_workflow.lab.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.databinding.SampleLayoutBinding
import com.hmis_tn.doctor.ui.emr_workflow.view.lab.view_model.PreviewViewModel

class SampleFragment : Fragment() {
    private var binding: SampleLayoutBinding? = null
    private var viewModel: PreviewViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.sample_layout,
                container,
                false
            )



        return binding!!.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }


}