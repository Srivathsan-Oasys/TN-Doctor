package com.hmis_tn.doctor.ui.emr_workflow.anesthesia_notes.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.databinding.FragmentAnesthesiaNotesHeadingBinding
import com.hmis_tn.doctor.ui.emr_workflow.anesthesia_notes.model.ProfileSection
import com.hmis_tn.doctor.ui.emr_workflow.anesthesia_notes.model.ProfileSectionCategory
import com.hmis_tn.doctor.ui.emr_workflow.anesthesia_notes.model.prev_records.observed_values.ResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.anesthesia_notes.view_model.AnesthesiaNotesViewModel
import com.hmis_tn.doctor.utils.Utils

class AnesthesiaNotesHeadingFragment() : Fragment() {

    private var anesthesiaNotesChildFragment: AnesthesiaNotesChildFragment? = null
    private var profileSection: ProfileSection? = null
    private var observedValueList: ArrayList<ResponseContent>? = null

    constructor(
        anesthesiaNotesChildFragment: AnesthesiaNotesChildFragment,
        profileSection: ProfileSection,
        observedValueList: ArrayList<ResponseContent>? = null
    ) : this() {
        this.anesthesiaNotesChildFragment = anesthesiaNotesChildFragment
        this.profileSection = profileSection
        this.observedValueList = observedValueList
    }

    private var binding: FragmentAnesthesiaNotesHeadingBinding? = null
    private var viewModel: AnesthesiaNotesViewModel? = null
    private var utils: Utils? = null

    private val subHeadingsList = ArrayList<ProfileSectionCategory>()
    private var subHeadingsAdapter: AnesthesiaNotesSubHeadingAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_anesthesia_notes_heading,
            container,
            false
        )
        viewModel = ViewModelProvider(this)[AnesthesiaNotesViewModel::class.java]
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
//                    AnesthesiaNotesSubHeadingAdapter(
//                        profileSection,
//                        it as ArrayList<ProfileSectionCategory>,
//                        anesthesiaNotesChildFragment!!
//                    )
//                }
//            }
//        }
        subHeadingsAdapter = AnesthesiaNotesSubHeadingAdapter(
            profileSection!!,
            profileSection!!.profile_section_categories as? ArrayList<ProfileSectionCategory>,
            anesthesiaNotesChildFragment!!,
            observedValueList
        )
        binding?.rvSubHeadings?.adapter = subHeadingsAdapter
    }

    private fun listeners() {

    }
}
