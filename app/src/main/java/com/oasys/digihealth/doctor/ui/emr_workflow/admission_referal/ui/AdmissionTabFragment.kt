package com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.ui

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
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentTabAdmissionBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.AdmissionWardResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.AmissionWardResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.ReasonResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.ReasonResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.request.AdmissionPatientUpdateRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.request.AdmissionSaveRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.request.AdmissionUpdateRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.response.*
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.view_model.AdmissionViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.view_model.AdmissionViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.model.RadiologyEncounterResponseModel
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AdmissionTabFragment : Fragment() {

    private var customdialog: Dialog? = null
    private var typeDepartmentList = mutableMapOf<Int, String>()
    private var facility_UUID: Int? = 0
    private var encounter_UUID: Int? = 0
    private var patient_UUID: Int? = 0
    private var doctor_UUID: Int? = 0
    private var doctor_en_uuid: Int? = 0
    private var encounterType: Int? = 0
    private var selectwardUUID: Int? = 0
    private var selectReasonUUID: Int? = 0
    private var selectedDepartment: Int? = 0
    private var uuid: Int? = 0
    private var PatientId: Int? = 0
    private var edit: Boolean? = false
    val admissionSaveRequestModel: AdmissionSaveRequestModel =
        AdmissionSaveRequestModel()

    val admissionPatientUpdateRequestModel: AdmissionPatientUpdateRequestModel =
        AdmissionPatientUpdateRequestModel()
    val admissionUpdateRequestModel: AdmissionUpdateRequestModel = AdmissionUpdateRequestModel()

    private val hashWardSpinnerList: HashMap<Int, Int> = HashMap()
    private val hashReasonSpinnerList: HashMap<Int, Int> = HashMap()
    private val hashDeptSpinnerList: HashMap<Int, Int> = HashMap()
    //private val hashReasonSpinnerList: HashMap<Int,Int> = HashMap()

    @SuppressLint("ClickableViewAccessibility")
    var binding: FragmentTabAdmissionBinding? = null
    private var department_uuid: Int? = null
    private var viewModel: AdmissionViewModel? = null
    private var listAllAddDepartmentItems: List<FavAddAllDepatResponseContent?> = ArrayList()
    private var wardItems: ArrayList<AmissionWardResponseContent?> = ArrayList()

    private var favAddResponseMap = mutableMapOf<Int, String>()

    private var wardResponseMap = mutableMapOf<Int, String>()

    private var listAllReasonItems: ArrayList<ReasonResponseContent?> = ArrayList()
    private var AddinstituteResponseMap = mutableMapOf<Int, String>()

    private var departmentList = mutableMapOf<Int, String>()

    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_tab_admission,
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
        encounter_UUID = appPreferences?.getInt(AppConstants.ENCOUNTER_UUID)
        patient_UUID = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        doctor_UUID = appPreferences?.getInt(AppConstants.ENCOUNTER_DOCTOR_UUID)
        encounterType = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)!!

        utils = Utils(requireContext())

        val userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        viewModel?.getAllDepartments(allDeptCallback)




        binding?.autoCompleteTextViewDepartment!!.onItemClickListener =
            object : AdapterView.OnItemClickListener {
                override fun onItemClick(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(position).toString()

                    selectedDepartment =
                        departmentList.filterValues { it == itemValue }.keys.toList()[0]

                    binding?.viewModel?.getWArdList(
                        facility_UUID,
                        selectedDepartment!!,
                        WardCallBack
                    )
                }

            }


        binding?.viewBed!!.setOnClickListener {
            val ft = childFragmentManager.beginTransaction()
            val bedFragment = BedViewDialogFragment()

            val bundle = Bundle()
            bundle.putInt("wardId", selectwardUUID!!)
            bedFragment.arguments = bundle
            bedFragment.show(ft, "tag")

        }

        binding?.calendarEditText?.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    val c1 = Calendar.getInstance()
                    val mHour = c1[Calendar.HOUR_OF_DAY]
                    val mMinute = c1[Calendar.MINUTE]
                    val mSeconds = c1[Calendar.SECOND]


                    binding?.calendarEditText?.setText(
                        String.format(
                            "%02d",
                            dayOfMonth
                        ) + "-" + String.format("%02d", monthOfYear + 1) + "-" + year
                    )
                }, mYear, mMonth, mDay
            )
            //datePickerDialog?.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }
        binding?.timerEditText?.setOnClickListener {
            val c1 = Calendar.getInstance()
            val mHour = c1[Calendar.HOUR_OF_DAY]
            val mMinute = c1[Calendar.MINUTE]
            val mSeconds = c1[Calendar.SECOND]

            val timePickerDialog = TimePickerDialog(
                this.activity,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->


                    val dateFor = hourOfDay.toString() + ":" + minute + ":" + mSeconds
                    val sdf = SimpleDateFormat("H:mm:ss")
                    val dateObj = sdf.parse(dateFor)
                    val targetFormat = SimpleDateFormat("K:mm a").format(dateObj)



                    binding?.timerEditText!!.setText(targetFormat.toString())

                    /*binding?.timerEditText?.setText(
                        String.format(
                            "%02d",
                            hourOfDay)+":"+ String.format(
                            "%02d",
                            minute)+":"+String.format(
                            "%02d",mSeconds)
                    )*/


                },
                mHour,
                mMinute,
                false
            )
            timePickerDialog.show()

        }


        binding?.autoCompleteTextViewDepartment!!.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 2 && s.length < 5) {
                    //  customProgressDialog!!.show()
                    viewModel?.getDepartmentAutoComplete(
                        s.toString(),
                        facility_UUID,
                        getInstitutionSearchRetrofitCallBack
                    )

                }
            }
        })

        binding?.wardSpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectwardUUID =
                        wardResponseMap.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {

                    val itemValue = parent!!.getItemAtPosition(pos).toString()

                    selectwardUUID =
                        wardResponseMap.filterValues { it == itemValue }.keys.toList()[0]

                }

            }


        binding?.reasonSpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectReasonUUID =
                        AddinstituteResponseMap.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {

                    val itemValue = parent!!.getItemAtPosition(pos).toString()

                    selectReasonUUID =
                        AddinstituteResponseMap.filterValues { it == itemValue }.keys.toList()[0]

                }

            }


        binding?.saveCardView!!.setOnClickListener {

            if (!binding?.calendarEditText!!.text.trim().toString()
                    .isEmpty() && !binding?.timerEditText!!.text.trim().toString().isEmpty()
                && selectwardUUID != 0 && selectReasonUUID != 0
            ) {


                if (edit!!) {

                    admissionPatientUpdateRequestModel.referral_comments =
                        binding!!.commentsEditText!!.text.toString()
                    admissionPatientUpdateRequestModel.referred_date = "2021-01-05T05:17:13.000Z"
                    admissionPatientUpdateRequestModel.referal_reason_uuid = selectReasonUUID
                    admissionPatientUpdateRequestModel.referral_deptartment_uuid = department_uuid
                    admissionPatientUpdateRequestModel.patient_referral_uuid = selectedDepartment
                    admissionPatientUpdateRequestModel.ward_uuid = selectwardUUID

                    viewModel?.getUpdatePatientAdmission(
                        admissionPatientUpdateRequestModel,
                        updatePatientCallback
                    )

                } else {
                    viewModel?.getEncounter(
                        facility_UUID!!,
                        patient_UUID!!,
                        encounterType!!,
                        fetchEncounterRetrofitCallBack
                    )
                }


            } else {
                Toast.makeText(activity, "Please enter all required fields", Toast.LENGTH_LONG)
                    .show()
            }
        }
        binding?.clearCardView?.setOnClickListener {
            binding?.calendarEditText!!.setText("")
            binding?.timerEditText!!.setText("")
            binding?.wardSpinner!!.setSelection(0)


        }

        viewModel?.getCurrentDateTime(currentDateTimeCallBack)

        return binding!!.root
    }

    fun saveAdmission() {
        admissionSaveRequestModel.facility_uuid = facility_UUID.toString()
        admissionSaveRequestModel.department_uuid = department_uuid.toString()
        admissionSaveRequestModel.encounter_type_uuid = encounterType
        admissionSaveRequestModel.referred_date = "2020-12-31T06:12:29.000Z"
        admissionSaveRequestModel.referral_type_uuid = 2
        admissionSaveRequestModel.referral_facility_uuid = facility_UUID.toString()
        admissionSaveRequestModel.referral_deptartment_uuid = department_uuid
        admissionSaveRequestModel.referal_reason_uuid = selectReasonUUID.toString()
        admissionSaveRequestModel.referral_comments = binding?.commentsEditText!!.text.toString()
        admissionSaveRequestModel.encounter_uuid = encounter_UUID
        admissionSaveRequestModel.patient_uuid = patient_UUID.toString()
        admissionSaveRequestModel.ward_uuid = selectwardUUID

        val requestmodel = Gson().toJson(admissionSaveRequestModel)

        Log.e("Save Data", requestmodel.toString())

        viewModel?.getSaveAdmission(admissionSaveRequestModel, admissionSaveRetrofitCallback)
    }

    val AddAllDepartmentCallBack =
        object : RetrofitCallback<FavAddAllDepatResponseModel> {
            @SuppressLint("NewApi")
            override fun onSuccessfulResponse(responseBody: Response<FavAddAllDepatResponseModel>?) {

                listAllAddDepartmentItems = responseBody?.body()?.responseContents!!
                favAddResponseMap =
                    listAllAddDepartmentItems.map { it?.uuid!! to it.name }.toMap().toMutableMap()
                val adapter =
                    ArrayAdapter<String>(
                        requireContext(),
                        R.layout.spinner_item,
                        favAddResponseMap.values.toMutableList()
                    )

                adapter.setDropDownViewResource(R.layout.spinner_item)
                binding?.spinnerFavLabdepartment!!.adapter = adapter

            }

            override fun onBadRequest(errorBody: Response<FavAddAllDepatResponseModel>?) {

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
    val WardCallBack =
        object : RetrofitCallback<AdmissionWardResponseModel> {
            @SuppressLint("NewApi")
            override fun onSuccessfulResponse(responseBody: Response<AdmissionWardResponseModel>?) {

                val admissionWardResponseContent =
                    AmissionWardResponseContent(ward_uuid = 0, ward_name = "Select Ward")
                wardItems.add(admissionWardResponseContent)
                wardItems.addAll(responseBody?.body()?.responseContents!!)
                wardResponseMap =
                    wardItems.map { it?.ward_uuid!! to it.ward_name }.toMap().toMutableMap()

                hashWardSpinnerList.clear()

                for (i in responseBody.body()?.responseContents?.indices!!) {

                    hashWardSpinnerList[responseBody.body()?.responseContents!![i].ward_uuid] = i
                }

                val adapter =
                    ArrayAdapter<String>(
                        requireContext(),
                        R.layout.spinner_item,
                        wardResponseMap.values.toMutableList()
                    )

                adapter.setDropDownViewResource(R.layout.spinner_item)
                binding?.wardSpinner!!.adapter = adapter
            }

            override fun onBadRequest(errorBody: Response<AdmissionWardResponseModel>?) {

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

    private val getEncounterByDocAndPatientIdRespCallback =
        object : RetrofitCallback<RadiologyEncounterResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<RadiologyEncounterResponseModel>?) {

                uuid = responseBody?.body()?.responseContents?.get(0)?.uuid
                PatientId = responseBody?.body()?.responseContents?.get(0)?.patient_uuid
                viewModel?.getSaveAdmission(
                    admissionSaveRequestModel,
                    admissionSaveRetrofitCallback
                )
            }

            override fun onBadRequest(errorBody: Response<RadiologyEncounterResponseModel>?) {
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


    val admissionSaveRetrofitCallback = object : RetrofitCallback<AdmissionSaveResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<AdmissionSaveResponseModel>?) {


            if (responseBody?.code() == 200) {
                Toast.makeText(activity, responseBody.body()?.message, Toast.LENGTH_LONG).show()

                binding?.autoCompleteTextViewDepartment!!.setText("")
                binding?.calendarEditText!!.setText("")
                binding?.timerEditText!!.setText("")
                binding?.commentsEditText!!.setText("")
                binding?.wardSpinner!!.setSelection(0)
                binding?.reasonSpinner!!.setSelection(0)
            }


        }

        override fun onBadRequest(errorBody: Response<AdmissionSaveResponseModel>?) {

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

    val ReasonRetrofitCallback = object : RetrofitCallback<ReasonResponseModel> {
        override fun onSuccessfulResponse(response: Response<ReasonResponseModel>) {
            val reasonResponseContent =
                ReasonResponseContent(uuid = 0, code = "", name = "")
            response.body()?.responseContent?.forEach {
                listAllReasonItems.add(0, reasonResponseContent)
                listAllReasonItems.add(it)
            }


            AddinstituteResponseMap =
                listAllReasonItems.map { it?.uuid!! to it.name }.toMap().toMutableMap()


            hashReasonSpinnerList.clear()

            for (i in response.body()?.responseContent?.indices!!) {

                hashReasonSpinnerList[response.body()?.responseContent!![i].uuid] = i
            }

            val adapter =
                ArrayAdapter<String>(
                    requireContext(),
                    R.layout.spinner_item,
                    AddinstituteResponseMap.values.toMutableList()
                )

            adapter.setDropDownViewResource(R.layout.spinner_item)
            //binding?.reason!!.adapter = adapter
            binding?.reasonSpinner!!.adapter = adapter

            viewModel?.getPatientAdmission(patientReferalList)

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

    val getInstitutionSearchRetrofitCallBack =
        object : RetrofitCallback<DepartmentAutoCompleteResponseModel> {

            override fun onSuccessfulResponse(response: Response<DepartmentAutoCompleteResponseModel>) {
                //responseContents = Gson().toJson(response.body()?.responseContents)
                if (response.body()?.responseContents?.isNotEmpty()!!) {

                    Log.e("SearchData", response.body()?.responseContents.toString())
                    setDepartmentToAutoComplete(response.body()?.responseContents)

                }
            }

            override fun onBadRequest(response: Response<DepartmentAutoCompleteResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: DepartmentAutoCompleteResponseModel
                try {
                    //      customProgressDialog!!.dismiss()
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        DepartmentAutoCompleteResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        responseModel.status!!
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

    fun setDepartmentToAutoComplete(responseContents: List<ResponseContent?>?) {

        departmentList = responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()
        val adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            departmentList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.autoCompleteTextViewDepartment!!.threshold = 1
        binding?.autoCompleteTextViewDepartment!!.setAdapter(adapter)

    }


    val currentDateTimeCallBack =
        object : RetrofitCallback<CurrentDateTimeResponseModel> {
            @SuppressLint("NewApi")
            override fun onSuccessfulResponse(responseBody: Response<CurrentDateTimeResponseModel>?) {

                Log.e("Date Time Resp", responseBody!!.body()!!.currentDateTime.toString())

                val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val output = SimpleDateFormat("dd/MM/yyyy")
                val outputTime = SimpleDateFormat("HH:mm a")

                var d: Date? = null
                try {
                    d = input.parse(responseBody.body()!!.currentDateTime.toString())
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                val formatted = output.format(d)
                val formattedTime = outputTime.format(d)
                Log.e("DATE", "" + formatted)
                Log.e("Time", "" + formattedTime)

                binding?.calendarEditText!!.setText(formatted)
                //binding?.timerEditText!!.setText()

            }

            override fun onBadRequest(errorBody: Response<CurrentDateTimeResponseModel>?) {

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

    //getPatientAdmission


    val patientReferalList =
        object : RetrofitCallback<AdmissionPatientRefResponseModel> {
            @SuppressLint("NewApi")
            override fun onSuccessfulResponse(responseBody: Response<AdmissionPatientRefResponseModel>?) {

                responseBody!!.body()!!.responseContents?.toString()?.let {
                    Log.e(
                        "PatientAdmission",
                        it
                    )
                }

                if (responseBody.body()!!.responseContents!!.size > 0) {
                    edit = true
                    //binding?.wardSpinner!!.setSelection(hashWardSpinnerList.get(responseBody!!.body()!!.responseContents!![0].ward_uuid)!!)
                    binding?.reasonSpinner!!.setSelection(hashReasonSpinnerList.get(responseBody.body()!!.responseContents!![0].referal_reason_uuid)!!)
                    binding?.commentsEditText!!.setText(responseBody.body()!!.responseContents!![0].referral_comments.toString())

                    //binding?.autoCompleteTextViewDepartment!!.setSelection(hashDeptSpinnerList.get(responseBody.body()!!.responseContents!![0].referral_deptartment_uuid)!!)
                }
            }

            override fun onBadRequest(errorBody: Response<AdmissionPatientRefResponseModel>?) {

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
                    saveAdmission()

                } else {
                    viewModel?.createEncounter(
                        patient_UUID!!,
                        encounterType!!,
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
            patient_UUID =
                response.body()?.responseContents?.encounterDoctor?.patient_uuid!!.toInt()

            saveAdmission()
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

    //allDeptCallback
    val allDeptCallback = object : RetrofitCallback<AllDepartmentsResponseModel> {
        override fun onSuccessfulResponse(response: Response<AllDepartmentsResponseModel>) {

            Log.e("Depts", response.body()?.responseContents.toString())

            hashDeptSpinnerList.clear()

            for (i in response.body()?.responseContents?.indices!!) {

                hashDeptSpinnerList[response.body()?.responseContents!![i].uuid!!] = i
            }

            viewModel?.getReason(facility_UUID!!, ReasonRetrofitCallback)
        }

        override fun onBadRequest(response: Response<AllDepartmentsResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: AllDepartmentsResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    AllDepartmentsResponseModel::class.java
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

    //updatePatientCallback

    val updatePatientCallback =
        object : RetrofitCallback<AdmissionUpdatePatientResponseModel> {
            @SuppressLint("NewApi")
            override fun onSuccessfulResponse(responseBody: Response<AdmissionUpdatePatientResponseModel>?) {

                admissionUpdateRequestModel.Id = 21
                admissionUpdateRequestModel.encounter_uuid = encounter_UUID
                admissionUpdateRequestModel.patient_uuid = patient_UUID.toString()
                admissionUpdateRequestModel.doctor_uuid = doctor_UUID.toString()
                admissionUpdateRequestModel.from_facility = facility_UUID.toString()
                admissionUpdateRequestModel.department_uuid = department_uuid
                admissionUpdateRequestModel.admitting_reason_uuid = selectReasonUUID
                admissionUpdateRequestModel.ward_uuid = selectwardUUID
                admissionUpdateRequestModel.admission_status_uuid = 1
                admissionUpdateRequestModel.requested_date = "2021-01-05T05:17:13.000Z"
                admissionUpdateRequestModel.comments = binding?.commentsEditText!!.text.toString()

                viewModel?.updateAdmission(admissionUpdateRequestModel, updateCallback)

            }

            override fun onBadRequest(errorBody: Response<AdmissionUpdatePatientResponseModel>?) {

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

    //updateCallback


    val updateCallback =
        object : RetrofitCallback<AdmissionUpdateRespModel> {
            @SuppressLint("NewApi")
            override fun onSuccessfulResponse(responseBody: Response<AdmissionUpdateRespModel>?) {

                Log.e("Update Success", responseBody?.body()?.responseContents.toString())
                Log.e("Update Success", responseBody?.body()?.msg.toString())

            }

            override fun onBadRequest(errorBody: Response<AdmissionUpdateRespModel>?) {

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

}