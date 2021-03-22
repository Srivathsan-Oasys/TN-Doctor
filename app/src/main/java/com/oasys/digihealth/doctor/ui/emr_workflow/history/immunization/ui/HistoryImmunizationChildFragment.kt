package com.oasys.digihealth.doctor.ui.emr_workflow.history.immunization.ui

import android.app.DatePickerDialog
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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.HistoryImmunizationChildFragmentBinding
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.model.response.EncounterAllergyTypeResponse
import com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.model.response.Type
import com.oasys.digihealth.doctor.ui.emr_workflow.history.immunization.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.history.immunization.model.edit.EditImmunizationResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.immunization.viewmodel.HistoryImmunizationViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.history.immunization.viewmodel.HistoryImmunizationViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.model.EmrWorkFlowResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.oasys.digihealth.doctor.utils.custom_views.CustomProgressDialog
import com.oasys.digihealth.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HistoryImmunizationChildFragment : Fragment() {
    private var historyImmunizationAdapter: HistoryImmunizationAdapter? = null
    private var binding: HistoryImmunizationChildFragmentBinding? = null
    private var viewModel: HistoryImmunizationViewModel? = null
    private var utils: Utils? = null
    private var typeNamesList = mutableMapOf<Int, String>()
    private var nameList = mutableMapOf<Int, String>()
    private var institutionList = mutableMapOf<Int, String>()
    private var selectitemUuid: Int = 0
    private var selectNameUuid: Int = 0
    private var selectInstitutionUuid: Int = 0
    private var facilityid: Int = 0
    private var patient_uuid: Int = 0
    private var encounter_uuid: Int = 0
    private var doctor_en_uuid: Int = 0
    private var encounter_type_uuid: Int? = 0
    private var mYear: Int? = null
    private var mMonth: Int? = null
    private var mDay: Int? = null
    private var mHour: Int? = null
    private var mMinute: Int? = null
    private var mSecond: Int? = null
    private var customProgressDialog: CustomProgressDialog? = null

    private val hashTypeSpinnerList: HashMap<Int, Int> = HashMap()
    private val hashNameSpinnerList: HashMap<Int, Int> = HashMap()

    private var updateId: Int = 0
    private var uuid: Int = 0

    var appPreferences: AppPreferences? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.history_immunization_child_fragment,
                container,
                false
            )

        viewModel = HistoryImmunizationViewModelFactory(
            requireActivity().application
        ).create(HistoryImmunizationViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        utils = Utils(requireContext())
        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })
        customProgressDialog = CustomProgressDialog(activity)


        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val currentDateandTime = sdf.format(Date())
        binding?.etImmunizationDateTime?.setText(currentDateandTime)
        binding?.etImmunizationDateTime?.setOnClickListener {

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

                            binding?.etImmunizationDateTime?.setText(
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
                                ) + ":" + String.format("%02d", mSecond)
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

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilityid = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        patient_uuid = appPreferences?.getInt(AppConstants.PATIENT_UUID)!!
        encounter_type_uuid = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)
        viewModel?.getImmunizationTypeList(facilityid, getTypeAll)

        binding!!.clear?.setOnClickListener {

            val sdf1 = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
            val currentDateandTime1 = sdf1.format(Date())
            binding?.etImmunizationDateTime?.setText(currentDateandTime1)

            binding?.nameSpinner?.setSelection(0)

            binding?.encounterTypeSpinner?.setSelection(0)

            binding?.autoCompleteTextViewInstitutionName?.setText("")

        }

        binding?.etImmunizationRemarks!!.requestFocus()
        binding?.autoCompleteTextViewInstitutionName!!.requestFocus()
        binding?.autoCompleteTextViewInstitutionName!!.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 2 && s.length < 5) {
                    //  customProgressDialog!!.show()
                    viewModel?.getInstitutionSearchResult(
                        facilityid,
                        s.toString(),
                        getInstitutionSearchRetrofitCallBack
                    )

                }
            }
        })


        binding?.addNewImmunizationIconView!!.setOnClickListener {
            if (binding?.etImmunizationDateTime?.text?.trim()!!
                    .isEmpty() || binding?.etImmunizationRemarks!!.text.trim().isEmpty()
                || selectInstitutionUuid == 0
            ) {
                Toast.makeText(activity, "Enter all fields", Toast.LENGTH_LONG).show()
            } else {

                viewModel?.getEncounter(
                    facilityid,
                    patient_uuid,
                    encounter_type_uuid!!,
                    fetchEncounterRetrofitCallBack
                )


            }
        }


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

                }
            }

        binding?.nameSpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectNameUuid = nameList.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {

                    val itemValue = parent!!.getItemAtPosition(pos).toString()

                    selectNameUuid = nameList.filterValues { it == itemValue }.keys.toList()[0]

                    Log.e("", "" + selectitemUuid)
                }

            }


        binding?.autoCompleteTextViewInstitutionName!!.onItemClickListener =
            object : AdapterView.OnItemClickListener {
                override fun onItemClick(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(position).toString()

                    selectInstitutionUuid =
                        institutionList.filterValues { it == itemValue }.keys.toList()[0]

                    //Log.e("InsSelectData",selectInstitutionUuid.toString())
                }

            }


        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding?.immunizationRecyclerView!!.layoutManager = linearLayoutManager

        historyImmunizationAdapter = HistoryImmunizationAdapter(
            requireActivity(),
            ArrayList()
        )
        binding?.immunizationRecyclerView!!.adapter = historyImmunizationAdapter


        historyImmunizationAdapter!!.setOnClickListener(object :
            HistoryImmunizationAdapter.OnClickListener {
            override fun OnClick(responseContent: GetImmunizationresponseContent?, position: Int) {

                viewModel?.getImmunizationEdit(
                    facilityid,
                    responseContent!!.pis_uuid!!,
                    editImmunizationCallback
                )
                binding?.autoCompleteTextViewInstitutionName!!.setText(responseContent!!.f_name)
                /*binding?.etImmunizationDateTime!!.setText(responseContent!!.pis_immunization_date)
                binding?.etImmunizationRemarks!!.setText(responseContent!!.pis_comments)
                binding?.autoCompleteTextViewInstitutionName!!.setText(responseContent!!.f_name)
                selectInstitutionUuid= responseContent.f_uuid!!
                updateId = 1
                binding?.addtext!!.setText("Update")
                uuid = responseContent.pis_uuid!!*/

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

    val getTypeAll = object : RetrofitCallback<EncounterAllergyTypeResponse> {
        override fun onSuccessfulResponse(responseBody: Response<EncounterAllergyTypeResponse>?) {
            //Log.e("AllergyTpye",responseBody!!.body().toString())
            setTypeValue(responseBody?.body()?.responseContents)
            viewModel!!.getImmunizationNameList(facilityid, getNameAll)

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
            viewModel!!.progressBar.value = 8
        }
    }

    fun setTypeValue(responseContents: List<Type?>?) {

        typeNamesList = responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        hashTypeSpinnerList.clear()

        for (i in responseContents.indices) {

            hashTypeSpinnerList[responseContents[i]!!.uuid!!] = i
        }

        Log.e("hasType", hashTypeSpinnerList.toString())


        val adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            typeNamesList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.encounterTypeSpinner!!.adapter = adapter

    }


    val getNameAll = object : RetrofitCallback<ImmunizationNameResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<ImmunizationNameResponseModel>?) {
            setNameValue(responseBody?.body()?.responseContents)
            // Log.e("NMEAAA",responseBody!!.body().toString())

            viewModel?.getImmunizationAll(
                facilityid,
                patient_uuid,
                getImmunizationAllRetrofitCallBack
            )

        }

        override fun onBadRequest(errorBody: Response<ImmunizationNameResponseModel>?) {
            //Log.e("NMEAAA", "badRequest")
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
            Log.e("NMEAAA", "servererr")
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            Log.e("NMEAAA", "unAuth")
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
            viewModel!!.progressBar.value = 8
        }
    }

    fun setNameValue(responseContents: List<ImmunizationnameresponseContent?>?) {

        nameList = responseContents?.map { it?.i_uuid!! to it.i_name!! }!!.toMap().toMutableMap()

        hashNameSpinnerList.clear()

        for (i in responseContents.indices) {

            hashNameSpinnerList[responseContents[i]!!.i_uuid!!] = i
        }

        Log.e("hashName", hashNameSpinnerList.toString())

        val adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            nameList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.nameSpinner!!.adapter = adapter

    }

    val getImmunizationAllRetrofitCallBack =
        object : RetrofitCallback<GetImmunizationResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<GetImmunizationResponseModel>) {

                if (responseBody.body()?.responseContents?.size == 0) {
                    binding?.immunizationRecyclerView?.visibility = View.GONE
                    binding?.contentLinearLayout?.visibility = View.GONE
                    binding?.resultLinearLayout?.visibility = View.VISIBLE
                    binding?.noRecordsTextView?.visibility = View.GONE
                } else {
                    historyImmunizationAdapter?.setData(responseBody.body()?.responseContents)
                    binding?.immunizationRecyclerView?.visibility = View.VISIBLE
                    binding?.contentLinearLayout?.visibility = View.VISIBLE
                    binding?.resultLinearLayout?.visibility = View.GONE
                    binding?.noRecordsTextView?.visibility = View.GONE
                }

            }

            override fun onBadRequest(errorBody: Response<GetImmunizationResponseModel>?) {
                //Log.e("NMEAAA", "badRequest")
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
                //Log.e("NMEAAA", "servererr")
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                //Log.e("NMEAAA", "unAuth")
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
                viewModel!!.progressBar.value = 8
            }

        }

    val createImmunizationRetrofitCallback =
        object : RetrofitCallback<CreateImmunizationResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<CreateImmunizationResponseModel>) {

                //Log.e("CreateImmData", responseBody.toString())

                if (responseBody.body()?.code == 200) {
                    viewModel?.getImmunizationAll(
                        facilityid,
                        patient_uuid,
                        getImmunizationAllRetrofitCallBack
                    )
                    Toast.makeText(activity, responseBody.body()?.message, Toast.LENGTH_LONG)
                        .show()
                    getCurrentDate()
                    binding?.etImmunizationRemarks!!.setText("")

                    binding?.autoCompleteTextViewInstitutionName!!.setText("")
                }
            }

            override fun onBadRequest(errorBody: Response<CreateImmunizationResponseModel>?) {
                val gson = GsonBuilder().create()
                val responseModel: CreateImmunizationResponseModel
                try {
                    responseModel = gson.fromJson(
                        errorBody!!.errorBody()!!.string(),
                        CreateImmunizationResponseModel::class.java
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
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
            }

            override fun onEverytime() {
                viewModel!!.progressBar.value = 8
            }
        }


    val updateImmunizationRetrofitCallback =
        object : RetrofitCallback<ImmuUpdateResponse> {
            override fun onSuccessfulResponse(responseBody: Response<ImmuUpdateResponse>) {

                Toast.makeText(activity, responseBody.body()?.message, Toast.LENGTH_LONG).show()
                getCurrentDate()
                binding?.etImmunizationRemarks!!.setText("")
                binding?.autoCompleteTextViewInstitutionName!!.setText("")
                updateId = 0
                binding?.addtext?.text = "Add"

                if (responseBody.body()?.code == 200) {
                    viewModel?.getImmunizationAll(
                        facilityid,
                        patient_uuid,
                        getImmunizationAllRetrofitCallBack
                    )
                }
            }

            override fun onBadRequest(errorBody: Response<ImmuUpdateResponse>?) {
                val gson = GsonBuilder().create()
                val responseModel: CreateImmunizationResponseModel
                try {
                    responseModel = gson.fromJson(
                        errorBody!!.errorBody()!!.string(),
                        CreateImmunizationResponseModel::class.java
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
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure!!)
            }

            override fun onEverytime() {
                viewModel!!.progressBar.value = 8
            }
        }

    val getInstitutionSearchRetrofitCallBack =
        object : RetrofitCallback<ImmunizationInstitutionResponseModel> {

            override fun onSuccessfulResponse(response: Response<ImmunizationInstitutionResponseModel>) {
                //responseContents = Gson().toJson(response.body()?.responseContents)
                if (response.body()?.responseContents?.isNotEmpty()!!) {

                    //Log.e("SearchData", response.body()?.responseContents.toString())

                    setInstitution(response.body()?.responseContents)
                    //   customProgressDialog!!.dismiss()

                }
            }

            override fun onBadRequest(response: Response<ImmunizationInstitutionResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: ImmunizationInstitutionResponseModel
                try {
                    //      customProgressDialog!!.dismiss()
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        ImmunizationInstitutionResponseModel::class.java
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
                viewModel!!.progressBar.value = 8
                //     customProgressDialog!!.dismiss()
            }

        }


    fun setInstitution(responseContents: List<ImmunizationInstitutionresponseContent?>?) {

        institutionList = responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()
        val adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            institutionList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.autoCompleteTextViewInstitutionName!!.threshold = 1
        binding?.autoCompleteTextViewInstitutionName!!.setAdapter(adapter)

    }

    val editImmunizationCallback = object : RetrofitCallback<EditImmunizationResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<EditImmunizationResponseModel>?) {

            Log.e("ImmEdit", responseBody?.body()?.responseContent.toString())


            try {

                binding?.etImmunizationDateTime!!.setText(responseBody?.body()?.responseContent!!.immunization_date)
                binding?.etImmunizationRemarks!!.setText(responseBody.body()?.responseContent!!.comments)
                //binding?.autoCompleteTextViewInstitutionName!!.setText(responseContent!!.f_name)
                selectInstitutionUuid = responseBody.body()?.responseContent!!.facility_uuid!!
                updateId = 1

                uuid = responseBody.body()?.responseContent!!.uuid!!
                binding?.nameSpinner!!.setSelection(hashNameSpinnerList.get(responseBody.body()?.responseContent!!.immunization_uuid)!!)
                binding?.addtext!!.text = "Update"
                binding?.resultLinearLayout?.visibility = View.VISIBLE
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }

        override fun onBadRequest(errorBody: Response<EditImmunizationResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: EditImmunizationResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    EditImmunizationResponseModel::class.java
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
            viewModel!!.progressBar.value = 8
        }
    }

    fun getCurrentDate() {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val currentDateandTime = sdf.format(Date())
        binding?.etImmunizationDateTime?.setText(currentDateandTime)
    }

    fun saveOrUpdateImmunization() {
        val jsonBody = JSONObject()
        try {
            jsonBody.put("encounter_uuid", encounter_uuid)
            jsonBody.put("consultation_uuid", "1")
            jsonBody.put("patient_uuid", patient_uuid.toString())
            jsonBody.put("immunization_schedule_uuid", selectitemUuid.toString())
            jsonBody.put("schedule_uuid", 0)
            jsonBody.put("immunization_uuid", selectNameUuid.toString())
            jsonBody.put("immunization_schedule_flag_uuid", 0)
            jsonBody.put("route_uuid", 0)
            jsonBody.put("immunization_dosage_uuid", 0)
            jsonBody.put("duration", "")
            jsonBody.put("immunization_period_uuid", 0)
            jsonBody.put("immunization_schedule_status_uuid", 0)
            jsonBody.put(
                "immunization_date",
                utils!!.historyISOdate(binding?.etImmunizationDateTime!!.text.trim().toString())
            )
            jsonBody.put(
                "administered_date",
                utils!!.historyISOdate(binding?.etImmunizationDateTime!!.text.trim().toString())
            )
            jsonBody.put("display_order", 0)
            jsonBody.put("comments", binding?.etImmunizationRemarks!!.text.toString())
            jsonBody.put("facility_uuid", selectInstitutionUuid)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        if (updateId == 0) {
            Log.e("JsBody", jsonBody.toString())
            viewModel?.createImmunization(
                this.facilityid,
                body, createImmunizationRetrofitCallback
            )
        } else {

            val jsonBodyupdate = JSONObject()
            try {
                jsonBodyupdate.put("comments", binding?.etImmunizationRemarks!!.text.toString())
                jsonBodyupdate.put("immunization_uuid", selectNameUuid)
                jsonBodyupdate.put("encounter_uuid", encounter_uuid)
                jsonBodyupdate.put("facility_uuid", selectInstitutionUuid)

            } catch (e: JSONException) {
                e.printStackTrace()
            }
            val bodyupdate = RequestBody.create(
                okhttp3.MediaType.parse("application/json; charset=utf-8"),
                jsonBodyupdate.toString()
            )


            viewModel?.updateImmunization(
                this.facilityid, uuid, bodyupdate, updateImmunizationRetrofitCallback
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
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_DOCTOR_UUID, doctor_en_uuid)
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_UUID, encounter_uuid)
                    saveOrUpdateImmunization()

                } else {
                    viewModel?.createEncounter(
                        patient_uuid,
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
            patient_uuid =
                response.body()?.responseContents?.encounterDoctor?.patient_uuid!!.toInt()

            saveOrUpdateImmunization()
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



