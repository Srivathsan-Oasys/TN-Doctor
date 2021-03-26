package com.hmis_tn.doctor.ui.out_patient.view

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
import com.hmis_tn.doctor.databinding.FragmentInpatientChildBinding

import com.hmis_tn.doctor.ui.emr_workflow.history.model.response.History
import com.hmis_tn.doctor.ui.out_patient.view_model.InPatientViewModel
import com.hmis_tn.doctor.ui.out_patient.view_model.InPatientViewModelFactory
import com.hmis_tn.doctor.utils.Utils


class OutPatientChildFragment : Fragment() {
    private var binding: FragmentInpatientChildBinding? = null
    private var viewModel: InPatientViewModel? = null
    private var responseContents: String? = ""
    private var fragmentBackClick: FragmentBackClick? = null
    private var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    private var tabsArrayList: List<History?>? = null
    private var viewpageradapter: ViewPagerAdapter? = null
    private var selectedFragment: Fragment? = null


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_inpatient_child, container, false)

        utils = Utils(this.requireContext())
        if (activity !is FragmentBackClick) {
//            throw ClassCastException("Hosting activity must implement BackHandlerInterface")
        } else {
            fragmentBackClick = activity as FragmentBackClick?
        }
        viewModel = InPatientViewModelFactory(
            requireActivity().application
        ).create(InPatientViewModel::class.java)

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        setupViewPager(binding?.viewPager!!)
        binding?.viewPager!!.setOffscreenPageLimit(2)
        binding?.tabLayout!!.setupWithViewPager(binding?.viewPager!!)


        return binding!!.root
    }

    private fun setupViewPager(viewPager: ViewPager) {
        viewpageradapter = ViewPagerAdapter(childFragmentManager)
        viewpageradapter!!.addFragment(OutPatientFragmentNew(), "Out patient")
        viewpageradapter!!.addFragment(MyPatientTabFragment(), "My Patient")

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

    override fun onStart() {
        super.onStart()
        fragmentBackClick?.setSelectedFragment(this)
    }
}

