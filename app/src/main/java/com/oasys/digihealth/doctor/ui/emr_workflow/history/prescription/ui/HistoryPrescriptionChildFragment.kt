package com.oasys.digihealth.doctor.ui.emr_workflow.history.prescription.ui

import android.os.Bundle
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
import com.oasys.digihealth.doctor.databinding.HistoryPrescriptionChildFragmentBinding
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.history.prescription.model.PrescriptionHistoryResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.prescription.view_model.HistoryPrescriptionViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.prescription.view_model.HistoryPrescriptionViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model.PrevPrescriptionModel
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response


class HistoryPrescriptionChildFragment : Fragment() {
    private var binding: HistoryPrescriptionChildFragmentBinding? = null
    private var viewModel: HistoryPrescriptionViewModel? = null
    private var utils: Utils? = null
    private var patientID: Int? = 0
    private var facilityid: Int? = 0
    var appPreferences: AppPreferences? = null
    private var mAdapter: HistoryPrescriptionParentAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.history_prescription_child_fragment,
                container,
                false
            )
        viewModel = HistoryPrescriptionViewModelFactory(
            requireActivity().application
        ).create(HistoryPrescriptionViewModel::class.java)
        binding!!.viewModel = viewModel
        binding!!.lifecycleOwner = this
        utils = Utils(requireContext())
        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding!!.mainLayout, toastMessage)
            })
        mAdapter = HistoryPrescriptionParentAdapter((requireActivity()))
        binding!!.historyPrescriptionRecyclerView.adapter = mAdapter
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        patientID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        facilityid = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        viewModel?.getPrescription_History(
            patientID,
            facilityid,
            historyPrescriptionrecordsRetrofitCallback
        )

        return binding!!.root
    }

    val historyPrescriptionrecordsRetrofitCallback = object :
        RetrofitCallback<PrescriptionHistoryResponseModel> {
        override fun onSuccessfulResponse(response: Response<PrescriptionHistoryResponseModel>) {

            if (response.body()?.responseContents?.rows!!.isNotEmpty()) {
                viewModel?.errorTextVisibility?.value = 8
                mAdapter?.refreshList(response.body()?.responseContents?.rows)
                binding?.historyPrescriptionRecyclerView?.visibility = View.VISIBLE
                binding?.mainLayout?.visibility = View.VISIBLE
                binding?.hideLayout?.visibility = View.VISIBLE
                binding?.noRecordsTextView?.visibility = View.GONE

            } else {
                binding?.historyPrescriptionRecyclerView?.visibility = View.GONE
                binding?.mainLayout?.visibility = View.GONE
                binding?.hideLayout?.visibility = View.GONE
                binding?.noRecordsTextView?.visibility = View.VISIBLE
                viewModel?.errorTextVisibility?.value = 0
            }

        }

        override fun onBadRequest(response: Response<PrescriptionHistoryResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: PrevPrescriptionModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    PrevPrescriptionModel::class.java
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
            viewModel!!.progressBar.value = 8
        }
    }


}