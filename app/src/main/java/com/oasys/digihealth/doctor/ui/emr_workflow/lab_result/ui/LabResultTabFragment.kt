package com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.callbacks.FragmentBackClick
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.LabResultTabLayoutBinding
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.view_model.LabResultViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.model.compare.ResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.view_model.LabResultViewModel
import com.oasys.digihealth.doctor.utils.Utils
import java.util.*

class LabResultTabFragment(
    private val compareDateList: ArrayList<String>,
    private val list: ArrayList<ResponseContent>
) : Fragment() {
    private var binding: LabResultTabLayoutBinding? = null
    private var viewModel: LabResultViewModel? = null
    private var fragmentBackClick: FragmentBackClick? = null
    private var appPreferences: AppPreferences? = null
    private var utils: Utils? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.lab_result_tab_layout, container, false)

        utils = Utils(requireContext())
        if (activity is FragmentBackClick) {
            fragmentBackClick = activity as FragmentBackClick?
        }
        viewModel = LabResultViewModelFactory(
            requireActivity().application
        ).create(LabResultViewModel::class.java)

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)

        initViews()

        return binding!!.root
    }

    private fun initViews() {
        setupViewPager(binding?.viewPagerLabResult!!)
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        val labResultGetDataFragment = LabResultGetDataFragment(
            compareDateList = compareDateList,
            list = list,
            backClick = {
                goBack()
            },
            cancelClick = {
                goBack()
            }
        )
        val labResultGrapicalFragment = LabResultGrapicalFragment(
            list = list,
            backClick = {
                goBack()
            },
            cancelClick = {
                goBack()
            }
        )
        adapter.addFragment(labResultGetDataFragment, "List View")
        adapter.addFragment(labResultGrapicalFragment, "Graphical View")
        viewPager!!.adapter = adapter

        binding?.viewPagerLabResult!!.offscreenPageLimit = 2
        binding?.tabLayout!!.setupWithViewPager(binding?.viewPagerLabResult!!)
    }

    private fun goBack() {
        fragmentManager?.popBackStack()
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager?) :
        FragmentStatePagerAdapter(manager!!) {

        private val mFragmentList: MutableList<Fragment> = ArrayList()
        private val mFragmentTitleList: MutableList<String> = ArrayList()
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

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }

    override fun onStart() {
        super.onStart()
        fragmentBackClick?.setSelectedFragment(this)
    }
}
