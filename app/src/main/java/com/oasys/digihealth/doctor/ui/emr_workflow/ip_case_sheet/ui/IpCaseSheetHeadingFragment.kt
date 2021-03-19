package com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.databinding.FragmentIpCaseSheetHeadingBinding
import com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.model.ProfileSection
import com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.model.ProfileSectionCategory
import com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.model.prev_records.observed_values.ResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.view_model.IpCaseSheetViewModel
import com.oasys.digihealth.doctor.utils.Utils

class IpCaseSheetHeadingFragment() : Fragment() {

    private var ipCaseSheetChildFragment: IpCaseSheetChildFragment? = null
    private var profileSection: ProfileSection? = null
    private var observedValueList: ArrayList<ResponseContent>? = null

    constructor(
        ipCaseSheetChildFragment: IpCaseSheetChildFragment,
        profileSection: ProfileSection,
        observedValueList: ArrayList<ResponseContent>? = null
    ) : this() {
        this.ipCaseSheetChildFragment = ipCaseSheetChildFragment
        this.profileSection = profileSection
        this.observedValueList = observedValueList
    }

    private var binding: FragmentIpCaseSheetHeadingBinding? = null
    private var viewModel: IpCaseSheetViewModel? = null
    private var utils: Utils? = null

    private val subHeadingsList = ArrayList<ProfileSectionCategory>()
    private var subHeadingsAdapter: IpCaseSheetSubHeadingAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_ip_case_sheet_heading,
            container,
            false
        )
        viewModel = ViewModelProvider(this)[IpCaseSheetViewModel::class.java]
        utils = Utils(requireActivity())

        initViews()
        listeners()

        return binding?.root
    }

    private fun initViews() {
        binding?.rvSubHeadings?.layoutManager = LinearLayoutManager(requireActivity())
//        val bundle = arguments
//        bundle?.let {
//            if (bundle.containsKey("sub_heading")) {
//                val subHeading = bundle.getSerializable("sub_heading")!!
//                val profileSection = subHeading as ProfileSection
//                subHeadingsAdapter = profileSection.profile_section_categories?.let {
//                    IpCaseSheetSubHeadingAdapter(
//                        profileSection,
//                        it as ArrayList<ProfileSectionCategory>,
//                        ipCaseSheetChildFragment
//                    )
//                }
//            }
//        }

        subHeadingsAdapter = IpCaseSheetSubHeadingAdapter(
            profileSection!!,
            profileSection!!.profile_section_categories as? ArrayList<ProfileSectionCategory>,
            ipCaseSheetChildFragment!!,
            observedValueList
        )
        binding?.rvSubHeadings?.adapter = subHeadingsAdapter
    }

    private fun listeners() {

    }
}
