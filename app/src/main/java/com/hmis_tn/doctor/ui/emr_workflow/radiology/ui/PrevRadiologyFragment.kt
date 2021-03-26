package com.hmis_tn.doctor.ui.emr_workflow.radiology.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.PrevRadiologyFragmentBinding
import com.hmis_tn.doctor.fire_base_analytics.AnalyticsManager
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.PodArrResult
import com.hmis_tn.doctor.ui.emr_workflow.view.lab.model.PrevLabResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.view.lab.view_model.PreviewRadiologyViewModel
import com.hmis_tn.doctor.ui.emr_workflow.view.lab.view_model.PreviewRadiologyViewModelFactory
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response


class PrevRadiologyFragment : Fragment() {
    private var binding: PrevRadiologyFragmentBinding? = null
    private var viewModel: PreviewRadiologyViewModel? = null
    private var utils: Utils? = null
    private var patientUuid: Int = 0
    private var mAdapter: PrevRadiologyParentAdapter? = null
    private var mainCallback: RadiologyPrevClickedListener? = null

    var appPreferences: AppPreferences? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.prev_radiology_fragment,
                container,
                false
            )

        viewModel = PreviewRadiologyViewModelFactory(
            requireActivity().application, prevlabrecordsRetrofitCallback
        ).create(PreviewRadiologyViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        utils = Utils(requireContext())
        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val patientID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        val facilityid = appPreferences?.getInt(AppConstants.FACILITY_UUID)

        viewModel?.getPrevRadiology_Records(patientID, facilityid)
        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })
        preparePatientLIstData()

        mAdapter!!.setOnItemClickListener(object : PrevRadiologyParentAdapter.OnItemClickListener {
            override fun onItemClick(responseContent: List<PodArrResult>?, isMofify: Boolean) {

                mainCallback!!.sendPrevtoChild(responseContent, isMofify)

            }
        })

        return binding!!.root
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (menuVisible) {
            trackPrevradiologyVisitAnalyticsVisit(utils!!.getEncounterType())
        }
    }

    val prevlabrecordsRetrofitCallback = object : RetrofitCallback<PrevLabResponseModel> {
        override fun onSuccessfulResponse(response: Response<PrevLabResponseModel>) {

            if (isAdded)
                if (!response.body()?.responseContents.isNullOrEmpty()) {
                    binding?.previewRecyclerView?.visibility = View.VISIBLE
                    binding?.noDataText?.visibility = View.GONE

                    mAdapter?.refreshList(response.body()?.responseContents!!)

                } else {
                    binding?.previewRecyclerView?.visibility = View.GONE
                    binding?.noDataText?.visibility = View.VISIBLE
                }


        }

        override fun onBadRequest(response: Response<PrevLabResponseModel>) {

            if (isAdded)
                if (response.code() == 400) {
                    binding?.previewRecyclerView?.visibility = View.GONE
                    binding?.noDataText?.visibility = View.VISIBLE

                } else {
                    binding?.previewRecyclerView?.visibility = View.VISIBLE
                    binding?.noDataText?.visibility = View.GONE
                }
        }

        override fun onServerError(response: Response<*>) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String) {
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progressBar.value = 8
        }
    }


    private fun preparePatientLIstData() {

        mAdapter =
            activity?.let {
                PrevRadiologyParentAdapter((requireActivity()))
            }!!
        binding?.previewRecyclerView!!.adapter = mAdapter
    }

    override fun onResume() {
        super.onResume()
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val patientID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        val facilityid = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        viewModel?.getPrevRadiology_Records(patientID, facilityid)
    }


    interface RadiologyPrevClickedListener {
        fun sendPrevtoChild(
            responseContent: List<PodArrResult>?, isModify: Boolean
        )
    }

    fun setOnTextClickedListener(callback: RadiologyPrevClickedListener) {
        this.mainCallback = callback
    }

    //track previous radiology

    fun trackPrevradiologyVisitAnalyticsVisit(type: String) {
        AnalyticsManager.getAnalyticsManager().trackPrevRadiologyVisit(type)
    }


    fun refreshList(patientID: Int?, facilityid: Int?) {
        viewModel?.getPrevRadiology_Records(patientID, facilityid)

    }

}


