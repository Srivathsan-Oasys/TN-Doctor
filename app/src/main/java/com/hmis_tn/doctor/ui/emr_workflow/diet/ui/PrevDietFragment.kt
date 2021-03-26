package com.hmis_tn.doctor.ui.emr_workflow.diet.ui

import android.os.Bundle
import android.util.Log
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
import com.hmis_tn.doctor.databinding.PrevDietFragmentBinding
import com.hmis_tn.doctor.fire_base_analytics.AnalyticsManager
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.diet.model.GetPreviousDietOrderResp
import com.hmis_tn.doctor.ui.emr_workflow.diet.model.PatientDiet
import com.hmis_tn.doctor.ui.emr_workflow.diet.model.request.GetPreviousDietOrderReq
import com.hmis_tn.doctor.ui.emr_workflow.diet.view_model.PreviousDietViewModel
import com.hmis_tn.doctor.utils.Utils
import retrofit2.Response

class PrevDietFragment : Fragment() {

    private var binding: PrevDietFragmentBinding? = null
    private var viewModel: PreviousDietViewModel? = null
    private var utils: Utils? = null

    private var appPreferences: AppPreferences? = null
    private var facilityId: Int? = null
    private var patientId: Int? = null

    private val prevDietList = ArrayList<PatientDiet>()
    private var prevDietAdapter: PrevDietAdapter? = null

    private var prevDiet: PatientDiet? = null
    private var callback: DietCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.prev_diet_fragment,
                container,
                false
            )
        utils = Utils(requireContext())

        viewModel = ViewModelProvider(this)[PreviousDietViewModel::class.java]

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilityId = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        patientId = appPreferences?.getInt(AppConstants.PATIENT_UUID)

        initViews()
        listeners()

        getPreviousDiet()

        return binding!!.root
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (menuVisible) {
            trackDietPreviousDiet(utils?.getEncounterType())
        }
    }

    private fun initViews() {
        binding?.previewRecyclerView?.layoutManager = LinearLayoutManager(activity)
        prevDietAdapter = PrevDietAdapter(prevDietList, callback!!) { previousDiet ->
            prevDiet = previousDiet
            prevDietList.forEach { patientDiet ->
                patientDiet.isExpanded = (patientDiet == previousDiet)
            }
        }
        binding?.previewRecyclerView?.adapter = prevDietAdapter
    }

    private fun listeners() {

    }

    private fun getPreviousDiet() {
        val body = GetPreviousDietOrderReq(5, patientId?.toString())
        viewModel?.getPreviousDiet(facilityId!!, body, getPreviousDietOrderRespCallback)
    }

    private val getPreviousDietOrderRespCallback =
        object : RetrofitCallback<GetPreviousDietOrderResp> {
            override fun onSuccessfulResponse(responseBody: Response<GetPreviousDietOrderResp>?) {
                if (responseBody?.isSuccessful == true) {
                    responseBody.body()?.let { getPreviousDietOrderResp ->
                        getPreviousDietOrderResp.responseContents.let { list ->
                            list.patient_diet_list?.let { prevDietList.addAll(it) }
                            if (prevDietList.size > 0)
                                prevDietList[0].isExpanded = true
                            prevDietAdapter?.notifyDataSetChanged()
                        }
                    }
                }
            }

            override fun onBadRequest(errorBody: Response<GetPreviousDietOrderResp>?) {
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

    private fun trackDietPreviousDiet(type: String?) {
        AnalyticsManager.getAnalyticsManager().trackDietPreviousDiet(type)
    }

    fun setCallbackListener(mcallback: DietCallback) {
        Log.e("setCallbackListener", "inside")
        callback = mcallback
    }
}