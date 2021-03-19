package com.oasys.digihealth.doctor.ui.emr_workflow.history.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.callbacks.FragmentBackClick
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentHistoryChildBinding
import com.oasys.digihealth.doctor.fire_base_analytics.AnalyticsManager
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.history.admission.ui.HistoryAdmissionFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.ui.AllergyFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.history.config.view.ConfigHistoryFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.history.diagnosis.view.HistoryDiagnosisFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.history.familyhistory.ui.HistoryFamilyFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.history.immunization.ui.HistoryImmunizationFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.history.lab.ui.HistoryLabFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.history.model.response.History
import com.oasys.digihealth.doctor.ui.emr_workflow.history.model.response.HistoryResponce
import com.oasys.digihealth.doctor.ui.emr_workflow.history.prescription.ui.HistoryPrescriptionFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.history.radiology.ui.HistoryRadiologFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.history.surgery.ui.HistorySurgeryFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.history.viewmodel.HistoryViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.viewmodel.HistoryViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.history.vitals.model.ui.HistoryVitalsFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.model.EmrWorkFlowResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.view.NoRecordsFragment
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response


class HistoryChildFragment : Fragment(), ConfigHistoryFragment.OnsaveHistoryRefreshListener {
    private var facility_id: Int? = 0
    private var binding: FragmentHistoryChildBinding? = null
    private var viewModel: HistoryViewModel? = null
    private var responseContents: String? = ""
    private var fragmentBackClick: FragmentBackClick? = null
    private var appPreferences: AppPreferences? = null
    private var utils: Utils? = null
    private var tabsArrayList: List<History?>? = null
    private var viewpageradapter: ViewPagerAdapter? = null
    private var selectedFragment: Fragment? = null
    private var selectedVPIndex = 0


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_history_child, container, false)

        utils = Utils(this.requireContext())
        if (activity !is FragmentBackClick) {
//            throw ClassCastException("Hosting activity must implement BackHandlerInterface")
        } else {
            fragmentBackClick = activity as FragmentBackClick?
        }
        viewModel = HistoryViewModelFactory(
            requireActivity().application
        ).create(HistoryViewModel::class.java)


        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        viewModel?.getHistoryList(facility_id!!, getHistoryAllResponseCallback)
        binding?.config?.setOnClickListener {
            val ft = childFragmentManager.beginTransaction()
            val dialog =
                ConfigHistoryFragment()
            dialog.show(ft, "Tag")
        }
        trackHistoryAnalyticsVisit(utils!!.getEncounterType())
        return binding!!.root
    }

    private fun setupViewPager(tabsArrayList: List<History?>) {
        viewpageradapter = ViewPagerAdapter(childFragmentManager)
        for (i in tabsArrayList.indices) {
            if (tabsArrayList[i]?.activity_code == "Allergy") {
                viewpageradapter!!.addFragment(AllergyFragment(), "Allergy")
            } else if (tabsArrayList[i]?.activity_code == AppConstants.ACTIVITY_CODE_RADIOLOGY) {

                viewpageradapter!!.addFragment(HistoryRadiologFragment(), "Radiology")
            } else if (tabsArrayList[i]?.activity_code == AppConstants.ACTIVITY_CODE_PRESCRIPTION) {

                viewpageradapter!!.addFragment(HistoryPrescriptionFragment(), "Prescription")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_FAMILY_HISTORY) {
                viewpageradapter!!.addFragment(HistoryFamilyFragment(), "Family History")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_LAB) {
                viewpageradapter!!.addFragment(HistoryLabFragment(), "Lab")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_VITALS) {

                viewpageradapter!!.addFragment(HistoryVitalsFragment(), "Vitals")
            } else if (tabsArrayList[i]!!.activity_code == "Surgury") {
                viewpageradapter!!.addFragment(HistorySurgeryFragment(), "Surgury")
            } else if (tabsArrayList[i]!!.activity_code == "IMM") {
                viewpageradapter!!.addFragment(HistoryImmunizationFragment(), "Immunization")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_DIAGNOSIS) {
                viewpageradapter!!.addFragment(HistoryDiagnosisFragment(), "Diagnosis")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_ADMISSION) {
                viewpageradapter!!.addFragment(HistoryAdmissionFragment(), "Admission")
            }
            /*else if(tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_HISTORY){
                viewpageradapter!!.addFragment(HistoryFragment(),"Diagnosis")
            }*/
            else {
                viewpageradapter!!.addFragment(NoRecordsFragment(), "NoRecord")
            }
        }
        binding?.viewPager?.adapter = viewpageradapter
        viewpageradapter?.notifyDataSetChanged()
        selectedVPIndex = binding?.viewPager?.currentItem!!
        binding?.viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                selectedVPIndex = position
//                getCurrentItem(page = position)


            }

        })

        binding?.cvNext!!.setOnClickListener {
            if (selectedVPIndex == binding?.viewPager?.adapter?.count!! - 1) {

                binding?.viewPager?.currentItem = getCurrentItem(page = 0)
            } else {

                binding?.viewPager?.currentItem = getCurrentItem(page = +1)
            }

        }
    }

    private fun getCurrentItem(page: Int): Int {
        return binding?.viewPager?.currentItem!! + page
    }

    val getHistoryAllResponseCallback = object : RetrofitCallback<HistoryResponce> {
        override fun onSuccessfulResponse(response: Response<HistoryResponce>?) {
            //Log.e("DataHistory", responseBody.toString())

            if (response!!.body()?.responseContents?.isNotEmpty()!!) {
                binding?.contentLinearLayout?.visibility = View.VISIBLE
                //binding?.noDataFoundTextView?.visibility = View.INVISIBLE
                tabsArrayList = response.body()?.responseContents!!
                setupViewPager(tabsArrayList!!)
                binding?.viewPager!!.offscreenPageLimit = 2
                binding?.tabLayout!!.setupWithViewPager(binding?.viewPager!!)
                for (i in tabsArrayList!!.indices) {
                    val layoutInflater: View? = LayoutInflater.from(activity)
                        .inflate(R.layout.history_custom_tab_row, null, false)
                    val tabTextView = layoutInflater!!.findViewById(R.id.tabTextView) as TextView
                    tabTextView.text = tabsArrayList!![i]?.activity_name
                    binding?.tabLayout?.getTabAt(i)!!.customView = layoutInflater
                }
            } else {
                binding?.tabLayout?.visibility = View.INVISIBLE
                binding?.cvNext?.visibility = View.INVISIBLE
                //binding?.noDataFoundTextView?.visibility = View.VISIBLE
            }
        }

        override fun onBadRequest(errorBody: Response<HistoryResponce>?) {
            //Log.e("DataHistory", "badRequest")
            val gson = GsonBuilder().create()
            val responseModel: EmrWorkFlowResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    EmrWorkFlowResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    responseModel.message!!
                )
            } catch (e: Exception) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                e.printStackTrace()
            }

        }

        override fun onServerError(response: Response<*>?) {
            // Log.e("DataHistory", "servererr")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            //Log.e("DataHistory", "unAuth")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            //Log.e("DataHistory", "forbidden")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String?) {
            //Log.e("DataHistory", "failure")
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
        }

        override fun onEverytime() {
            //Log.e("DataHistory", "everytime")
            viewModel!!.progress.value = 8
        }

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

    override fun onRefreshhistoryList() {
        viewModel?.getHistoryList(facility_id!!, getHistoryAllResponseCallback)
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is ConfigHistoryFragment) {
            childFragment.setOnSaveHistoryRefreshListener(this)
        }


    }

    fun trackHistoryAnalyticsVisit(type: String) {
        AnalyticsManager.getAnalyticsManager().trackHistory(type)
    }
}

