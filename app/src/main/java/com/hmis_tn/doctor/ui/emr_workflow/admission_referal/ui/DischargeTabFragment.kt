package com.hmis_tn.doctor.ui.emr_workflow.admission_referal.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.FragmentTabDischargeBinding
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.DischargSaveeRequest
import com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.DischargeSaveResponse
import com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.response.AdmissionDischargeTypeResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.response.AdmissionDischargeTypeResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.admission_referal.view_model.AdmissionViewModel
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.view_model.AdmissionViewModelFactory
import com.hmis_tn.doctor.utils.Utils
import kotlinx.android.synthetic.main.activity_quick_registration.*
import retrofit2.Response


class DischargeTabFragment : Fragment() {

    private var customdialog: Dialog? = null
    private var typeDepartmentList = mutableMapOf<Int, String>()
    private var facility_UUID: Int? = 0

    @SuppressLint("ClickableViewAccessibility")
    var binding: FragmentTabDischargeBinding? = null
    private var department_uuid: Int? = null
    private var viewModel: AdmissionViewModel? = null
    private var listAllAddDepartmentItems: ArrayList<AdmissionDischargeTypeResponseContent?> =
        ArrayList()
    private var favAddResponseMap = mutableMapOf<Int, String>()
    private var patientUUId: Int? = 0
    var encounter_id: Int? = 0


    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_tab_discharge,
                container,
                false
            )

        viewModel = AdmissionViewModelFactory(
            requireActivity().application
        ).create(AdmissionViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        patientUUId = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        encounter_id = appPreferences?.getInt(AppConstants.ENCOUNTER_UUID)

        val encounter_Type = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)



        utils = Utils(requireContext())

        val tf = ResourcesCompat.getFont(requireContext(), R.font.poppins)
        viewModel?.getADmissionDischargeType(getADmissionDischargeCallBack, facility_UUID)
        binding?.clearCardView?.setOnClickListener {
            binding?.dischargeSpinner?.setSelection(0)

        }

        binding?.saveCardView?.setOnClickListener {
            val refferalNextRequestModel: DischargSaveeRequest = DischargSaveeRequest()
            refferalNextRequestModel.patient_uuid = patientUUId!!.toString()
            refferalNextRequestModel.admission_request_uuid = "52"
            refferalNextRequestModel.admission_status_uuid = facility_UUID!!
            refferalNextRequestModel.admission_uuid = "92"
            refferalNextRequestModel.death_type_uuid = 0
            refferalNextRequestModel.discharge_type_uuid = encounter_Type.toString()
            refferalNextRequestModel.encounter_type_uuid = encounter_Type!!
            refferalNextRequestModel.encounter_uuid = encounter_id.toString()

            viewModel!!.getDischargeSaveNext(
                refferalNextRequestModel,
                NextOrderRegistrationRetrofitCallback
            )
        }






        return binding!!.root
    }
    //defining Interface

    val getADmissionDischargeCallBack =
        object : RetrofitCallback<AdmissionDischargeTypeResponseModel> {
            @SuppressLint("NewApi")
            override fun onSuccessfulResponse(responseBody: Response<AdmissionDischargeTypeResponseModel>?) {

                val admissionDischargeTypeResponseContent =
                    AdmissionDischargeTypeResponseContent(uuid = 0, name = "Select Status")
                listAllAddDepartmentItems.add(admissionDischargeTypeResponseContent)

                listAllAddDepartmentItems.addAll(responseBody?.body()?.responseContents!!)
                favAddResponseMap =
                    listAllAddDepartmentItems.map { it?.uuid!! to it.name }.toMap().toMutableMap()
                val adapter =
                    ArrayAdapter<String>(
                        requireContext(),
                        R.layout.spinner_item,
                        favAddResponseMap.values.toMutableList()
                    )

                adapter.setDropDownViewResource(R.layout.spinner_item)
                binding?.dischargeSpinner!!.adapter = adapter
            }

            override fun onBadRequest(errorBody: Response<AdmissionDischargeTypeResponseModel>?) {

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

            override fun onFailure(failure: String) {
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }

        }

    var NextOrderRegistrationRetrofitCallback = object : RetrofitCallback<DischargeSaveResponse> {
        override fun onSuccessfulResponse(responseBody: Response<DischargeSaveResponse>?) {

            utils?.showToast(
                R.color.positiveToast,
                mainLayout!!,
                responseBody?.body()?.msg.toString()

            )


        }

        override fun onBadRequest(response: Response<DischargeSaveResponse>) {


        }

        override fun onServerError(response: Response<*>) {
            utils?.showToast(
                R.color.negativeToast,
                mainLayout!!,
                "serverError"
            )

        }

        override fun onUnAuthorized() {
            utils?.showToast(
                R.color.negativeToast,
                mainLayout,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            utils?.showToast(
                R.color.negativeToast,
                mainLayout!!,
                "Forbidden"
            )

        }

        override fun onFailure(failure: String) {
            utils?.showToast(
                R.color.negativeToast,
                mainLayout!!,
                failure
            )
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }

    }


}