package com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.ui

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentAllergyChildBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.model.Request.AllergyCreateRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.model.Request.EditPatientAllergies
import com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.model.Request.JsonobjectClass
import com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.model.Request.PatientAllergies
import com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.model.response.*
import com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.model.response.edit.EditAllergyBindResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.model.response.period.PeriodresponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.model.response.period.PeriodsResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.viewmodel.AllergyViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.viewmodel.AllergyViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.model.EmrWorkFlowResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class AllergyChildFragment : Fragment() {

    private var patient_allergies: PatientAllergies? = PatientAllergies()
    private var edit_patient_allergies: EditPatientAllergies? = EditPatientAllergies()
    private var facility_id: Int? = 0
    private var patient_id: Int? = 0
    private var doctor_en_uuid: Int? = 0
    private var encounter_uuid: Int? = 0
    private var encounter_type_uuid: Int? = 0
    private var binding: FragmentAllergyChildBinding? = null
    private var viewModel: AllergyViewModel? = null
    private var appPreferences: AppPreferences? = null
    private var durationAllergyAdapter: DurationAllergyAdapter? = null
    private var allergyAdapter: AllergyAdapter? = null
    private var mYear: Int? = null
    private var mMonth: Int? = null
    private var mDay: Int? = null
    private var mHour: Int? = null
    private var mMinute: Int? = null
    private var mSeconds: Int? = null
    private var typeNamesList = mutableMapOf<Int, String>()
    private var AllergyNamesList = mutableMapOf<Int, String>()
    private var AllergySourceList = mutableMapOf<Int, String>()
    private var AllergySeverityList = mutableMapOf<Int, String>()
    private var utils: Utils? = null
    private var selectitemUuid: Int = 0
    private var selectnameUuid: Int = 0
    private var selectseverityUuid: Int = 0
    private var selectsourceUuid: Int = 0
    private var updateId: Int = 0
    private var uuid: Int = 0
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var jsonobjectclass: JsonobjectClass? = null
    val postAllergyList = ArrayList<AllergyCreateRequestModel>()

    var hashmapbody: HashMap<String, Objects> = HashMap()

    //private var allergyArrayList: List<AllergyAll?>? = ArrayList()
    val allergyRequestModel: AllergyCreateRequestModel? = AllergyCreateRequestModel()

    private val responseContents: List<EncounterAllergyTypeResponse?>? = null
    private var durationArrayList: ArrayList<PeriodresponseContent?>? = ArrayList()

    private val hashtypeSpinnerList: HashMap<Int, Int> = HashMap()
    private val hashNameSpinnerList: HashMap<Int, Int> = HashMap()
    private val hashSourceSpinnerList: HashMap<Int, Int> = HashMap()
    private val hashSeveritySpinnerList: HashMap<Int, Int> = HashMap()

    private var allergyNameSpinnerList: ArrayList<Name?>? = ArrayList()
    private var allergySourceSpinnerList: ArrayList<Source?>? = ArrayList()
    private var allergySeveritySpinnerList: ArrayList<Severity?>? = ArrayList()

    val date: Date? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_allergy_child, container, false)

        utils = Utils(this.requireContext())
        viewModel = AllergyViewModelFactory(
            requireActivity().application
        ).create(AllergyViewModel::class.java)


        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val currentDateandTime = sdf.format(Date())
        binding?.etAllergyDateTime?.setText(currentDateandTime)

        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        binding?.etAllergyDateTime?.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireActivity(),
                OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    val c = Calendar.getInstance()
                    mHour = c[Calendar.HOUR_OF_DAY]
                    mMinute = c[Calendar.MINUTE]
                    mSeconds = c[Calendar.SECOND]

                    val timePickerDialog = TimePickerDialog(
                        this.activity,
                        OnTimeSetListener { view, hourOfDay, minute ->

                            binding?.etAllergyDateTime?.setText(
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
                                ) + ":" + String.format(
                                    "%02d", mSeconds
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

        binding?.encounterTypeSpinner!!.isEnabled = false
        binding?.encounterTypeSpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectitemUuid = typeNamesList.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {

                    val itemValue = parent!!.getItemAtPosition(pos).toString()

                    selectitemUuid = typeNamesList.filterValues { it == itemValue }.keys.toList()[0]

                    Log.e("", "" + selectitemUuid)
                }

            }


        binding?.clear?.setOnClickListener {

            val sdf1 = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
            val currentDateandTime1 = sdf1.format(Date())
            binding?.etAllergyDateTime?.setText(currentDateandTime1)

            binding?.encounterTypeSpinner!!.setSelection(0)

            binding?.allergyNameSpinner?.setSelection(0)

            binding?.allergySourceSpinner?.setSelection(0)

            binding?.allergySeveritySpinner?.setSelection(0)

            binding?.durationTextView!!.setText("")


        }
        binding?.previewLinearLayout?.setOnClickListener {
            if (binding?.resultLinearLayout?.visibility == View.VISIBLE) {
                binding?.resultLinearLayout?.visibility = View.GONE
            } else {
                binding?.resultLinearLayout?.visibility = View.VISIBLE
            }
        }



        binding?.allergyNameSpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectnameUuid = 0
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    if (pos == 0) {
                        selectnameUuid = 0
                    } else {
                        val itemValue = parent!!.getItemAtPosition(pos).toString()

                        selectnameUuid =
                            AllergyNamesList.filterValues { it == itemValue }.keys.toList()[0]
                    }

                }

            }


        binding?.allergySeveritySpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectseverityUuid = 0
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {

                    if (pos == 0) {
                        selectseverityUuid = 0
                    } else {
                        val itemValue = parent!!.getItemAtPosition(pos).toString()

                        selectseverityUuid =
                            AllergySeverityList.filterValues { it == itemValue }.keys.toList()[0]
                    }

                    //Log.e("sev",""+selectseverityUuid)
                }

            }

        binding?.allergySourceSpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectsourceUuid = 0
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {

                    if (pos == 0) {
                        selectsourceUuid = 0
                    } else {
                        val itemValue = parent!!.getItemAtPosition(pos).toString()

                        selectsourceUuid =
                            AllergySourceList.filterValues { it == itemValue }.keys.toList()[0]
                    }

                    //Log.e("source", "" + selectsourceUuid)
                }

            }

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        patient_id = appPreferences?.getInt(AppConstants.PATIENT_UUID)

        encounter_type_uuid = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)
        viewModel?.getAllergyTypeList(facility_id!!, getTypeAll)


        binding?.addNewAllergyIconView!!.setOnClickListener {

            postAllergyList.clear()

            if (binding?.etAllergyDateTime?.text?.trim()!!.isEmpty() ||
                selectseverityUuid.equals(0) || selectnameUuid.equals(0) || selectsourceUuid.equals(
                    0
                ) || selectitemUuid.equals(0)
                || durationAllergyAdapter?.selectPositionValidation()!!.equals(0)
            ) {

                Log.e("severty", binding?.allergySeveritySpinner?.selectedItem.toString())
                Log.e("source", binding?.allergySourceSpinner?.selectedItem.toString())
                Log.e("type", binding?.encounterTypeSpinner?.selectedItem.toString())
                Log.e("name", binding?.allergyNameSpinner?.selectedItem.toString())
                Toast.makeText(activity, "Enter all fields", Toast.LENGTH_LONG).show()
            } else {
                viewModel?.getEncounter(
                    facility_id!!,
                    patient_id!!,
                    encounter_type_uuid!!,
                    fetchEncounterRetrofitCallBack
                )
            }

        }

        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding?.allergyRecyclerView!!.layoutManager = linearLayoutManager
        viewModel?.getAllergyAll(facility_id!!, patient_id!!, getAllAllergyRetrofitCallback)

        binding?.minusImageView!!.setOnClickListener {
            binding?.durationTextView!!.setText(
                (binding?.durationTextView!!.text.toString().toInt() - 1).toString()
            )
            //chiefComplaintsModel.duration = binding?.durationTextView!!.text.toString()
            if (binding?.durationTextView!!.text.toString().toInt() == 0) {
                binding?.minusImageView!!.alpha = 0.5f
                binding?.minusImageView!!.isEnabled = false
            }
        }

        binding?.plusImageView!!.setOnClickListener {
            binding?.minusImageView!!.alpha = 1f
            binding?.minusImageView!!.isEnabled = true
            binding?.durationTextView!!.setText(
                (binding?.durationTextView!!.text.toString().toInt() + 1).toString()
            )
            //chiefComplaintsModel.duration = holder.durationTextView.text.toString()
        }
        val gridLayoutManager = GridLayoutManager(
            context, 1,
            LinearLayoutManager.HORIZONTAL, false
        )
        binding?.durationRecyclerView!!.layoutManager =
            gridLayoutManager
        durationAllergyAdapter = DurationAllergyAdapter(requireActivity(), ArrayList())
        binding?.durationRecyclerView!!.adapter = durationAllergyAdapter

        allergyAdapter = AllergyAdapter(
            requireActivity(),
            ArrayList()
        )
        binding?.allergyRecyclerView!!.adapter = allergyAdapter

        allergyAdapter?.setOnClickListener(object : AllergyAdapter.OnClickListener {
            override fun OnClick(responseContent: AllergyAll?, position: Int) {

                viewModel?.EditAllergyData(
                    facility_id!!,
                    responseContent!!.uuid!!,
                    editAllergyCallback
                )
                /*try {
                    binding?.etAllergyDateTime!!.setText(responseContent?.start_date)
                    binding?.durationTextView!!.setText(responseContent?.duration.toString())
                    binding?.encounterTypeSpinner!!.setSelection(
                        hashtypeSpinnerList.get(
                            responseContent?.encounter?.encounter_type?.uuid!!
                        )!!
                    )
                    durationAllergyAdapter?.selectDurationPosition(responseContent.periods?.uuid!!)
                    binding?.allergyNameSpinner!!.setSelection(
                        hashNameSpinnerList.get(
                            responseContent.allergy_masters?.uuid!!
                        )!!
                    )
                    binding?.allergySourceSpinner!!.setSelection(
                        hashSourceSpinnerList.get(
                            responseContent.allergy_source?.uuid!!
                        )!!
                    )
                    binding?.allergySeveritySpinner!!.setSelection(
                        hashSeveritySpinnerList.get(
                            responseContent.allergy_severity?.uuid!!
                        )!!
                    )
                  *//*  binding?.addNewAllergyIconView!!.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            getResources(),
                            R.drawable.ic_update_black_24dp,
                            null
                        )
                    )*//*
                    binding?.addtext!!.setText("Update")

                    updateId = 1
                    uuid = responseContent.uuid!!

                }catch (e: java.lang.Exception){

                }*/
            }

        })

        return binding!!.root
    }

    val getTypeAll = object : RetrofitCallback<EncounterAllergyTypeResponse> {
        override fun onSuccessfulResponse(responseBody: Response<EncounterAllergyTypeResponse>?) {
            //Log.e("AllergyTpye", responseBody!!.body().toString())

            setTypeValue(responseBody?.body()?.responseContents)

            viewModel?.getDuration(getDurationRetrofitCallBack)
        }

        override fun onBadRequest(errorBody: Response<EncounterAllergyTypeResponse>?) {
            //Log.e("DataHistory", "badRequest")
            val gson = GsonBuilder().create()
            val responseModel: EmrWorkFlowResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    EmrWorkFlowResponseModel::class.java
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

        override fun onServerError(response: Response<*>?) {
            // Log.e("DataHistory", "servererr")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            //Log.e("DataHistory", "unAuth")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            //Log.e("DataHistory", "forbidden")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String?) {
            //Log.e("DataHistory", "failure")
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
        }

        override fun onEverytime() {
            //Log.e("DataHistory", "everytime")
            viewModel!!.progress.value = 8
        }
    }

    fun setTypeValue(responseContents: List<Type?>?) {

        typeNamesList = responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        hashtypeSpinnerList.clear()

        for (i in responseContents.indices) {

            hashtypeSpinnerList[responseContents[i]!!.uuid!!] = i
        }
        if (isAdded) {
            val adapter = ArrayAdapter<String>(
                requireActivity(),
                android.R.layout.simple_spinner_item,
                typeNamesList.values.toMutableList()
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding?.encounterTypeSpinner!!.adapter = adapter
        }

    }


    val getDurationRetrofitCallBack =
        object : RetrofitCallback<PeriodsResponseModel> {
            override fun onSuccessfulResponse(response: Response<PeriodsResponseModel>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    setDuration(response.body()?.responseContents!! as ArrayList<PeriodresponseContent?>)

                    durationAllergyAdapter?.setDurationData(response.body()?.responseContents)

                    viewModel?.getAllergyNamesList(facility_id!!, getAllergyNameRetrofitCallBack)
                }
            }

            override fun onBadRequest(response: Response<PeriodsResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: PeriodsResponseModel
                try {

                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        PeriodsResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.something_went_wrong)
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

    fun setDuration(durationArrayList_: ArrayList<PeriodresponseContent?>) {
        durationArrayList = durationArrayList_
    }

    val getAllergyNameRetrofitCallBack = object : RetrofitCallback<AllergyNameResponse> {
        override fun onSuccessfulResponse(responseBody: Response<AllergyNameResponse>?) {

            allergyNameSpinnerList?.add(Name())
            allergyNameSpinnerList?.addAll(responseBody?.body()?.responseContents!!)
            setAllergyName(allergyNameSpinnerList)

            viewModel?.getAllergySourceList(facility_id!!, getAllergySourceRetrofitCallback)
        }

        override fun onBadRequest(errorBody: Response<AllergyNameResponse>?) {
            //Log.e("DataHistory", "badRequest")
            val gson = GsonBuilder().create()
            val responseModel: EmrWorkFlowResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    EmrWorkFlowResponseModel::class.java
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

        override fun onServerError(response: Response<*>?) {
            // Log.e("DataHistory", "servererr")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            //Log.e("DataHistory", "unAuth")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            //Log.e("DataHistory", "forbidden")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String?) {
            //Log.e("DataHistory", "failure")
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
        }

        override fun onEverytime() {
            //Log.e("DataHistory", "everytime")
            viewModel!!.progress.value = 8
        }

    }

    fun setAllergyName(responseContents: List<Name?>?) {
        AllergyNamesList =
            responseContents?.map { it?.uuid!! to it.allergy_name!! }!!.toMap().toMutableMap()

        hashNameSpinnerList.clear()

        for (i in responseContents.indices) {

            hashNameSpinnerList[responseContents[i]!!.uuid!!] = i
        }

        if (isAdded) {
            val adapter = ArrayAdapter<String>(
                requireActivity(),
                android.R.layout.simple_spinner_item,
                AllergyNamesList.values.toMutableList()
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding?.allergyNameSpinner!!.adapter = adapter

            binding?.allergyNameSpinner?.prompt = responseContents.get(0)?.allergy_name
            binding?.allergyNameSpinner?.setSelection(0)
        }
    }

    val getAllergySourceRetrofitCallback = object : RetrofitCallback<AllergySourceResponse> {
        override fun onSuccessfulResponse(responseBody: Response<AllergySourceResponse>?) {

            allergySourceSpinnerList?.add(Source())
            allergySourceSpinnerList?.addAll(responseBody?.body()?.responseContents!!)
            setAllergySource(allergySourceSpinnerList)

            viewModel?.getAllergySeverityList(facility_id!!, getAllergySeverityRetrofitCallback)
        }

        override fun onBadRequest(errorBody: Response<AllergySourceResponse>?) {
            //Log.e("DataHistory", "badRequest")
            val gson = GsonBuilder().create()
            val responseModel: EmrWorkFlowResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    EmrWorkFlowResponseModel::class.java
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

        override fun onServerError(response: Response<*>?) {
            //Log.e("DataHistory", "servererr")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            //Log.e("DataHistory", "unAuth")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            //Log.e("DataHistory", "forbidden")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String?) {
            //Log.e("DataHistory", "failure")
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
        }

        override fun onEverytime() {
            //Log.e("DataHistory", "everytime")
            viewModel!!.progress.value = 8
        }
    }

    fun setAllergySource(responseContents: List<Source?>?) {
        AllergySourceList =
            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        hashSourceSpinnerList.clear()

        for (i in responseContents.indices) {

            hashSourceSpinnerList[responseContents[i]!!.uuid!!] = i

        }
        if (isAdded) {
            val adapter = ArrayAdapter<String>(
                requireActivity(),
                android.R.layout.simple_spinner_item,
                AllergySourceList.values.toMutableList()
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding?.allergySourceSpinner!!.adapter = adapter
        }

    }

    val getAllergySeverityRetrofitCallback = object : RetrofitCallback<AllergySeverityResponse> {
        override fun onSuccessfulResponse(responseBody: Response<AllergySeverityResponse>?) {

            allergySeveritySpinnerList?.add(Severity())
            allergySeveritySpinnerList?.addAll(responseBody?.body()?.responseContents!!)
            setAllergySeverity(allergySeveritySpinnerList)
        }

        override fun onBadRequest(errorBody: Response<AllergySeverityResponse>?) {
            //Log.e("DataHistory", "badRequest")
            val gson = GsonBuilder().create()
            val responseModel: EmrWorkFlowResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    EmrWorkFlowResponseModel::class.java
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

        override fun onServerError(response: Response<*>?) {
            //Log.e("DataHistory", "servererr")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            Log.e("DataHistory", "unAuth")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            //Log.e("DataHistory", "forbidden")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String?) {
            //Log.e("DataHistory", "failure")
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
        }

        override fun onEverytime() {
            // Log.e("DataHistory", "everytime")
            viewModel!!.progress.value = 8
        }
    }

    fun setAllergySeverity(responseContents: List<Severity?>?) {
        AllergySeverityList =
            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        hashSeveritySpinnerList.clear()

        for (i in responseContents.indices) {

            hashSeveritySpinnerList[responseContents[i]!!.uuid!!] = i
        }
        if (isAdded) {
            val adapter = ArrayAdapter<String>(
                requireActivity(),
                android.R.layout.simple_spinner_item,
                AllergySeverityList.values.toMutableList()
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding?.allergySeveritySpinner!!.adapter = adapter
        }

    }


    val getAllAllergyRetrofitCallback = object : RetrofitCallback<AllergyAllGetResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<AllergyAllGetResponseModel>?) {

            if (responseBody!!.body()?.responseContent?.size == 0) {
                binding?.allergyRecyclerView?.visibility = View.GONE
                binding?.contentLinearLayout?.visibility = View.GONE
                binding?.resultLinearLayout?.visibility = View.VISIBLE
                binding?.noRecordsTextView?.visibility = View.GONE
            } else {
                allergyAdapter!!.setData(responseBody.body()!!.responseContent)
                binding?.allergyRecyclerView?.visibility = View.VISIBLE
                binding?.contentLinearLayout?.visibility = View.VISIBLE
                binding?.resultLinearLayout?.visibility = View.GONE
                binding?.noRecordsTextView?.visibility = View.GONE
            }

        }

        override fun onBadRequest(errorBody: Response<AllergyAllGetResponseModel>?) {
            //Log.e("DataHistory", "badRequest")
            val gson = GsonBuilder().create()
            val responseModel: EmrWorkFlowResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    EmrWorkFlowResponseModel::class.java
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

        override fun onServerError(response: Response<*>?) {
            //Log.e("DataHistory", "servererr")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            //Log.e("DataHistory", "unAuth")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            //Log.e("DataHistory", "forbidden")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String?) {
            //Log.e("DataHistory", "failure")
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
        }

        override fun onEverytime() {
            // Log.e("DataHistory", "everytime")
            viewModel!!.progress.value = 8
        }
    }


    val postAllergyDataCreate = object : RetrofitCallback<AllergyCreateResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<AllergyCreateResponseModel>?) {

            if (responseBody?.body()?.code == 200) {

                Toast.makeText(activity, responseBody.body()?.message, Toast.LENGTH_LONG).show()
                getCurrentDate()
                viewModel?.getAllergyAll(facility_id!!, patient_id!!, getAllAllergyRetrofitCallback)
            }

        }

        override fun onBadRequest(errorBody: Response<AllergyCreateResponseModel>?) {

            val gson = GsonBuilder().create()
            val responseModel: AllergyCreateResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    AllergyCreateResponseModel::class.java
                )

                Log.e("postAllergyData", "BadRequest" + responseModel.message!!)

            } catch (e: Exception) {
                Log.e("postAllergyData", "BadRequest")
                e.printStackTrace()
            }

            // Log.e("postAllergyData", "BadRequest")

        }

        override fun onServerError(response: Response<*>?) {

            Log.e("postAllergyData", "server")
        }

        override fun onUnAuthorized() {
            Log.e("postAllergyData", "UnAuth")
        }

        override fun onForbidden() {
            Log.e("postAllergyData", "ForBidd")
        }

        override fun onFailure(s: String?) {
            Log.e("postAllergyData", s.toString())
        }

        override fun onEverytime() {

        }

    }

    val updateAllergyRetrofitCallback = object : RetrofitCallback<AllergyUpdateResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<AllergyUpdateResponseModel>?) {

            Toast.makeText(activity, responseBody!!.body()?.message, Toast.LENGTH_LONG).show()
            getCurrentDate()
            updateId = 0
            binding?.addtext?.text = "Add"
            viewModel?.getAllergyAll(facility_id!!, patient_id!!, getAllAllergyRetrofitCallback)

        }

        override fun onBadRequest(errorBody: Response<AllergyUpdateResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: AllergyUpdateResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    AllergyUpdateResponseModel::class.java
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

        override fun onServerError(response: Response<*>?) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            //Log.e("DataHistory", "unAuth")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            //Log.e("DataHistory", "forbidden")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String?) {
            //Log.e("DataHistory", "failure")
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
        }

        override fun onEverytime() {
            // Log.e("DataHistory", "everytime")
            viewModel!!.progress.value = 8
        }
    }

    val editAllergyCallback = object : RetrofitCallback<EditAllergyBindResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<EditAllergyBindResponseModel>?) {

            Log.e("EditData", responseBody?.body()?.responseContent.toString())

            try {
                updateId = 1
                uuid = responseBody?.body()?.responseContent?.uuid!!
                binding?.etAllergyDateTime!!.setText(responseBody.body()?.responseContent?.created_date)
                binding?.durationTextView!!.setText(responseBody.body()?.responseContent?.duration.toString())
                binding?.encounterTypeSpinner!!.setSelection(
                    hashtypeSpinnerList.get(
                        responseBody.body()?.responseContent?.allergy_type_uuid!!
                    )!!
                )
                durationAllergyAdapter?.selectDurationPosition(responseBody.body()?.responseContent?.period_uuid!!)
                binding?.allergyNameSpinner!!.setSelection(
                    hashNameSpinnerList.get(
                        responseBody.body()?.responseContent?.allergy_master_uuid!!
                    )!!
                )
                binding?.allergySourceSpinner!!.setSelection(
                    hashSourceSpinnerList.get(
                        responseBody.body()?.responseContent?.allergy_source_uuid!!
                    )!!
                )
                binding?.allergySeveritySpinner!!.setSelection(
                    hashSeveritySpinnerList.get(
                        responseBody.body()?.responseContent?.allergy_severity_uuid!!
                    )!!
                )
                /*binding?.addNewAllergyIconView!!.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        getResources(),
                        R.drawable.ic_update_black_24dp,
                        null
                    )
                )*/
                binding?.addtext!!.text = "Update"

                binding?.resultLinearLayout?.visibility = View.VISIBLE

                uuid = responseBody.body()?.responseContent?.uuid!!

            } catch (e: java.lang.Exception) {

            }

        }

        override fun onBadRequest(errorBody: Response<EditAllergyBindResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: EditAllergyBindResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    EditAllergyBindResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
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
            //Log.e("DataHistory", "unAuth")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.unauthorized)
            )
        }

        override fun onForbidden() {
            //Log.e("DataHistory", "forbidden")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onFailure(failure: String?) {
            //Log.e("DataHistory", "failure")
            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
        }

        override fun onEverytime() {
            // Log.e("DataHistory", "everytime")
            viewModel!!.progress.value = 8
        }
    }


    fun getCurrentDate() {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val currentDateandTime = sdf.format(Date())
        binding?.etAllergyDateTime?.setText(currentDateandTime)
    }

    fun saveOrUpdateAllergy() {


        patient_allergies?.patient_uuid = patient_id?.toString()
        patient_allergies?.encounter_uuid = encounter_uuid
        patient_allergies?.consultation_uuid = 1
        patient_allergies?.allergy_master_uuid = selectnameUuid.toString()
        patient_allergies?.allergy_type_uuid = selectitemUuid.toString()
        patient_allergies?.allergy_severity_uuid = selectseverityUuid.toString()
        patient_allergies?.allergy_source_uuid = selectsourceUuid.toString()
        patient_allergies?.duration = binding?.durationTextView!!.text.toString()
        patient_allergies?.period_uuid = durationAllergyAdapter?.selectedPosition.toString()
        patient_allergies?.performed_date =
            utils!!.historyISOdate(binding?.etAllergyDateTime!!.text.trim().toString())
        patient_allergies?.patient_allergy_status_uuid = 1
        patient_allergies?.encounter_type_uuid = encounter_type_uuid?.toString()

        val jsonBody = JSONObject()
        var result = ""
        try {
            val responseobject = Gson().toJson(patient_allergies)

            jsonBody.put("patient_allergies", responseobject)

            result = jsonBody.toString()

            result = result.replace("\\", "")

            result = result.replace("\"{", "{")

            result = result.replace("}\"", "}")

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            result
        )


        //Log.e("PostAllergyData",result.toString())

        if (updateId == 0) {
            viewModel?.postAllergyData(
                this.facility_id!!,
                body, postAllergyDataCreate
            )
        } else {

            edit_patient_allergies?.patient_uuid = patient_id?.toString()
            edit_patient_allergies?.encounter_uuid = encounter_uuid
            edit_patient_allergies?.consultation_uuid = 1
            edit_patient_allergies?.allergy_master_uuid = selectnameUuid.toString()
            edit_patient_allergies?.allergy_type_uuid = selectitemUuid.toString()
            edit_patient_allergies?.allergy_severity_uuid = selectseverityUuid.toString()
            edit_patient_allergies?.allergy_source_uuid = selectsourceUuid.toString()
            edit_patient_allergies?.duration = binding?.durationTextView!!.text.toString()
            edit_patient_allergies?.period_uuid =
                durationAllergyAdapter?.selectedPosition.toString()

            val jsonBody1 = JSONObject()
            var result1 = ""
            try {
                val responseobject = Gson().toJson(edit_patient_allergies)

                jsonBody1.put("patient_allergies", responseobject)

                result1 = jsonBody1.toString()

                result1 = result1.replace("\\", "")

                result1 = result1.replace("\"{", "{")

                result1 = result1.replace("}\"", "}")

            } catch (e: JSONException) {
                e.printStackTrace()
            }
            val body1 = RequestBody.create(
                okhttp3.MediaType.parse("application/json; charset=utf-8"),
                result1
            )

            viewModel?.updateAllergyData(
                this.facility_id!!,
                uuid,
                body1,
                updateAllergyRetrofitCallback
            )
        }
    }

    private val fetchEncounterRetrofitCallBack =
        object : RetrofitCallback<FectchEncounterResponseModel> {
            override fun onSuccessfulResponse(response: Response<FectchEncounterResponseModel>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    var encounterResponseContent = response.body()?.responseContents!!
                    doctor_en_uuid =
                        encounterResponseContent.get(0)?.encounter_doctors?.get(0)?.uuid!!
                    encounter_uuid = encounterResponseContent.get(0)?.uuid!!
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_DOCTOR_UUID, doctor_en_uuid!!)
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_UUID, encounter_uuid!!)
                    saveOrUpdateAllergy()

                } else {
                    viewModel?.createEncounter(
                        patient_id!!,
                        encounter_type_uuid!!,
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
            encounter_uuid = response.body()?.responseContents?.encounter?.uuid!!.toInt()
            patient_id = response.body()?.responseContents?.encounterDoctor?.patient_uuid!!.toInt()

            saveOrUpdateAllergy()
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