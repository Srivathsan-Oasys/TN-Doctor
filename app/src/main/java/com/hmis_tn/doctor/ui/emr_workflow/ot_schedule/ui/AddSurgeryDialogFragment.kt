package com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.ui

import android.app.Application
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.DialogOtscheduleAddFragmentBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.fire_base_analytics.AnalyticsManager
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.op_notes.model.GetOpNotesEncounterByDocAndPatientIdResp
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.PharmacyDispenseResponse
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.modify.ModifyOtScheduleReq
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.modify.ModifyOtScheduleResp
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.response.*
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery.*
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery.save.AddSurgeryResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery.spinners.*
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.ui.adapter.addsurgery.*
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.viewmodel.AddSurgeryViewModel
import com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.viewmodel.AddSurgeryViewModelFactory
import com.hmis_tn.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class AddSurgeryDialogFragment(
    private val isEdit: Boolean = false,
    private val otScheduleToCalendarresponseContent: OtScheduleToCalendarresponseContent? = null
) : DialogFragment() {

    private var viewModel: AddSurgeryViewModel? = null
    var binding: DialogOtscheduleAddFragmentBinding? = null
    private var appPreferences: AppPreferences? = null
    private var facilityId: Int? = null
    private var departId: Int? = null
    private var encounterType: Int? = null

    //    private var doctorId: Int? = null
    private var utils: Utils? = null
    var callbacksurgury: OnRefreshListener? = null
    private var switchChecks: Boolean? = false


    //calendar
    private var mYear: Int? = null
    private var mMonth: Int? = null
    private var mDay: Int? = null
    private var mHour: Int? = null
    private var mMinute: Int? = null
    private var mSecond: Int? = null
    private var startDate: String? = null
    private var endDate: String? = null

    //id
    private var auto_pin_id: Int? = 0
    private var autoCheifpinid: Int? = 0
    private var autoSurgeonid: Int? = 0
    private var autoAssistSurgeonid: Int? = 0
    private var autoAssistNurseid: Int? = 0
    private var selectedAnesthetistId: Int? = 0
    private var selectedLessionId: Int? = 0
    private var selectedSideId: Int? = 0
    private var selectedAnaeesthesiaId: Int? = 0
    private var selectedPositionsId: Int? = 0
    private var selectedOtNameId: Int? = 0
    private var selectedOtTypeId: Int? = 0
    private var selectedPriorityId: Int? = 0
    private var selectedGradeId: Int? = 0
    private var autoSearchNameId: Int? = 0
    private var autoSearchDiagnosisId: Int? = 0

    //spinnerMap
    private val anesthetistList = mutableMapOf<Int, String>()
    private val hashAnesthetistSpinnerMap: HashMap<Int, Int> = HashMap()

    private val lessionList = mutableMapOf<Int, String>()
    private val hashLessionSpinnerMap: HashMap<Int, Int> = HashMap()

    private val sideList = mutableMapOf<Int, String>()
    private val hashWideSpinnerMap: HashMap<Int, Int> = HashMap()

    private val AnaeesthesiaList = mutableMapOf<Int, String>()
    private val hashAnaeesthesiaSpinnerMap: HashMap<Int, Int> = HashMap()

    private val positionList = mutableMapOf<Int, String>()
    private val hashPositionSpinnerMap: HashMap<Int, Int> = HashMap()

    private val otNamesList = mutableMapOf<Int, String>()
    private val hashOtNameSpinnerMap: HashMap<Int, Int> = HashMap()

    private val otTypeList = mutableMapOf<Int, String>()
    private val hashOtTypeSpinnerMap: HashMap<Int, Int> = HashMap()

    private val priorityList = mutableMapOf<Int, String>()
    private val hashPrioritySpinnerMap: HashMap<Int, Int> = HashMap()

    private val gradeList = mutableMapOf<Int, String>()
    private val hashGradeSpinnerMap: HashMap<Int, Int> = HashMap()

    private var patientUuid: Int = 0
    private var encounter_uuid: Int = 0
    private var doctor_en_uuid: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                R.layout.dialog_otschedule_add_fragment,
                container,
                false
            )
        viewModel = AddSurgeryViewModelFactory(
            requireActivity().application
        )
            .create(AddSurgeryViewModel::class.java)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        utils = Utils(requireContext())
        requireActivity().window
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilityId = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        departId = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
//        doctorId = appPreferences?.getInt(AppConstants.ENCOUNTER_DOCTOR_UUID)
        patientUuid = appPreferences?.getInt(AppConstants.PATIENT_UUID) ?: 0
        encounterType = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)

        initViews()
        listeners()

        return binding?.root
    }

    private fun initViews() {
        if (isEdit) {
            binding?.tvHeading?.text = getString(R.string.ot_edit_surgery)
            binding?.pinSearch?.setText(otScheduleToCalendarresponseContent?.patient_uhid ?: "")
            binding?.pinSearch?.isFocusable = false
            binding?.pinSearch?.isClickable = false
            binding?.detailsTextView?.text =
                "${otScheduleToCalendarresponseContent?.patient_first_name ?: ""} " +
                        "${otScheduleToCalendarresponseContent?.patient_last_name ?: ""} / " +
                        "${otScheduleToCalendarresponseContent?.patient_age ?: "0"} Year(s)"
            binding?.surgeryNameSearch?.setText(
                otScheduleToCalendarresponseContent?.procedures_name ?: ""
            )
            binding?.fromDate?.setText(otScheduleToCalendarresponseContent?.os_start_date ?: "")
            binding?.toDate?.setText(otScheduleToCalendarresponseContent?.os_end_date ?: "")

            viewModel?.getLessionSpinner(facilityId, lessionRetrofitCallback)
            viewModel?.getSpinnerSide(facilityId, sideRetrofitCallback)
            viewModel?.getAnaeesthesia(facilityId, AnaeesthesiaRetrofitCallback)
            viewModel?.getPositions(facilityId, positionRetrofitCallback)


            binding?.llnEditText?.setText(otScheduleToCalendarresponseContent?.os_incision ?: "")
            binding?.approachEditText?.setText(
                otScheduleToCalendarresponseContent?.os_approach ?: ""
            )
//            binding?.spinnerOTtype?.setText(otScheduleToCalendarresponseContent?.ot_type_name ?: "")
//            binding?.spinnerOTName?.setText(
//                otScheduleToCalendarresponseContent?.ot_theatre_name ?: ""
//            )
            binding?.diagnosisSearch?.setText(
                otScheduleToCalendarresponseContent?.diagnosis_name ?: ""
            )
            binding?.chiefSearch?.setText(
                "${otScheduleToCalendarresponseContent?.cheif_salutation_name ?: ""} ${otScheduleToCalendarresponseContent?.cheif_first_name ?: ""}"
            )
            binding?.surgenSearch?.setText(
                "${otScheduleToCalendarresponseContent?.surgeon_salutation_name ?: ""} ${otScheduleToCalendarresponseContent?.surgeon_first_name ?: ""}"
            )
            binding?.assistantSurgenSearch?.setText(
                "${otScheduleToCalendarresponseContent?.ass_surgeon_salutation_name ?: ""} ${otScheduleToCalendarresponseContent?.ass_surgeon_first_name ?: ""}"
            )
            binding?.assistantNurseSearch?.setText(
                "${otScheduleToCalendarresponseContent?.assistant_nurse_salutation_name ?: ""} ${otScheduleToCalendarresponseContent?.assistant_nurse_first_name ?: ""}"
            )
//            binding?.spinnerAnth?.setText(
//                "${otScheduleToCalendarresponseContent?.anaesthetist_salutation_name ?: ""} ${otScheduleToCalendarresponseContent?.anaesthetist_first_name ?: ""}"
//            )
//            binding?.etForceBook?.setText(
//                otScheduleToCalendarresponseContent?.os_is_force_booking?.toString() ?: ""
//            )
//            binding?.spinnerPriority?.setText(
//                otScheduleToCalendarresponseContent?.priority_name ?: ""
//            )
//            binding?.spinnerGrade?.setText(otScheduleToCalendarresponseContent?.grade_name ?: "")
            binding?.commentsSearch?.setText(otScheduleToCalendarresponseContent?.os_comments ?: "")
        } else {
            val date = Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
            val scheduleDate = sdf.format(date.time)
            //Log.e("curDat", scheduleDate + "Z")
            startDate = scheduleDate + "Z"
            endDate = scheduleDate + "Z"

            val sdf1 = SimpleDateFormat("dd-MM-yyyy HH:mm")
            val curDate = sdf1.format(date.time)
            binding?.fromDate!!.setText(curDate)
            binding?.toDate!!.setText(curDate)
        }

        if (!isEdit) {
            viewModel?.getAnesthetist(facilityId, anesthetistRetrofitCallback)
            viewModel?.getLessionSpinner(facilityId, lessionRetrofitCallback)
            viewModel?.getSpinnerSide(facilityId, sideRetrofitCallback)
            viewModel?.getAnaeesthesia(facilityId, AnaeesthesiaRetrofitCallback)
            viewModel?.getPositions(facilityId, positionRetrofitCallback)
            viewModel?.getOtName(facilityId, getOtNameRetrofitCallback)
            viewModel?.getOtType(facilityId, getOtTypeRetrofitCallback)
            viewModel?.getPriority(facilityId, getPriorityRetrofitCallback)
            viewModel?.getGrade(facilityId, getGradeRetrofitCallback)
            viewModel?.getPatientById(patientUuid, getDispenseListRespCallback)

            /*
            binding?.spinnerAnth!!.setOnTouchListener { v, event ->
                viewModel?.getAnesthetist(facilityId, anesthetistRetrofitCallback)
                false
            }
            binding?.spinnerLession!!.setOnTouchListener { v, event ->
                viewModel?.getLessionSpinner(facilityId, lessionRetrofitCallback)
                false
            }

            binding?.spinnerSide!!.setOnTouchListener { v, event ->
                viewModel?.getSpinnerSide(facilityId, sideRetrofitCallback)
                false
            }
            binding?.spinnerAnaes!!.setOnTouchListener { v, event ->
                viewModel?.getAnaeesthesia(facilityId, AnaeesthesiaRetrofitCallback)
                false
            }

            binding?.spinnerPosition!!.setOnTouchListener { v, event ->
                viewModel?.getPositions(facilityId, positionRetrofitCallback)
                false
            }

            binding?.spinnerOTName?.setOnTouchListener { v, event ->
                viewModel?.getOtName(facilityId, getOtNameRetrofitCallback)
                false
            }

            binding?.spinnerOTtype?.setOnTouchListener { v, event ->
                viewModel?.getOtType(facilityId, getOtTypeRetrofitCallback)
                false
            }

            binding?.spinnerPriority?.setOnTouchListener { v, event ->
                viewModel?.getPriority(facilityId, getPriorityRetrofitCallback)
                false
            }

            //getGrade
            binding?.spinnerGrade?.setOnTouchListener { v, event ->
                viewModel?.getGrade(facilityId, getGradeRetrofitCallback)
                false
            }
*/

        }
    }

    private fun listeners() {
        binding?.closeImageView!!.setOnClickListener {
            dialog!!.dismiss()
        }

        binding?.switchCheck!!.setOnCheckedChangeListener { _, isChecked ->

            switchChecks = isChecked
        }

        binding?.fromDate!!.setOnClickListener {

            startDate = ""
            val c: Calendar = Calendar.getInstance()
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this.requireActivity(),
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

                    val c = Calendar.getInstance()
                    mHour = c[Calendar.HOUR_OF_DAY]
                    mMinute = c[Calendar.MINUTE]
                    mSecond = c[Calendar.SECOND]

                    val timePickerDialog = TimePickerDialog(
                        this.activity,
                        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->

                            binding?.fromDate?.setText(
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


                            startDate = year.toString() + "-" + String.format(
                                "%02d",
                                monthOfYear + 1
                            ) + "-" + String.format("%02d", dayOfMonth) + "T" +
                                    String.format(
                                        "%02d", hourOfDay
                                    ) + ":" + String.format(
                                "%02d", minute
                            ) + ":" + String.format("%02d", mSecond) + ".000Z"
                        },
                        mHour!!,
                        mMinute!!,
                        false
                    )
                    timePickerDialog.show()
                }, mYear!!, mMonth!!, mDay!!
            )
            datePickerDialog.show()
        }
        binding?.toDate!!.setOnClickListener {

            endDate = ""
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

                            binding?.toDate?.setText(
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

                            endDate = year.toString() + "-" + String.format(
                                "%02d",
                                monthOfYear + 1
                            ) + "-" + String.format("%02d", dayOfMonth) + "T" +
                                    String.format(
                                        "%02d", hourOfDay
                                    ) + ":" + String.format(
                                "%02d", minute
                            ) + ":" + String.format("%02d", mSecond) + ".000Z"
                        },
                        mHour!!,
                        mMinute!!,
                        false
                    )
                    timePickerDialog.show()
                }, mYear!!, mMonth!!, mDay!!
            )
            datePickerDialog.show()
        }



        if (!isEdit) {
            viewModel?.getSurgeryPin(facilityId, getPinRetrofitCallback)
            binding!!.pinSearch.addTextChangedListener(object : TextWatcher {

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable) {
                    val datasize = s.trim().length
                    if (datasize == 0) {
                        auto_pin_id = 0
                        binding!!.pinSearch.error = "Please select one department"
                    } else {
                        if (auto_pin_id == 0) {
                            binding!!.pinSearch.error = "Please select one department"
                        } else {
                            binding!!.pinSearch.error = null
                        }
                    }
                }
            })
        }

        viewModel?.getCheif(facilityId, getCheifRetrofitCallback)
        binding!!.chiefSearch.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                val datasize = s.trim().length
                if (datasize == 0) {
                    autoCheifpinid = 0
                    binding!!.chiefSearch.error = "Please select one department"
                } else {
                    if (autoCheifpinid == 0) {
                        binding!!.chiefSearch.error = "Please select one department"
                    } else {
                        binding!!.chiefSearch.error = null
                    }
                }

                if (s.length > 2 && s.length < 5) {
                    binding?.chiefSearch?.hideKeyboard()
                }
            }
        })

        viewModel!!.getSurgeon(facilityId, getSurgeonRetrofitCallback)
        binding!!.surgenSearch.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                val datasize = s.trim().length
                if (datasize == 0) {
                    autoSurgeonid = 0
                    binding!!.surgenSearch.error = "Please select one department"
                } else {
                    if (autoSurgeonid == 0) {
                        binding!!.surgenSearch.error = "Please select one department"
                    } else {
                        binding!!.surgenSearch.error = null
                    }
                }
                if (s.length > 2 && s.length < 5) {
                    binding?.surgenSearch?.hideKeyboard()
                }
            }
        })

        binding!!.assistantSurgenSearch.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                val datasize = s.trim().length
                if (datasize == 0) {
                    autoAssistSurgeonid = 0
                    binding!!.assistantSurgenSearch.error = "Please select one department"
                } else {
                    if (autoAssistSurgeonid == 0) {
                        binding!!.assistantSurgenSearch.error = "Please select one department"
                    } else {
                        binding!!.assistantSurgenSearch.error = null
                    }
                }
                if (s.length > 2 && s.length < 5) {
                    binding?.assistantSurgenSearch?.hideKeyboard()
                }
            }
        })

        viewModel?.getNurse(facilityId, getNurseRetrofitCallback)
        binding!!.assistantNurseSearch.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                val datasize = s.trim().length
                if (datasize == 0) {
                    autoAssistNurseid = 0
                    binding!!.assistantNurseSearch.error = "Please select one department"
                } else {
                    if (autoAssistNurseid == 0) {
                        binding!!.assistantNurseSearch.error = "Please select one department"
                    } else {
                        binding!!.assistantNurseSearch.error = null
                    }
                }

                if (s.length > 2 && s.length < 5) {
                    binding?.assistantNurseSearch?.hideKeyboard()
                }
            }
        })

        binding?.spinnerAnth!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectedAnesthetistId =
                        anesthetistList.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(pos).toString()
                    selectedAnesthetistId =
                        anesthetistList.filterValues { it == itemValue }.keys.toList()[0]
                    Log.e("anetSpinn", "" + selectedAnesthetistId)
                }
            }

        binding?.spinnerLession!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectedLessionId =
                        lessionList.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(pos).toString()
                    selectedLessionId =
                        lessionList.filterValues { it == itemValue }.keys.toList()[0]
                    Log.e("lessionSpinn", "" + selectedLessionId)
                }
            }

        binding?.spinnerSide!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectedSideId = sideList.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(pos).toString()
                    selectedSideId = sideList.filterValues { it == itemValue }.keys.toList()[0]
                    Log.e("SideSpinn", "" + selectedSideId)
                }
            }

        binding?.spinnerAnaes!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectedAnaeesthesiaId =
                        AnaeesthesiaList.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(pos).toString()
                    selectedAnaeesthesiaId =
                        AnaeesthesiaList.filterValues { it == itemValue }.keys.toList()[0]
                    Log.e("AneathesiSpinn", "" + selectedAnaeesthesiaId)
                }
            }

        binding?.spinnerPosition!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectedPositionsId =
                        positionList.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(pos).toString()
                    selectedPositionsId =
                        positionList.filterValues { it == itemValue }.keys.toList()[0]
                    Log.e("PositioniSpinn", "" + selectedPositionsId)
                }
            }
        binding?.spinnerOTName!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectedOtNameId =
                        otNamesList.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(pos).toString()
                    selectedOtNameId =
                        otNamesList.filterValues { it == itemValue }.keys.toList()[0]
                    Log.e("OtName", "" + selectedOtNameId)

                }
            }

        binding?.spinnerOTtype!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectedOtTypeId =
                        otTypeList.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(pos).toString()
                    selectedOtTypeId =
                        otTypeList.filterValues { it == itemValue }.keys.toList()[0]
                    Log.e("OtType", "" + selectedOtTypeId)

                }
            }

        binding?.spinnerPriority!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectedPriorityId =
                        priorityList.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(pos).toString()
                    selectedPriorityId =
                        priorityList.filterValues { it == itemValue }.keys.toList()[0]
                    Log.e("Priority", "" + selectedPriorityId)

                }
            }

        binding?.spinnerGrade!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectedGradeId =
                        gradeList.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(pos).toString()
                    selectedGradeId =
                        gradeList.filterValues { it == itemValue }.keys.toList()[0]
                    Log.e("Grade", "" + selectedGradeId)

                }
            }


        binding?.surgeryNameSearch?.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 2 && s.length < 5) {

                    viewModel?.getSurgeryName(
                        facilityId,
                        s.toString(),
                        departId,
                        surgerySearchRetrofitCallback
                    )

                    binding?.surgeryNameSearch?.hideKeyboard()
                }
            }
        })

        //getDiagnosis
        binding?.diagnosisSearch?.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 2 && s.length < 5) {

                    viewModel?.getDiagnosis(
                        facilityId,
                        s.toString(),
                        diagnosisSearchRetrofitCallback
                    )

                    binding?.diagnosisSearch?.hideKeyboard()
                }
            }
        })

        binding?.saveCardView!!.setOnClickListener {
            getEncounter()
        }

        binding?.clearCardView?.setOnClickListener {
            binding?.pinSearch?.setText("")
            binding?.surgeryNameSearch?.setText("")
//            binding?.fromDate?.setText("")
//            binding?.toDate?.setText("")
            binding?.spinnerLession?.setSelection(0)
            binding?.spinnerSide?.setSelection(0)
            binding?.spinnerAnaes?.setSelection(0)
            binding?.spinnerPosition?.setSelection(0)
            binding?.llnEditText?.setText("")
            binding?.approachEditText?.setText("")
            binding?.spinnerOTtype?.setSelection(0)
            binding?.spinnerOTName?.setSelection(0)
            binding?.diagnosisSearch?.setText("")
            binding?.chiefSearch?.setText("")
            binding?.surgenSearch?.setText("")
            binding?.assistantSurgenSearch?.setText("")
            binding?.assistantNurseSearch?.setText("")
            binding?.spinnerAnth?.setSelection(0)
            binding?.spinnerPriority?.setSelection(0)
            binding?.spinnerGrade?.setSelection(0)
            binding?.commentsSearch?.setText("")


            val date = Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
            val scheduleDate = sdf.format(date.time)
            //Log.e("curDat", scheduleDate + "Z")
            startDate = scheduleDate + "Z"
            endDate = scheduleDate + "Z"

            val sdf1 = SimpleDateFormat("dd-MM-yyyy HH:mm")
            val curDate = sdf1.format(date.time)
            binding?.fromDate!!.setText(curDate)
            binding?.toDate!!.setText(curDate)
        }
    }

    private fun selectSpinnerItemByValue(spinner: Spinner, value: String) {
        val adapter = spinner.adapter
        for (position in 0 until adapter.count) {
            if (adapter.getItem(position) == value) {
                spinner.setSelection(position)
                return
            }
        }
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun getEncounter() {
        viewModel?.getEncounter(
            facilityId!!,
            patientUuid,
            departId!!,
            encounter_uuid,
            getOpNotesEncounterByDocAndPatientIdRespCallback
        )
    }

    private val getOpNotesEncounterByDocAndPatientIdRespCallback =
        object : RetrofitCallback<GetOpNotesEncounterByDocAndPatientIdResp> {
            override fun onSuccessfulResponse(responseBody: Response<GetOpNotesEncounterByDocAndPatientIdResp>?) {
                responseBody?.body()?.let { getOpNotesEncounterByDocAndPatientIdResp ->
                    if (getOpNotesEncounterByDocAndPatientIdResp.responseContents?.isNotEmpty() == true) {
                        getOpNotesEncounterByDocAndPatientIdResp.responseContents?.get(0)
                            .let { responseContentXX ->
                                doctor_en_uuid =
                                    responseContentXX?.encounter_doctors?.get(0)?.uuid ?: 0
                                encounter_uuid = responseContentXX?.uuid ?: 0
                                patientUuid = responseContentXX?.patient_uuid ?: 0
                                appPreferences?.saveInt(
                                    AppConstants.ENCOUNTER_DOCTOR_UUID,
                                    doctor_en_uuid
                                )
                                appPreferences?.saveInt(AppConstants.ENCOUNTER_UUID, encounter_uuid)

                                val userDetailsRoomRepository =
                                    UserDetailsRoomRepository(context?.applicationContext as Application)
                                val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
                                val doctorId = userDataStoreBean?.uuid
                                if (isEdit) {
                                    if (otScheduleToCalendarresponseContent?.os_uuid == doctorId) {
//                                    if (true) {
                                        trackOtScheduleDelete(utils?.getEncounterType())
                                        updateSurgery()
                                    } else {
                                        utils?.showToast(
                                            R.color.negativeToast,
                                            binding?.mainLayout!!,
                                            "Not Accessible"
                                        )
                                    }
                                } else {
                                    saveSurgury()
                                }
                            }
                    } else {
                        viewModel?.createEncounter(
                            patientUuid,
                            encounterType!!,
                            createEncounterRetrofitCallback
                        )
                    }
                }
            }

            override fun onBadRequest(errorBody: Response<GetOpNotesEncounterByDocAndPatientIdResp>?) {
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
                viewModel!!.progressBar.value = 8
            }
        }

    private val createEncounterRetrofitCallback =
        object : RetrofitCallback<CreateEncounterResponseModel> {
            override fun onSuccessfulResponse(response: Response<CreateEncounterResponseModel>) {


                doctor_en_uuid = response.body()?.responseContents?.encounterDoctor?.uuid!!.toInt()
                encounter_uuid = response.body()?.responseContents?.encounter?.uuid!!.toInt()
                patientUuid =
                    response.body()?.responseContents?.encounterDoctor?.patient_uuid!!.toInt()

                val userDetailsRoomRepository =
                    UserDetailsRoomRepository(context?.applicationContext as Application)
                val userDataStoreBean = userDetailsRoomRepository.getUserDetails()
                val doctorId = userDataStoreBean?.uuid
                if (isEdit) {
                    if (otScheduleToCalendarresponseContent?.os_uuid == doctorId) {
//                        if (true) {
                        trackOtScheduleDelete(utils?.getEncounterType())
                        updateSurgery()
                    } else {
                        utils?.showToast(
                            R.color.negativeToast,
                            binding?.mainLayout!!,
                            "Not Accessible"
                        )
                    }
                } else {
                    saveSurgury()
                }
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
                viewModel!!.progressBar.value = 8
            }
        }

    val getPinRetrofitCallback = object : RetrofitCallback<GetSurgeryPinResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<GetSurgeryPinResponseModel>?) {
            Log.i("", "" + responseBody?.body()?.responseContents)

            if (responseBody!!.body()?.responseContents?.isNotEmpty()!!) {
                setAdapter(responseBody.body()?.responseContents as List<GetSurgeryPinresponseContent>)
            }

        }

        override fun onBadRequest(errorBody: Response<GetSurgeryPinResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: GetSurgeryPinResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    GetSurgeryPinResponseModel::class.java
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
            viewModel!!.progressBar.value = 8
        }
    }

    fun setAdapter(
        responseContents: List<GetSurgeryPinresponseContent>
    ) {
        val responseContentAdapter = SurgeryPinSearchResultAdapter(
            requireContext(),
            R.layout.row_chief_complaint_search_result,
            responseContents
        )
        binding!!.pinSearch.threshold = 1
        binding!!.pinSearch.setAdapter(responseContentAdapter)
        //binding!!.pinSearch.setText(responseContents[0].uhid)
        //auto_pin_id=responseContents[0].uuid

        binding!!.pinSearch.error = null


        binding!!.pinSearch.setOnItemClickListener { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as GetSurgeryPinresponseContent?

            binding!!.pinSearch.setText(selectedPoi?.uhid)

            var gender: String = ""
            if (selectedPoi?.gender_uuid == 1) {
                gender = "Male"
            } else if (selectedPoi?.gender_uuid == 2) {
                gender = "Female"
            } else if (selectedPoi?.gender_uuid == 3) {
                gender = "Transgender"
            }
            binding!!.detailsTextView.text =
                "Mr/Miss ." + selectedPoi?.first_name + "/" + selectedPoi?.age + "Year(s)/" + gender + "/" +
                        selectedPoi!!.patient_detail!!.mobile

            binding!!.pinSearch.error = null

            auto_pin_id = selectedPoi.uuid

        }
    }

    val getCheifRetrofitCallback = object : RetrofitCallback<CheifResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<CheifResponseModel>?) {
            //Log.i("", "" + responseBody?.body()?.responseContents);

            if (responseBody!!.body()?.responseContents?.isNotEmpty()!!) {
                setCheifAdapter(responseBody.body()?.responseContents as List<CheifresponseContent>)
            }

        }

        override fun onBadRequest(errorBody: Response<CheifResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: CheifResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    CheifResponseModel::class.java
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
            viewModel!!.progressBar.value = 8
        }
    }

    fun setCheifAdapter(
        responseContents: List<CheifresponseContent>
    ) {
        val responseContentAdapter = CheifSearchResultAdapter(
            requireContext(),
            R.layout.row_chief_complaint_search_result,
            responseContents
        )
        binding!!.chiefSearch.threshold = 1
        binding!!.chiefSearch.setAdapter(responseContentAdapter)
        //binding!!.pinSearch.setText(responseContents[0].uhid)
        //auto_pin_id=responseContents[0].uuid

        binding!!.chiefSearch.error = null


        binding!!.chiefSearch.setOnItemClickListener { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as CheifresponseContent?

            binding!!.chiefSearch.setText(selectedPoi?.first_name)
            binding!!.chiefSearch.error = null
            autoCheifpinid = selectedPoi!!.uuid

        }
    }


    val getSurgeonRetrofitCallback = object : RetrofitCallback<SurgeonResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<SurgeonResponseModel>?) {

            if (responseBody!!.body()?.responseContents?.isNotEmpty()!!) {
                setSurgeonAdapter(responseBody.body()?.responseContents as List<SurgeonresponseContent>)
                setAssistantSurgeonAdapter(responseBody.body()?.responseContents as List<SurgeonresponseContent>)
            }

        }

        override fun onBadRequest(errorBody: Response<SurgeonResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: SurgeonResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    SurgeonResponseModel::class.java
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
            viewModel!!.progressBar.value = 8
        }
    }

    fun setSurgeonAdapter(
        responseContents: List<SurgeonresponseContent>
    ) {
        val responseContentAdapter = SurgeonSearchResultAdapter(
            requireContext(),
            R.layout.row_chief_complaint_search_result,
            responseContents
        )
        binding!!.surgenSearch.threshold = 1
        binding!!.surgenSearch.setAdapter(responseContentAdapter)
        binding!!.surgenSearch.error = null
        binding!!.surgenSearch.setOnItemClickListener { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as SurgeonresponseContent?
            binding!!.surgenSearch.setText(selectedPoi?.first_name)
            binding!!.surgenSearch.error = null
            autoSurgeonid = selectedPoi!!.uuid
        }
    }

    fun setAssistantSurgeonAdapter(
        responseContents: List<SurgeonresponseContent>
    ) {
        val responseContentAdapter = SurgeonSearchResultAdapter(
            requireContext(),
            R.layout.row_chief_complaint_search_result,
            responseContents
        )
        binding!!.assistantSurgenSearch.threshold = 1
        binding!!.assistantSurgenSearch.setAdapter(responseContentAdapter)
        binding!!.assistantSurgenSearch.error = null
        binding!!.assistantSurgenSearch.setOnItemClickListener { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as SurgeonresponseContent?
            binding!!.assistantSurgenSearch.setText(selectedPoi?.first_name)
            binding!!.assistantSurgenSearch.error = null
            autoAssistSurgeonid = selectedPoi!!.uuid
        }
    }

    //getNurseRetrofitCallback
    val getNurseRetrofitCallback = object : RetrofitCallback<NurseAssistantResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<NurseAssistantResponseModel>?) {

            if (responseBody!!.body()?.responseContents?.isNotEmpty()!!) {
                setNurseAdapter(responseBody.body()?.responseContents as List<NurseAssistantresponseContent>)
            }

        }

        override fun onBadRequest(errorBody: Response<NurseAssistantResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: NurseAssistantResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    NurseAssistantResponseModel::class.java
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
            viewModel!!.progressBar.value = 8
        }
    }

    fun setNurseAdapter(
        responseContents: List<NurseAssistantresponseContent>
    ) {
        val responseContentAdapter = NurseSearchResultAdapter(
            requireContext(),
            R.layout.row_chief_complaint_search_result,
            responseContents
        )
        binding!!.assistantNurseSearch.threshold = 1
        binding!!.assistantNurseSearch.setAdapter(responseContentAdapter)
        //binding!!.pinSearch.setText(responseContents[0].uhid)
        //auto_pin_id=responseContents[0].uuid

        binding!!.assistantNurseSearch.error = null


        binding!!.assistantNurseSearch.setOnItemClickListener { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as NurseAssistantresponseContent?
            binding!!.assistantNurseSearch.setText(selectedPoi?.first_name)
            binding!!.assistantNurseSearch.error = null
            autoAssistNurseid = selectedPoi!!.uuid
        }
    }

    val anesthetistRetrofitCallback = object : RetrofitCallback<AnesthetistSpinnerResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<AnesthetistSpinnerResponseModel>?) {
            Log.i("", "" + responseBody?.body()?.responseContents)

            if (responseBody!!.body()?.responseContents?.isNotEmpty()!!) {
                setAnesthetistAdapter(responseBody.body()?.responseContents as List<AnesthetistSpinnerresponseContent>)

            }

        }

        override fun onBadRequest(errorBody: Response<AnesthetistSpinnerResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: AnesthetistSpinnerResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    AnesthetistSpinnerResponseModel::class.java
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
            viewModel!!.progressBar.value = 8
        }
    }

    fun setAnesthetistAdapter(responseContents: List<AnesthetistSpinnerresponseContent?>?) {
        anesthetistList[0] = "Select Anaesthesia"
        anesthetistList.putAll(
            responseContents?.map { it?.uuid!! to it.first_name!! }!!.toMap().toMutableMap()
        )
        hashAnesthetistSpinnerMap.clear()

        for (i in responseContents.indices) {

            hashAnesthetistSpinnerMap[responseContents[i]!!.uuid!!] = i
        }

        val adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            anesthetistList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spinnerAnth!!.adapter = adapter
    }

    val lessionRetrofitCallback = object : RetrofitCallback<LessionSpinnerResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<LessionSpinnerResponseModel>?) {
            Log.i("", "" + responseBody?.body()?.responseContents)

            if (responseBody!!.body()?.responseContents?.isNotEmpty()!!) {
                setLessionAdapter(responseBody.body()?.responseContents as List<LessionSpinnerresponseContent>)

            }

        }

        override fun onBadRequest(errorBody: Response<LessionSpinnerResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: LessionSpinnerResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    LessionSpinnerResponseModel::class.java
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
            viewModel!!.progressBar.value = 8
        }
    }

    fun setLessionAdapter(responseContents: List<LessionSpinnerresponseContent?>?) {
        lessionList[0] = "Select Lession"
        lessionList.putAll(
            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()
        )

        hashLessionSpinnerMap.clear()

        for (i in responseContents.indices) {

            hashLessionSpinnerMap[responseContents[i]!!.uuid!!] = i
        }

        val adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            lessionList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spinnerLession!!.adapter = adapter

        if (isEdit) {
            selectSpinnerItemByValue(
                binding?.spinnerLession!!,
                otScheduleToCalendarresponseContent?.lesion_name ?: ""
            )
        }
    }

    val sideRetrofitCallback = object : RetrofitCallback<SideSpinnerResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<SideSpinnerResponseModel>?) {
            Log.i("", "" + responseBody?.body()?.responseContents)

            if (responseBody!!.body()?.responseContents?.isNotEmpty()!!) {
                setSideAdapter(responseBody.body()?.responseContents as List<SideSpinnerresponseContent>)

            }

        }

        override fun onBadRequest(errorBody: Response<SideSpinnerResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: SideSpinnerResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    SideSpinnerResponseModel::class.java
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
            viewModel!!.progressBar.value = 8
        }
    }

    fun setSideAdapter(responseContents: List<SideSpinnerresponseContent?>?) {
        sideList[0] = "Select Side"
        sideList.putAll(responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap())

        hashWideSpinnerMap.clear()

        for (i in responseContents.indices) {

            hashWideSpinnerMap[responseContents[i]!!.uuid!!] = i
        }

        val adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            sideList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spinnerSide!!.adapter = adapter

        if (isEdit) {
            selectSpinnerItemByValue(
                binding?.spinnerSide!!,
                otScheduleToCalendarresponseContent?.body_side_name ?: ""
            )
        }
    }

    val AnaeesthesiaRetrofitCallback = object : RetrofitCallback<AnaeesthesiaSpinnerResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<AnaeesthesiaSpinnerResponseModel>?) {
            Log.i("", "" + responseBody?.body()?.responseContents)

            if (responseBody!!.body()?.responseContents?.isNotEmpty()!!) {
                setAnaeesthesiaAdapter(responseBody.body()?.responseContents as List<AnaesthesiaresponseContent>)

            }

        }

        override fun onBadRequest(errorBody: Response<AnaeesthesiaSpinnerResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: AnaeesthesiaSpinnerResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    AnaeesthesiaSpinnerResponseModel::class.java
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
            viewModel!!.progressBar.value = 8
        }
    }

    fun setAnaeesthesiaAdapter(responseContents: List<AnaesthesiaresponseContent?>?) {
        AnaeesthesiaList[0] = "Select Anaesthesia"
        AnaeesthesiaList.putAll(
            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()
        )

        hashAnaeesthesiaSpinnerMap.clear()

        for (i in responseContents.indices) {

            hashAnaeesthesiaSpinnerMap[responseContents[i]!!.uuid!!] = i
        }

        val adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            AnaeesthesiaList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spinnerAnaes!!.adapter = adapter

        if (isEdit) {
            selectSpinnerItemByValue(
                binding?.spinnerAnaes!!,
                otScheduleToCalendarresponseContent?.anesthesia_type_name ?: ""
            )
        }
    }

    //positionRetrofitCallback

    val positionRetrofitCallback = object : RetrofitCallback<PositionResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<PositionResponseModel>?) {
            Log.i("", "" + responseBody?.body()?.responseContents)

            if (responseBody!!.body()?.responseContents?.isNotEmpty()!!) {
                setPositionAdapter(responseBody.body()?.responseContents as List<PositionresponseContent>)

            }

        }

        override fun onBadRequest(errorBody: Response<PositionResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: PositionResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    PositionResponseModel::class.java
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
            viewModel!!.progressBar.value = 8
        }
    }

    fun setPositionAdapter(responseContents: List<PositionresponseContent?>?) {
        positionList[0] = "Select position"
        positionList.putAll(
            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()
        )

        hashPositionSpinnerMap.clear()

        for (i in responseContents.indices) {

            hashPositionSpinnerMap[responseContents[i]!!.uuid!!] = i
        }

        val adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            positionList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spinnerPosition!!.adapter = adapter

        if (isEdit) {
            selectSpinnerItemByValue(
                binding?.spinnerPosition!!,
                otScheduleToCalendarresponseContent?.postions_name ?: ""
            )
        }
    }


    val getOtNameRetrofitCallback = object : RetrofitCallback<OtNameSpinnerResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<OtNameSpinnerResponseModel>?) {
            if (responseBody?.isSuccessful == true) {

                setOtNameValue(responseBody.body()?.responseContents!!)

            }
        }

        override fun onBadRequest(errorBody: Response<OtNameSpinnerResponseModel>?) {
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
            viewModel!!.progressBar.value = 8
        }
    }


    fun setOtNameValue(responseContents: List<OtNameSpinnerresponseContent?>?) {
        otNamesList[0] = "Select OT Name"
        otNamesList.putAll(
            responseContents?.map { it?.om_uuid!! to it.om_name!! }!!.toMap().toMutableMap()
        )

        hashOtNameSpinnerMap.clear()

        for (i in responseContents.indices) {

            hashOtNameSpinnerMap[responseContents[i]!!.om_uuid!!] = i
        }
        val adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            otNamesList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spinnerOTName!!.adapter = adapter

    }

    val getOtTypeRetrofitCallback = object : RetrofitCallback<OtTypeResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<OtTypeResponseModel>?) {
            if (responseBody?.isSuccessful == true) {

                setOtTypeValue(responseBody.body()?.responseContents!!)

            }
        }

        override fun onBadRequest(errorBody: Response<OtTypeResponseModel>?) {
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
            viewModel!!.progressBar.value = 8
        }
    }


    fun setOtTypeValue(responseContents: List<OtTyperesponseContent?>?) {
        otTypeList[0] = "Select OT Type"
        otTypeList.putAll(
            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()
        )

        hashOtTypeSpinnerMap.clear()

        for (i in responseContents.indices) {

            hashOtTypeSpinnerMap[responseContents[i]!!.uuid!!] = i
        }
        val adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            otTypeList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spinnerOTtype!!.adapter = adapter

    }

    val getPriorityRetrofitCallback = object : RetrofitCallback<PriorityResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<PriorityResponseModel>?) {
            if (responseBody?.isSuccessful == true) {

                setPriority(responseBody.body()?.responseContents!!)

            }
        }

        override fun onBadRequest(errorBody: Response<PriorityResponseModel>?) {
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
            viewModel!!.progressBar.value = 8
        }
    }


    fun setPriority(responseContents: List<PriorityresponseContent?>?) {
        priorityList[0] = "Select priority"
        priorityList.putAll(
            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()
        )
        hashPrioritySpinnerMap.clear()

        for (i in responseContents.indices) {

            hashPrioritySpinnerMap[responseContents[i]!!.uuid!!] = i
        }
        val adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            priorityList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spinnerPriority!!.adapter = adapter
    }

    val getGradeRetrofitCallback = object : RetrofitCallback<GradeResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<GradeResponseModel>?) {
            if (responseBody?.isSuccessful == true) {

                setGrade(responseBody.body()?.responseContents!!)

            }
        }

        override fun onBadRequest(errorBody: Response<GradeResponseModel>?) {
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
            viewModel!!.progressBar.value = 8
        }
    }


    fun setGrade(responseContents: List<GraderesponseContent?>?) {
        gradeList[0] = "Select grade"
        gradeList.putAll(responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap())
        hashGradeSpinnerMap.clear()

        for (i in responseContents.indices) {

            hashGradeSpinnerMap[responseContents[i]!!.uuid!!] = i
        }
        val adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            gradeList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spinnerGrade!!.adapter = adapter
    }

    val surgerySearchRetrofitCallback = object : RetrofitCallback<SurgerySearchNameResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<SurgerySearchNameResponseModel>?) {
            //Log.i("", "" + responseBody?.body()?.responseContents);

            if (responseBody!!.body()?.responseContents?.isNotEmpty()!!) {
                setsurgNameAdapter(responseBody.body()?.responseContents as List<SurgerySearchNameresponseContent>)
            }

        }

        override fun onBadRequest(errorBody: Response<SurgerySearchNameResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: SurgerySearchNameResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    SurgerySearchNameResponseModel::class.java
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
            viewModel!!.progressBar.value = 8
        }
    }


    fun setsurgNameAdapter(
        responseContents: List<SurgerySearchNameresponseContent>
    ) {
        val responseContentAdapter = SurguryNameSearchResultAdapter(
            requireContext(),
            R.layout.row_chief_complaint_search_result,
            responseContents
        )
        binding!!.surgeryNameSearch.threshold = 1
        binding!!.surgeryNameSearch.setAdapter(responseContentAdapter)

        binding!!.surgeryNameSearch.error = null


        binding!!.surgeryNameSearch.setOnItemClickListener { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as SurgerySearchNameresponseContent?

            binding!!.surgeryNameSearch.setText(selectedPoi?.p_name)
            binding!!.surgeryNameSearch.error = null
            autoSearchNameId = selectedPoi!!.p_uuid
        }
    }

    val diagnosisSearchRetrofitCallback =
        object : RetrofitCallback<OtDiagnosisSearchResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<OtDiagnosisSearchResponseModel>?) {
                //Log.i("", "" + responseBody?.body()?.responseContents);

                if (responseBody!!.body()?.responseContents?.isNotEmpty()!!) {
                    setdiagnosisAdapter(responseBody.body()?.responseContents as List<OtDiagnosisresponseContent>)
                }

            }

            override fun onBadRequest(errorBody: Response<OtDiagnosisSearchResponseModel>?) {
                val gson = GsonBuilder().create()
                val responseModel: OtDiagnosisSearchResponseModel
                try {
                    responseModel = gson.fromJson(
                        errorBody?.errorBody()!!.string(),
                        OtDiagnosisSearchResponseModel::class.java
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
                viewModel!!.progressBar.value = 8
            }
        }


    fun setdiagnosisAdapter(
        responseContents: List<OtDiagnosisresponseContent>
    ) {
        val responseContentAdapter = DiagnosisSearchResultAdapter(
            requireContext(),
            R.layout.row_chief_complaint_search_result,
            responseContents
        )
        binding!!.diagnosisSearch.threshold = 1
        binding!!.diagnosisSearch.setAdapter(responseContentAdapter)

        binding!!.diagnosisSearch.error = null


        binding!!.diagnosisSearch.setOnItemClickListener { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as OtDiagnosisresponseContent?

            binding!!.diagnosisSearch.setText(selectedPoi?.name)
            binding!!.diagnosisSearch.error = null
            autoSearchDiagnosisId = selectedPoi!!.uuid
        }
    }


    fun saveSurgury() {

        /*if(autoCheifpinid == 0 || autoSurgeonid == 0 || autoAssistSurgeonid == 0 ||autoAssistNurseid == 0
            ||selectedAnesthetistId == 0 ) {*/

        val jsonBody = JSONObject()
        try {

            val date = Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
            val scheduleDate = sdf.format(date.time)
            Log.e("curDat", scheduleDate + "Z")
            startDate?.let { Log.e("startDate", it) }
            endDate?.let { Log.e("endDate", it) }


            jsonBody.put("facility_uuid", facilityId)
            jsonBody.put("department_uuid", departId)
            jsonBody.put("encounter_uuid", 0)
            jsonBody.put("ward_master_uuid", 0)
            jsonBody.put("ward_bed_mapping_uuid", 0)
            jsonBody.put("ot_type_uuid", selectedOtTypeId)
            jsonBody.put("ot_priority_uuid", selectedPriorityId)
            jsonBody.put("ot_grade_uuid", selectedGradeId)
            jsonBody.put("ot_scheduled_on", scheduleDate)
            jsonBody.put("start_date", startDate)
            jsonBody.put("end_date", endDate)
            jsonBody.put("ot_master_uuid", selectedOtNameId)
            jsonBody.put("procedure_category_uuid", 0)
            jsonBody.put("procedure_type_uuid", 0)
            jsonBody.put("procedure_uuid", autoSearchNameId)
            jsonBody.put("lesion_uuid", selectedLessionId)
            jsonBody.put("body_side_uuid", selectedSideId)
            jsonBody.put("position_uuid", selectedPositionsId)
            jsonBody.put("incision", binding?.llnEditText!!.text.trim().toString())
            jsonBody.put("approach", binding?.approachEditText!!.text.trim().toString())
            jsonBody.put("doctor_uuid", doctor_en_uuid)
            jsonBody.put("cheif_uuid", autoCheifpinid)
            jsonBody.put("surgeon_uuid", autoSurgeonid)
            jsonBody.put("assistant_surgeon_uuid", autoAssistSurgeonid)
            jsonBody.put("assistant_nurse_uuid", autoAssistNurseid)
            jsonBody.put("scrub_nurse_uuid", 0)
            jsonBody.put("anesthesia_type_uuid", selectedAnaeesthesiaId)
            jsonBody.put("anaesthetist_uuid", selectedAnesthetistId)
            jsonBody.put("anaesthetic_nurse_uuid", 0)
            jsonBody.put("ot_schedule_status_uuid", 0)
            jsonBody.put("is_force_booking", switchChecks)
            jsonBody.put("comments", binding?.commentsSearch!!.text.trim().toString())
            jsonBody.put("notes", "")
            jsonBody.put("instructions", "")


        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        Log.e("ReqParams", jsonBody.toString())

        viewModel?.addSurgery(facilityId, body, addNewSurguryRetrofitCallback)

        /*}else{
            Toast.makeText(activity,"Enter mandatory fields!!",Toast.LENGTH_LONG).show()
        }*/
    }

    val addNewSurguryRetrofitCallback = object : RetrofitCallback<AddSurgeryResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<AddSurgeryResponseModel>?) {

            Log.e("addSurguryResponse", responseBody!!.body()?.responseContents.toString())
            Toast.makeText(activity, "OT Scheduled Successfully", Toast.LENGTH_LONG).show()

            dialog!!.dismiss()
            callbacksurgury!!.onRefreshList()

        }

        override fun onBadRequest(errorBody: Response<AddSurgeryResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: AddSurgeryResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    AddSurgeryResponseModel::class.java
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
            viewModel!!.progressBar.value = 8
        }
    }

    private fun updateSurgery() {
        //TODO sri rectify doubts
        val body = ModifyOtScheduleReq(
            Id = otScheduleToCalendarresponseContent?.os_uuid,
            anaesthetic_nurse_uuid = otScheduleToCalendarresponseContent?.os_assistant_nurse_uuid,   //doubt
            anaesthetist_uuid = otScheduleToCalendarresponseContent?.os_anaesthetist_uuid,
            anesthesia_type_uuid = otScheduleToCalendarresponseContent?.os_anesthesia_type_uuid,
            approach = otScheduleToCalendarresponseContent?.os_approach,
            assistant_nurse_uuid = otScheduleToCalendarresponseContent?.os_assistant_nurse_uuid,
            assistant_surgeon_uuid = otScheduleToCalendarresponseContent?.os_assistant_surgeon_uuid,
            body_side_uuid = otScheduleToCalendarresponseContent?.os_body_side_uuid,
            body_site_uuid = otScheduleToCalendarresponseContent?.os_body_side_uuid, //doubt
            cheif_uuid = otScheduleToCalendarresponseContent?.os_cheif_uuid,
            comments = otScheduleToCalendarresponseContent?.os_comments,
            department_uuid = otScheduleToCalendarresponseContent?.os_department_uuid,
            diagnosis_uuid = otScheduleToCalendarresponseContent?.os_diagnosis_uuid,
            doctor_uuid = otScheduleToCalendarresponseContent?.os_uuid,  //doubt
            encounter_uuid = otScheduleToCalendarresponseContent?.os_encounter_uuid,
            end_date = otScheduleToCalendarresponseContent?.os_end_date,
            facility_uuid = otScheduleToCalendarresponseContent?.os_facility_uuid,
            incision = otScheduleToCalendarresponseContent?.os_incision,
            instructions = "",   //doubt
            is_force_booking = otScheduleToCalendarresponseContent?.os_is_force_booking,
            lesion_uuid = otScheduleToCalendarresponseContent?.os_lesion_uuid,
            notes = "",  //doubt
            ot_grade_uuid = otScheduleToCalendarresponseContent?.ot_grade_uuid,
            ot_master_uuid = otScheduleToCalendarresponseContent?.os_ot_master_uuid,
            ot_priority_uuid = otScheduleToCalendarresponseContent?.os_ot_priority_uuid,
            ot_schedule_status_uuid = 0,    //doubt
            ot_scheduled_on = otScheduleToCalendarresponseContent?.os_ot_scheduled_on,
            ot_team_uuid = 0,   //doubt
            ot_type_uuid = otScheduleToCalendarresponseContent?.os_ot_type_uuid,
            position_uuid = otScheduleToCalendarresponseContent?.os_position_uuid,
            procedure_category_uuid = otScheduleToCalendarresponseContent?.os_procedure_uuid,    //doubt
            procedure_type_uuid = otScheduleToCalendarresponseContent?.os_procedure_uuid,        //doubt
            procedure_uuid = otScheduleToCalendarresponseContent?.os_procedure_uuid,
            scrub_nurse_uuid = 0,    //doubt
            start_date = otScheduleToCalendarresponseContent?.os_start_date,
            surgeon_uuid = otScheduleToCalendarresponseContent?.os_surgeon_uuid,
            ward_bed_mapping_uuid = 0,   //doubt
            ward_master_uuid = 0         //doubt
        )
        viewModel?.modifyOtSchedule(facilityId!!, body, modifyOtScheduleRespCallback)
    }

    private val modifyOtScheduleRespCallback = object : RetrofitCallback<ModifyOtScheduleResp> {
        override fun onSuccessfulResponse(responseBody: Response<ModifyOtScheduleResp>?) {
            if (responseBody?.isSuccessful == true) {
                utils?.showToast(
                    R.color.positiveToast,
                    binding?.mainLayout!!,
                    getString(R.string.data_save)
                )
                dialog?.dismiss()
            }
        }

        override fun onBadRequest(errorBody: Response<ModifyOtScheduleResp>?) {
            val gson = GsonBuilder().create()
            val responseModel: ModifyOtScheduleResp
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    ModifyOtScheduleResp::class.java
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
            viewModel!!.progressBar.value = 8
        }
    }


    private val getDispenseListRespCallback =
        object : RetrofitCallback<PharmacyDispenseResponse> {
            override fun onSuccessfulResponse(responseBody: Response<PharmacyDispenseResponse>?) {
                if (responseBody?.isSuccessful == true) {


                    Log.e("AddOTSch", responseBody.body()?.responseContent.toString())

                    var title = ""
                    if (responseBody.body()?.responseContent!!.title_uuid!!.equals("1")) {
                        title = "Mr."
                    } else if (responseBody.body()?.responseContent!!.title_uuid!!.equals("2")) {
                        title = "Mrs."
                    }

                    binding?.detailsTextView?.text =
                        title + responseBody.body()?.responseContent!!.first_name + " / " +
                                responseBody.body()?.responseContent!!.age + " Year(s) / " +
                                responseBody.body()?.responseContent!!.gender_details?.name + " / " +
                                responseBody.body()?.responseContent!!.uhid + " / " +
                                responseBody.body()?.responseContent!!.para_2


                }
            }

            override fun onBadRequest(errorBody: Response<PharmacyDispenseResponse>?) {
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
                viewModel!!.progressBar.value = 8
            }
        }


    fun setOnRefreshListener(callback: OnRefreshListener) {
        this.callbacksurgury = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    interface OnRefreshListener {
        fun onRefreshList()
    }

    private fun trackOtScheduleDelete(type: String?) {
        AnalyticsManager.getAnalyticsManager().trackOtScheduleDelete(type)
    }
}