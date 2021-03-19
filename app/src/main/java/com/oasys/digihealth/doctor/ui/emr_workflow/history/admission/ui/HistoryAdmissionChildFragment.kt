package com.oasys.digihealth.doctor.ui.emr_workflow.history.admission.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.HistoryAdmissionFragmentBinding
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.history.admission.model.AdmissionReferalResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.admission.model.AdmissionReferalresponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.history.admission.viewmodel.HistoryAdmissionViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.admission.viewmodel.HistoryAdmissionViewModelFactory
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response


class HistoryAdmissionChildFragment : Fragment() {
    private var historyAdmissionAdapter: HistoryAdmissionAdapter? = null
    private var binding: HistoryAdmissionFragmentBinding? = null
    private var viewModel: HistoryAdmissionViewModel? = null
    private var utils: Utils? = null


    var appPreferences: AppPreferences? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.history_admission_fragment,
                container,
                false
            )

        viewModel = HistoryAdmissionViewModelFactory(
            requireActivity().application
        ).create(HistoryAdmissionViewModel::class.java)
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


        viewModel!!.getHistoryAdmissionCallback(patientID, facilityid, admissionRetrofitCallback)

        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding?.historyLabRecyclerView!!.layoutManager = linearLayoutManager
        historyAdmissionAdapter = HistoryAdmissionAdapter(
            requireActivity(),
            ArrayList()
        )
        binding?.historyLabRecyclerView!!.adapter = historyAdmissionAdapter



        return binding!!.root
    }

    val admissionRetrofitCallback = object : RetrofitCallback<AdmissionReferalResponseModel> {
        override fun onSuccessfulResponse(response: Response<AdmissionReferalResponseModel>) {

            Log.e("HisAdmissionData", response.body()?.responseContent.toString())

            if (response.body()?.responseContent?.size == 0) {
                binding?.historyLabRecyclerView?.visibility = View.GONE
                binding?.mainLayout?.visibility = View.GONE
                binding?.hideLayout?.visibility = View.GONE
                binding?.noRecordsTextView?.visibility = View.VISIBLE
            } else {
                historyAdmissionAdapter?.setData(response.body()!!.responseContent as List<AdmissionReferalresponseContent>?)
                binding?.historyLabRecyclerView?.visibility = View.VISIBLE
                binding?.mainLayout?.visibility = View.VISIBLE
                binding?.hideLayout?.visibility = View.VISIBLE
                binding?.noRecordsTextView?.visibility = View.GONE
            }
        }

        override fun onBadRequest(response: Response<AdmissionReferalResponseModel>) {

            //Log.e("HisLabData", "badReq")
            val gson = GsonBuilder().create()
            val responseModel: AdmissionReferalResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    AdmissionReferalResponseModel::class.java
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
            //Log.e("HisLabData", response.message())
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            //Log.e("HisRadData", "UnAuth")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            //Log.e("HisRadData", "Forbidden")
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


