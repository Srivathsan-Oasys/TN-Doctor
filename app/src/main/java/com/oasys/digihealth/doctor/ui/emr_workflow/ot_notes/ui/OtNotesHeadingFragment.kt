package com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.databinding.FragmentOtNotesHeadingBinding
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.ProfileSection
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.ProfileSectionCategory
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.prev_records.observed_values.ResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.view_model.OtNotesViewModel
import com.oasys.digihealth.doctor.utils.Utils

class OtNotesHeadingFragment(

) : Fragment() {

    private var otNotesChildFragment: OtNotesChildFragment? = null
    private var profileSection: ProfileSection? = null
    private var observedValueList: ArrayList<ResponseContent>? = null

    constructor(
        otNotesChildFragment: OtNotesChildFragment,
        profileSection: ProfileSection,
        observedValueList: ArrayList<ResponseContent>? = null
    ) : this() {
        this.otNotesChildFragment = otNotesChildFragment
        this.profileSection = profileSection
        this.observedValueList = observedValueList
    }

    private var binding: FragmentOtNotesHeadingBinding? = null
    private var viewModel: OtNotesViewModel? = null
    private var utils: Utils? = null

    private var subHeadingsAdapter: OtNotesSubHeadingAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_ot_notes_heading,
            container,
            false
        )
        viewModel = ViewModelProvider(this)[OtNotesViewModel::class.java]
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
//                    OtNotesSubHeadingAdapter(
//                        profileSection,
//                        it as ArrayList<ProfileSectionCategory>,
//                        otNotesChildFragment
//                    )
//                }
//            }
//        }
        subHeadingsAdapter = OtNotesSubHeadingAdapter(
            profileSection!!,
            profileSection!!.profile_section_categories as? ArrayList<ProfileSectionCategory>,
            otNotesChildFragment!!,
            observedValueList
        )
        binding?.rvSubHeadings?.adapter = subHeadingsAdapter
    }

    private fun listeners() {

    }
}
