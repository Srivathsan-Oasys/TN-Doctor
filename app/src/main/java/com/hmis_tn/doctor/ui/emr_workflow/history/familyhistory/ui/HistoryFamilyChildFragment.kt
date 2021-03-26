package com.hmis_tn.doctor.ui.emr_workflow.history.familyhistory.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
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
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.HistoryFamilyChildFragmentBinding
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.history.allergy.model.response.period.PeriodresponseContent
import com.hmis_tn.doctor.ui.emr_workflow.history.allergy.model.response.period.PeriodsResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.history.familyhistory.model.*
import com.hmis_tn.doctor.ui.emr_workflow.history.familyhistory.model.Request.CreateFamilyHistoryRequestModel
import com.hmis_tn.doctor.ui.emr_workflow.history.familyhistory.viewmodel.HistoryFamilyViewModel
import com.hmis_tn.doctor.ui.emr_workflow.history.familyhistory.viewmodel.HistoryFamilyViewModelFactory
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.hmis_tn.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryFamilyChildFragment : Fragment() {
    private var binding: HistoryFamilyChildFragmentBinding? = null
    private var viewModel: HistoryFamilyViewModel? = null
    private var createFamilyHistory: CreateFamilyHistoryRequestModel? =
        CreateFamilyHistoryRequestModel()
    private var durationFamilyHistoryAdapter: DurationFamilyHistoryAdapter? = null
    private var facility_id: Int? = 0
    private var patient_id: Int? = 0
    private var encounter_uuid: Int? = 0
    private var doctor_en_uuid: Int? = 0
    private var encounter_type_uuid: Int? = 0
    private var appPreferences: AppPreferences? = null
    lateinit var historyFamilyAdapter: HistoryFamilyAdapter
    private var mYear: Int? = null
    private var mMonth: Int? = null
    private var mDay: Int? = null
    private var mHour: Int? = null
    private var mMinute: Int? = null
    private var mSecond: Int? = null
    private var durationArrayList: java.util.ArrayList<PeriodresponseContent?>? =
        java.util.ArrayList()
    private var familyTypeSpinnerList = mutableMapOf<Int, String>()
    private var selectTypeItem: Int = 0

    //private var allergyAdapter: HistoryFamilyAdapter? = null
    private val hashtypeSpinnerList: HashMap<Int, Int> = HashMap()
    private var updateId: Int = 0
    private var uuid: Int = 0

    private var familyList: ArrayList<FamilyTyperesponseContent?>? = ArrayList()

    private var utils: Utils? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.history_family_child_fragment,
                container,
                false
            )
        viewModel = HistoryFamilyViewModelFactory(
            requireActivity().application
        ).create(HistoryFamilyViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        utils = Utils(requireContext())
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        patient_id = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        encounter_type_uuid = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding?.familyRecyclerView!!.layoutManager = linearLayoutManager

        viewModel?.getHistoryFamilyType(facility_id!!, getFamilyTypeSpinnerRetrofiCallback)

        viewModel?.getDuration(getDurationRetrofitCallBack)


        historyFamilyAdapter = HistoryFamilyAdapter(
            requireActivity(),
            ArrayList()
        )
        binding?.familyRecyclerView!!.adapter = historyFamilyAdapter

        binding?.clear?.setOnClickListener {


            val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
            val currentDateandTime = sdf.format(Date())
            binding?.etFamilyDateTime?.setText(currentDateandTime)

            binding?.encounterTypeSpinner?.setSelection(0)

            binding?.diseaseName?.setText("")

            binding?.durationTextView?.setText("1")
        }

        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val currentDateandTime = sdf.format(Date())
        binding?.etFamilyDateTime?.setText(currentDateandTime)
        binding?.etFamilyDateTime?.setOnClickListener {

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

                            binding?.etFamilyDateTime?.setText(
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
                                    "%02d", mSecond
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

        binding?.encounterTypeSpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectTypeItem = 0
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {

                    if (pos == 0) {
                        selectTypeItem = 0
                    } else {

                        val itemValue = parent!!.getItemAtPosition(pos).toString()

                        selectTypeItem =
                            familyTypeSpinnerList.filterValues { it == itemValue }.keys.toList()[0]

                    }

                    //Log.e("sev",""+selectseverityUuid)
                }

            }

        val gridLayoutManager = GridLayoutManager(
            context, 1,
            LinearLayoutManager.HORIZONTAL, false
        )
        binding?.durationRecyclerView!!.layoutManager = gridLayoutManager
        durationFamilyHistoryAdapter = DurationFamilyHistoryAdapter(requireActivity(), ArrayList())
        binding?.durationRecyclerView!!.adapter = durationFamilyHistoryAdapter

        binding?.addNewFamilyIconView!!.setOnClickListener {
            if (binding?.etFamilyDateTime?.text?.trim()!!
                    .isEmpty() || binding?.diseaseName!!.text.trim().isEmpty()
                || selectTypeItem.equals(0) || durationFamilyHistoryAdapter?.selectPositionValidation()!!
                    .equals(0)
            ) {
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


        historyFamilyAdapter.setOnClickListener(object : HistoryFamilyAdapter.OnClickListener {
            override fun OnClick(responseContent: FamilyHistoryResponseContent?, position: Int) {
                //Log.e("SelectedFamilyData", responseContent?.family_relation_type?.uuid.toString())
                try {
                    binding?.etFamilyDateTime!!.setText(responseContent?.created_date)
                    binding?.durationTextView!!.setText(responseContent?.duration.toString())
                    durationFamilyHistoryAdapter?.selectDurationPosition(responseContent?.periods?.uuid!!)
                    binding?.encounterTypeSpinner!!.setSelection(
                        hashtypeSpinnerList.get(
                            responseContent?.family_relation_type?.uuid!!
                        )!!
                    )
                    binding?.diseaseName!!.setText(responseContent.disease_name)
                    updateId = 1
                    uuid = responseContent.uuid
                    binding?.addtext!!.text = "Update"
                    binding?.resultLinearLayout?.visibility = View.VISIBLE
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        })
        binding?.previewLinearLayout?.setOnClickListener {
            if (binding?.resultLinearLayout?.visibility == View.VISIBLE) {
                binding?.resultLinearLayout?.visibility = View.GONE
            } else {
                binding?.resultLinearLayout?.visibility = View.VISIBLE
            }
        }



        return binding!!.root
    }

    fun setDuration(durationArrayList_: java.util.ArrayList<PeriodresponseContent?>) {
        durationArrayList = durationArrayList_
    }

    val getDurationRetrofitCallBack =
        object : RetrofitCallback<PeriodsResponseModel> {
            override fun onSuccessfulResponse(response: Response<PeriodsResponseModel>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    setDuration(response.body()?.responseContents!! as java.util.ArrayList<PeriodresponseContent?>)

                    durationFamilyHistoryAdapter?.setDurationData(response.body()?.responseContents)

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
                viewModel!!.progressBar.value = 8
            }
        }

    val getFamilyTypeAllRetrofitCallback = object : RetrofitCallback<FamilyHistoryResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<FamilyHistoryResponseModel>?) {

            if (responseBody!!.body()?.responseContent?.size == 0) {
                binding?.familyRecyclerView?.visibility = View.GONE
                binding?.contentLinearLayout?.visibility = View.GONE
                binding?.resultLinearLayout?.visibility = View.VISIBLE
                binding?.noRecordsTextView?.visibility = View.GONE
            } else {
                historyFamilyAdapter.setData(responseBody.body()?.responseContent)
                binding?.familyRecyclerView?.visibility = View.VISIBLE
                binding?.contentLinearLayout?.visibility = View.VISIBLE
                binding?.resultLinearLayout?.visibility = View.GONE
                binding?.noRecordsTextView?.visibility = View.GONE
            }


        }

        override fun onBadRequest(errorBody: Response<FamilyHistoryResponseModel>?) {
            //Log.e("DataHistory", "badRequest")
            val gson = GsonBuilder().create()
            val responseModel: FamilyHistoryResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    FamilyHistoryResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    responseModel.message
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
            viewModel!!.progressBar.value = 8
        }
    }


    val getFamilyTypeSpinnerRetrofiCallback =
        object : RetrofitCallback<FamilyTypeSpinnerResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<FamilyTypeSpinnerResponseModel>?) {

                familyList?.add(FamilyTyperesponseContent())
                familyList?.addAll(responseBody?.body()?.responseContents!!)
                setFamilyType(familyList)

                viewModel?.getFamilyTypeList(
                    facility_id!!,
                    patient_id!!,
                    getFamilyTypeAllRetrofitCallback
                )
            }

            override fun onBadRequest(errorBody: Response<FamilyTypeSpinnerResponseModel>?) {
                //Log.e("DataHistory", "badRequest")
                val gson = GsonBuilder().create()
                val responseModel: FamilyTypeSpinnerResponseModel
                try {
                    responseModel = gson.fromJson(
                        errorBody!!.errorBody()!!.string(),
                        FamilyTypeSpinnerResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        responseModel.req!!
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
                viewModel!!.progressBar.value = 8
            }

        }

    fun setFamilyType(responseContents: List<FamilyTyperesponseContent?>?) {
        familyTypeSpinnerList =
            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        hashtypeSpinnerList.clear()

        for (i in responseContents.indices) {

            hashtypeSpinnerList[responseContents[i]!!.uuid!!] = i
        }

        if (isAdded) {
            val adapter = ArrayAdapter<String>(
                requireActivity(),
                android.R.layout.simple_spinner_item,
                familyTypeSpinnerList.values.toMutableList()
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding?.encounterTypeSpinner!!.adapter = adapter
        }

    }

    val createFamilyHistoryRestrofitCallback =
        object : RetrofitCallback<CreateFamilyHistoryResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<CreateFamilyHistoryResponseModel>?) {

                //Log.e("CreateFamilyResponse", responseBody?.body().toString())

                Toast.makeText(activity, responseBody!!.body()?.message, Toast.LENGTH_LONG).show()
                getCurrentDate()
                binding?.diseaseName!!.setText("")

                viewModel?.getFamilyTypeList(
                    facility_id!!,
                    patient_id!!,
                    getFamilyTypeAllRetrofitCallback
                )

            }

            override fun onBadRequest(errorBody: Response<CreateFamilyHistoryResponseModel>?) {
                //Log.e("DataHistory", "badRequest")
                val gson = GsonBuilder().create()
                val responseModel: CreateFamilyHistoryResponseModel
                try {
                    responseModel = gson.fromJson(
                        errorBody!!.errorBody()!!.string(),
                        CreateFamilyHistoryResponseModel::class.java
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
                viewModel!!.progressBar.value = 8
            }

        }

    val updateFamilyRetrofitCallback = object : RetrofitCallback<FamilyUpdateResponseModel> {


        override fun onSuccessfulResponse(responseBody: Response<FamilyUpdateResponseModel>?) {

            //Log.e("UpdatedFamilyData",responseBody?.body()?.message)
            //viewModel?.getAllergyAll(facility_id!!, patient_id!!, getAllAllergyRetrofitCallback)

            Toast.makeText(activity, responseBody!!.body()?.message, Toast.LENGTH_LONG).show()
            getCurrentDate()
            binding?.diseaseName!!.setText("")
            updateId = 0
            binding?.addtext?.text = "Add"
            viewModel?.getFamilyTypeList(
                facility_id!!,
                patient_id!!,
                getFamilyTypeAllRetrofitCallback
            )

        }

        override fun onBadRequest(errorBody: Response<FamilyUpdateResponseModel>?) {
            //Log.e("DataHistory", "badRequest")
            val gson = GsonBuilder().create()
            val responseModel: FamilyUpdateResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    FamilyUpdateResponseModel::class.java
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
            viewModel!!.progressBar.value = 8
        }
    }

    fun getCurrentDate() {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val currentDateandTime = sdf.format(Date())
        binding?.etFamilyDateTime?.setText(currentDateandTime)
    }

    fun saveOrUpdateFamilyHistory() {

        val jsonBody = JSONObject()
        try {
            jsonBody.put("patient_uuid", patient_id.toString())
            jsonBody.put("encounter_uuid", encounter_uuid)
            jsonBody.put("consultation_uuid", 1)
            jsonBody.put("relation_type_uuid", selectTypeItem.toString())
            jsonBody.put("disease_name", binding?.diseaseName!!.text.toString())
            jsonBody.put(
                "identified_date",
                utils!!.reqDate(binding?.etFamilyDateTime!!.text.trim().toString())
            )
            jsonBody.put("duration", binding?.durationTextView!!.text.toString())
            jsonBody.put("period_uuid", durationFamilyHistoryAdapter?.selectedPosition.toString())

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )


        if (updateId == 0) {
            viewModel?.createFamilyHistory(
                this.facility_id!!,
                body, createFamilyHistoryRestrofitCallback
            )
        } else {

            viewModel?.updateFamilyData(
                this.facility_id!!,
                uuid,
                body,
                updateFamilyRetrofitCallback
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
                    saveOrUpdateFamilyHistory()

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
                viewModel!!.progressBar.value = 8
            }
        }

    val createEncounterRetrofitCallback = object : RetrofitCallback<CreateEncounterResponseModel> {
        override fun onSuccessfulResponse(response: Response<CreateEncounterResponseModel>) {


            doctor_en_uuid = response.body()?.responseContents?.encounterDoctor?.uuid!!.toInt()
            encounter_uuid = response.body()?.responseContents?.encounter?.uuid!!.toInt()
            patient_id = response.body()?.responseContents?.encounterDoctor?.patient_uuid!!.toInt()

            saveOrUpdateFamilyHistory()
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

}