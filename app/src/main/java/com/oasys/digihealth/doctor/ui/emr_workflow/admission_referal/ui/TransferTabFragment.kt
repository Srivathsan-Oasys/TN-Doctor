package com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentTabTrasfereBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.AdmissionWardResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.AmissionWardResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.TransmissionReasonResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.TransmissionReasonResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.request.TrasfferedRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.response.*
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.view_model.AdmissionViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.view_model.AdmissionViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.history.surgery.model.response.InstitutionresponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.history.surgery.model.response.SurgeryInstitutionResponseModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.request.LabName
import com.oasys.digihealth.doctor.ui.quick_reg.model.request.LabNameSearchResponseModel
import com.oasys.digihealth.doctor.utils.Utils
import kotlinx.android.synthetic.main.activity_quick_registration.*
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TransferTabFragment : Fragment() {

    private var customdialog: Dialog? = null
    private var typeDepartmentList = mutableMapOf<Int, String>()
    private var facility_UUID: Int? = 0

    @SuppressLint("ClickableViewAccessibility")
    var binding: FragmentTabTrasfereBinding? = null
    private var viewModel: AdmissionViewModel? = null
    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
    private var responseContents: String? = ""
    private var department_uuid: Int? = null
    private var selectedDepartment: Int? = null
    private var selectedInstitutionUUID: Int? = null
    var encounter_id: Int? = 0
    private var patientUUId: Int? = 0
    var radioButton: RadioButton? = null

    private var deptList = mutableMapOf<Int, String>()
    private var institutionList = mutableMapOf<Int, String>()


    private var listAllAddDepartmentItems: ArrayList<DepartmentresponseContent> = ArrayList()
    private var listAllinstituteItems: List<InstitutionresponseContent?> = ArrayList()
    private var listAllReasonItems: ArrayList<TransmissionReasonResponseContent?> = ArrayList()
    private var wardItems: ArrayList<AmissionWardResponseContent?> = ArrayList()
    private var wardResponseMap = mutableMapOf<Int, String>()
    private var departmentSelected = false
    private var instituteSelected = false
    private var wardSelected = false


    private var favAddResponseMap = mutableMapOf<Int, String>()
    private var AddinstituteResponseMap = mutableMapOf<Int, String>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_tab_trasfere,
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



        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)

        var userDataStoreBean = userDetailsRoomRepository?.getUserDetails()


        utils = Utils(requireContext())

        /*viewModel!!.getSurgeryInstitutionCallback(
            userDataStoreBean?.uuid!!,
            facility_UUID,
            surgeryInstitutionRetrofitCallback
        )*/
        //viewModel?.getAllDepartment(facility_UUID, AddAllDepartmentCallBack)
        viewModel?.getTransmissionReason(facility_UUID!!, ReasonRetrofitCallback)
        binding?.viewModel?.getWArdList(
            facility_UUID, department_uuid!!, WardCallBack

        )

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        val dateInString = sdf.format(Date())
        responseContents = binding?.departmentRemarks.text.toString()

        binding?.radioGroup?.setOnCheckedChangeListener { group, checkedId ->

            val intSelectButton: Int = radioGroup!!.checkedRadioButtonId
            radioButton = group.findViewById(intSelectButton)
            Toast.makeText(requireActivity(), radioButton!!.text, Toast.LENGTH_SHORT).show()

            binding?.headingTextView!!.text = radioButton!!.text.toString()

            if (radioButton!!.text.toString().equals("Refer to Institution")) {

                binding!!.wardLayout!!.visibility = View.GONE
                binding!!.institutionLayout!!.visibility = View.VISIBLE

            } else {

                binding!!.wardLayout!!.visibility = View.VISIBLE
                binding!!.institutionLayout!!.visibility = View.GONE

            }

        }

        binding?.autoCompleteTextViewDepartment!!.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length in 3..4) {

                    viewModel?.getAutoCompleteDepartment(
                        s.toString(),
                        facility_UUID,
                        getDeptSearchRetrofitCallBack
                    )

                }
            }
        })


        binding?.autoCompleteTextViewDepartment!!.onItemClickListener =
            object : AdapterView.OnItemClickListener {
                override fun onItemClick(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(position).toString()

                    selectedDepartment = deptList.filterValues { it == itemValue }.keys.toList()[0]

                    binding?.viewModel?.getWArdList(
                        facility_UUID,
                        selectedDepartment!!,
                        WardCallBack
                    )
                }

            }


        binding?.autoCompleteTextViewInstitution!!.onItemClickListener =
            object : AdapterView.OnItemClickListener {
                override fun onItemClick(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(position).toString()

                    selectedInstitutionUUID =
                        institutionList.filterValues { it == itemValue }.keys.toList()[0]

                    //Log.e("selectInstitutionID", selectedInstitutionUUID.toString())

                    viewModel?.getAllReferalDepartments(
                        selectedInstitutionUUID!!,
                        AddAllDepartmentCallBack
                    )

                }

            }

        binding?.autoCompleteTextViewInstitution!!.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 2 && s.length < 5) {

                    viewModel?.getInstitutionName(
                        s.toString(),
                        getInstitutionSearchRetrofitCallBack
                    )

                }
            }
        })


        binding?.saveCardView?.setOnClickListener {

            /* if (instituteSelected == true || departmentSelected == true || wardSelected && !binding?.departmentRemarks!!.text.trim()
                     .toString().isEmpty() || !binding?.departmentRemarks!!.text.trim().toString()
                     .isEmpty() || !binding?.wardRemarks!!.text.trim().toString().isEmpty()
             ) {*/
            val refferalNextRequestModel: TrasfferedRequestModel = TrasfferedRequestModel()
            val departmentRemarks = binding?.departmentRemarks.text.toString()
            val institutionRemarks = binding?.institutionRemarks?.text.toString()
            val wardRemarks = binding?.wardRemarks?.text.toString()

            refferalNextRequestModel.patient_uuid = patientUUId!!.toString()
            refferalNextRequestModel.encounter_uuid = encounter_id!!.toString()
            refferalNextRequestModel.encounter_type_uuid = encounter_Type.toString()
            refferalNextRequestModel.transfer_date = dateInString
            refferalNextRequestModel.facility_uuid = facility_UUID.toString()
            refferalNextRequestModel.department_uuid = department_uuid.toString()
            if (departmentRemarks != null && institutionRemarks != "") {
                refferalNextRequestModel.comments = departmentRemarks
            } else if (institutionRemarks.isNotEmpty() && institutionRemarks != null) {
                refferalNextRequestModel.comments = institutionRemarks

            } else {
                refferalNextRequestModel.comments = wardRemarks.toString()

            }


            if (departmentRemarks != null && institutionRemarks != "") {
                refferalNextRequestModel.transfer_comments = departmentRemarks
            } else if (institutionRemarks.isNotEmpty() && institutionRemarks != null) {
                refferalNextRequestModel.transfer_comments = institutionRemarks

            } else {
                refferalNextRequestModel.transfer_comments = wardRemarks.toString()

            }
            refferalNextRequestModel.transfer_type_uuid = 1
            refferalNextRequestModel.transfer_department_uuid = department_uuid.toString()
            refferalNextRequestModel.transfer_facility_uuid = facility_UUID.toString()
            refferalNextRequestModel.transfer_reason_uuid = "1"
            refferalNextRequestModel.transfer_ward_uuid = ""

            viewModel!!.getTransferSaveNext(
                refferalNextRequestModel,
                NextOrderRegistrationRetrofitCallback
            )
            /*} else {
                Toast.makeText(activity, "Please enter all required fields", Toast.LENGTH_LONG)
                    .show()
            }*/


        }
        itemClickListener()


        return binding!!.root
    }


    private fun itemClickListener() {
        binding?.department.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    if (position > 0) {
                        instituteSelected = false
                        wardSelected = false
                        departmentSelected = true
                        updateTheSelection()
                    } else {
                        instituteSelected = false
                        departmentSelected = false
                        wardSelected = false

                        updateTheSelection()
                    }
                }
            }
        binding?.departmentReason.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0) {
                        instituteSelected = false
                        wardSelected = false

                        departmentSelected = true
                        updateTheSelection()
                    } else {
                        instituteSelected = false
                        departmentSelected = false
                        wardSelected = false

                        updateTheSelection()
                    }
                }
            }




        binding?.institution.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    if (position > 0) {
                        instituteSelected = true
                        departmentSelected = false
                        wardSelected = false

                        updateTheSelection()

                        listAllinstituteItems[position]?.also {
                            val valueAPI = it.facility?.name
                        }
                    } else {
                        instituteSelected = false
                        departmentSelected = false
                        wardSelected = false

                        updateTheSelection()
                    }
                }
            }

        binding?.institutionReason?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0) {
                        departmentSelected = true
                        instituteSelected = false
                        wardSelected = false

                        updateTheSelection()
                    } else {
                        instituteSelected = false
                        departmentSelected = false
                        wardSelected = false

                        updateTheSelection()
                    }
                }
            }
        binding?.institutionDepartment?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0) {
                        departmentSelected = false
                        instituteSelected = true
                        updateTheSelection()
                    } else {
                        instituteSelected = false
                        departmentSelected = false
                        wardSelected = false

                        updateTheSelection()
                    }
                }
            }
        binding?.wardSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    departmentSelected = false
                    instituteSelected = false
                    wardSelected = true

                    updateTheSelection()
                } else {
                    instituteSelected = false
                    departmentSelected = false
                    wardSelected = false

                    updateTheSelection()
                }
            }
        }
        binding?.wardReason?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    departmentSelected = false
                    instituteSelected = false
                    wardSelected = true

                    updateTheSelection()
                } else {
                    instituteSelected = false
                    departmentSelected = false
                    wardSelected = false

                    updateTheSelection()
                }
            }
        }


    }

    private fun updateTheSelection() {
        when {
            departmentSelected -> {
                /*binding?.department!!.isEnabled = true
                binding?.departmentReason!!.isEnabled = true
                binding?.departmentRemarks?.isEnabled = true*/

                binding?.institution.isEnabled = false
                binding?.institutionDepartment?.isEnabled = false
                binding?.institutionReason?.isEnabled = false
                binding?.institutionRemarks?.isEnabled = false

                binding?.wardSpinner?.isEnabled = false
                binding?.wardReason?.isEnabled = false
                binding?.wardRemarks?.isEnabled = false


            }
            instituteSelected -> {

                binding?.department!!.isEnabled = false
                binding?.departmentReason!!.isEnabled = false
                binding?.departmentRemarks.isEnabled = false

                binding?.wardSpinner?.isEnabled = false
                binding?.wardReason?.isEnabled = false
                binding?.wardRemarks?.isEnabled = false


                binding?.institution.isEnabled = true
                //binding?.department?.isEnabled = true
                binding?.institutionReason?.isEnabled = true
                binding?.institutionRemarks?.isEnabled = true
            }
            wardSelected -> {

                binding?.wardSpinner?.isEnabled = true
                binding?.wardReason?.isEnabled = true
                binding?.wardRemarks?.isEnabled = true


                /*binding?.department!!.isEnabled = false
                binding?.departmentReason!!.isEnabled = false
                binding?.departmentRemarks?.isEnabled = false*/

                binding?.institution.isEnabled = false
                binding?.department.isEnabled = false
                binding?.institutionReason?.isEnabled = false
                binding?.institutionRemarks?.isEnabled = false
            }

            else -> {
                /*binding?.departmentReason!!.isEnabled = true
                binding?.departmentReason!!.isEnabled = true
                binding?.departmentRemarks?.isEnabled = true*/

                binding?.institution.isEnabled = true
                //binding?.department?.isEnabled = true
                binding?.institutionReason?.isEnabled = true
                binding?.institutionRemarks?.isEnabled = true

                binding?.wardSpinner?.isEnabled = true
                binding?.wardReason?.isEnabled = true
                binding?.wardRemarks?.isEnabled = true

            }
        }
    }


    val surgeryInstitutionRetrofitCallback =
        object : RetrofitCallback<SurgeryInstitutionResponseModel> {
            override fun onSuccessfulResponse(response: Response<SurgeryInstitutionResponseModel?>) {

                listAllinstituteItems = response.body()?.responseContents!!
                AddinstituteResponseMap =
                    listAllinstituteItems.map { it?.uuid!! to it.facility?.name!! }.toMap()
                        .toMutableMap()
                val adapter =
                    ArrayAdapter<String>(
                        requireContext(),
                        R.layout.spinner_item,
                        AddinstituteResponseMap.values.toMutableList()
                    )

                adapter.setDropDownViewResource(R.layout.spinner_item)
                //binding?.autoCompleteTextViewInstitution!!.adapter = adapter
            }

            override fun onBadRequest(response: Response<SurgeryInstitutionResponseModel?>) {

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
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, s ?: "")
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }
    val AddAllDepartmentCallBack =
        object : RetrofitCallback<DepartmentResponseModel> {
            @SuppressLint("NewApi")
            override fun onSuccessfulResponse(responseBody: Response<DepartmentResponseModel?>) {

                val departmentresponseContent =
                    DepartmentresponseContent(uuid = 0, name = "Select Department")
                listAllAddDepartmentItems.add(departmentresponseContent)

                listAllAddDepartmentItems.addAll(responseBody.body()!!.responseContents!!)
                favAddResponseMap =
                    listAllAddDepartmentItems.map { it.uuid!! to it.name!! }.toMap().toMutableMap()
                val adapter =
                    ArrayAdapter<String>(
                        requireContext(),
                        R.layout.spinner_item,
                        favAddResponseMap.values.toMutableList()
                    )

                adapter.setDropDownViewResource(R.layout.spinner_item)
                binding?.institutionDepartment!!.adapter = adapter
                //binding?.department!!.adapter = adapter
            }

            override fun onBadRequest(errorBody: Response<DepartmentResponseModel?>) {

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
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, s ?: "")
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    val ReasonRetrofitCallback = object : RetrofitCallback<TransmissionReasonResponseModel> {
        override fun onSuccessfulResponse(response: Response<TransmissionReasonResponseModel?>) {

            val transmissionReasonResponseContent =
                TransmissionReasonResponseContent(uuid = 0, name = "Select Reason")
            listAllReasonItems.add(transmissionReasonResponseContent)

            listAllReasonItems.addAll(response.body()?.responseContent!!)
            AddinstituteResponseMap =
                listAllReasonItems.map { it?.uuid!! to it.name }.toMap().toMutableMap()
            val adapter =
                ArrayAdapter<String>(
                    requireContext(),
                    R.layout.spinner_item,
                    AddinstituteResponseMap.values.toMutableList()
                )

            adapter.setDropDownViewResource(R.layout.spinner_item)
            // binding?.departmentReason!!.adapter = adapter
            binding?.institutionReason!!.adapter = adapter
            binding?.wardReason!!.adapter = adapter
        }

        override fun onBadRequest(response: Response<TransmissionReasonResponseModel?>) {

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

        override fun onFailure(failure: String?) {
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure ?: "")
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }
    val WardCallBack =
        object : RetrofitCallback<AdmissionWardResponseModel> {
            @SuppressLint("NewApi")
            override fun onSuccessfulResponse(responseBody: Response<AdmissionWardResponseModel?>) {

                val amissionWardResponseContent =
                    AmissionWardResponseContent(ward_uuid = 0, ward_name = "Select ward")
                wardItems.add(amissionWardResponseContent)

                wardItems.addAll(responseBody.body()?.responseContents!!)
                wardResponseMap =
                    wardItems.map { it?.ward_uuid!! to it.ward_name }.toMap().toMutableMap()
                val adapter =
                    ArrayAdapter<String>(
                        requireContext(),
                        R.layout.spinner_item,
                        wardResponseMap.values.toMutableList()
                    )

                adapter.setDropDownViewResource(R.layout.spinner_item)
                binding?.wardSpinner!!.adapter = adapter
            }

            override fun onBadRequest(errorBody: Response<AdmissionWardResponseModel?>) {

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

            override fun onFailure(failure: String?) {
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure ?: "")
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }

        }

    var NextOrderRegistrationRetrofitCallback = object :
        RetrofitCallback<TransferredResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<TransferredResponseModel?>) {

            utils?.showToast(
                R.color.positiveToast,
                mainLayout!!,
                responseBody.body()?.message.toString()
            )
        }

        override fun onBadRequest(response: Response<TransferredResponseModel?>) {

        }

        override fun onServerError(response: Response<*>?) {
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

        override fun onFailure(s: String?) {
            utils?.showToast(
                R.color.negativeToast,
                mainLayout!!,
                s ?: ""
            )
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }

    }


    val getDeptSearchRetrofitCallBack =
        object : RetrofitCallback<DeptsRespModel> {

            override fun onSuccessfulResponse(response: Response<DeptsRespModel?>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {

                    Log.e("SearchDeptData", response.body()?.responseContents.toString())
                    setDeptToAutoComplete(response.body()?.responseContents)

                }
            }

            override fun onBadRequest(response: Response<DeptsRespModel?>) {
                val gson = GsonBuilder().create()
                val responseModel: DeptsRespModel
                try {
                    //      customProgressDialog!!.dismiss()
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        DeptsRespModel::class.java
                    )
                    /*utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        responseModel.status!!
                    )*/
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
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                //          customProgressDialog!!.dismiss()
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.unauthorized)
                )
                //         customProgressDialog!!.dismiss()
            }

            override fun onForbidden() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                //      customProgressDialog!!.dismiss()
            }

            override fun onFailure(failure: String?) {
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure ?: "")
                //     customProgressDialog!!.dismiss()
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
                //     customProgressDialog!!.dismiss()
            }

        }

    fun setDeptToAutoComplete(responseContents: List<DeptsresponseContent?>?) {

        deptList = responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()
        val adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            deptList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.autoCompleteTextViewDepartment!!.threshold = 1
        binding?.autoCompleteTextViewDepartment!!.setAdapter(adapter)

    }

    val getInstitutionSearchRetrofitCallBack =
        object : RetrofitCallback<LabNameSearchResponseModel> {

            override fun onSuccessfulResponse(response: Response<LabNameSearchResponseModel?>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {

                    Log.e("SearchInstitutionData", response.body()?.responseContents.toString())
                    setInstitutionToAutoComplete(response.body()?.responseContents)

                }
            }

            override fun onBadRequest(response: Response<LabNameSearchResponseModel?>) {
                val gson = GsonBuilder().create()
                val responseModel: LabNameSearchResponseModel
                try {
                    //      customProgressDialog!!.dismiss()
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        LabNameSearchResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        responseModel.status
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
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                //          customProgressDialog!!.dismiss()
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.unauthorized)
                )
                //         customProgressDialog!!.dismiss()
            }

            override fun onForbidden() {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
                //      customProgressDialog!!.dismiss()
            }

            override fun onFailure(failure: String?) {
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure ?: "")
                //     customProgressDialog!!.dismiss()
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
                //     customProgressDialog!!.dismiss()
            }

        }

    fun setInstitutionToAutoComplete(responseContents: List<LabName?>?) {

        institutionList = responseContents?.map { it?.uuid!! to it.name }!!.toMap().toMutableMap()
        val adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            institutionList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.autoCompleteTextViewInstitution!!.threshold = 1
        binding?.autoCompleteTextViewInstitution!!.setAdapter(adapter)

    }

}