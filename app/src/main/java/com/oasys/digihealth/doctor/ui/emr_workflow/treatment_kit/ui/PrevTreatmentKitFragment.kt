package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.PrevTreatmentKitFragmentBinding
import com.oasys.digihealth.doctor.fire_base_analytics.AnalyticsManager
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.TreatmentKitPrevResponsModel
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.TreatmentKitResponsResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.view_model.TreatmentKitViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.view_model.TreatmentKitViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.view.lab.model.PrevLabResponseModel
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response

class PrevTreatmentKitFragment : Fragment() {

    private var binding: PrevTreatmentKitFragmentBinding? = null
    private var viewModel: TreatmentKitViewModel? = null
    private var utils: Utils? = null
    private var patientUuid: Int = 0
    private var mAdapter: PrevTreatmentParentParentAdapter? = null
    var appPreferences: AppPreferences? = null
    private var mainCallback: LabPrevClickedListener? = null
    private var mainModifyCallback: LabModifyClickedListener? = null
    private var addMainCallback: PrevAddNewClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Utils(requireContext()).setCalendarLocale("en", requireContext())

        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.prev_treatment_kit_fragment,
                container,
                false
            )

        viewModel = TreatmentKitViewModelFactory(
            requireActivity().application
        ).create(TreatmentKitViewModel::class.java)
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

        viewModel?.getPrevTreatment_kit_Records(
            patientID!!,
            facilityid,
            prevlabrecordsRetrofitCallback
        )
        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })
        preparePatientLIstData()

        mAdapter!!.setOnRepeatItemClickListener(
            object : PrevTreatmentParentParentAdapter.OnRepeatItemClickListener {
                override fun onRepeatItemClick(responseContent: TreatmentKitResponsResponseContent) {
                    Log.e("Repeat", "" + responseContent)
                    mainCallback!!.sendPrevtoChild(responseContent)
                }
            })

        mAdapter!!.setOnModifyItemClickListener(
            object : PrevTreatmentParentParentAdapter.OnModifyItemClickListener {
                override fun onModifyItemClick(responseContent: TreatmentKitResponsResponseContent) {
                    Log.e("Modify", "" + responseContent)
                    mainModifyCallback!!.sendModify(responseContent)
                }
            })

        mAdapter?.addNewItem(object : PrevTreatmentParentParentAdapter.AddClickListener {
            override fun onAddNew() {
                addMainCallback!!.addnewItem()
            }
        })

        return binding!!.root
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (menuVisible) {
            trackPrevTreatmentAnalyticsVisit(utils!!.getEncounterType())
        }
    }

    val prevlabrecordsRetrofitCallback = object : RetrofitCallback<TreatmentKitPrevResponsModel> {
        override fun onSuccessfulResponse(response: Response<TreatmentKitPrevResponsModel>) {

            if (response.body()?.responseContents?.isNotEmpty()!!) {
                //viewModel?.errorText?.value = 8.toString()
                mAdapter?.refreshList(response.body()?.responseContents!!)

            } /*else {
                viewModel?.errorText?.value = 0.toString()
            }*/

        }

        override fun onBadRequest(response: Response<TreatmentKitPrevResponsModel>) {
            val gson = GsonBuilder().create()
            val responseModel: PrevLabResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    PrevLabResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    response.message()
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
            viewModel!!.progress.value = 8
        }


    }

    private fun preparePatientLIstData() {
        mAdapter = PrevTreatmentParentParentAdapter((requireActivity()))
        binding?.previewRecyclerView!!.adapter = mAdapter


    }

    override fun onResume() {
        super.onResume()
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val patientID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        val facilityid = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        viewModel?.getPrevTreatment_kit_Records(
            patientID!!,
            facilityid,
            prevlabrecordsRetrofitCallback
        )
    }

    fun prevLabRecords() {
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val patientID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        val facilityid = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        viewModel?.getPrevTreatment_kit_Records(
            patientID!!,
            facilityid,
            prevlabrecordsRetrofitCallback
        )
    }

    interface LabPrevClickedListener {
        fun sendPrevtoChild(
            responseContent: TreatmentKitResponsResponseContent?
        )
    }

    interface LabModifyClickedListener {
        fun sendModify(
            responseContent: TreatmentKitResponsResponseContent?
        )
    }

    fun setOnTextClickedListener(callback: LabPrevClickedListener) {
        this.mainCallback = callback
    }

    fun setOnModifyClickedListener(callback: LabModifyClickedListener) {
        this.mainModifyCallback = callback
    }

    interface PrevAddNewClickListener {
        fun addnewItem()
    }

    fun setAddnewItemListener(callbackAc: PrevAddNewClickListener) {
        this.addMainCallback = callbackAc
    }

    fun trackPrevTreatmentAnalyticsVisit(type: String) {
        AnalyticsManager.getAnalyticsManager().trackPrevTreatmentVisit(type)
    }
}
