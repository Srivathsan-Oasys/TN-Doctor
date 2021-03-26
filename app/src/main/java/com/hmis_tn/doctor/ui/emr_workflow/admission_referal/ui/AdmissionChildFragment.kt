package com.hmis_tn.doctor.ui.emr_workflow.admission_referal.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.callbacks.FragmentBackClick
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.FragmentAdmissionChildBinding
import com.hmis_tn.doctor.fire_base_analytics.AnalyticsManager
import com.hmis_tn.doctor.ui.emr_workflow.admission_referal.view_model.AdmissionViewModel
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.view_model.AdmissionViewModelFactory
import com.hmis_tn.doctor.ui.emr_workflow.history.model.response.History
import com.hmis_tn.doctor.utils.Utils

class AdmissionChildFragment : Fragment() {

    private var binding: FragmentAdmissionChildBinding? = null
    private var viewModel: AdmissionViewModel? = null
    private var responseContents: String? = ""
    private var fragmentBackClick: FragmentBackClick? = null
    private var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    private var tabsArrayList: List<History?>? = null
    private var viewpageradapter: ViewPagerAdapter? = null
    private var selectedFragment: Fragment? = null
    private var flow: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            flow = this.getString("flow", flow)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_admission_child, container, false)

        utils = Utils(this.requireContext())
        trackAdmissionVisit(utils!!.getEncounterType())
        if (activity !is FragmentBackClick) {
//            throw ClassCastException("Hosting activity must implement BackHandlerInterface")
        } else {
            fragmentBackClick = activity as FragmentBackClick?
        }
        viewModel = AdmissionViewModelFactory(
            requireActivity().application
        ).create(AdmissionViewModel::class.java)

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        setupViewPager(binding?.viewPager!!)
        binding?.viewPager!!.offscreenPageLimit = 2
        binding?.tabLayout!!.setupWithViewPager(binding?.viewPager!!)

        return binding!!.root
    }

    private fun setupViewPager(viewPager: ViewPager) {
        viewpageradapter = ViewPagerAdapter(childFragmentManager)
        viewpageradapter?.apply {
            if (flow == AppConstants.OUT_PATIENT) {
                addFragment(AdmissionTabFragment(), "Admission")
                addFragment(ReferralTabFragment(), "Referral")
            }

            if (flow != AppConstants.OUT_PATIENT) {
                addFragment(ReferralTabFragment(), "Referral")
                addFragment(TransferTabFragment(), "Transfer")
                addFragment(DischargeTabFragment(), "Discharge Advice")
            }
        }

        viewPager.adapter = viewpageradapter

    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) :
        FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val mFragmentList = java.util.ArrayList<Fragment>()
        private val mFragmentTitleList = java.util.ArrayList<String>()

        override fun getItem(position: Int): Fragment {

            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }
    }

    private fun trackAdmissionVisit(type: String) {
        AnalyticsManager.getAnalyticsManager().trackAdmissionRefferalVisit(type)
    }
}