package com.hmis_tn.doctor.ui.emr_workflow.critical_care_chart.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.databinding.DialogFragmentCriticalCareChartCompareBinding

class CompareDialogFragment(
    private val compareData: LinkedHashMap<String, LinkedHashMap<String, Float>>?
) : DialogFragment() {

    var binding: DialogFragmentCriticalCareChartCompareBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_fragment_critical_care_chart_compare,
            container,
            false
        )

        initViews()
        listeners()

        return binding?.root!!
    }

    private fun initViews() {
        binding?.tabCompare?.setupWithViewPager(binding?.viewPagerCompare)
        setupViewPager(compareData)
    }

    private fun listeners() {
        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun setupViewPager(compareData: LinkedHashMap<String, LinkedHashMap<String, Float>>?) {
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        val criticalCareChartCompareListFragment =
            CriticalCareChartCompareListFragment(compareData)
        //     var empty:LinkedHashMap<String, LinkedHashMap<String, Float>>?

        val criticalCareChartCompareGraphicalFragment =
            CriticalCareChartCompareGraphicalFragment(compareData)
        viewPagerAdapter.addFragment(criticalCareChartCompareListFragment, "List View")
        viewPagerAdapter.addFragment(criticalCareChartCompareGraphicalFragment, "Graphical View")
        binding?.viewPagerCompare?.adapter = viewPagerAdapter
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) :
        FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
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
}
