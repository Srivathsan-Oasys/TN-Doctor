package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.component.extention.isTablet
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.DialogSaveOrderTreatmentBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.fire_base_analytics.AnalyticsManager
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.model.DiagnosisListData
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.OrderSaveTreatmentKitResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.request.*
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.response.TreatmentKitCreateResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.treatmentKitPresModel.TKtPrescriptionListData
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.view_model.TreatmentKitSaveOrderViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.view_model.TreatmentSaveOrderKitViewModel
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SaveOrderTreatmentFragment(private val type: String) : DialogFragment() {

    private var deparment_UUID: Int? = 0
    private var content: String? = null
    private var utils: Utils? = null
    private var viewModel: TreatmentSaveOrderKitViewModel? = null
    var binding: DialogSaveOrderTreatmentBinding? = null
    private var facility_UUID: Int? = 0
    private var patient_id: Int = 0
    private var doctor_UUID: Int? = 0
    private var encounter_id: Int? = 0
    private var encounter_type: Int? = 0
    private var storemaster_Id: Int? = 0
    private var treatmentKitUuid: Int? = 0
    private var warduuid: Int = 0
    private var favAddResponseMap = mutableMapOf<Int, String>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var dropdownReferenceView: AutoCompleteTextView? = null
    private var listDepartmentItems: ArrayList<FavAddResponseContent?> = ArrayList()

    private var key: Int? = 0
    private var selectDepartmentId: Int? = 0
    private var selectStatus: Boolean? = false
    var treatmentCreate: CreateTreatmentkitRequestModel = CreateTreatmentkitRequestModel()

    var appPreferences: AppPreferences? = null

    var mcallback: RefreshClickedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString(AppConstants.ALERTDIALOG)
        key = arguments?.getInt("key")
        Log.e("key", key.toString())
        val style = STYLE_NO_FRAME
        val theme = R.style.DialogTheme
        setStyle(style, theme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_save_order_treatment,
                container,
                false
            )
        viewModel = TreatmentKitSaveOrderViewModelFactory(
            requireActivity().application
        ).create(TreatmentSaveOrderKitViewModel::class.java)

        dialog?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        utils = Utils(requireContext())


        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        deparment_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)!!
        patient_id = appPreferences?.getInt(AppConstants.PATIENT_UUID)!!
        //encounter_id = appPreferences?.getInt(AppConstants.ENCOUNTER_UUID)!!
        encounter_type = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)!!
        storemaster_Id = appPreferences?.getInt(AppConstants.STOREMASTER_UUID)!!
        warduuid = appPreferences?.getInt(AppConstants.WARDUUID)!!

        binding?.viewModel?.getSaveOrderDepartmentList(
            facility_UUID,
            templateRadiodepartmentRetrofitCallBack
        )

        initViews()
        listeners()

        return binding?.root
    }

    private fun initViews() {
        binding?.tvSave?.text = type
    }

    @SuppressLint("SetTextI18n")
    private fun listeners() {
        binding?.clearCardView?.setOnClickListener {
            binding?.treatmentCode?.setText("")
            binding?.treatmentName?.setText("")
            binding?.treatmentDescription?.setText("")
//            binding?.actvieFromDateEditText?.setText("")
//            binding?.actvieTo?.setText("")
        }

        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }

        binding?.actvieFromDateEditText!!.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireActivity(),
                { view, year, monthOfYear, dayOfMonth ->

                    val cal = Calendar.getInstance()
                    val mHour = cal[Calendar.HOUR_OF_DAY]
                    val mMinute = cal[Calendar.MINUTE]
                    val mSeconds = cal[Calendar.SECOND]

                    val timePickerDialog = TimePickerDialog(
                        this.activity,
                        { view1, hourOfDay, minute ->

                            binding?.actvieFromDateEditText?.setText(
                                String.format(
                                    "%02d",
                                    dayOfMonth
                                ) + "-" + String.format(
                                    "%02d",
                                    monthOfYear + 1
                                ) + "-" + year + " " + String.format("%02d", hourOfDay) + ":" +
                                        String.format("%02d", minute) + ":" + String.format(
                                    "%02d",
                                    mSeconds
                                )
                            )
                        },
                        mHour,
                        mMinute,
                        false
                    )
                    timePickerDialog.show()
                }, mYear, mMonth, mDay
            )
            //datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        binding?.actvieTo!!.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireActivity(),
                { view, year, monthOfYear, dayOfMonth ->

                    val cal = Calendar.getInstance()
                    val mHour = cal[Calendar.HOUR_OF_DAY]
                    val mMinute = cal[Calendar.MINUTE]
                    val mSeconds = cal[Calendar.SECOND]

                    val timePickerDialog = TimePickerDialog(
                        this.activity,
                        { view1, hourOfDay, minute ->

                            val myFormat = "dd-MM-yyyy"
                            val sdf = SimpleDateFormat(myFormat, Locale.US)

                            binding?.actvieTo?.setText(
                                String.format(
                                    "%02d",
                                    dayOfMonth
                                ) + "-" + String.format(
                                    "%02d",
                                    monthOfYear + 1
                                ) + "-" + year + " " + String.format("%02d", hourOfDay) + ":" +
                                        String.format("%02d", minute) + ":" + String.format(
                                    "%02d",
                                    mSeconds
                                )
                            )

                            val fromDate = binding?.actvieFromDateEditText?.text?.toString()
                            val toDate = binding?.actvieTo?.text?.toString()

                            val status = utils?.datecompare(fromDate, toDate)

                            if (!status!!) {
                                Toast.makeText(
                                    requireContext(),
                                    "Active to date greater than from active from date Please check",
                                    Toast.LENGTH_LONG
                                ).show()
                                binding?.actvieTo?.setText("")
                            }

                        },
                        mHour,
                        mMinute,
                        false
                    )
                    timePickerDialog.show()
                }, mYear, mMonth, mDay
            )
            //datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()

        }

        binding?.spinnerInvestdepartment!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()

                    selectDepartmentId =
                        favAddResponseMap.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(position).toString()

                    selectDepartmentId =
                        favAddResponseMap.filterValues { it == itemValue }.keys.toList()[0]
                }
            }

        binding?.switchStatus!!.setOnCheckedChangeListener { buttonView, isChecked ->
            selectStatus = isChecked
        }

        binding?.saveTreatement?.setOnClickListener {
            if (binding?.treatmentCode!!.text.trim().toString().isEmpty()) {
                binding?.treatmentCode!!.error = "Enter Treatment Code"
                return@setOnClickListener
            }
            if (binding?.treatmentName!!.text.trim().toString().isEmpty()) {
                binding?.treatmentName!!.error = "Enter Treatment Name"
                return@setOnClickListener
            }
//            if (binding?.actvieFromDateEditText!!.text.toString().isEmpty()) {
//                binding?.actvieFromDateEditText!!.error = "Select Date"
//                return@setOnClickListener
//            }
            if (selectDepartmentId == 0) {
                Toast.makeText(activity, "Select Department", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
//            val fromDate = binding?.actvieFromDateEditText?.text?.toString()
//            val toDate = binding?.actvieTo?.text?.toString()

//            val status = utils?.datecompare(fromDate, toDate)

//            if (!status!!) {
//                Toast.makeText(
//                    requireContext(),
//                    "Active to date greater than from active from date Please check",
//                    Toast.LENGTH_LONG
//                ).show()
//                return@setOnClickListener
//            }

            viewModel?.getEncounter(
                facility_UUID!!,
                patient_id,
                encounter_type!!,
                fetchEncounterRetrofitCallBack
            )

        }
    }

    interface RefreshClickedListener {
        fun refresh()
    }

    fun setOnRefreshClickedListener(callback: RefreshClickedListener) {
        this.mcallback = callback
    }

    val templateRadiodepartmentRetrofitCallBack =
        object : RetrofitCallback<FavAddResponseModel> {
            override fun onSuccessfulResponse(response: Response<FavAddResponseModel>) {

                listDepartmentItems.add(response.body()?.responseContent)
                favAddResponseMap =
                    listDepartmentItems.map { it?.uuid!! to it.name }.toMap().toMutableMap()

                val adapter = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    favAddResponseMap.values.toMutableList()
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding?.spinnerInvestdepartment!!.adapter = adapter
            }

            override fun onBadRequest(response: Response<FavAddResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: FavAddResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        FavAddResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        ""
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

    private val createTreatmentRetrofitCallback =
        object : RetrofitCallback<TreatmentKitCreateResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<TreatmentKitCreateResponseModel>?) {


                if (responseBody?.body()?.code == 200) {

                    if (key?.equals(0) == true) {
                        trackTreatmentAnalyticsSaveComplete(
                            utils?.getEncounterType(),
                            "success",
                            ""
                        )
                        Log.e("OrdCreate", key.toString())
                        Toast.makeText(activity, responseBody.body()?.message, Toast.LENGTH_LONG)
                            .show()

                        mcallback!!.refresh()
                        dialog?.dismiss()
                    } else if (key?.equals(1) == true) {
                        orderCreate()
                        Log.e("OrdCreate", key.toString())
                    }
                }
            }

            override fun onBadRequest(response: Response<TreatmentKitCreateResponseModel>) {
                trackTreatmentAnalyticsSaveComplete(
                    utils!!.getEncounterType(), "failure",
                    response.message()
                )
                val gson = GsonBuilder().create()
                val responseModel: TreatmentKitCreateResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        TreatmentKitCreateResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        ""
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
                trackTreatmentAnalyticsSaveComplete(
                    utils!!.getEncounterType(),
                    "failure",
                    getString(R.string.something_went_wrong)
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                trackTreatmentAnalyticsSaveComplete(
                    utils!!.getEncounterType(),
                    "failure",
                    getString(R.string.unauthorized)
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                trackTreatmentAnalyticsSaveComplete(
                    utils!!.getEncounterType(),
                    "failure",
                    getString(R.string.something_went_wrong)
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String) {
                trackTreatmentAnalyticsSaveComplete(utils!!.getEncounterType(), "failure", failure)
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    fun orderCreate() {
        val treatmentKitUuid: Int = appPreferences?.getInt(AppConstants.PREF_TKT_UUID) ?: 0
        val args = arguments
        if (args != null) {
            val favouriteDataLab =
                args.getParcelableArrayList<FavouritesModel>(AppConstants.RESPONSECONTENT)
            /*if (favouriteDataLab!!.size > 0) {
                favouriteDataLab.removeAt(favouriteDataLab.size - 1);
            }*/
            val favouriteDataDiagnosis = if (isTablet(requireContext())) {
                args.getParcelableArrayList<FavouritesModel>(AppConstants.DIAGNISISRESPONSECONTENT)
            } else {
                args.getParcelableArrayList<DiagnosisListData>(AppConstants.DIAGNISISRESPONSECONTENT)
            }
            /*if (favouriteDataDiagnosis!!.size > 0) {
                favouriteDataDiagnosis.removeAt(favouriteDataDiagnosis.size - 1);
            }*/


            val favouriteDataPrescription =
                if (isTablet(requireContext()))
                    args.getParcelableArrayList<FavouritesModel>(AppConstants.PRESCRIPTIONRESPONSECONTENT)
                else
                    args.getParcelableArrayList<TKtPrescriptionListData>(AppConstants.PRESCRIPTIONRESPONSECONTENT)
            /*if (favouriteDataPrescription!!.size > 0) {
                favouriteDataPrescription.removeAt(favouriteDataPrescription.size - 1);
            }*/

            val favouriteDataRadiology =
                args.getParcelableArrayList<FavouritesModel>(AppConstants.RADIOLOGYRESPONSECONTENT)
            /* if (favouriteDataRadiology!!.size > 0) {
                 favouriteDataRadiology.removeAt(favouriteDataRadiology.size - 1);
             }*/

            val favouriteDataInvestigation =
                args.getParcelableArrayList<FavouritesModel>(AppConstants.INVESTIGATIONRESPONSECONTENT)
            /* if (favouriteDataInvestigation!!.size > 0) {
                 favouriteDataInvestigation.removeAt(favouriteDataInvestigation.size - 1);
             }*/


            Log.e("diagsize", favouriteDataDiagnosis?.size.toString())
            Log.e("labsize", favouriteDataLab?.size.toString())
            Log.e("radiosize", favouriteDataRadiology?.size.toString())
            Log.e("Invgsize", favouriteDataInvestigation?.size.toString())


            if (favouriteDataLab!!.size > 0 || favouriteDataRadiology!!.size > 0
                || favouriteDataPrescription!!.size > 0 || favouriteDataInvestigation!!.size > 0
            ) {

                val tkOrderRequestModel: TKOrderRequestModel = TKOrderRequestModel()

                tkOrderRequestModel.patientTreatment?.facility_uuid = facility_UUID.toString()
                tkOrderRequestModel.patientTreatment?.department_uuid =
                    deparment_UUID.toString()
                tkOrderRequestModel.patientTreatment?.patient_uuid = patient_id.toString()
                tkOrderRequestModel.patientTreatment?.encounter_uuid = encounter_id
                tkOrderRequestModel.patientTreatment?.encounter_type_uuid = encounter_type
                tkOrderRequestModel.patientTreatment?.consultation_uuid = 0
                tkOrderRequestModel.patientTreatment?.treatment_kit_uuid = treatmentKitUuid
                tkOrderRequestModel.patientTreatment?.treatment_status_uuid = 0
                tkOrderRequestModel.patientTreatment?.comments = "test comments"

                if (favouriteDataDiagnosis!!.size > 0) {

                    val reqDiagnosis: ArrayList<PatientDiagnosis> = ArrayList()
                    reqDiagnosis.clear()

                    val diagList = favouriteDataDiagnosis.size
                    for (i in favouriteDataDiagnosis.indices) {
                        val temp: Int? = if (isTablet(requireContext())) {
                            (favouriteDataDiagnosis.get(i) as? FavouritesModel)?.diagnosisUUiD
                        } else {
                            (favouriteDataDiagnosis.get(i) as? DiagnosisListData)?.diagnosisUUiD
                        }

                        val treatmentKitDiagOrder: PatientDiagnosis = PatientDiagnosis()
                        treatmentKitDiagOrder.facility_uuid = facility_UUID.toString()
                        treatmentKitDiagOrder.department_uuid =
                            deparment_UUID.toString()
                        treatmentKitDiagOrder.patient_uuid = patient_id.toString()
                        treatmentKitDiagOrder.encounter_uuid = encounter_id
                        treatmentKitDiagOrder.encounter_type_uuid = encounter_type
                        treatmentKitDiagOrder.consultation_uuid = 0
                        treatmentKitDiagOrder.diagnosis_uuid = temp
                        treatmentKitDiagOrder.condition_type_uuid = 0
                        treatmentKitDiagOrder.condition_status_uuid = 0
                        treatmentKitDiagOrder.category_uuid = 0
                        treatmentKitDiagOrder.type_uuid = 0
                        treatmentKitDiagOrder.grade_uuid = 0
                        treatmentKitDiagOrder.body_site_uuid = 0
                        treatmentKitDiagOrder.prescription_uuid = 0
                        treatmentKitDiagOrder.patient_treatment_uuid = 0

                        reqDiagnosis.add(treatmentKitDiagOrder)
                    }

                    //reqDiagnosis.removeAt(reqDiagnosis.size - 1)
                    tkOrderRequestModel.patientDiagnosis = reqDiagnosis
                }

                if (favouriteDataLab.size > 0) {

                    tkOrderRequestModel.patientLab?.header?.patient_uuid = patient_id.toString()
                    tkOrderRequestModel.patientLab?.header?.encounter_uuid = encounter_id
                    tkOrderRequestModel.patientLab?.header?.encounter_type_uuid = encounter_type
                    tkOrderRequestModel.patientLab?.header?.lab_master_type_uuid = 1
                    tkOrderRequestModel.patientLab?.header?.doctor_uuid = doctor_UUID.toString()
                    tkOrderRequestModel.patientLab?.header?.department_uuid =
                        deparment_UUID.toString()
                    tkOrderRequestModel.patientLab?.header?.facility_uuid =
                        facility_UUID.toString()
                    tkOrderRequestModel.patientLab?.header?.sub_department_uuid = 0
                    tkOrderRequestModel.patientLab?.header?.order_to_location_uuid = 1
                    tkOrderRequestModel.patientLab?.header?.consultation_uuid = 0
                    tkOrderRequestModel.patientLab?.header?.patient_treatment_uuid = 0
                    tkOrderRequestModel.patientLab?.header?.treatment_plan_uuid = 0
                    tkOrderRequestModel.patientLab?.header?.order_status_uuid = 0
                    tkOrderRequestModel.patientLab?.header?.application_type_uuid = 10
                    tkOrderRequestModel.patientLab?.header?.treatment_kit_uuid = treatmentKitUuid

                    //wardidbind for ip

                    if (encounter_type == AppConstants.TYPE_IN_PATIENT) {


                        tkOrderRequestModel.patientLab?.header?.ward_uuid = warduuid
                    }

                    val requestLabDetail: ArrayList<Detail?> = ArrayList()
                    requestLabDetail.clear()

                    val labList = favouriteDataLab.size
                    for (i in 0..labList - 1) {
                        val labDetail: Detail = Detail()
                        labDetail.profile_uuid = 1
                        if (favouriteDataLab[i]!!.test_master_id == 0) {
                            labDetail.test_master_uuid =
                                favouriteDataLab[i]!!.profile_master_uuid
                        } else {
                            labDetail.test_master_uuid = favouriteDataLab[i]!!.test_master_id
                        }
                        labDetail.test_master_uuid = favouriteDataLab[i]?.test_master_id
                        labDetail.is_profile = false
                        labDetail.lab_master_type_uuid = 1
                        labDetail.to_department_uuid = 1
                        labDetail.order_priority_uuid =
                            favouriteDataLab[i]?.selectTypeUUID.toString()
                        labDetail.to_location_uuid =
                            favouriteDataLab[i]?.selectToLocationUUID.toString()
                        labDetail.group_uuid = 0
                        labDetail.to_sub_department_uuid = 0
                        labDetail.patient_work_order_by = 0
                        labDetail.comments = favouriteDataLab[i]?.commands
                        labDetail.encounter_type_uuid = encounter_type
                        labDetail.is_active = 1
                        labDetail.status = 1
                        labDetail.application_type_uuid = 10

                        if (encounter_type == AppConstants.TYPE_IN_PATIENT) {

                            labDetail.ward_uuid = warduuid
                        }

                        requestLabDetail.add(labDetail)
                    }
                    //requestLabDetail.removeAt(requestLabDetail.size - 1)
                    tkOrderRequestModel.patientLab!!.details = requestLabDetail
                }


                if (favouriteDataRadiology!!.size > 0) {
                    tkOrderRequestModel.patientRadiology?.header?.patient_uuid =
                        patient_id.toString()
                    tkOrderRequestModel.patientRadiology?.header?.encounter_uuid = encounter_id
                    tkOrderRequestModel.patientRadiology?.header?.encounter_type_uuid =
                        encounter_type
                    tkOrderRequestModel.patientRadiology?.header?.lab_master_type_uuid = 1
                    tkOrderRequestModel.patientRadiology?.header?.consultation_uuid = 0
                    tkOrderRequestModel.patientRadiology?.header?.patient_treatment_uuid = 0
                    tkOrderRequestModel.patientRadiology?.header?.treatment_plan_uuid = 0
                    tkOrderRequestModel.patientRadiology?.header?.doctor_uuid =
                        doctor_UUID.toString()
                    tkOrderRequestModel.patientRadiology?.header?.department_uuid =
                        deparment_UUID.toString()
                    tkOrderRequestModel.patientRadiology?.header?.sub_department_uuid = 0
                    tkOrderRequestModel.patientRadiology?.header?.order_to_location_uuid = 1
                    tkOrderRequestModel.patientRadiology?.header?.treatment_kit_uuid =
                        treatmentKitUuid

                    val requestRadiologyDetail: ArrayList<DetailXX?> = ArrayList()
                    requestRadiologyDetail.clear()


                    val radioList = favouriteDataRadiology.size
                    for (i in 0 until radioList) {
                        val radiologyDetail: DetailXX = DetailXX()

                        radiologyDetail.profile_uuid = 1
                        if (favouriteDataRadiology[i]!!.test_master_id == 0) {
                            radiologyDetail.test_master_uuid =
                                favouriteDataRadiology[i]!!.profile_master_uuid
                        } else {
                            radiologyDetail.test_master_uuid =
                                favouriteDataRadiology[i]!!.test_master_id
                        }

                        radiologyDetail.is_profile = true
                        radiologyDetail.lab_master_type_uuid = 2
                        radiologyDetail.to_department_uuid = 0
                        radiologyDetail.order_priority_uuid =
                            favouriteDataRadiology[i]!!.selectTypeUUID.toString()
                        radiologyDetail.to_location_uuid =
                            favouriteDataRadiology[i]!!.selectToLocationUUID.toString()
                        radiologyDetail.group_uuid = 0
                        radiologyDetail.to_sub_department_uuid = 0
                        radiologyDetail.details_comments = favouriteDataRadiology[i]?.commands
                        radiologyDetail.encounter_type_uuid = encounter_type

                        requestRadiologyDetail.add(radiologyDetail)

                    }
                    //requestRadiologyDetail.removeAt(requestRadiologyDetail.size - 1)
                    tkOrderRequestModel.patientRadiology?.details = requestRadiologyDetail

                }

                if (favouriteDataPrescription!!.size > 0) {
                    tkOrderRequestModel.patientPrescription?.header?.dispense_status_uuid = 1
                    tkOrderRequestModel.patientPrescription?.header?.prescription_status_uuid = 2
                    tkOrderRequestModel.patientPrescription?.header?.store_master_uuid =
                        storemaster_Id
                    tkOrderRequestModel.patientPrescription?.header?.patient_uuid =
                        patient_id.toString()
                    tkOrderRequestModel.patientPrescription?.header?.department_uuid =
                        deparment_UUID.toString()
                    tkOrderRequestModel.patientPrescription?.header?.doctor_uuid =
                        doctor_UUID.toString()
                    tkOrderRequestModel.patientPrescription?.header?.encounter_uuid =
                        encounter_id
                    tkOrderRequestModel.patientPrescription?.header?.encounter_type_uuid =
                        encounter_type
                    tkOrderRequestModel.patientPrescription?.header?.treatment_kit_uuid =
                        treatmentKitUuid
                    tkOrderRequestModel.patientPrescription?.header?.notes = ""


                    val requestPrescriptionDetail: ArrayList<DetailX?> =
                        ArrayList()
                    requestPrescriptionDetail.clear()

                    val prescList = favouriteDataPrescription.size
                    for (i in 0 until prescList) {
                        if (isTablet(requireContext())) {
                            val prescriptionDetail = DetailX()

                            prescriptionDetail.item_master_uuid =
                                (favouriteDataPrescription[i] as? FavouritesModel)?.test_master_id
                            prescriptionDetail.drug_route_uuid =
                                (favouriteDataPrescription[i] as? FavouritesModel)?.drug_route_id.toString()
                            prescriptionDetail.drug_frequency_uuid =
                                (favouriteDataPrescription[i] as? FavouritesModel)?.drug_frequency_id.toString()
                            prescriptionDetail.duration =
                                (favouriteDataPrescription[i] as? FavouritesModel)?.duration.toString()
                            prescriptionDetail.duration_period_uuid =
                                (favouriteDataPrescription[i] as? FavouritesModel)?.durationPeriodId.toString()
                            prescriptionDetail.drug_instruction_uuid =
                                (favouriteDataPrescription[i] as? FavouritesModel)?.drug_instruction_id.toString()
                            prescriptionDetail.prescribed_quantity =
                                (favouriteDataPrescription[i] as? FavouritesModel)?.drug_quantity.toString()

                            requestPrescriptionDetail.add(prescriptionDetail)
                        } else {
                            val prescriptionDetail = DetailX()

                            prescriptionDetail.item_master_uuid =
                                (favouriteDataPrescription[i] as? TKtPrescriptionListData)?.drug_id
                            prescriptionDetail.drug_route_uuid =
                                (favouriteDataPrescription[i] as? TKtPrescriptionListData)?.drug_route_id.toString()
                            prescriptionDetail.drug_frequency_uuid =
                                (favouriteDataPrescription[i] as? TKtPrescriptionListData)?.drug_frequency_id.toString()
                            prescriptionDetail.duration =
                                (favouriteDataPrescription[i] as? TKtPrescriptionListData)?.drug_duration.toString()
                            prescriptionDetail.duration_period_uuid =
                                (favouriteDataPrescription[i] as? TKtPrescriptionListData)?.drug_period_id.toString()
                            prescriptionDetail.drug_instruction_uuid =
                                (favouriteDataPrescription[i] as? TKtPrescriptionListData)?.drug_instruction_id.toString()
                            prescriptionDetail.prescribed_quantity =
                                (favouriteDataPrescription[i] as? TKtPrescriptionListData)?.drug_quantity.toString()

                            requestPrescriptionDetail.add(prescriptionDetail)
                        }
                    }
                    //requestPrescriptionDetail.removeAt(requestPrescriptionDetail.size - 1)
                    tkOrderRequestModel.patientPrescription?.details = requestPrescriptionDetail
                }

                if (favouriteDataInvestigation!!.size > 0) {

                    tkOrderRequestModel.patientInvestigation?.header?.patient_uuid =
                        patient_id.toString()
                    tkOrderRequestModel.patientInvestigation?.header?.encounter_uuid = encounter_id
                    tkOrderRequestModel.patientInvestigation?.header?.encounter_type_uuid =
                        encounter_type
                    tkOrderRequestModel.patientInvestigation?.header?.lab_master_type_uuid = 1
                    tkOrderRequestModel.patientInvestigation?.header?.consultation_uuid = 0
                    tkOrderRequestModel.patientInvestigation?.header?.patient_treatment_uuid = 0
                    tkOrderRequestModel.patientInvestigation?.header?.treatment_plan_uuid = 0
                    tkOrderRequestModel.patientInvestigation?.header?.doctor_uuid =
                        doctor_UUID.toString()
                    tkOrderRequestModel.patientInvestigation?.header?.department_uuid =
                        deparment_UUID.toString()
                    tkOrderRequestModel.patientInvestigation?.header?.sub_department_uuid = 0
                    tkOrderRequestModel.patientInvestigation?.header?.order_to_location_uuid = 1
                    tkOrderRequestModel.patientInvestigation?.header?.treatment_kit_uuid =
                        treatmentKitUuid


                    val requestInvestigationDetail: ArrayList<DetailXXX?> = ArrayList()
                    requestInvestigationDetail.clear()


                    val investList = favouriteDataInvestigation.size
                    for (i in 0..investList - 1) {

                        val invesDetail: DetailXXX = DetailXXX()
                        invesDetail.profile_uuid = 1
                        if (favouriteDataInvestigation[i]!!.test_master_id == 0) {
                            invesDetail.test_master_uuid =
                                favouriteDataInvestigation[i]!!.profile_master_uuid
                        } else {
                            invesDetail.test_master_uuid =
                                favouriteDataInvestigation[i]!!.test_master_id
                        }
                        invesDetail.is_profile = true
                        invesDetail.lab_master_type_uuid = 2
                        invesDetail.to_department_uuid = 0
                        invesDetail.order_priority_uuid =
                            favouriteDataInvestigation[i]!!.selectTypeUUID.toString()
                        invesDetail.to_location_uuid =
                            favouriteDataInvestigation[i]!!.selectToLocationUUID.toString()
                        invesDetail.group_uuid = 0
                        invesDetail.to_sub_department_uuid = 0
                        invesDetail.details_comments = favouriteDataInvestigation[i]?.commands
                        invesDetail.encounter_type_uuid = encounter_type

                        requestInvestigationDetail.add(invesDetail)

                    }

                    //requestInvestigationDetail.removeAt(requestInvestigationDetail.size - 1)
                    tkOrderRequestModel.patientInvestigation?.details = requestInvestigationDetail

                }

                val responseobject = Gson().toJson(tkOrderRequestModel)
                Log.e("RequestOrder", responseobject.toString())

                viewModel?.createOrderTreatmentKit(
                    facility_UUID,
                    tkOrderRequestModel,
                    orderSaveRetrofitCallback
                )


            } else {
                Toast.makeText(
                    activity,
                    "Enter Any One From Lab, Prescription, Investigation, Radiology..!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    val orderSaveRetrofitCallback = object : RetrofitCallback<OrderSaveTreatmentKitResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<OrderSaveTreatmentKitResponseModel>?) {
            responseBody?.body()?.message?.let { Log.e("OrderSaveSuccess", it) }

            Toast.makeText(activity, responseBody?.body()?.message, Toast.LENGTH_LONG).show()

            mcallback!!.refresh()
            dialog?.dismiss()

            //refresh()
        }

        override fun onBadRequest(response: Response<OrderSaveTreatmentKitResponseModel>) {
            //trackTreatmentOrderComplete(utils!!.getEncounterType(),"failure",response?.message())
            val gson = GsonBuilder().create()
            val responseModel: OrderSaveTreatmentKitResponseModel
            try {

                responseModel = gson.fromJson(
                    response.errorBody()?.string(),
                    OrderSaveTreatmentKitResponseModel::class.java
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
            //trackTreatmentOrderComplete(utils!!.getEncounterType(),"failure",   getString(R.string.something_went_wrong))
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            //trackTreatmentOrderComplete(utils!!.getEncounterType(),"failure",getString(R.string.unauthorized))
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            //trackTreatmentOrderComplete(utils!!.getEncounterType(),"failure", getString(R.string.something_went_wrong))
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String) {
            //trackTreatmentOrderComplete(utils!!.getEncounterType(),"failure", failure)
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }

    }

    fun trackTreatmentAnalyticsSaveComplete(type: String?, status: String?, message: String?) {
        AnalyticsManager.getAnalyticsManager()
            .trackTreatmentSaveOrderComplete(type, status, message)
    }

    fun saveAndOrder() {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val args = arguments
        if (args != null) {
            val favouriteDataLab =
                args.getParcelableArrayList<FavouritesModel>(AppConstants.RESPONSECONTENT)
            if (favouriteDataLab!!.size > 1) {
                favouriteDataLab.removeAt(favouriteDataLab.size - 1)
            }
            val favouriteDataDiagnosis =
                args.getParcelableArrayList<FavouritesModel>(AppConstants.DIAGNISISRESPONSECONTENT)
            if (favouriteDataDiagnosis!!.size > 1) {
                favouriteDataDiagnosis.removeAt(favouriteDataDiagnosis.size - 1)
            }


            val favouriteDataPrescription = if (isTablet(requireContext())) {
                args.getParcelableArrayList<FavouritesModel>(AppConstants.PRESCRIPTIONRESPONSECONTENT)
            } else {
                args.getParcelableArrayList<TKtPrescriptionListData>(AppConstants.PRESCRIPTIONRESPONSECONTENT)
            }
            if (favouriteDataPrescription!!.size > 1) {
                favouriteDataPrescription.removeAt(favouriteDataPrescription.size - 1)
            }

            val favouriteDataRadiology =
                args.getParcelableArrayList<FavouritesModel>(AppConstants.RADIOLOGYRESPONSECONTENT)
            if (favouriteDataRadiology!!.size > 1) {
                favouriteDataRadiology.removeAt(favouriteDataRadiology.size - 1)
            }

            val favouriteDataInvestigation =
                args.getParcelableArrayList<FavouritesModel>(AppConstants.INVESTIGATIONRESPONSECONTENT)
            if (favouriteDataInvestigation!!.size > 1) {
                favouriteDataInvestigation.removeAt(favouriteDataInvestigation.size - 1)
            }

            treatmentCreate.treatment_kit!!.name =
                binding?.treatmentName!!.text.trim().toString()
            treatmentCreate.treatment_kit!!.code =
                binding?.treatmentCode!!.text.trim().toString()
            treatmentCreate.treatment_kit!!.treatment_kit_type_uuid = selectDepartmentId
            treatmentCreate.treatment_kit!!.department_uuid = deparment_UUID.toString()
            treatmentCreate.treatment_kit!!.is_public = selectStatus.toString()
//            treatmentCreate.treatment_kit!!.activefrom =
//                binding?.actvieFromDateEditText!!.text.trim().toString()
            treatmentCreate.treatment_kit!!.is_active = "true"
            treatmentCreate.treatment_kit!!.user_uuid = userDataStoreBean?.uuid.toString()
            treatmentCreate.treatment_kit!!.facility_uuid = facility_UUID?.toString()


            val readtreatmentket: ArrayList<TreatmentKitLab?> = ArrayList()
            readtreatmentket.clear()
            for (i in favouriteDataLab.indices) {
                val treatmentKitLab: TreatmentKitLab = TreatmentKitLab()
                treatmentKitLab.test_master_uuid = favouriteDataLab[i].test_master_id
                treatmentKitLab.profile_master_uuid =
                    favouriteDataLab[i].profile_master_uuid.toString()
                treatmentKitLab.order_to_location_uuid =
                    favouriteDataLab[i].selectToLocationUUID
                treatmentKitLab.order_priority_uuid =
                    favouriteDataLab[i].selectTypeUUID.toString()
                readtreatmentket.add(treatmentKitLab)
            }

            treatmentCreate.treatment_kit_lab = readtreatmentket


            val readTreatmentkitRadiology: ArrayList<TreatmentKitRadiology> = ArrayList()
            readTreatmentkitRadiology.clear()
            for (i in favouriteDataRadiology.indices) {
                val treatmentRadiology: TreatmentKitRadiology = TreatmentKitRadiology()
                treatmentRadiology.test_master_uuid =
                    favouriteDataRadiology[i].test_master_id
                treatmentRadiology.profile_master_uuid =
                    favouriteDataRadiology[i].profile_master_uuid.toString()
                treatmentRadiology.order_to_location_uuid =
                    favouriteDataRadiology[i].selectToLocationUUID.toString()
                treatmentRadiology.order_priority_uuid =
                    favouriteDataRadiology[i].selectTypeUUID.toString()
                treatmentRadiology.radiology_description =
                    favouriteDataRadiology[i].radiology_description
                readTreatmentkitRadiology.add(treatmentRadiology)
            }
            treatmentCreate.treatment_kit_radiology = readTreatmentkitRadiology


            val readDiagnosis: ArrayList<TreatmentKitDiagnosis?> = ArrayList()
            readDiagnosis.clear()
            for (i in favouriteDataDiagnosis.indices) {
                val treatmentKitDiagnosis = TreatmentKitDiagnosis()
                try {
                    treatmentKitDiagnosis.diagnosis_uuid =
                        favouriteDataDiagnosis[i].diagnosis_id?.toInt()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                readDiagnosis.add(treatmentKitDiagnosis)
            }

            treatmentCreate.treatment_kit_diagnosis = readDiagnosis


            val readInvestigation: ArrayList<TreatmentKitInvestigation?> = ArrayList()
            readInvestigation.clear()
            for (i in favouriteDataInvestigation.indices) {
                val treatmentkitInvestigationn: TreatmentKitInvestigation =
                    TreatmentKitInvestigation()
                treatmentkitInvestigationn.test_master_uuid =
                    favouriteDataInvestigation[i].test_master_id.toString()
                treatmentkitInvestigationn.profile_master_uuid =
                    favouriteDataInvestigation[i].profile_master_uuid
                treatmentkitInvestigationn.order_to_location_uuid =
                    favouriteDataInvestigation[i].selectToLocationUUID.toString()
                treatmentkitInvestigationn.order_priority_uuid =
                    favouriteDataInvestigation[i].selectTypeUUID.toString()
                treatmentkitInvestigationn.investigation_description =
                    favouriteDataInvestigation[i].investigation_description.toString()
                readInvestigation.add(treatmentkitInvestigationn)
            }

            treatmentCreate.treatment_kit_investigation = readInvestigation


            val readPrescription: ArrayList<TreatmentKitPrescription?> = ArrayList()
            readPrescription.clear()
            for (i in favouriteDataPrescription.indices) {
                if (isTablet(requireContext())) {
                    val treatmentkitPrescription: TreatmentKitPrescription =
                        TreatmentKitPrescription()
                    treatmentkitPrescription.item_master_uuid =
                        (favouriteDataPrescription[i] as FavouritesModel).drug_id
                    treatmentkitPrescription.drug_route_uuid =
                        (favouriteDataPrescription[i] as FavouritesModel).selectRouteID.toString()
                    treatmentkitPrescription.drug_frequency_uuid =
                        (favouriteDataPrescription[i] as FavouritesModel).selecteFrequencyID.toString()

                    treatmentkitPrescription.duration =
                        (favouriteDataPrescription[i] as FavouritesModel).duration

                    treatmentkitPrescription.duration_period_uuid =
                        (favouriteDataPrescription[i] as FavouritesModel).PrescriptiondurationPeriodId.toString()

                    treatmentkitPrescription.drug_instruction_uuid =
                        (favouriteDataPrescription[i] as FavouritesModel).selectInvestID.toString()

                    treatmentkitPrescription.quantity =
                        (favouriteDataPrescription[i] as FavouritesModel).perdayquantityPrescription

                    readPrescription.add(treatmentkitPrescription)
                } else {
                    val treatmentkitPrescription: TreatmentKitPrescription =
                        TreatmentKitPrescription()
                    treatmentkitPrescription.item_master_uuid =
                        (favouriteDataPrescription[i] as TKtPrescriptionListData).drug_id
                    treatmentkitPrescription.drug_route_uuid =
                        (favouriteDataPrescription[i] as TKtPrescriptionListData).drug_route_id.toString()
                    treatmentkitPrescription.drug_frequency_uuid =
                        (favouriteDataPrescription[i] as TKtPrescriptionListData).drug_frequency_id.toString()

                    treatmentkitPrescription.duration =
                        (favouriteDataPrescription[i] as TKtPrescriptionListData).drug_duration.toString()

                    treatmentkitPrescription.duration_period_uuid =
                        (favouriteDataPrescription[i] as TKtPrescriptionListData).drug_period_id.toString()

                    treatmentkitPrescription.drug_instruction_uuid =
                        (favouriteDataPrescription[i] as TKtPrescriptionListData).drug_instruction_id.toString()

                    treatmentkitPrescription.quantity =
                        (favouriteDataPrescription[i] as TKtPrescriptionListData).perday_quantity

                    readPrescription.add(treatmentkitPrescription)
                }
            }

            treatmentCreate.treatment_kit_drug = readPrescription

            val responseobject = Gson().toJson(treatmentCreate)
            Log.e("AddedData", responseobject.toString())

            viewModel?.createTreatmentKit(
                facility_UUID,
                treatmentCreate,
                createTreatmentRetrofitCallback
            )
        }
    }

    private val fetchEncounterRetrofitCallBack =
        object : RetrofitCallback<FectchEncounterResponseModel> {
            override fun onSuccessfulResponse(response: Response<FectchEncounterResponseModel>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    val encounterResponseContent = response.body()?.responseContents!!
                    doctor_UUID = encounterResponseContent.get(0)?.encounter_doctors?.get(0)?.uuid!!
                    encounter_id = encounterResponseContent.get(0)?.uuid!!
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_DOCTOR_UUID, doctor_UUID!!)
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_UUID, encounter_id!!)
                    saveAndOrder()

                } else {
                    viewModel?.createEncounter(
                        patient_id,
                        encounter_type!!,
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


            doctor_UUID = response.body()?.responseContents?.encounterDoctor?.uuid!!.toInt()
            encounter_id = response.body()?.responseContents?.encounter?.uuid!!.toInt()
            patient_id = response.body()?.responseContents?.encounterDoctor?.patient_uuid!!.toInt()

            saveAndOrder()
        }

        override fun onBadRequest(response: Response<CreateEncounterResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: CreateEncounterResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    CreateEncounterResponseModel::class.java
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
}


