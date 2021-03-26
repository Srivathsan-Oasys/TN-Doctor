package com.hmis_tn.doctor.ui.emr_workflow.speciality_sketch.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.PrevSpecialitySketchFragmentBinding
import com.hmis_tn.doctor.fire_base_analytics.AnalyticsManager
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.speciality_sketch.model.SpecialitySketchPrevContent
import com.hmis_tn.doctor.ui.emr_workflow.speciality_sketch.model.SpecialitySketchPrevResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.speciality_sketch.viewmodel.PrevSpecialitySketchViewModel
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response

class SpeciaitySketchPrevFragment : Fragment() {

    private var binding: PrevSpecialitySketchFragmentBinding? = null
    private var viewModel: PrevSpecialitySketchViewModel? = null
    private var utils: Utils? = null

    private var appPreferences: AppPreferences? = null
    private var facilityId: Int? = null
    private var patientId: Int? = null

    private val prevSpecialitySketchList = ArrayList<SpecialitySketchPrevContent>()
    private var prevSpeciaitySketchAdapter: PrevSpeciaitySketchAdapter? = null

    private var prevDiet: SpecialitySketchPrevContent? = null

    private var mainCallback: PrevClickedListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.prev_speciality_sketch_fragment,
                container,
                false
            )
        utils = Utils(requireActivity())

        viewModel = ViewModelProvider(this)[PrevSpecialitySketchViewModel::class.java]

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilityId = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        patientId = appPreferences?.getInt(AppConstants.PATIENT_UUID)


        initViews()

        getPreviousSpecilitySketch()

        return binding!!.root
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (menuVisible) {
            trackSpecialitySketchPreviousSpecialitySketch(utils?.getEncounterType())
        }
    }

    private fun initViews() {
        binding?.previewRecyclerView?.layoutManager = LinearLayoutManager(activity)
        prevSpeciaitySketchAdapter = PrevSpeciaitySketchAdapter(requireContext(), ArrayList())
        binding?.previewRecyclerView?.adapter = prevSpeciaitySketchAdapter

        prevSpeciaitySketchAdapter!!.setOnViewClickListener(object :
            PrevSpeciaitySketchAdapter.OnViewClickListener {
            override fun onViewClick(responseContent: SpecialitySketchPrevContent) {

                mainCallback!!.sendPrevToMain(responseContent)

            }
        })

    }

    private fun getPreviousSpecilitySketch() {
        viewModel?.getPreviousSpecialitySketch(
            facilityId!!,
            patientId!!,
            getPreviousDietOrderRespCallback
        )
        //getData()
    }

    private val getPreviousDietOrderRespCallback =
        object : RetrofitCallback<SpecialitySketchPrevResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<SpecialitySketchPrevResponseModel>?) {

                prevSpeciaitySketchAdapter?.setData(responseBody!!.body()!!.responseContents)

                prevSpeciaitySketchAdapter?.notifyDataSetChanged()

            }

            override fun onBadRequest(errorBody: Response<SpecialitySketchPrevResponseModel>?) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onServerError(response: Response<*>?) {
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

            override fun onFailure(s: String?) {
                if (s != null) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        s
                    )
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private fun getData() {


    }


    interface PrevClickedListener {
        fun sendPrevToMain(
            sentData: SpecialitySketchPrevContent
        )
    }

    fun setOnTextClickedListener(callback: PrevClickedListener) {
        this.mainCallback = callback
    }


    override fun onResume() {
        super.onResume()


        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilityId = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        patientId = appPreferences?.getInt(AppConstants.PATIENT_UUID)


        initViews()

        getPreviousSpecilitySketch()

    }

    private fun trackSpecialitySketchPreviousSpecialitySketch(type: String?) {
        AnalyticsManager.getAnalyticsManager().trackSpecialitySketchPreviousSpecialitySketch(type)
    }

}