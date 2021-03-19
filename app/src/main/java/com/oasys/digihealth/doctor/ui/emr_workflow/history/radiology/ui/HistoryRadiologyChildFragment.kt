package com.oasys.digihealth.doctor.ui.emr_workflow.view.lab.ui

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
import com.oasys.digihealth.doctor.databinding.HistoryRadiologyFragmentBinding
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.history.radiology.model.RadiologyResModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.radiology.ui.HistoryRadiologyAdapter
import com.oasys.digihealth.doctor.ui.emr_workflow.history.radiology.viewmodel.HistoryRadiologyViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.radiology.viewmodel.HistoryRadiologyViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.ui.PrevLabParentAdapter
import com.oasys.digihealth.doctor.ui.emr_workflow.view.lab.model.PrevLabResponseModel
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response


class HistoryRadiologyChildFragment : Fragment() {
    private var binding: HistoryRadiologyFragmentBinding? = null
    private var viewModel: HistoryRadiologyViewModel? = null
    private var utils: Utils? = null
    private var patientUuid: Int = 0
    private var mAdapter: PrevLabParentAdapter? = null
    private var historyRadiologyAdapter: HistoryRadiologyAdapter? = null
    private var appPreferences: AppPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.history_radiology_fragment,
                container,
                false
            )

        viewModel = HistoryRadiologyViewModelFactory(
            requireActivity().application
        ).create(HistoryRadiologyViewModel::class.java)
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

        historyRadiologyAdapter = HistoryRadiologyAdapter(
            requireActivity(),
            ArrayList()
        )
        binding?.historyRadilogyRecyclerView!!.adapter = historyRadiologyAdapter


        viewModel!!.getHistoryRadiologyCallback(
            patientID,
            facilityid,
            prevlabrecordsRetrofitCallback
        )




        return binding!!.root
    }

    val prevlabrecordsRetrofitCallback = object : RetrofitCallback<PrevLabResponseModel> {
        override fun onSuccessfulResponse(response: Response<PrevLabResponseModel>) {

            if (!response.body()?.responseContents.isNullOrEmpty()) {
                Log.e("Radsize", "_______" + response.body()?.responseContents?.size!!)

                val sendData: ArrayList<RadiologyResModel> = ArrayList()

                sendData.clear()

                val res = response.body()?.responseContents

                for (i in res!!.indices) {
                    val date = res[i].created_date

                    val pod_result = res[i].pod_arr_result

                    for (j in pod_result.indices) {
                        val AddRes: RadiologyResModel = RadiologyResModel()

                        try {
                            AddRes.code = pod_result[j].code
                            AddRes.name = pod_result[j].name
                            AddRes.type = pod_result[j].type
                            AddRes.created_date = date
                            AddRes.order_to_location = pod_result[j].order_to_location


                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        sendData.add(AddRes)
                    }

                }


                historyRadiologyAdapter?.setData(sendData)
                binding?.historyRadilogyRecyclerView?.visibility = View.VISIBLE
                binding?.mainLayout?.visibility = View.VISIBLE
                binding?.hideLayout?.visibility = View.VISIBLE
                binding?.noRecordsTextView?.visibility = View.GONE
            } else {
                binding?.historyRadilogyRecyclerView?.visibility = View.GONE
                binding?.mainLayout?.visibility = View.GONE
                binding?.hideLayout?.visibility = View.GONE
                binding?.noRecordsTextView?.visibility = View.VISIBLE
            }
        }

        override fun onBadRequest(response: Response<PrevLabResponseModel>) {

            Log.e("HisRadData", "badReq")
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
            Log.e("HisRadData", response.message())
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            Log.e("HisRadData", "UnAuth")
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

}


