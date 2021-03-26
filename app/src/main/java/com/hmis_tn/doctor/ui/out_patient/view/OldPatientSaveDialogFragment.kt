package com.hmis_tn.doctor.ui.out_patient.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.gson.GsonBuilder

import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.DialogOldPatientFragmentBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.dashboard.model.*
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseModel

import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.ErrorAPIClass
import com.hmis_tn.doctor.ui.out_patient.model.requestaddpatient.RequestAddPatient
import com.hmis_tn.doctor.ui.out_patient.model.responseaddpatient.ResponseAddPatient
import com.hmis_tn.doctor.ui.out_patient.search_response_model.ResponseContent
import com.hmis_tn.doctor.ui.out_patient.view_model.OldPatientSaveViewModel
import com.hmis_tn.doctor.ui.out_patient.view_model.OldPatientSaveViewModelFactory
import com.hmis_tn.doctor.ui.quick_reg.model.FacilityLocationResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.QuickRegistrationSaveResponseModel

import com.hmis_tn.doctor.utils.Utils
import kotlinx.android.synthetic.main.activity_quick_registration.*
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class OldPatientSaveDialogFragment : DialogFragment() {
    private var DOB: String = ""
    private var tat_start_time: String = ""
    private var selectStateUuid: Int? = 0
    private var selectcountryUuid: Int? = 0
    private var selectDistictUuid: Int? = 0
    private var oldPINnumber: String? = ""
    private var firstName: String? = ""
    private var creatdate: String? = ""
    private var gender: String? = ""
    private var age: Int? = 0
    private var Str_auto_id: Int? = 0
    private var content: String? = null
    private var deletefavouriteID: Int? = 0
    lateinit var drugNmae: TextView
    private var viewModel: OldPatientSaveViewModel? = null
    var binding: DialogOldPatientFragmentBinding? = null
    var appPreferences: AppPreferences? = null
    private var utils: Utils? = null

    private var favouriteData: FavouritesModel? = null
    private var facility_UUID: Int? = 0
    private var customdialog: Dialog? = null
    private var deparment_UUID: Int? = 0
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var A1selectGenderUuid: Int = 0
    private var A1selectSalutationUuid: Int = 0
    private var A1selectPeriodUuid: Int = 0

    private var CovidGenderList = mutableMapOf<Int, String>()
    private var CovidPeriodList = mutableMapOf<Int, String>()
    private var CovidSalutationList = mutableMapOf<Int, String>()

    private val hashPeriodSpinnerList: HashMap<Int, Int> = HashMap()
    private val hashGenderSpinnerList: HashMap<Int, Int> = HashMap()
    private val hashSalutationSpinnerList: HashMap<Int, Int> = HashMap()

    private var listAllAddDepartmentItems: List<FavAddAllDepatResponseContent?> = ArrayList()
    private var favAddResponseMap = mutableMapOf<Int, String>()
    var callbackOldPatientProcess: OnOldPatientProcessListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString(AppConstants.ALERTDIALOG)
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
                R.layout.dialog_old_patient_fragment,
                container,
                false
            )
        viewModel = OldPatientSaveViewModelFactory(
            requireActivity().application
        )
            .create(OldPatientSaveViewModel::class.java)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        utils = Utils(requireContext())
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        tat_start_time = sdf.format(Date())

        facility_UUID = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        deparment_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)


        binding?.closeImageView?.setOnClickListener {
            dialog?.dismiss()
        }
        binding?.cancel?.setOnClickListener {
            dialog?.dismiss()
        }

        val args = arguments
        if (args == null) {

            //  Toast.makeText(activity, "arguments is null ", Toast.LENGTH_LONG).show()
        } else {
            // get value from bundle..

            oldPINnumber = args.getString(AppConstants.OLDPINNUMBER)
            firstName = args.getString(AppConstants.FirstTime)
            creatdate = args.getString(AppConstants.Createdate)
            gender = args.getString(AppConstants.GENDER)
            age = args.getInt(AppConstants.AGE)
            binding?.edtage?.setText("" + age)
            binding?.name?.setText(firstName)

            /*val sdf1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val sdf = SimpleDateFormat("yyyy")
            val createdDate =sdf1.parse(creatdate)
            val createdYear= sdf.format(createdDate)
            val currentYear =sdf.format(Date())
            age = (Integer.parseInt(currentYear)-Integer.parseInt(createdYear)) +age!!*/
            viewModel!!.getFaciltyLocation(facilityLocationResponseCallback)

        }

        binding?.cancel?.setOnClickListener {
            dialog!!.dismiss()
        }
        binding?.savecardview?.setOnClickListener {
            val namefield = binding?.name?.text.toString()
            var edtagefiedl = binding?.name?.text.toString()
            val oldpinnumber = oldPINnumber
            val mobilenumber = binding?.mobilenumber?.text.toString()

            if (namefield.isEmpty()) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    "Enter the name"
                )
                return@setOnClickListener
            }

            if (edtagefiedl.isEmpty()) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    "Enter the age"
                )
                return@setOnClickListener
            }
            if (mobilenumber.isEmpty()) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    "Enter the mobile number"
                )
                return@setOnClickListener
            }

            if (A1selectPeriodUuid == 0) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    "Enter the Period"
                )
                return@setOnClickListener
            }
            if (A1selectSalutationUuid == 0) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    "Enter the Salutation"
                )
                return@setOnClickListener
            }

            if (A1selectGenderUuid == 0) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    "Enter the Gender"
                )
                return@setOnClickListener
            }

            when (A1selectPeriodUuid) {
                4 -> {

                    DOB =
                        utils!!.getYear(
                            binding!!.edtage.text.toString().toInt()
                        )
                            .toString()
                }
                2 -> {
                    DOB =
                        utils!!.getAgeMonth(
                            binding!!.edtage.text.toString().toInt()
                        )
                            .toString()

                }
                3 -> {

                    DOB =
                        utils!!.getDateDaysAgo(
                            binding!!.edtage.text.toString().toInt()
                        )
                            .toString()

                }
            }

            Log.i("DOB", DOB)

            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
            val endtime = sdf.format(Date())
            val requestAddPatient: RequestAddPatient = RequestAddPatient()
            requestAddPatient.first_name = namefield
            requestAddPatient.mobile = mobilenumber
            requestAddPatient.gender_uuid = A1selectGenderUuid
            requestAddPatient.department_uuid = deparment_UUID?.toString()
            requestAddPatient.speciality_department_uuid = 0
            requestAddPatient.unit_uuid = ""
            requestAddPatient.old_pin = oldpinnumber?.toLong()
            requestAddPatient.title_uuid = A1selectSalutationUuid
            requestAddPatient.period_uuid = A1selectPeriodUuid
            requestAddPatient.registred_facility_uuid = "1"
            requestAddPatient.age = age
            requestAddPatient.created_date = creatdate
            requestAddPatient.country_uuid = selectcountryUuid
            requestAddPatient.state_uuid = selectStateUuid
            requestAddPatient.district_uuid = selectDistictUuid
            requestAddPatient.saveExists = false
            requestAddPatient.is_adult = 1
            requestAddPatient.tat_start_time = tat_start_time
            requestAddPatient.tat_end_time = endtime
            requestAddPatient.session_uuid = 3
            requestAddPatient.dob = DOB

            viewModel?.quickRegistrationSaveList(
                requestAddPatient,
                saveAddPatientRetrofitCallback
            )

        }
        binding?.viewModel?.getAllDepartment(facility_UUID, favLabAddAllDepartmentCallBack)

        binding?.gender!!.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->

                        viewModel?.getCovidGenderList(facility_UUID!!, covidGenderResponseCallback)

                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        binding?.gender?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    A1selectGenderUuid =
                        CovidGenderList.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(position).toString()
                    A1selectGenderUuid =
                        CovidGenderList.filterValues { it == itemValue }.keys.toList()[0]

                    Log.e(
                        "Gender",
                        binding?.gender?.selectedItem.toString() + "-" + A1selectGenderUuid
                    )
                }

            }

        binding?.period!!.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->

                        viewModel?.getCovidPeriodList(facility_UUID!!, covidPeriodResponseCallback)

                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        binding?.salutationSpinner!!.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->

                        viewModel?.getCovidNameTitleList(
                            facility_UUID!!,
                            covidSalutationResponseCallback
                        )
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        binding?.salutationSpinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    A1selectSalutationUuid =
                        CovidSalutationList.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(position).toString()
                    A1selectSalutationUuid =
                        CovidSalutationList.filterValues { it == itemValue }.keys.toList()[0]

                    Log.e(
                        "Salutation",
                        binding?.salutationSpinner?.selectedItem.toString() + "-" + A1selectSalutationUuid
                    )
                }

            }

        binding?.period?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    A1selectPeriodUuid =
                        CovidPeriodList.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(position).toString()
                    A1selectPeriodUuid =
                        CovidPeriodList.filterValues { it == itemValue }.keys.toList()[0]



                    Log.e(
                        "Period",
                        binding?.period?.selectedItem.toString() + "-" + A1selectPeriodUuid
                    )
                }

            }
        return binding!!.root
    }

    val covidGenderResponseCallback = object : RetrofitCallback<CovidGenderResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<CovidGenderResponseModel>?) {

            A1selectGenderUuid = responseBody?.body()?.responseContents?.get(0)!!.uuid!!
            setGender(responseBody.body()?.responseContents)
        }

        override fun onBadRequest(errorBody: Response<CovidGenderResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: CovidGenderResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    CovidGenderResponseModel::class.java
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
            viewModel!!.progress.value = 8
        }
    }

    fun setGender(responseContents: List<GenderresponseContent?>?) {
        CovidGenderList =
            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        hashGenderSpinnerList.clear()

        for (i in responseContents.indices) {

            hashGenderSpinnerList[responseContents[i]!!.uuid!!] = i
        }

        if (gender != null) {
            when {
                gender.equals("SXML") -> {
                    binding?.period!!.setSelection(hashGenderSpinnerList.get(1)!!)
                }
                gender.equals("SXFML") -> {
                    binding?.period!!.setSelection(hashGenderSpinnerList.get(2)!!)
                }
            }
        } else {
            binding?.period!!.setSelection(hashGenderSpinnerList.get(3)!!)
        }

        val adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            CovidGenderList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.gender!!.adapter = adapter

    }

    val covidPeriodResponseCallback = object : RetrofitCallback<CovidPeriodResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<CovidPeriodResponseModel>?) {

            A1selectPeriodUuid = responseBody?.body()?.responseContents?.get(0)!!.uuid!!
            setPeriod(responseBody.body()?.responseContents)
        }

        override fun onBadRequest(errorBody: Response<CovidPeriodResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: CovidPeriodResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    CovidPeriodResponseModel::class.java
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
            viewModel!!.progress.value = 8
        }
    }

    fun setPeriod(responseContents: List<PeriodresponseContent?>?) {
        CovidPeriodList =
            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        hashPeriodSpinnerList.clear()

        for (i in responseContents.indices) {

            hashPeriodSpinnerList[responseContents[i]!!.uuid!!] = i
        }

        binding?.period!!.setSelection(hashPeriodSpinnerList.get(4)!!)


        val adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            CovidPeriodList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.period!!.adapter = adapter

    }

    val covidSalutationResponseCallback =
        object : RetrofitCallback<CovidSalutationTitleResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<CovidSalutationTitleResponseModel>?) {
                A1selectSalutationUuid = responseBody?.body()?.responseContents?.get(0)!!.uuid!!
                setSalutation(responseBody.body()?.responseContents)

            }

            override fun onBadRequest(errorBody: Response<CovidSalutationTitleResponseModel>?) {
                val gson = GsonBuilder().create()
                val responseModel: CovidSalutationTitleResponseModel
                try {
                    responseModel = gson.fromJson(
                        errorBody!!.errorBody()!!.string(),
                        CovidSalutationTitleResponseModel::class.java
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
                viewModel!!.progress.value = 8
            }
        }

    fun setSalutation(responseContents: List<SalutationresponseContent?>?) {
        CovidSalutationList =
            responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()
        hashSalutationSpinnerList.clear()

        for (i in responseContents.indices) {
            hashSalutationSpinnerList[responseContents[i]!!.uuid!!] = i
        }
        Log.i("respose", "" + hashSalutationSpinnerList)
        val adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            CovidSalutationList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.salutationSpinner!!.adapter = adapter

    }

    val favLabAddAllDepartmentCallBack = object : RetrofitCallback<FavAddAllDepatResponseModel> {
        @SuppressLint("NewApi")
        override fun onSuccessfulResponse(responseBody: Response<FavAddAllDepatResponseModel>?) {
            Log.i("", "" + responseBody?.body()?.responseContents)
            listAllAddDepartmentItems = responseBody?.body()?.responseContents!!
            favAddResponseMap =
                listAllAddDepartmentItems.map { it?.uuid!! to it.name }.toMap().toMutableMap()

            val adapter =
                ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    favAddResponseMap.values.toMutableList()
                )

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding?.spinnerFavLabdepartment!!.adapter = adapter

        }

        override fun onBadRequest(errorBody: Response<FavAddAllDepatResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: FavAddAllDepatResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    FavAddAllDepatResponseModel::class.java
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
            viewModel!!.progress.value = 8
        }
    }

    fun setRefreshOldPatientListener(callback: OnOldPatientProcessListener) {
        callbackOldPatientProcess = callback
    }

    // or a separate test implementation.
    interface OnOldPatientProcessListener {
        fun onRefreshOldPatientList(responseContent: ResponseContent)
    }

    val facilityLocationResponseCallback =
        object : RetrofitCallback<FacilityLocationResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<FacilityLocationResponseModel>?) {

                if (responseBody!!.body()!!.responseContents != null) {


                    if (responseBody.body()!!.responseContents.country_master != null) {

                        selectcountryUuid =
                            responseBody.body()!!.responseContents.country_master.uuid
                    }

                    if (responseBody.body()!!.responseContents.state_master != null) {
                        selectStateUuid = responseBody.body()!!.responseContents.state_master.uuid

                    }

                    if (responseBody.body()!!.responseContents.district_master != null) {
                        selectDistictUuid =
                            responseBody.body()!!.responseContents.district_master.uuid
                    }

                }

            }

            override fun onBadRequest(errorBody: Response<FacilityLocationResponseModel>?) {
                val gson = GsonBuilder().create()
                val responseModel: FacilityLocationResponseModel
                try {
                    responseModel = gson.fromJson(
                        errorBody!!.errorBody()!!.string(),
                        FacilityLocationResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        "Something Wrong"
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
                viewModel!!.progress.value = 8
            }
        }

    var saveAddPatientRetrofitCallback = object : RetrofitCallback<ResponseAddPatient> {

        override fun onSuccessfulResponse(responseBody: Response<ResponseAddPatient>?) {

            utils?.showToast(
                R.color.positiveToast,
                mainLayout!!,
                "Register Success"
            )

            val responseContent: ResponseContent = ResponseContent()

            responseContent.first_name = responseBody!!.body()?.responseContent?.first_name
            responseContent.last_name = responseBody.body()?.responseContent?.last_name
            responseContent.age = responseBody.body()?.responseContent?.age
            responseContent.uhid = responseBody.body()?.responseContent?.uhid
            responseContent.patient_detail?.mobile =
                responseBody.body()?.responseContent?.patient_details?.mobile
            responseContent.uuid = responseBody.body()?.responseContent?.uuid

            Log.i("", "" + responseBody.body()?.responseContent)
            callbackOldPatientProcess!!.onRefreshOldPatientList(responseContent)
            dialog!!.dismiss()

        }

        override fun onBadRequest(response: Response<ResponseAddPatient>) {
            Log.e("badreq", response.toString())
            val gson = GsonBuilder().create()
            val responseModel: QuickRegistrationSaveResponseModel
            var mError = ErrorAPIClass()
            try {
                mError = gson.fromJson(response.errorBody()!!.string(), ErrorAPIClass::class.java)

                Toast.makeText(context!!, mError.message, Toast.LENGTH_LONG).show()


            } catch (e: IOException) { // handle failure to read error
            }
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