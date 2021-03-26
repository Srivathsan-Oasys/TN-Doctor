package com.hmis_tn.doctor.ui.emr_workflow.admission_referal.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
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
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.FragmentTabRefferalBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.ReasonResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.ReasonResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.RefferaNextResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.RefferalNextRequestModel
import com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.response.DepartmentResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.response.DepartmentresponseContent
import com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.response.DeptsRespModel
import com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.response.DeptsresponseContent
import com.hmis_tn.doctor.ui.emr_workflow.admission_referal.view_model.AdmissionViewModel
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.view_model.AdmissionViewModelFactory
import com.hmis_tn.doctor.ui.emr_workflow.history.surgery.model.response.Facility
import com.hmis_tn.doctor.ui.emr_workflow.history.surgery.model.response.InstitutionresponseContent
import com.hmis_tn.doctor.ui.emr_workflow.history.surgery.model.response.SurgeryInstitutionResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.request.LabName
import com.hmis_tn.doctor.ui.quick_reg.model.request.LabNameSearchResponseModel
import com.hmis_tn.doctor.utils.Utils
import kotlinx.android.synthetic.main.activity_quick_registration.*
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ReferralTabFragment : Fragment() {

    private var customdialog: Dialog? = null
    private var typeDepartmentList = mutableMapOf<Int, String>()
    private var facility_UUID: Int? = 0
    private var selectedInstitutionUUID: Int? = 0
    private var encounter_Type: Int? = 0

    private var doctor_en_uuid: Int? = 0
    private var encounter_UUID: Int? = 0

    @SuppressLint("ClickableViewAccessibility")
    var binding: FragmentTabRefferalBinding? = null
    private var department_uuid: Int? = null
    var encounter_id: Int? = 0
    private var patientUUId: Int? = 0
    private var dateInString: String? = ""


    private var viewModel: AdmissionViewModel? = null
    private var listAllAddDepartmentItems: ArrayList<DepartmentresponseContent?> = ArrayList()
    private var listAllinstituteItems: ArrayList<InstitutionresponseContent?> = ArrayList()
    private var listAllReasonItems: ArrayList<ReasonResponseContent?> = ArrayList()

    private var institutionList = mutableMapOf<Int, String>()
    private var deptList = mutableMapOf<Int, String>()

    private var favAddResponseMap = mutableMapOf<Int, String>()
    private var AddinstituteResponseMap = mutableMapOf<Int, String>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null

    private var mYear: Int? = null
    private var mMonth: Int? = null
    private var mDay: Int? = null
    private var mHour: Int? = null
    private var mMinute: Int? = null
    private var mSecond: Int? = null

    private var departmentSelected = false
    private var instituteSelected = false

    var radioButton: RadioButton? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_tab_refferal,
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
        encounter_Type = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)



        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)

        var userDataStoreBean = userDetailsRoomRepository?.getUserDetails()


        utils = Utils(requireContext())

        viewModel!!.getSurgeryInstitutionCallback(
            userDataStoreBean?.uuid!!,
            facility_UUID,
            surgeryInstitutionRetrofitCallback
        )

        viewModel?.getReason(facility_UUID!!, ReasonRetrofitCallback)
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")


        dateInString = sdf.format(Date())

        binding?.calendarEditText!!.setOnClickListener {

            val c: Calendar = Calendar.getInstance()
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this.requireActivity(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    val c = Calendar.getInstance()
                    mHour = c[Calendar.HOUR_OF_DAY]
                    mMinute = c[Calendar.MINUTE]
                    mSecond = c[Calendar.SECOND]

                    val timePickerDialog = TimePickerDialog(
                        this.activity,
                        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->

                            binding?.calendarEditText?.setText(
                                String.format(
                                    "%02d",
                                    dayOfMonth
                                ) + "-" + String.format(
                                    "%02d",
                                    monthOfYear + 1
                                ) + "-" + year + " " + String.format(
                                    "%02d", hourOfDay
                                ) + ":" + String.format(
                                    "%02d", minute
                                )
                            )
                        },
                        mHour!!,
                        mMinute!!,
                        false
                    )
                    timePickerDialog.show()
                }, mYear!!, mMonth!!, mDay!!
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }


        binding?.saveCardView?.setOnClickListener {

            if (instituteSelected == true || departmentSelected == true && !binding?.departmentRemarks!!.text.trim()
                    .toString().isEmpty() || !binding?.departmentRemarks!!.text.trim().toString()
                    .isEmpty()
            ) {

                viewModel?.getEncounter(
                    facility_UUID!!,
                    patientUUId!!,
                    encounter_Type!!,
                    fetchEncounterRetrofitCallBack
                )


            } else {
                Toast.makeText(activity, "Please enter all required fields", Toast.LENGTH_LONG)
                    .show()
            }


        }

        binding?.clearCardView?.setOnClickListener {

            clearAll()

        }

        itemClickListener()

        return binding!!.root
    }

    private fun clearAll() {


        binding?.autoCompleteTextViewDepartment?.setText("")
        binding?.departmentRemarks?.setText("")
        binding?.calendarEditText?.setText("")

        binding?.reasonSpinner?.setSelection(0)


    }

    fun saveRefferal() {
        val refferalNextRequestModel: RefferalNextRequestModel = RefferalNextRequestModel()
        val departmentRemarks = binding?.departmentRemarks?.text.toString()
        val institutionREmarks = binding?.departmentRemarks?.text.toString()

        refferalNextRequestModel.patient_uuid = patientUUId!!.toString()
        refferalNextRequestModel.encounter_uuid = encounter_UUID!!
        refferalNextRequestModel.encounter_type_uuid = encounter_Type!!
        refferalNextRequestModel.facility_uuid = facility_UUID.toString()
        refferalNextRequestModel.department_uuid = department_uuid.toString()
        refferalNextRequestModel.referred_date = dateInString!!
        refferalNextRequestModel.referral_type_uuid = 1
        refferalNextRequestModel.referral_facility_uuid = facility_UUID.toString()
        refferalNextRequestModel.referral_deptartment_uuid = department_uuid.toString()
        if (departmentRemarks != null && departmentRemarks != "") {
            refferalNextRequestModel.referral_comments = departmentRemarks
        } else {
            refferalNextRequestModel.referral_comments = institutionREmarks

        }
        refferalNextRequestModel.referal_reason_uuid = "1"

        viewModel!!.getSaveNext(refferalNextRequestModel, NextOrderRegistrationRetrofitCallback)
    }

    private fun itemClickListener() {

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


        binding?.autoCompleteTextViewDepartment!!.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 2 && s.length < 5) {

                    viewModel?.getAutoCompleteDepartment(
                        s.toString(),
                        facility_UUID,
                        getDeptSearchRetrofitCallBack
                    )

                }
            }
        })



        binding?.radioGroup?.setOnCheckedChangeListener { group, checkedId ->

            val intSelectButton: Int = radioGroup!!.checkedRadioButtonId
            radioButton = group.findViewById(intSelectButton)
            Toast.makeText(requireActivity(), radioButton!!.text, Toast.LENGTH_SHORT).show()

            binding?.headingTextView!!.text = radioButton!!.text.toString()

            if (radioButton!!.text.toString().equals("Refer to Institution")) {

                binding?.insText!!.visibility = View.VISIBLE
                binding?.autoCompleteTextViewInstitution!!.visibility = View.VISIBLE

                binding?.department!!.visibility = View.VISIBLE
                binding?.autoCompleteTextViewDepartment!!.visibility = View.VISIBLE

                binding?.deptText!!.visibility = View.GONE
                binding?.autoCompleteTextViewDepartment!!.visibility = View.GONE

                binding?.deptTxt!!.visibility = View.VISIBLE
                binding?.spinnerDepartment!!.visibility = View.VISIBLE

            } else {
                binding?.insText!!.visibility = View.GONE
                binding?.autoCompleteTextViewInstitution!!.visibility = View.GONE

                binding?.department!!.visibility = View.GONE
                binding?.autoCompleteTextViewDepartment!!.visibility = View.GONE

                binding?.deptText!!.visibility = View.VISIBLE
                binding?.autoCompleteTextViewDepartment!!.visibility = View.VISIBLE

                binding?.deptTxt!!.visibility = View.GONE
                binding?.spinnerDepartment!!.visibility = View.GONE
            }

        }

        binding?.spinnerDepartment?.onItemSelectedListener =
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
                        departmentSelected = true
                        updateTheSelection()
                    } else {
                        instituteSelected = false
                        departmentSelected = false
                        updateTheSelection()
                    }
                }
            }



        binding?.institutionSpinner?.onItemSelectedListener =
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
                        updateTheSelection()

                        listAllinstituteItems[position]?.also {
                            val valueAPI = it.facility?.name
                        }
                    } else {
                        instituteSelected = false
                        departmentSelected = false
                        updateTheSelection()
                    }
                }
            }



        binding?.reasonSpinner?.onItemSelectedListener =
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
                        departmentSelected = true
                        updateTheSelection()
                    } else {
                        instituteSelected = false
                        departmentSelected = false
                        updateTheSelection()
                    }
                }
            }



        binding?.reason?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                    updateTheSelection()
                } else {
                    instituteSelected = false
                    departmentSelected = false
                    updateTheSelection()
                }
            }
        }
        binding?.department?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                    updateTheSelection()
                }
            }
        }
    }

    private fun updateTheSelection() {
        when {
            departmentSelected -> {
                binding?.spinnerDepartment!!.isEnabled = true
                binding?.reasonSpinner!!.isEnabled = true
                binding?.departmentRemarks?.isEnabled = true

                binding?.institutionSpinner?.isEnabled = false
                binding?.department?.isEnabled = false
                binding?.reason?.isEnabled = false
                binding?.institutionREmarks?.isEnabled = false

            }
            instituteSelected -> {

                binding?.spinnerDepartment!!.isEnabled = false
                binding?.reasonSpinner!!.isEnabled = false
                binding?.departmentRemarks?.isEnabled = false

                binding?.institutionSpinner?.isEnabled = true
                binding?.department?.isEnabled = true
                binding?.reason?.isEnabled = true
                binding?.institutionREmarks?.isEnabled = true
            }
            else -> {
                binding?.spinnerDepartment!!.isEnabled = true
                binding?.reasonSpinner!!.isEnabled = true
                binding?.departmentRemarks?.isEnabled = true

                binding?.institutionSpinner?.isEnabled = true
                binding?.department?.isEnabled = true
                binding?.reason?.isEnabled = true
                binding?.institutionREmarks?.isEnabled = true
            }
        }
    }

    val AddAllDepartmentCallBack =
        object : RetrofitCallback<DepartmentResponseModel> {
            @SuppressLint("NewApi")
            override fun onSuccessfulResponse(responseBody: Response<DepartmentResponseModel>?) {
                val favAddAllDepatResponseContent =
                    DepartmentresponseContent(uuid = 0, code = "", name = "Select Department")
                responseBody?.body()?.responseContents?.forEach {
                    listAllAddDepartmentItems.add(0, favAddAllDepatResponseContent)
                    listAllAddDepartmentItems.add(it)
                }

                favAddResponseMap =
                    listAllAddDepartmentItems.map { it?.uuid!! to it.name!! }.toMap().toMutableMap()
                val adapter =
                    ArrayAdapter<String>(
                        requireContext(),
                        R.layout.spinner_item,
                        favAddResponseMap.values.toMutableList()
                    )

                adapter.setDropDownViewResource(R.layout.spinner_item)
                binding?.spinnerDepartment!!.adapter = adapter
                binding?.department!!.adapter = adapter


            }

            override fun onBadRequest(errorBody: Response<DepartmentResponseModel>?) {

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

    val surgeryInstitutionRetrofitCallback =
        object : RetrofitCallback<SurgeryInstitutionResponseModel> {
            override fun onSuccessfulResponse(response: Response<SurgeryInstitutionResponseModel>) {
                val facility = Facility(code = "", name = "Select Institute")
                val institutionResponseContent =
                    InstitutionresponseContent(uuid = 0, facility = facility)
                response.body()?.responseContents?.forEach {
                    listAllinstituteItems.add(0, institutionResponseContent)
                    listAllinstituteItems.add(it)
                }


                AddinstituteResponseMap =
                    listAllinstituteItems.map { it?.uuid!! to it.facility?.name!! }.toMap()
                        .toMutableMap()

                if (isAdded) {
                    val adapter =
                        ArrayAdapter<String>(
                            requireContext(),
                            R.layout.spinner_item,
                            AddinstituteResponseMap.values.toMutableList()
                        )

                    adapter.setDropDownViewResource(R.layout.spinner_item)
                    //binding?.institutionSpinner!!.adapter = adapter
                }

            }

            override fun onBadRequest(response: Response<SurgeryInstitutionResponseModel>) {


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

    val ReasonRetrofitCallback = object : RetrofitCallback<ReasonResponseModel> {
        override fun onSuccessfulResponse(response: Response<ReasonResponseModel>) {
            val reasonResponseContent =
                ReasonResponseContent(uuid = 0, code = "", name = "Select reason")
            listAllReasonItems.add(0, reasonResponseContent)
            response.body()?.responseContent?.forEach {
                listAllReasonItems.add(it)
            }

            AddinstituteResponseMap =
                listAllReasonItems.map { it?.uuid!! to it.name }.toMap().toMutableMap()
            val adapter =
                ArrayAdapter<String>(
                    requireContext(),
                    R.layout.spinner_item,
                    AddinstituteResponseMap.values.toMutableList()
                )

            adapter.setDropDownViewResource(R.layout.spinner_item)
            binding?.reason!!.adapter = adapter
            binding?.reasonSpinner!!.adapter = adapter
        }

        override fun onBadRequest(response: Response<ReasonResponseModel>) {

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

    var NextOrderRegistrationRetrofitCallback = object :
        RetrofitCallback<RefferaNextResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<RefferaNextResponseModel>?) {

            Toast.makeText(
                requireActivity(),
                responseBody?.body()!!.message.toString(),
                Toast.LENGTH_LONG
            ).show()

            binding!!.autoCompleteTextViewDepartment!!.setText("")
            binding!!.autoCompleteTextViewInstitution!!.setText("")
            binding!!.remarksEditText!!.setText("")
            binding!!.reasonSpinner!!.setSelection(0)
        }

        override fun onBadRequest(response: Response<RefferaNextResponseModel>) {

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


    val getInstitutionSearchRetrofitCallBack =
        object : RetrofitCallback<LabNameSearchResponseModel> {

            override fun onSuccessfulResponse(response: Response<LabNameSearchResponseModel>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {

                    Log.e("SearchInstitutionData", response.body()?.responseContents.toString())
                    setInstitutionToAutoComplete(response.body()?.responseContents)

                }
            }

            override fun onBadRequest(response: Response<LabNameSearchResponseModel>) {
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

            override fun onServerError(response: Response<*>) {
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

            override fun onFailure(failure: String) {
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
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

    val getDeptSearchRetrofitCallBack =
        object : RetrofitCallback<DeptsRespModel> {

            override fun onSuccessfulResponse(response: Response<DeptsRespModel>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {

                    Log.e("SearchDeptData", response.body()?.responseContents.toString())
                    setDeptToAutoComplete(response.body()?.responseContents)

                }
            }

            override fun onBadRequest(response: Response<DeptsRespModel>) {
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

            override fun onServerError(response: Response<*>) {
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

            override fun onFailure(failure: String) {
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
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

    private val fetchEncounterRetrofitCallBack =
        object : RetrofitCallback<FectchEncounterResponseModel> {
            override fun onSuccessfulResponse(response: Response<FectchEncounterResponseModel>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    val encounterResponseContent = response.body()?.responseContents!!
                    doctor_en_uuid =
                        encounterResponseContent.get(0)?.encounter_doctors?.get(0)?.uuid!!
                    encounter_UUID = encounterResponseContent.get(0)?.uuid!!
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_DOCTOR_UUID, doctor_en_uuid!!)
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_UUID, encounter_UUID!!)
                    saveRefferal()


                } else {
                    viewModel?.createEncounter(
                        patientUUId!!,
                        encounter_Type!!,
                        createEncounterRetrofitCallback
                    )
                }
            }

            override fun onBadRequest(response: Response<FectchEncounterResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: FectchEncounterResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        FectchEncounterResponseModel::class.java
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

    val createEncounterRetrofitCallback = object : RetrofitCallback<CreateEncounterResponseModel> {
        override fun onSuccessfulResponse(response: Response<CreateEncounterResponseModel>) {


            doctor_en_uuid = response.body()?.responseContents?.encounterDoctor?.uuid!!.toInt()
            encounter_UUID = response.body()?.responseContents?.encounter?.uuid!!.toInt()
            patientUUId =
                response.body()?.responseContents?.encounterDoctor?.patient_uuid!!.toInt()

            saveRefferal()
        }

        override fun onBadRequest(response: Response<CreateEncounterResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: CreateEncounterResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    CreateEncounterResponseModel::class.java
                )
                /*   utils?.showToast(
                       R.color.negativeToast,
                       binding?.mainLayout!!,
                       responseModel.message!!
                   )*/
            } catch (e: Exception) {
                /*   utils?.showToast(
                       R.color.negativeToast,
                       binding?.mainLayout!!,
                       getString(R.string.something_went_wrong)
                   )*/
                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>) {
            /* utils?.showToast(
                 R.color.negativeToast,
                 binding?.mainLayout!!,
                 getString(R.string.something_went_wrong)
             )*/
        }

        override fun onUnAuthorized() {
            /*    utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.unauthorized)
                )*/
        }

        override fun onForbidden() {
            /*   utils?.showToast(
                   R.color.negativeToast,
                   binding?.mainLayout!!,
                   getString(R.string.something_went_wrong)
               )*/
        }

        override fun onFailure(failure: String) {
//            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }


}