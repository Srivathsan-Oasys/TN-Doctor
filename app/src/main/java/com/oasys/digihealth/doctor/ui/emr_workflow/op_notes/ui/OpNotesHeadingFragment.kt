package com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.databinding.FragmentOpNotesHeadingBinding
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.ProfileSection
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.ProfileSectionCategory
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.prev_records.observed_values.ResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.view_model.OpNotesViewModel
import com.oasys.digihealth.doctor.utils.Utils

class OpNotesHeadingFragment() : Fragment() {

    private var opNotesChildFragment: OpNotesChildFragment? = null
    private var profileSection: ProfileSection? = null
    private var observedValueList: ArrayList<ResponseContent>? = null

    constructor(
        opNotesChildFragment: OpNotesChildFragment,
        profileSection: ProfileSection,
        observedValueList: ArrayList<ResponseContent>? = null
    ) : this() {
        this.opNotesChildFragment = opNotesChildFragment
        this.profileSection = profileSection
        this.observedValueList = observedValueList
    }

    private var binding: FragmentOpNotesHeadingBinding? = null
    private var viewModel: OpNotesViewModel? = null
    private var utils: Utils? = null

    private val subHeadingsList = ArrayList<ProfileSectionCategory>()
    private var subHeadingsAdapter: OpNotesSubHeadingAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_op_notes_heading,
            container,
            false
        )
        viewModel = ViewModelProvider(this)[OpNotesViewModel::class.java]
        utils = Utils(requireContext())

        initViews()
        listeners()

        return binding?.root
    }

    private fun initViews() {
        binding?.rvSubHeadings?.layoutManager = LinearLayoutManager(requireContext())
//        val bundle = arguments
//        bundle?.let {
//            if (bundle.containsKey("sub_heading")) {
//                val subHeading = bundle.getSerializable("sub_heading")!!
//                val profileSection = subHeading as ProfileSection
//                subHeadingsAdapter = profileSection.profile_section_categories?.let {
//                    OpNotesSubHeadingAdapter(
//                        profileSection,
//                        it as ArrayList<ProfileSectionCategory>,
//                        opNotesChildFragment
//                    )
//                }
//            }
//        }
        subHeadingsAdapter = OpNotesSubHeadingAdapter(
            profileSection!!,
            profileSection!!.profile_section_categories as? ArrayList<ProfileSectionCategory>,
            opNotesChildFragment!!,
            observedValueList
        )
        binding?.rvSubHeadings?.adapter = subHeadingsAdapter
    }

    private fun listeners() {

    }
}
