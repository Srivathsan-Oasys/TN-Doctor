package com.hmis_tn.doctor.ui.emr_workflow.history.surgery.ui

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
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.HistorySurguryFragmentBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.history.immunization.model.ImmunizationInstitutionResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.history.immunization.model.ImmunizationInstitutionresponseContent
import com.hmis_tn.doctor.ui.emr_workflow.history.surgery.model.response.*
import com.hmis_tn.doctor.ui.emr_workflow.history.surgery.model.response.edit.EditSurgeryResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.history.surgery.viewmodel.HistorySurgeryViewModel
import com.hmis_tn.doctor.ui.emr_workflow.history.surgery.viewmodel.HistorySurgeryViewModelFactory
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.view.lab.model.PrevLabResponseModel
import com.hmis_tn.doctor.utils.Utils
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HistorySurgeryChildFragment : Fragment() {
    private var historySurgeryAdapter: HistorySurgeryAdapter? = null
    private var binding: HistorySurguryFragmentBinding? = null
    private var viewModel: HistorySurgeryViewModel? = null
    private var utils: Utils? = null
    private var institutionNamesList = mutableMapOf<Int, String>()
    private var NamesList = mutableMapOf<Int, String>()
    private var selectitemid: Int = 0
    private var selectNameitemid: Int = 0
    private var patientId: Int = 0
    private var encounter_uuid: Int? = 0
    private var encounter_type_uuid: Int? = 0
    private var facilityid: Int = 0
    private var doctor_en_uuid: Int = 0
    private var mYear: Int? = null
    private var mMonth: Int? = null
    private var mDay: Int? = null
    private var mHour: Int? = null
    private var mMinute: Int? = null
    private var mSecond: Int? = null
    private val hashNameSpinnerList: HashMap<Int, Int> = HashMap()
    private val hashInstitutionSpinnerList: HashMap<Int, Int> = HashMap()
    private var updateId: Int = 0
    private var uuid: Int = 0
    private var dateString: String = ""

    private var surgeryNameSpinnerList: ArrayList<NameresponseContent?>? = ArrayList()

    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    var appPreferences: AppPreferences? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.history_surgury_fragment,
                container,
                false
            )

        viewModel = HistorySurgeryViewModelFactory(
            requireActivity().application
        ).create(HistorySurgeryViewModel::class.java)
        binding?.viewModel = viewModel
        binding!!.lifecycleOwner = this
        utils = Utils(requireContext())
        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilityid = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        patientId = appPreferences?.getInt(AppConstants.PATIENT_UUID)!!
        encounter_type_uuid = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        var userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

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

        binding!!.clear.setOnClickListener {

            val sdf1 = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
            val currentDateandTime1 = sdf1.format(Date())
            binding?.etFamilyDateTime?.setText(currentDateandTime1)

            binding!!.etRemarks.setText("")

            binding!!.surguryNameSpinner.setSelection(0)

            binding?.autoCompleteTextViewInstitutionName?.setText("")

            selectitemid = 0


        }

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


        binding?.surguryNameSpinner!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectNameitemid = 0
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {

                    if (pos == 0) {
                        selectNameitemid = 0
                    } else {

                        val itemValue = parent!!.getItemAtPosition(pos).toString()

                        selectNameitemid =
                            NamesList.filterValues { it == itemValue }.keys.toList()[0]
                    }

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

                    selectitemid =
                        institutionNamesList.filterValues { it == itemValue }.keys.toList()[0]

                    Log.e("InsSelectData", selectitemid.toString())
                }

            }


        binding?.addNewSurgeryIconView!!.setOnClickListener {
            if (binding?.etFamilyDateTime?.text?.trim()!!
                    .isEmpty() || binding?.etRemarks!!.text.trim().isEmpty()
                || selectitemid.equals(0) || selectNameitemid.equals(0)
            ) {
                Toast.makeText(activity, "Enter all fields", Toast.LENGTH_LONG).show()
            } else {

                viewModel?.getEncounter(
                    facilityid,
                    patientId,
                    encounter_type_uuid!!,
                    fetchEncounterRetrofitCallBack
                )

            }
        }

        viewModel?.getSurgeryNameCallback(facilityid, surgeryNameRetrofitCallback)

        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding?.historySurguryRecyclerView!!.layoutManager = linearLayoutManager
        historySurgeryAdapter = HistorySurgeryAdapter(
            requireActivity(),
            ArrayList()
        )
        binding?.historySurguryRecyclerView!!.adapter = historySurgeryAdapter


        historySurgeryAdapter!!.setOnClickListener(object : HistorySurgeryAdapter.OnClickListener {
            override fun OnClick(responseContent: SurgeryresponseContent?, position: Int) {
                Log.e("SelectSurgeryData", responseContent!!.toString())

                viewModel?.getSurgeryEdit(
                    facilityid,
                    responseContent.ps_uuid!!,
                    editSurgeryCallback
                )
                binding?.autoCompleteTextViewInstitutionName?.setText(responseContent.institution_name)


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


    val surgeryRetrofitCallback = object : RetrofitCallback<HistorySurgeryResponseModel> {
        override fun onSuccessfulResponse(response: Response<HistorySurgeryResponseModel>) {

            //Log.e("SurData", response.body()?.toString())
            if (response.body()?.responseContent?.size == 0) {
                binding?.historySurguryRecyclerView?.visibility = View.GONE
                binding?.mainLayout?.visibility = View.GONE
                binding?.resultLinearLayout?.visibility = View.VISIBLE
                binding?.noRecordsTextView?.visibility = View.GONE
            } else {
                historySurgeryAdapter?.setData(response.body()?.responseContent)
                binding?.historySurguryRecyclerView?.visibility = View.VISIBLE
                binding?.mainLayout?.visibility = View.VISIBLE
                binding?.resultLinearLayout?.visibility = View.GONE
                binding?.noRecordsTextView?.visibility = View.GONE
            }

        }

        override fun onBadRequest(response: Response<HistorySurgeryResponseModel>) {

            //Log.e("SurData", "badReq")
            val gson = GsonBuilder().create()
            val responseModel: PrevLabResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    PrevLabResponseModel::class.java
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
            //Log.e("InsData", response.message())
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
        }

        override fun onUnAuthorized() {
            //Log.e("InsData", "UnAuth")
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


    val surgeryNameRetrofitCallback = object : RetrofitCallback<SurgeryNameResponseModel> {
        override fun onSuccessfulResponse(response: Response<SurgeryNameResponseModel>) {

            //Log.e("nameData", response.body()?.toString())

            surgeryNameSpinnerList?.add(NameresponseContent())
            surgeryNameSpinnerList?.addAll(response.body()?.responseContents!!)
            setNameValue(surgeryNameSpinnerList)

            viewModel?.getSurgeryCallback(patientId, facilityid, surgeryRetrofitCallback)

        }

        override fun onBadRequest(response: Response<SurgeryNameResponseModel>) {

            // Log.e("nameData", "badReq")
            val gson = GsonBuilder().create()
            val responseModel: PrevLabResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    PrevLabResponseModel::class.java
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


    fun setNameValue(responseContents: List<NameresponseContent?>?) {

        NamesList = responseContents?.map { it!!.uuid!! to it.name!! }!!.toMap().toMutableMap()

        hashNameSpinnerList.clear()

        for (i in responseContents.indices) {

            hashNameSpinnerList[responseContents[i]!!.uuid!!] = i
        }

        //Log.e("Name",hashNameSpinnerList.toString())

        val adapter = ArrayAdapter<String>(
            requireActivity(),
            R.layout.spinner_item,
            NamesList.values.toMutableList()
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.surguryNameSpinner!!.adapter = adapter


    }


    val createSurgeryRetrofitCallback = object : RetrofitCallback<CreateSurgeryResponseModel> {
        override fun onSuccessfulResponse(response: Response<CreateSurgeryResponseModel>) {

            //Log.e("createSurData", response.body()?.toString())

            Toast.makeText(activity, response.body()?.message, Toast.LENGTH_LONG).show()
            getCurrentDate()
            binding?.etRemarks!!.setText("")
            binding?.autoCompleteTextViewInstitutionName?.setText("")
            selectitemid = 0

            if (response.body()?.code == 200) {
                viewModel?.getSurgeryCallback(patientId, facilityid, surgeryRetrofitCallback)
            }

        }

        override fun onBadRequest(response: Response<CreateSurgeryResponseModel>) {

            //Log.e("createSurData", "badReq")
            val gson = GsonBuilder().create()
            val responseModel: PrevLabResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    PrevLabResponseModel::class.java
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

    val updateSurgeryRetrofitCallback = object : RetrofitCallback<SurgeryUpdateResponseModel> {
        override fun onSuccessfulResponse(response: Response<SurgeryUpdateResponseModel>) {

            //Log.e("updateSurgData", response.body()?.toString())

            Toast.makeText(activity, response.body()?.message, Toast.LENGTH_LONG).show()
            getCurrentDate()
            binding?.etRemarks!!.setText("")
            binding?.autoCompleteTextViewInstitutionName?.setText("")
            updateId = 0
            selectitemid = 0
            binding?.addtext?.text = "Add"
            if (response.body()?.code == 200) {
                viewModel?.getSurgeryCallback(patientId, facilityid, surgeryRetrofitCallback)
            }

        }

        override fun onBadRequest(response: Response<SurgeryUpdateResponseModel>) {

            val gson = GsonBuilder().create()
            val responseModel: SurgeryUpdateResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    SurgeryUpdateResponseModel::class.java
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


    val editSurgeryCallback = object : RetrofitCallback<EditSurgeryResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<EditSurgeryResponseModel>?) {

            //Log.e("SurEdit", responseBody?.body()?.responseContent.toString())

            try {
                binding?.etFamilyDateTime!!.setText(responseBody?.body()?.responseContent?.created_date)
                binding?.etRemarks!!.setText(responseBody?.body()?.responseContent?.comments)
                updateId = 1
                uuid = responseBody!!.body()?.responseContent?.uuid!!
                binding?.surguryNameSpinner!!.setSelection(hashNameSpinnerList.get(responseBody.body()?.responseContent?.procedure_uuid!!)!!)
                selectitemid = responseBody.body()?.responseContent?.facility_uuid!!
                binding?.addtext!!.text = "Update"
                binding?.resultLinearLayout?.visibility = View.VISIBLE
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }

        override fun onBadRequest(errorBody: Response<EditSurgeryResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: EditSurgeryResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    EditSurgeryResponseModel::class.java
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

        institutionNamesList =
            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()
        val adapter = ArrayAdapter<String>(
            requireActivity(),
            R.layout.spinner_item,
            institutionNamesList.values.toMutableList()
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.autoCompleteTextViewInstitutionName!!.threshold = 1
        binding?.autoCompleteTextViewInstitutionName!!.setAdapter(adapter)

    }

    fun getCurrentDate() {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val currentDateandTime = sdf.format(Date())
        binding?.etFamilyDateTime?.setText(currentDateandTime)
    }

    fun saveOrUpdateSurgery() {
        val utils = Utils(requireActivity())

        val jsonBody = JSONObject()
        try {
            jsonBody.put("facility_uuid", selectitemid.toString())
            jsonBody.put("patient_uuid", patientId)
            jsonBody.put("encounter_uuid", encounter_uuid)
            jsonBody.put("consultation_uuid", 1)
            jsonBody.put("procedure_uuid", selectNameitemid.toString())
            jsonBody.put("surgery_name", "SurgeryMobileApp")
            jsonBody.put("surgery_description", "SurgeryMobileApp")
            jsonBody.put(
                "performed_date",
                utils.historyISOdate(binding?.etFamilyDateTime!!.text.trim().toString())
            )
            jsonBody.put("comments", binding?.etRemarks!!.text.toString())

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val body = RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonBody.toString()
        )

        if (updateId == 0) {
            viewModel?.createSurgery(
                this.facilityid,
                body, createSurgeryRetrofitCallback
            )
        } else {

            val jsonBody1 = JSONObject()
            try {
                jsonBody1.put("facility_uuid", selectitemid.toString())
                jsonBody1.put("procedure_uuid", selectNameitemid.toString())
                jsonBody1.put("comments", binding?.etRemarks!!.text.toString())

            } catch (e: JSONException) {
                e.printStackTrace()
            }
            val body1 = RequestBody.create(
                okhttp3.MediaType.parse("application/json; charset=utf-8"),
                jsonBody1.toString()
            )

            viewModel?.updateSurgery(facilityid, uuid, body1, updateSurgeryRetrofitCallback)
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
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_UUID, encounter_uuid!!)
                    saveOrUpdateSurgery()

                } else {
                    viewModel?.createEncounter(
                        patientId,
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
            patientId = response.body()?.responseContents?.encounterDoctor?.patient_uuid!!.toInt()

            saveOrUpdateSurgery()
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


