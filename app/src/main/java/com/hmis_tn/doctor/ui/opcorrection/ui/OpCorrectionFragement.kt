package com.hmis_tn.doctor.ui.opcorrection.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.FragmentOpCorrectionBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.dashboard.model.*
import com.hmis_tn.doctor.ui.dashboard.model.registration.*
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.FavAddResponseModel
import com.hmis_tn.doctor.ui.quick_reg.model.*
import com.hmis_tn.doctor.ui.quick_reg.model.activitysession.ResponseContentsactivitysession
import com.hmis_tn.doctor.ui.quick_reg.model.request.QuickRegistrationRequestModel
import com.hmis_tn.doctor.ui.quick_reg.view.SearchPatientDialogFragment
import com.hmis_tn.doctor.ui.quickregistration.model.GetAllResponse
import com.hmis_tn.doctor.ui.quickregistration.model.GetAllResponseModel
import com.hmis_tn.doctor.ui.quickregistration.model.OpCorrectionRequest
import com.hmis_tn.doctor.ui.quickregistration.model.QuickNewPrevilege
import com.hmis_tn.doctor.ui.quickregistration.ui.SessionAdapter
import com.hmis_tn.doctor.ui.quickregistration.viewmodel.QuickRegistrationNewViewModel
import com.hmis_tn.doctor.ui.quickregistration.viewmodel.QuickRegistrationNewViewModelFactory
import com.hmis_tn.doctor.utils.Utils
import com.hmis_tn.doctor.utils.custom_views.CustomProgressDialog
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OpCorrectionFragement : Fragment(), SearchPatientDialogFragment.OnOrderProcessListener {
    private var MobileNumber: String? = ""
    private var quicksearch: String? = ""
    private var pinnumber: String? = ""
    private var session_uuid: Int? = 0
    private var currentDateandTime: String? = null
    private var sharepreferlastPin: String? = ""
    private var currentPage = 0
    private var pageSize = 10


    var salutaioData: ArrayList<SalutationresponseContent> = ArrayList()

    var GenderlistData: ArrayList<GenderresponseContent?> = ArrayList()

    private var mAdapter: SessionAdapter? = null

    var opMorStartTime: Date? = null
    var opMorEndTime: Date? = null
    var opEveStartTime: Date? = null

    //    private var roleUUID: Int?  =0
    private var array_testmethod: List<ResponseTestMethodContent?>? = ArrayList()
    private var array_testmethod1: List<ResponseTestMethodContent?>? = ArrayList()
    var NasopharyngealID1: Int? = 0
    var rtpcrID: Int? = 0

    var TimesetStatus: Boolean = true

    private var customProgressDialog: CustomProgressDialog? = null
    var rtpcrID1: Int? = 0
    var NasopharyngealID: Int? = 0

    var session_name: String? = "Morning"

    private var uhid: String = ""
    var binding: FragmentOpCorrectionBinding? = null
    var utils: Utils? = null
    private var viewModel: QuickRegistrationNewViewModel? = null

    private var prvage: Boolean = false

    private var registerDate: String = ""
    var appPreferences: AppPreferences? = null
    private var autocompleteNameTestResponse: List<CovidRegistrationSearchResponseContent>? = null

    private var customdialog: Dialog? = null

    private var addressenable: Boolean? = null

    private var aatherNo: String = ""

    private var TOTAL_PAGES: Int = 0
    private var CovidGenderList = mutableMapOf<Int, String>()
    private var CovidPeriodList = mutableMapOf<Int, String>()

    private var SalutaionList = mutableMapOf<Int, String>()

    private var ProfestioanlList = mutableMapOf<Int, String>()

    private var SuffixList = mutableMapOf<Int, String>()

    private var UnitList = mutableMapOf<Int, String>()

    private var typeNamesList = mutableMapOf<Int, String>()

    private var CommunityList = mutableMapOf<Int, String>()

    private var facility_id: Int? = 0
    private var departmentUUId: Int? = 0
    private var patientUUId: Int? = 0

    private var selectdepartmentUUId: Int? = 0

    private var mYear: Int? = null
    private var mMonth: Int? = null
    private var mDay: Int? = null
    private var fromDate: String = ""
    private var fromDateRev: String = ""

    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var updateId: Int? = 0
    private var selectPeriodUuid: Int? = 0
    private var selectGenderUuid: Int? = 0
    private var radioid: Int? = 0
    private var locationId: Int? = 0
    private var onNext: Int? = 0
    private var Matanity: Boolean? = false

    private var quickRegistrationSaveResponseModel = QuickRegistrationRequestModel()
    private var quickRegistrationPinSaveResponseModel = QuickRegistrationRequestModel()

    private val hashSalutaionSpinnerList: HashMap<Int, Int> = HashMap()
    private val hashProfestioanlSpinnerList: HashMap<Int, Int> = HashMap()
    private val hashSuffixSpinnerList: HashMap<Int, Int> = HashMap()
    private val hashUnitSpinnerList: HashMap<Int, Int> = HashMap()
    private val hashCommunitypinnerList: HashMap<Int, Int> = HashMap()
    private val hashPeriodSpinnerList: HashMap<Int, Int> = HashMap()
    private val hashGenderSpinnerList: HashMap<Int, Int> = HashMap()
    private val hashNationalitySpinnerList: HashMap<Int, Int> = HashMap()
    private val hashStateSpinnerList: HashMap<Int, Int> = HashMap()
    private val hashDistrictSpinnerList: HashMap<Int, Int> = HashMap()
    private val hashBlockSpinnerList: HashMap<Int, Int> = HashMap()
    private val hashVillageSpinnerList: HashMap<Int, Int> = HashMap()

    private var is_dob_auto_calculate: Int = 1
    var selectNationalityUuid: Int = 0
    var selectStateUuid: Int = 0
    var selectDistictUuid: Int = 0
    var selectBelongUuid: Int = 0
    var selectVillageUuid: Int = 0
    var selectSalutationUuid: Int = 0
    var selectProffestionalUuid: Int = 0
    var selectSuffixUuid: Int = 0
    var selectCoummityUuid: Int = 0
    var selectUnitUuid: Int = 0
    var locationMasterX: LocationMasterX = LocationMasterX()
    var gettest: GetTestMasterList = GetTestMasterList()
    var getReference: GetReference = GetReference()
    var encounter_doctor_id: Int? = 0
    var encounter_id: Int? = 0

    private var sariStatus: Boolean = false
    private var iliStatus: Boolean = false
    private var nosymptomsStatus: Boolean = false

    private var CovidNationalityList = mutableMapOf<Int, String>()
    private var CovidStateList = mutableMapOf<Int, String>()
    private var CovidDistictList = mutableMapOf<Int, String>()
    private var CovidBlockList = mutableMapOf<Int, String>()
    private var CovidVillageList = mutableMapOf<Int, String>()

    private var ispublic: Boolean? = null
    private var sampleid: Int? = null
    private var selectLabNameID: Int? = 0
    private var alreadyExists: Boolean = false
    private var setdob: Boolean = false
    private var adultFromAge: Int = 14
    private var childToAge: Int = 14

    private var isadult: Int = 0
    private var adultToAge: Int = 100

    private var facility_Name: String = ""
    private var oldPin: String? = ""
    private var created_date: String = ""

    var timer: Timer? = null
    var timerTask: TimerTask? = null

    var mon_op_start_time = ""
    var mon_op_end_time = ""
    var Evn_op_start_time = ""
    var Evn_op_end_time = ""

    var opEveEndTimeMin: Date? = null
    var opExTime: Int? = 10
    var pdsNumber: String? = ""

    var linearLayoutManager: LinearLayoutManager? = null

    var morning_session_uuid: Int = 1
    var morning_session_name: String = "Morning"

    var evening_session_uuid: Int = 2
    var evening_session_name: String = "Evening"

    var case_session_uuid: Int = 3
    var case_session_name: String = "Caslaty"
    var roleid: Int? = 0
    var tat_start_time = ""

    //private var customProgressDialog: CustomProgressDialog? = null
    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_op_correction,
                container,
                false
            )

        viewModel = QuickRegistrationNewViewModelFactory(
            requireActivity().application
        )
            .create(QuickRegistrationNewViewModel::class.java)
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        utils = Utils(requireContext())

        requireActivity().window
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        departmentUUId = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        sharepreferlastPin = appPreferences?.getString(AppConstants.LASTPIN)
        facility_Name = appPreferences?.getString(AppConstants.INSTITUTION_NAME)!!

        //    selectdepartmentUUId = departmentUUId

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        roleid = userDataStoreBean?.role_uuid

        Utils(requireContext()).setCalendarLocale("en", requireContext())

        linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding?.sessionlist!!.layoutManager = linearLayoutManager

        mAdapter = SessionAdapter(requireContext(), ArrayList())
        binding?.sessionlist!!.adapter = mAdapter

        customProgressDialog = CustomProgressDialog(requireContext())
        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })

        viewModel!!.progress.observe(viewLifecycleOwner,
            Observer { progress ->
                if (progress == View.VISIBLE) {
                    customProgressDialog!!.show()
                } else if (progress == View.GONE) {
                    customProgressDialog!!.dismiss()
                }
            })

        viewModel?.getTextMethod(facility_id!!, getTestMethdCallBack)
        //Privillage
        viewModel?.getPrevilage(facility_id!!, roleid, getPrivilageRetrofitCallback)

        viewModel!!.getLocationAPI(covidLocationResponseCallback)

        viewModel!!.getAll("suffix", suffixResponseCallback)

        viewModel?.getCovidNameTitleList(
            facility_id!!,
            covidSalutationResponseCallback
        )

        initViews(false)
        listeners()

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        tat_start_time = sdf.format(Date())
        viewModel!!.getDepartmentList(FavLabdepartmentRetrofitCallBack)
        binding?.drawerLayout?.drawerElevation = 0f

        binding?.drawerLayout?.setScrimColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )

        viewModel?.getCovidPeriodList(facility_id!!, covidPeriodResponseCallback)

        viewModel?.getCovidGenderList(facility_id!!, covidGenderResponseCallback)

        return binding!!.root
    }

    private fun initViews(enableStatus: Boolean) {
        binding?.quickMobile?.isEnabled = enableStatus
        binding?.quickAge?.isEnabled = enableStatus
        binding?.quickName?.isEnabled = enableStatus
        binding?.etmiddlename?.isEnabled = enableStatus
        binding?.etLastname?.isEnabled = enableStatus

        binding?.quickAather?.isEnabled = enableStatus
        binding?.salutation?.isEnabled = enableStatus
        binding?.qucikGender?.isEnabled = enableStatus
        binding?.suffixcode?.isEnabled = enableStatus

        binding?.qucikPeriod?.isEnabled = enableStatus
        binding?.qucikGender?.isEnabled = enableStatus
        binding?.quickMobile?.isEnabled = enableStatus
        binding?.profesitonal?.isEnabled = enableStatus

        binding?.etFathername?.isEnabled = enableStatus
        binding?.dob?.isEnabled = enableStatus
        binding?.doorNo?.isEnabled = enableStatus
        binding?.quickAddress?.isEnabled = enableStatus

        binding?.qucikCountry?.isEnabled = enableStatus
        binding?.qucikState?.isEnabled = enableStatus
        binding?.qucikDistrict?.isEnabled = enableStatus
        binding?.qucikBlock?.isEnabled = enableStatus
        binding?.qucikVillage?.isEnabled = enableStatus

        binding?.quickPincode?.isEnabled = enableStatus

        binding?.ReasonEdt?.isEnabled = enableStatus


    }

    private fun listeners() {
        binding?.Search?.setOnClickListener {
            MobileNumber = binding?.edtSeachPin?.text?.toString()
            if (MobileNumber != "") {
                val searchPatientRequestModelCovid = SearchPatientRequestModelCovid()
                searchPatientRequestModelCovid.pageNo = currentPage
                searchPatientRequestModelCovid.paginationSize = pageSize
                searchPatientRequestModelCovid.sortField =
                    "patient_visits[0].registered_date"
                searchPatientRequestModelCovid.sortOrder = "DESC"

                if (MobileNumber?.trim()?.length!! > 0 && MobileNumber?.trim()?.length!! <= 10) {
                    searchPatientRequestModelCovid.mobile = MobileNumber?.trim()
                    viewModel?.searchPatient(
                        searchPatientRequestModelCovid,
                        patientSearchRetrofitCallBack
                    )
                } else {
                    searchPatientRequestModelCovid.pin = MobileNumber?.trim()
                    viewModel?.searchPatient(
                        searchPatientRequestModelCovid,
                        patientSearchRetrofitCallBack
                    )
                }
            } else {
                Toast.makeText(context, "Please enter any one felid", Toast.LENGTH_SHORT).show()
            }
        }

        binding?.addressheader?.setOnClickListener {
            if (binding?.addressresultlayout?.visibility == View.VISIBLE) {
                binding?.addressresultlayout?.visibility = View.GONE
            } else {
                binding?.addressresultlayout?.visibility = View.VISIBLE
            }
        }

        binding?.reasonheader?.setOnClickListener {
            if (binding?.reasonresultlayout?.visibility == View.VISIBLE) {
                binding?.reasonresultlayout?.visibility = View.GONE
            } else {
                binding?.reasonresultlayout?.visibility = View.VISIBLE
            }
        }
        binding?.dob!!.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    fromDate = String.format(
                        "%02d",
                        dayOfMonth
                    ) + "-" + String.format("%02d", monthOfYear + 1) + "-" + year

                    fromDateRev = year.toString() + "-" + String.format(
                        "%02d",
                        monthOfYear + 1
                    ) + "-" + String.format(
                        "%02d",
                        dayOfMonth
                    )

                    setdob = true
                    if (mYear!! > year) {
                        binding?.quickAge?.setText((mYear!! - year).toString())
                        binding?.qucikPeriod?.setSelection(3)
                    } else if (mMonth!! > monthOfYear) {
                        binding?.quickAge?.setText((mMonth!! - monthOfYear).toString())
                        binding?.qucikPeriod?.setSelection(1)
                    } else {
                        binding?.quickAge?.setText((mDay!! - dayOfMonth).toString())
                        binding?.qucikPeriod?.setSelection(2)
                    }
                    is_dob_auto_calculate = 0
                    binding!!.dob.setText(fromDate)
                }, mYear!!, mMonth!!, mDay!!
            )

            datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
            datePickerDialog.show()
        }

        binding!!.clearCardView.setOnClickListener {
            clearAll()
        }

        viewModel!!.getApplicationRules(getApplicationRulesResponseCallback)

        binding?.salutation?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val itemValue = parent!!.getItemAtPosition(position).toString()

                selectSalutationUuid =
                    SalutaionList.filterValues { it == itemValue }.keys.toList()[0]

                val selectedData = salutaioData[position]

                if (selectedData.code == "1" || selectedData.code == "5") {

                    for (i in GenderlistData.indices) {

                        if (GenderlistData[i]?.code == "1") {

                            binding?.qucikGender?.setSelection(i)

                            break
                        }
                    }

                } else if (selectedData.code == "2" || selectedData.code == "3") {

                    for (i in GenderlistData.indices) {

                        if (GenderlistData[i]?.code == "2") {

                            binding?.qucikGender?.setSelection(i)

                            break
                        }
                    }

                } else if (selectedData.code == "4" || selectedData.code == "8") {

                    if (selectGenderUuid != 0) {

                        for (i in GenderlistData.indices) {

                            if (GenderlistData[i]?.uuid == selectGenderUuid) {

                                if (salutaioData[i].code != "3") {


                                } else {

                                    Toast.makeText(
                                        context,
                                        "Please select valid Gender",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    binding?.qucikGender?.setSelection(0)
                                }


                            }

                        }

                    }
                }
            }

        }

        binding?.profesitonal?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(position).toString()
                    selectProffestionalUuid =
                        ProfestioanlList.filterValues { it == itemValue }.keys.toList()[0]

                }

            }

        binding?.suffixcode?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(position).toString()
                    selectSuffixUuid = SuffixList.filterValues { it == itemValue }.keys.toList()[0]

                }

            }

        binding!!.quickAge.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                if (s.toString() != "") {

                    val datasize = s.toString().toInt()

                    if (selectPeriodUuid == 4) {

                        if (datasize > adultToAge) {

                            binding!!.quickAge.error = "Age Year must be less than $adultToAge"

                        } else {

                            binding!!.quickAge.error = null

                        }
                    } else if (selectPeriodUuid == 2) {

                        if (datasize > 12) {

                            binding!!.quickAge.error = "Age (Month period) must be less than 12"
                        } else {

                            binding!!.quickAge.error = null

                        }
                    } else if (selectPeriodUuid == 3) {

                        if (datasize > 31) {

                            binding!!.quickAge.error = "Age (Day period) must be less than 31"

                        } else {

                            binding!!.quickAge.error = null

                        }
                    }



                    if (datasize >= 1) {


                        if (!setdob) {

                            binding?.dobtext?.visibility = View.GONE

                            binding?.doblayout?.visibility = View.GONE

                        } else {

                            binding?.dobtext?.visibility = View.VISIBLE

                            binding?.doblayout?.visibility = View.VISIBLE
                        }


                    } else {

                        if (prvage) {

                            binding?.dobtext?.visibility = View.VISIBLE

                            binding?.doblayout?.visibility = View.VISIBLE
                        }
                    }


                } else {

                    if (prvage) {

                        binding?.dobtext?.visibility = View.VISIBLE

                        binding?.doblayout?.visibility = View.VISIBLE
                    }

                }
            }
        })


        binding!!.quickMobile.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                val datasize = s.trim().length

                if (datasize == 10) {


                    if (s.trim().toString().toLong() < 6000000000) {

                        binding!!.quickMobile.error = "Mobile Number should start with [6,7,8,9]"

                        Toast.makeText(
                            context,
                            "Mobile Number should start with [6,7,8,9]",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {

                        binding!!.quickMobile.error = null
                    }
                } else {


                    binding!!.quickMobile.error = "Mobile Number Must be 10 digit"
                }

            }
        })


        binding!!.quickPincode.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                val datasize = s.trim().length
                if (datasize == 6) {
                    binding!!.quickPincode.error = null
                } else {
                    binding!!.quickPincode.error = "Pin code Must be 6 digit"
                }
            }
        })

        binding!!.quickName.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                val datasize = s.trim().length

                if (datasize == 100) {
                    binding!!.quickName.error = "Name must be Maximum 100 letters"
                } else
                    if (datasize >= 3) {
                        binding!!.quickName.error = null
                    } else {
                        binding!!.quickName.error = "Name must be Minimum 3 letters"
                    }
            }
        })


        binding!!.etFathername.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                val datasize = s.trim().length

                if (datasize == 100) {
                    binding!!.etFathername.error = "Name must be Maximum 100 letters"
                } else
                    if (datasize >= 3) {
                        binding!!.etFathername.error = null
                    } else {
                        binding!!.etFathername.error = "Name must be Minimum 3 letters"
                    }
            }
        })


        binding!!.quickAather.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                val datasize = s.trim().length

                if (datasize in 1..11) {
                    binding!!.quickAather.error = "Aadhaar number must be Maximum 12 letters"
                } else {
                    binding!!.quickAather.error = null
                }
            }
        })

        binding?.saveCardView!!.setOnClickListener {

            if (updateId != 1) {


                Toast.makeText(
                    this.context,
                    "Please Search One patient",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener

            }


            if (binding?.ReasonEdt?.text.isNullOrEmpty()) {

                binding?.reasonresultlayout?.visibility = View.VISIBLE

                binding?.ReasonEdt?.error = "Please Enter Reason of correction"

                Toast.makeText(context, "Please Enter Reason of correction", Toast.LENGTH_SHORT)
                    .show()

                return@setOnClickListener

            }

            if (!binding?.ReasonEdt?.text.isNullOrEmpty() && binding?.ReasonEdt?.text?.toString()?.length!! < 3) {

                Toast.makeText(context, "Reason of Correction Minimum 3 letter", Toast.LENGTH_SHORT)
                    .show()

                return@setOnClickListener

            }

            if (selectPeriodUuid == 0) {

                Toast.makeText(context, "Please Select Period", Toast.LENGTH_SHORT).show()

                return@setOnClickListener

            }


            if (!binding!!.quickName.text.toString().isNullOrEmpty()) {

                if (!binding!!.quickAge.text.toString().isNullOrEmpty()) {

                    if (!binding!!.quickMobile.text.toString().isNullOrEmpty()) {

                        if (selectGenderUuid != 0) {


                            //   if (selectSalutationUuid != 0) {

                            var DOB = ""

                            isadult =
                                if (selectPeriodUuid == 4 && binding!!.quickAge.text.toString()
                                        .toInt() >= adultFromAge
                                ) {
                                    1
                                } else {
                                    0
                                }
                            Log.i("save", "atlut" + isadult)
                            when (selectPeriodUuid) {
                                4 -> {
                                    DOB =
                                        utils!!.getYear(
                                            binding!!.quickAge.text.toString().toInt()
                                        )
                                            .toString()
                                }
                                2 -> {
                                    DOB =
                                        utils!!.getAgeMonth(
                                            binding!!.quickAge.text.toString().toInt()
                                        )
                                            .toString()
                                }
                                3 -> {
                                    DOB =
                                        utils!!.getDateDaysAgo(
                                            binding!!.quickAge.text.toString().toInt()
                                        )
                                            .toString()
                                }
                            }
                            Log.i("DOB", DOB)
                            val quickUpdateRequestModel =
                                OpCorrectionRequest()

//                            quickUpdateRequestModel.registred_facility_uuid= selectdepartmentUUId?.toString()

                            quickUpdateRequestModel.is_dob_auto_calculate = 1
                            quickUpdateRequestModel.country_uuid = selectNationalityUuid
                            quickUpdateRequestModel.state_uuid = selectStateUuid
                            quickUpdateRequestModel.pincode =
                                binding!!.quickPincode.text.toString()
                            quickUpdateRequestModel.address_line2 =
                                binding!!.quickAddress.text.toString()
                            quickUpdateRequestModel.district_uuid = selectDistictUuid
                            quickUpdateRequestModel.taluk_uuid = selectBelongUuid.toString()
                            quickUpdateRequestModel.village_uuid = selectVillageUuid.toString()
                            quickUpdateRequestModel.saveExists = false

                            if (is_dob_auto_calculate == 0) {
                                DOB = utils!!.convertDateFormat(
                                    binding?.dob?.text.toString(),
                                    "dd-MM-yyyy",
                                    "yyyy-MM-dd'T'HH:mm:ss.SSS"
                                )
                            } else {

                            }
                            quickUpdateRequestModel.dob = DOB
                            quickUpdateRequestModel.is_adult = isadult
                            quickUpdateRequestModel.uuid = patientUUId!!

                            quickUpdateRequestModel.uhid = uhid

                            quickUpdateRequestModel.first_name =
                                binding!!.quickName.text.toString()
                            quickUpdateRequestModel.mobile =
                                binding!!.quickMobile.text.toString()
                            quickUpdateRequestModel.age =
                                binding!!.quickAge.text.toString()
                            quickUpdateRequestModel.gender_uuid =
                                selectGenderUuid!!

                            quickUpdateRequestModel.period_uuid =
                                selectPeriodUuid!!
                            quickUpdateRequestModel.registred_facility_uuid =
                                facility_id.toString()
                            quickUpdateRequestModel.is_dob_auto_calculate = 1

                            quickUpdateRequestModel.country_uuid =
                                selectNationalityUuid
                            quickUpdateRequestModel.state_uuid =
                                selectStateUuid
                            quickUpdateRequestModel.pincode =
                                binding!!.quickPincode.text.toString()
                            quickUpdateRequestModel.address_line2 =
                                binding!!.quickAddress.text.toString()

                            quickUpdateRequestModel.address_line1 =
                                binding!!.doorNo.text.toString()
                            quickUpdateRequestModel.district_uuid =
                                selectDistictUuid
                            quickUpdateRequestModel.taluk_uuid =
                                selectBelongUuid.toString()
                            quickUpdateRequestModel.saveExists =
                                alreadyExists

                            if (is_dob_auto_calculate == 0) {
                                DOB = utils!!.convertDateFormat(
                                    binding?.dob?.text.toString(),
                                    "dd-MM-yyyy",
                                    "yyyy-MM-dd'T'HH:mm:ss.SSS"
                                )
                            } else {

                            }
                            quickUpdateRequestModel.dob =
                                DOB
                            quickUpdateRequestModel.is_adult = isadult

                            quickUpdateRequestModel.last_name =
                                binding!!.etLastname.text.toString()

                            quickUpdateRequestModel.title_uuid =
                                selectSalutationUuid

                            quickUpdateRequestModel.aadhaar_number =
                                binding!!.quickAather.text.toString()

                            quickUpdateRequestModel.correction_reason =
                                binding?.ReasonEdt?.text?.trim().toString()

                            if (!binding?.etFathername?.text?.trim().isNullOrEmpty()) {
                                quickUpdateRequestModel.father_name =
                                    binding?.etFathername?.text?.trim().toString()
                            }
                            viewModel?.quickRegistrationUpdateformOPcorrection(
                                quickUpdateRequestModel,
                                updateQuickRegistrationRetrofitCallback
                            )
                        } else {
                            Toast.makeText(this.context, "Please Select Gender", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        binding!!.quickMobile.error = "Mobile Can't be empty"
                    }
                } else {
                    binding!!.quickAge.error = "Age Can't be empty"
                }
            } else {
                binding!!.quickName.error = "Name Can't be empty"
            }
        }

        binding?.qucikPeriod?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectPeriodUuid =
                        CovidPeriodList.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(position).toString()
                    selectPeriodUuid =
                        CovidPeriodList.filterValues { it == itemValue }.keys.toList()[0]




                    if (binding!!.quickAge.text.toString() != "") {

                        val datasize = binding!!.quickAge.text.toString().toInt()

                        if (selectPeriodUuid == 4) {

                            if (datasize > adultToAge) {

                                binding!!.quickAge.error = "Age Year must be less than $adultToAge"

                            } else {

                                binding!!.quickAge.error = null

                            }
                        } else if (selectPeriodUuid == 2) {

                            if (datasize > 12) {

                                binding!!.quickAge.error = "Age (Month period) must be less than 12"
                            } else {

                                binding!!.quickAge.error = null

                            }
                        } else if (selectPeriodUuid == 3) {

                            if (datasize > 31) {

                                binding!!.quickAge.error = "Age (Day period) must be less than 31"

                            } else {

                                binding!!.quickAge.error = null

                            }
                        }

                    }

                    Log.e(
                        "Period",
                        binding?.qucikPeriod?.selectedItem.toString() + "-" + selectPeriodUuid
                    )
                }

            }

        binding?.qucikGender?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    selectGenderUuid =
                        CovidGenderList.filterValues { it == itemValue }.keys.toList()[0]
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(position).toString()
                    selectGenderUuid =
                        CovidGenderList.filterValues { it == itemValue }.keys.toList()[0]


                    val selectedData = GenderlistData[position]

                    if (selectedData?.code == "1") {

                        if (selectSalutationUuid != 0) {

                            for (i in salutaioData.indices) {

                                if (salutaioData[i].uuid == selectSalutationUuid) {

                                    if (salutaioData[i].code == "1" || salutaioData[i].code == "5") {


                                    } else {

                                        Toast.makeText(
                                            context,
                                            "Please select valid Salutation",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        binding?.salutation?.setSelection(0)

                                    }


                                }

                            }

                        }
                    } else if (selectedData?.code == "2") {


                        if (selectSalutationUuid != 0) {

                            for (i in salutaioData.indices) {

                                if (salutaioData[i].uuid == selectSalutationUuid) {

                                    if (salutaioData[i].code == "2" || salutaioData[i].code == "3") {


                                    } else {

                                        Toast.makeText(
                                            context,
                                            "Please select valid Salutation",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        binding?.salutation?.setSelection(0)
                                    }


                                }

                            }

                        }
                    } else if (selectedData?.code == "3") {

                        if (selectSalutationUuid != 0) {

                            for (i in salutaioData.indices) {

                                if (salutaioData[i].uuid == selectSalutationUuid) {

                                    if (salutaioData[i].code != "4" || salutaioData[i].code != "4") {


                                    } else {

                                        Toast.makeText(
                                            context,
                                            "Please select valid Salutation",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        binding?.salutation?.setSelection(0)
                                    }


                                }

                            }

                        }
                    }

                    Log.e(
                        "Gender",
                        binding?.qucikGender?.selectedItem.toString() + "-" + selectGenderUuid
                    )
                }

            }

        binding?.qucikCountry?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    /* selectNationalityUuid =
                         CovidNationalityList.filterValues { it == itemValue }.keys.toList()[0]*/
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val itemValue = parent!!.getItemAtPosition(position).toString()
                    selectNationalityUuid =
                        CovidNationalityList.filterValues { it == itemValue }.keys.toList()[0]

                    viewModel!!.getStateList(selectNationalityUuid, getStateRetrofitCallback)

                    Log.e("NAtionality", selectNationalityUuid.toString())
                }

            }

        binding?.qucikState?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    /*     selectStateUuid =
                             CovidStateList.filterValues { it == itemValue }.keys.toList()[0]*/
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val itemValue = parent!!.getItemAtPosition(position).toString()
                    selectStateUuid =
                        CovidStateList.filterValues { it == itemValue }.keys.toList()[0]

                    //   selectDistictUuid=0

                    viewModel!!.getDistrict(selectStateUuid, getDistictRetrofitCallback)

                    Log.e("NAtionality", selectStateUuid.toString())
                }

            }

        binding?.qucikDistrict?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
/*                    selectDistictUuid =
                        CovidDistictList.filterValues { it == itemValue }.keys.toList()[0]*/
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val itemValue = parent!!.getItemAtPosition(position).toString()

                    selectDistictUuid =
                        CovidDistictList.filterValues { it == itemValue }.keys.toList()[0]

                    //  selectBelongUuid=0

                    viewModel!!.getTaluk(selectDistictUuid, getTalukRetrofitCallback)

                    //viewModel!!.getBlock(selectDistictUuid,getBlockRetrofitCallback)


                    Log.e("Distict", selectDistictUuid.toString())
                }

            }

        binding?.qucikBlock?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    /*      selectBelongUuid =
                              CovidBlockList.filterValues { it == itemValue }.keys.toList()[0]*/
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val itemValue = parent!!.getItemAtPosition(position).toString()

                    selectBelongUuid =
                        CovidBlockList.filterValues { it == itemValue }.keys.toList()[0]

                    viewModel!!.getVillage(selectBelongUuid, getVillageRetrofitCallback1)

                    Log.e("NAtionality", selectBelongUuid.toString())
                }

            }

        binding?.qucikVillage?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val itemValue = parent!!.getItemAtPosition(0).toString()
                    /*      selectBelongUuid =
                              CovidBlockList.filterValues { it == itemValue }.keys.toList()[0]*/
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val itemValue = parent!!.getItemAtPosition(position).toString()

                    selectVillageUuid =
                        CovidVillageList.filterValues { it == itemValue }.keys.toList()[0]


                }

            }

        mAdapter?.setOnPrintClickListener(object : SessionAdapter.OnPrintClickListener {
            override fun onPrintClick(uuid: ResponseContentsactivitysession) {

                session_uuid = uuid.uuid

                session_name = uuid.name
            }
        })


    }

    private fun clearAll() {

        selectNationalityUuid = 0

        selectStateUuid = 0

        selectDistictUuid = 0

        selectBelongUuid = 0

        selectVillageUuid = 0

        selectSalutationUuid = 0

        selectProffestionalUuid = 0

        selectSuffixUuid = 0

        selectCoummityUuid = 0

        selectUnitUuid = 0

        uhid = ""

        updateId = 0

        alreadyExists = false

        binding!!.quickMobile.setText("")

        binding!!.quickAge.setText("")
        binding!!.quickName.setText("")
        binding!!.etmiddlename.setText("")
        binding!!.etLastname.setText("")

        binding!!.quickAather.setText("")
        binding!!.salutation.setSelection(0)
        binding!!.qucikGender.setSelection(0)
        binding!!.suffixcode.setSelection(0)

        binding?.qucikPeriod?.setSelection(0)
        binding?.qucikGender?.setSelection(0)
        binding?.quickMobile?.setSelection(0)
        binding?.profesitonal?.setSelection(0)

        binding?.edtSeachPin?.setText("")

        binding?.ReasonEdt?.setText("")

        binding?.doorNo?.setText("")

        binding?.quickAddress?.setText("")

        binding?.quickPincode?.setText("")

        binding?.quickPincode?.error = null
        binding?.quickName?.error = null

        binding?.quickMobile?.error = null


        viewModel?.getTextMethod(facility_id!!, getTestMethdCallBack)
        //Privillage
        viewModel?.getPrevilage(facility_id!!, roleid, getPrivilageRetrofitCallback)


        viewModel!!.getLocationAPI(covidLocationResponseCallback)

        viewModel!!.getAll("suffix", suffixResponseCallback)

        viewModel?.getCovidNameTitleList(
            facility_id!!,
            covidSalutationResponseCallback
        )


    }

    val getTalukSearchRetrofitCallback = object : RetrofitCallback<TalukListResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<TalukListResponseModel>?) {


            setTaluk(responseBody?.body()?.responseContents!!)

        }

        override fun onBadRequest(errorBody: Response<TalukListResponseModel>?) {

        }

        override fun onServerError(response: Response<*>?) {

        }

        override fun onUnAuthorized() {

        }

        override fun onForbidden() {

        }

        override fun onFailure(s: String?) {

        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    val getTalukRetrofitCallback = object : RetrofitCallback<TalukListResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<TalukListResponseModel>?) {

            try {

                if (selectBelongUuid != null && selectBelongUuid != 0) {

                    setTaluk(responseBody?.body()?.responseContents!!)

                    viewModel!!.getVillage(selectBelongUuid, getVillageRetrofitCallback1)
                } else {
                    val selectBelong = responseBody?.body()?.responseContents?.get(0)!!.uuid

                    viewModel!!.getVillage(selectBelong, getVillageRetrofitCallback1)

                    setTaluk(responseBody.body()?.responseContents!!)

                }
            } catch (e: Exception) {


            }

        }

        override fun onBadRequest(errorBody: Response<TalukListResponseModel>?) {

        }

        override fun onServerError(response: Response<*>?) {

        }

        override fun onUnAuthorized() {

        }

        override fun onForbidden() {

        }

        override fun onFailure(s: String?) {

        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    private fun setTaluk(responseContent: ArrayList<Taluk>) {

        var dummy: Taluk = Taluk()

        dummy.uuid = 0

        dummy.name = "Select Taluk"

        var responseContents: ArrayList<Taluk> = ArrayList()


        responseContents.add(dummy)

        responseContents.addAll(responseContent)

        CovidBlockList = responseContents.map { it.uuid to it.name }.toMap().toMutableMap()

        hashBlockSpinnerList.clear()

        for (i in responseContents.indices) {

            hashBlockSpinnerList[responseContents[i].uuid] = i
        }

        val adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            CovidBlockList.values.toMutableList()
        )

        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.qucikBlock!!.adapter = adapter


        if (selectBelongUuid != null) {
            val checkblockuuid =
                hashBlockSpinnerList.any { it.key == selectBelongUuid }
            if (checkblockuuid) {
                binding?.qucikBlock!!.setSelection(hashBlockSpinnerList.get(selectBelongUuid)!!)
            } else {
                binding?.qucikBlock!!.setSelection(0)
            }
        }

    }

    val getVillageRetrofitCallback1 = object : RetrofitCallback<VilliageListResponceModel> {
        override fun onSuccessfulResponse(responseBody: Response<VilliageListResponceModel>?) {

            setVillage(responseBody!!.body()!!.responseContents)

        }

        override fun onBadRequest(errorBody: Response<VilliageListResponceModel>?) {

        }

        override fun onServerError(response: Response<*>?) {

        }

        override fun onUnAuthorized() {

        }

        override fun onForbidden() {

        }

        override fun onFailure(s: String?) {

        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    private fun setVillage(responseContent: ArrayList<Villiage>) {


        var dummy: Villiage = Villiage()

        dummy.uuid = 0

        dummy.name = "Select Village"

        var responseContents: ArrayList<Villiage> = ArrayList()

        responseContents.add(dummy)

        responseContents.addAll(responseContent)

        CovidVillageList =
            responseContents.map { it.uuid to it.name }.toMap().toMutableMap()

        hashVillageSpinnerList.clear()

        for (i in responseContents.indices) {

            hashVillageSpinnerList[responseContents[i].uuid] = i
        }

        val adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            CovidVillageList.values.toMutableList()
        )

        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.qucikVillage!!.adapter = adapter


        if (selectVillageUuid != null) {
            val checkvillageuuid =
                hashVillageSpinnerList.any { it.key == selectVillageUuid }
            if (checkvillageuuid) {
                binding?.qucikVillage!!.setSelection(hashVillageSpinnerList.get(selectVillageUuid)!!)
            } else {
                binding?.qucikVillage!!.setSelection(0)
            }
        }

    }

    val covidLocationResponseCallback = object : RetrofitCallback<LocationMasterResponseModelX> {
        override fun onSuccessfulResponse(responseBody: Response<LocationMasterResponseModelX>?) {

            val data = responseBody!!.body()!!.responseContents

            if (data.isNotEmpty()) {
                locationMasterX = data[0]
            }
            viewModel!!.getReference(covidgetReferenceResponseCallback)


        }

        override fun onBadRequest(errorBody: Response<LocationMasterResponseModelX>?) {
            val gson = GsonBuilder().create()
            val responseModel: LocationMasterResponseModelX
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    LocationMasterResponseModelX::class.java
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

    val covidgetReferenceResponseCallback = object : RetrofitCallback<GetReferenceResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<GetReferenceResponseModel>?) {
            viewModel!!.getTotest(covidtestResponseCallback)

            val data = responseBody!!.body()!!.responseContents

            if (data.isNotEmpty()) {
                getReference = data[0]
            }


        }

        override fun onBadRequest(errorBody: Response<GetReferenceResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: GetReferenceResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    GetReferenceResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    responseModel.statusCode.toString()
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

    val covidtestResponseCallback = object : RetrofitCallback<GettestResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<GettestResponseModel>?) {

            val data = responseBody!!.body()!!.responseContents

            if (data.isNotEmpty()) {
                gettest = data[0]
            }


        }

        override fun onBadRequest(errorBody: Response<GettestResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: GettestResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    GettestResponseModel::class.java
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

    val FavLabdepartmentRetrofitCallBack =
        object : RetrofitCallback<FavAddResponseModel> {
            override fun onSuccessfulResponse(response: Response<FavAddResponseModel>) {
                /*if (response.body()!!.responseContent != null) {

                    binding!!.department.setText(response.body()!!.responseContent.name)

                    if(response.body()!!.responseContent.is_speciality!=null && response.body()!!.responseContent.is_speciality== 1){

                        binding?.unittext?.visibility=View.VISIBLE

                        binding?.unit?.visibility = View.VISIBLE


                    }
                    else{

                        binding?.unittext?.visibility=View.GONE

                        binding?.unit?.visibility = View.GONE
                    }

                }*/
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

    val getApplicationRulesResponseCallback =
        object : RetrofitCallback<GetApplicationRulesResponseModel> {
            @SuppressLint("NewApi")
            override fun onSuccessfulResponse(responseBody: Response<GetApplicationRulesResponseModel>?) {

                val data = responseBody!!.body()!!.responseContents

                val dateformat = responseBody.body()?.currentDateTime

//            val date = utils?.convertServerDateToUserTimeZone(responseBody!!.body()!!.currentDateTime)
                val date = dateformat!!.getDateWithServerTimeStamp()
                val sdf =
                    SimpleDateFormat("HH:mm:ss")
                currentDateandTime = sdf.format(date)


                if (data.isNotEmpty()) {

                    for (i in data.indices) {

                        if (data[i].field_name == "adultFromAge") {

                            adultFromAge = data[i].field_value.toInt()
                        }

                        if (data[i].field_name == "adultToAge") {

                            adultToAge = data[i].field_value.toInt()

                            binding!!.quickAge.filters += InputFilter.LengthFilter(data[i].field_value.length)
                        }

                        if (data[i].field_name == "childToAge") {

                            childToAge = data[i].field_value.toInt()

                        }

                    }

                }

            }

            override fun onBadRequest(errorBody: Response<GetApplicationRulesResponseModel>?) {
                val gson = GsonBuilder().create()
                val responseModel: GetApplicationRulesResponseModel
                try {
                    responseModel = gson.fromJson(
                        errorBody!!.errorBody()!!.string(),
                        GetApplicationRulesResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        "Something Went Wrong"
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

    val suffixResponseCallback = object : RetrofitCallback<GetAllResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<GetAllResponseModel>?) {

            var responseData: ArrayList<GetAllResponse> = ArrayList()

            var dummy: GetAllResponse = GetAllResponse()

            dummy.uuid = 0

            dummy.name = "Suffix Code"

            responseData.add(dummy)

            var responseDatas = responseBody!!.body()!!.responseContents

            responseData.addAll(responseDatas)

            SuffixList = responseData.map { it.uuid to it.name }.toMap().toMutableMap()

            hashSuffixSpinnerList.clear()

            for (i in responseData.indices) {

                hashSuffixSpinnerList[responseData[i].uuid] = i
            }


            val adapter = ArrayAdapter<String>(
                context!!,
                R.layout.spinner_item,
                SuffixList.values.toMutableList()
            )

            adapter.setDropDownViewResource(R.layout.spinner_item)

            binding?.suffixcode!!.adapter = adapter


        }

        override fun onBadRequest(errorBody: Response<GetAllResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: GetAllResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    GetAllResponseModel::class.java
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    responseModel.req
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

    val covidSalutationResponseCallback =
        object : RetrofitCallback<CovidSalutationTitleResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<CovidSalutationTitleResponseModel>?) {

                val res = responseBody?.body()?.responseContents
                //     A1selectSalutationUuid = responseBody?.body()?.responseContents?.get(0)!!.uuid!!


                salutaioData.clear()

                var professtionalData: ArrayList<SalutationresponseContent> = ArrayList()

                var defult: SalutationresponseContent = SalutationresponseContent()

                defult.uuid = 0
                defult.name = "Select Professional"

                val suldefult: SalutationresponseContent = SalutationresponseContent()

                suldefult.uuid = 0
                suldefult.name = "Select Salutation"


                salutaioData.add(suldefult)
                professtionalData.add(defult)

                for (i in res!!.indices) {


                    if (res[i]?.type_code == "1") {
                        salutaioData.add(res[i]!!)
                    } else {

                        professtionalData.add(res[i]!!)
                    }
                }

                SalutaionList =
                    salutaioData.map { it.uuid!! to it.name!! }.toMap().toMutableMap()

                hashSalutaionSpinnerList.clear()

                for (i in salutaioData.indices) {

                    hashSalutaionSpinnerList[salutaioData[i].uuid!!] = i
                }


                val adapter = ArrayAdapter<String>(
                    context!!,
                    R.layout.spinner_item,
                    SalutaionList.values.toMutableList()
                )
                adapter.setDropDownViewResource(R.layout.spinner_item)

                binding?.salutation!!.adapter = adapter


                ProfestioanlList =
                    professtionalData.map { it.uuid!! to it.name!! }.toMap().toMutableMap()

                hashProfestioanlSpinnerList.clear()

                for (i in professtionalData.indices) {

                    hashProfestioanlSpinnerList[professtionalData[i].uuid!!] = i
                }


                val adapter2 = ArrayAdapter<String>(
                    context!!,
                    R.layout.spinner_item,
                    ProfestioanlList.values.toMutableList()
                )
                adapter.setDropDownViewResource(R.layout.spinner_item)
                binding?.profesitonal!!.adapter = adapter2


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

    val LocationMasterResponseCallback = object : RetrofitCallback<LocationMasterResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<LocationMasterResponseModel>?) {
            Log.i("", "locationdata" + responseBody!!.body()!!.responseContents)
            val data = responseBody.body()!!.responseContents
            if (data.isNotEmpty()) {
                locationId = data[0].uuid
            }
            //    labnameAdapter(responseBody!!.body()!!.responseContents)
        }

        override fun onBadRequest(errorBody: Response<LocationMasterResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: LocationMasterResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    LocationMasterResponseModel::class.java
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


    val covidPeriodResponseCallback = object : RetrofitCallback<CovidPeriodResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<CovidPeriodResponseModel>?) {

            selectPeriodUuid = responseBody?.body()?.responseContents?.get(0)!!.uuid!!
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

        var dummy: PeriodresponseContent = PeriodresponseContent()

        dummy.uuid = 0

        dummy.name = "Select Period"

        var datalist: ArrayList<PeriodresponseContent?> = ArrayList()

        datalist.add(dummy)

        datalist.addAll(responseContents!!)

        CovidPeriodList =
            datalist.map { it?.uuid!! to it.name!! }.toMap().toMutableMap()

        hashPeriodSpinnerList.clear()

        for (i in datalist.indices) {

            hashPeriodSpinnerList[datalist[i]!!.uuid!!] = i
        }


        val adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            CovidPeriodList.values.toMutableList()
        )

        adapter.setDropDownViewResource(R.layout.spinner_item)

        binding?.qucikPeriod!!.adapter = adapter

        binding!!.qucikPeriod.setSelection(3)

    }

    val covidGenderResponseCallback = object : RetrofitCallback<CovidGenderResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<CovidGenderResponseModel>?) {

            selectGenderUuid = responseBody?.body()?.responseContents?.get(0)!!.uuid!!
            setGender(responseBody.body()?.responseContents)

            viewModel!!.getFaciltyLocation(facilityLocationResponseCallback)
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

        var dummy: GenderresponseContent = GenderresponseContent()

        dummy.uuid = 0

        dummy.name = "Select Gender"

        GenderlistData.clear()


        GenderlistData.add(dummy)

        GenderlistData.addAll(responseContents!!)

        CovidGenderList =
            GenderlistData.map { it?.uuid!! to it.name!! }.toMap().toMutableMap()

        hashGenderSpinnerList.clear()

        for (i in GenderlistData.indices) {

            hashGenderSpinnerList[GenderlistData[i]!!.uuid!!] = i
        }

        val adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            CovidGenderList.values.toMutableList()
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.qucikGender!!.adapter = adapter

    }

    val facilityLocationResponseCallback =
        object : RetrofitCallback<FacilityLocationResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<FacilityLocationResponseModel>?) {

                if (responseBody!!.body()!!.responseContents != null) {

                    facility_Name = responseBody.body()!!.responseContents.facility.name

                    if (responseBody.body()!!.responseContents.country_master != null) {

                        selectNationalityUuid =
                            responseBody.body()!!.responseContents.country_master.uuid
                    }

                    if (responseBody.body()!!.responseContents.state_master != null) {
                        selectStateUuid = responseBody.body()!!.responseContents.state_master.uuid

                    }

                    if (responseBody.body()!!.responseContents.district_master != null) {
                        selectDistictUuid =
                            responseBody.body()!!.responseContents.district_master.uuid
                    }
                    viewModel?.getCovidNationalityList(
                        "nationality_type",
                        covidNationalityResponseCallback
                    )
                } else {

                    viewModel?.getCovidNationalityList(
                        "nationality_type",
                        covidNationalityResponseCallback
                    )
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


    val covidNationalityResponseCallback =
        object : RetrofitCallback<CovidNationalityResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<CovidNationalityResponseModel>?) {


                if (selectNationalityUuid != null && selectNationalityUuid != 0) {

                    setNationality(responseBody?.body()?.responseContents!!)
                    viewModel!!.getStateList(selectNationalityUuid, getStateRetrofitCallback)

                } else {

                    if (!responseBody?.body()?.responseContents!!.isEmpty()) {

                        selectNationalityUuid =
                            responseBody.body()?.responseContents?.get(0)!!.uuid!!

                        setNationality(responseBody.body()?.responseContents!!)
                    }
                    viewModel!!.getStateList(selectNationalityUuid, getStateRetrofitCallback)
                }

            }

            override fun onBadRequest(errorBody: Response<CovidNationalityResponseModel>?) {
                /*    val gson = GsonBuilder().create()
                    val responseModel: CovidNationalityResponseModel
                    try {
                        responseModel = gson.fromJson(
                            errorBody!!.errorBody()!!.string(),
                            CovidNationalityResponseModel::class.java
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
                    }*/

            }

            override fun onServerError(response: Response<*>?) {

            }

            override fun onUnAuthorized() {

            }

            override fun onForbidden() {

            }

            override fun onFailure(failure: String?) {
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    fun setNationality(responseContents: List<NationalityresponseContent?>?) {


        var dummy: NationalityresponseContent = NationalityresponseContent()

        dummy.uuid = 0

        dummy.name = "Select Country"

        var countryList: ArrayList<NationalityresponseContent?>? = ArrayList()

        countryList?.add(dummy)

        countryList?.addAll(responseContents!!)

        CovidNationalityList =
            countryList?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        hashNationalitySpinnerList.clear()

        for (i in countryList.indices) {

            hashNationalitySpinnerList[countryList[i]!!.uuid!!] = i
        }

        val adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            CovidNationalityList.values.toMutableList()
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.qucikCountry!!.adapter = adapter

        if (selectNationalityUuid != null) {
            val checknationality =
                hashNationalitySpinnerList.any { it.key == selectNationalityUuid }
            if (checknationality) {
                binding?.qucikCountry!!.setSelection(
                    hashNationalitySpinnerList.get(
                        selectNationalityUuid
                    )!!
                )
            } else {
                binding?.qucikCountry!!.setSelection(0)
            }
        }

    }


    val getStateRetrofitCallback = object : RetrofitCallback<StateListResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<StateListResponseModel>?) {

            try {


                if (selectStateUuid != null && selectStateUuid != 0) {

                    viewModel!!.getDistrict(selectStateUuid, getDistictRetrofitCallback)

                    setState(responseBody?.body()?.responseContents!!)

                } else {

                    selectStateUuid = responseBody?.body()?.responseContents?.get(0)!!.uuid

                    viewModel!!.getDistrict(selectStateUuid, getDistictRetrofitCallback)

                    setState(responseBody.body()?.responseContents!!)
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        override fun onBadRequest(errorBody: Response<StateListResponseModel>?) {

            /*     val gson = GsonBuilder().create()
                 val responseModel: StateListResponseModel
                 try {
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
                 }*/

        }

        override fun onServerError(response: Response<*>?) {

        }

        override fun onUnAuthorized() {

        }

        override fun onForbidden() {

        }

        override fun onFailure(s: String?) {

        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    private fun setState(responseContents: ArrayList<State>) {

        CovidStateList = responseContents.map { it.uuid to it.name }.toMap().toMutableMap()

        //hashStateSpinnerList

        hashStateSpinnerList.clear()

        for (i in responseContents.indices) {

            hashStateSpinnerList[responseContents[i].uuid] = i
        }

        val adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            CovidStateList.values.toMutableList()
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.qucikState!!.adapter = adapter

        if (selectStateUuid != null) {
            val checkstate =
                hashStateSpinnerList.any { it.key == selectStateUuid }
            if (checkstate) {
                binding?.qucikState!!.setSelection(hashStateSpinnerList.get(selectStateUuid)!!)
            } else {
                binding?.qucikState!!.setSelection(0)
            }
        }
    }

    val getDistictRetrofitCallback = object : RetrofitCallback<DistrictListResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<DistrictListResponseModel>?) {
            try {
                if (selectDistictUuid != 0 && selectDistictUuid != null) {
                    setDistict(responseBody?.body()?.responseContents!!)
                    // viewModel!!.getBlock(selectDistictUuid, getBlockRetrofitCallback)
                    viewModel!!.getTaluk(selectDistictUuid, getTalukRetrofitCallback)
                } else {
                    //   selectDistictUuid = responseBody?.body()?.responseContents?.get(0)!!.uuid!!
                    setDistict(responseBody?.body()?.responseContents!!)
                    viewModel!!.getTaluk(selectDistictUuid, getTalukRetrofitCallback)
                    // viewModel!!.getBlock(selectDistictUuid, getBlockRetrofitCallback)
                }
            } catch (e: Exception) {

            }
        }

        override fun onBadRequest(errorBody: Response<DistrictListResponseModel>?) {

        }

        override fun onServerError(response: Response<*>?) {

        }

        override fun onUnAuthorized() {

        }

        override fun onForbidden() {

        }

        override fun onFailure(s: String?) {

        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    private fun setDistict(responseContents: ArrayList<District>) {

        CovidDistictList =
            responseContents.map { it.uuid to it.name }.toMap().toMutableMap()

        hashDistrictSpinnerList.clear()

        for (i in responseContents.indices) {
            hashDistrictSpinnerList[responseContents[i].uuid] = i
        }

        val adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            CovidDistictList.values.toMutableList()
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.qucikDistrict!!.adapter = adapter


        if (selectDistictUuid != null) {
            val checkdistrict =
                hashDistrictSpinnerList.any { it.key == selectDistictUuid }
            if (checkdistrict) {
                binding?.qucikDistrict!!.setSelection(hashDistrictSpinnerList.get(selectDistictUuid)!!)
            } else {
                binding?.qucikDistrict!!.setSelection(0)
            }
        }

    }

    val getBlockRetrofitCallback = object : RetrofitCallback<BlockZoneResponseModel> {

        override fun onSuccessfulResponse(responseBody: Response<BlockZoneResponseModel>?) {
            try {
                if (selectBelongUuid != null && selectBelongUuid != 0) {
                    setBlock(responseBody?.body()?.responseContents!!)
                } else {
                    selectBelongUuid = responseBody?.body()?.responseContents?.get(0)!!.uuid
                    setBlock(responseBody.body()?.responseContents!!)
                }
            } catch (e: Exception) {

            }
        }

        override fun onBadRequest(errorBody: Response<BlockZoneResponseModel>?) {

        }

        override fun onServerError(response: Response<*>?) {

        }

        override fun onUnAuthorized() {

        }

        override fun onForbidden() {

        }

        override fun onFailure(s: String?) {

        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    private fun setBlock(responseContents: List<BlockZone>) {

        CovidBlockList = responseContents.map { it.uuid to it.name }.toMap().toMutableMap()

        hashBlockSpinnerList.clear()

        for (i in responseContents.indices) {

            hashBlockSpinnerList[responseContents[i].uuid] = i
        }

        val adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            CovidBlockList.values.toMutableList()
        )

        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.qucikBlock!!.adapter = adapter

    }

    val getTestMethdCallBack =
        object : RetrofitCallback<ResponseTestMethod> {
            override fun onSuccessfulResponse(response: Response<ResponseTestMethod>) {
                array_testmethod = response.body()?.responseContents


            }

            override fun onBadRequest(response: Response<ResponseTestMethod>) {
                val gson = GsonBuilder().create()
                val responseModel: ResponseTestMethod
                try {

                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        ResponseTestMethod::class.java
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

    val getPrivilageRetrofitCallback =
        object : RetrofitCallback<ResponsePrivillageModule> {
            override fun onSuccessfulResponse(response: Response<ResponsePrivillageModule>) {

                val quickPrevilege: QuickNewPrevilege = QuickNewPrevilege()
                val checksave =
                    response.body()?.responseContents!!.any { it!!.code == quickPrevilege.save }

                if (checksave) {
                    binding?.saveCardView?.isClickable = true
                } else {
                    binding?.saveCardView?.alpha = .5f
                    binding?.saveCardView?.isClickable = false

                }

                ///Save Order

                val checksaveOrder =
                    response.body()?.responseContents!!.any { it!!.code == quickPrevilege.savenext }

                if (checksaveOrder) {
                    binding?.saveOrderCardView?.isClickable = true
                } else {
                    binding?.saveOrderCardView?.alpha = .5f
                    binding?.saveOrderCardView?.isClickable = false

                }

                //search


                //Lab

                //search


            }

            override fun onBadRequest(response: Response<ResponsePrivillageModule>) {
                val gson = GsonBuilder().create()
                Log.i("", "Badddddddd")
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


    /*  val getActivityPrivilageRetrofitCallback =
          object : RetrofitCallback<QuickelementRoleResponseModel> {
              override fun onSuccessfulResponse(response: Response<QuickelementRoleResponseModel>) {

                  val checksal =
                      response?.body()?.responseContents!!.any {
                          it!!.facility_registration_config.code == AppConstants?.SALUTAION
                      }

                  if (checksal) {

                      binding?.salutaionText?.visibility = View.VISIBLE

                      binding?.salutaionList?.visibility = View.VISIBLE


                  } else {

                      binding?.salutaionText?.visibility = View.GONE

                      binding?.salutaionList?.visibility = View.GONE

                  }


                  val checkprof =
                      response?.body()?.responseContents!!.any {
                          it!!.facility_registration_config.code == AppConstants?.PROFESTIONALSULUTAION
                      }


                  if (checkprof || checksal) {


                      val tabletSize = getResources().getBoolean(R.bool.isTablet)

                      if (tabletSize) {

                          binding?.SPList?.visibility = View.VISIBLE

                      }

                  } else {


                      val tabletSize = getResources().getBoolean(R.bool.isTablet)

                      if (tabletSize) {

                          binding?.SPList?.visibility = View.GONE

                      }


                  }

                  if (checkprof) {


                      val tabletSize = getResources().getBoolean(R.bool.isTablet)

                      if (tabletSize) {

                          binding?.profestionallayout?.visibility = View.VISIBLE

                      } else {

                          binding?.proftext?.visibility = View.VISIBLE

                          binding?.proflist?.visibility = View.VISIBLE
                      }


                  } else {

                      val tabletSize = getResources().getBoolean(R.bool.isTablet)

                      if (tabletSize) {

                          binding?.profestionallayout?.visibility = View.GONE

                      } else {

                          binding?.proftext?.visibility = View.GONE

                          binding?.proflist?.visibility = View.GONE
                      }


                      *//*   binding?.proftext?.visibility = View.GONE

                       binding?.proflist?.visibility = View.GONE*//*

                }

                val checkFATHER =
                    response?.body()?.responseContents!!.any {
                        it!!.facility_registration_config.code == AppConstants?.FATHERNAME
                    }

                if (checkFATHER) {

                    binding?.fathernametext?.visibility = View.VISIBLE


                    binding?.etFathername?.visibility = View.VISIBLE


                } else {

                    binding?.fathernametext?.visibility = View.GONE

                    binding?.etFathername?.visibility = View.GONE

                }

                val checksuf =
                    response?.body()?.responseContents!!.any {
                        it!!.facility_registration_config.code == AppConstants?.SUFFIXCODE
                    }

                if (checksuf) {

                    binding?.suffixtext?.visibility = View.VISIBLE


                    binding?.suffixlist?.visibility = View.VISIBLE


                } else {

                    binding?.suffixtext?.visibility = View.GONE

                    binding?.suffixlist?.visibility = View.GONE

                }

                val checkdob =
                    response?.body()?.responseContents!!.any {
                        it!!.facility_registration_config.code == AppConstants?.DATEOFBIRTH
                    }

                prvage = checkdob

                if (checkdob) {

                    binding?.dobtext?.visibility = View.VISIBLE


                    binding?.doblayout?.visibility = View.VISIBLE


                } else {

                    binding?.dobtext?.visibility = View.GONE

                    binding?.doblayout?.visibility = View.GONE

                }

                val checkmiddlename =
                    response?.body()?.responseContents!!.any {
                        it!!.facility_registration_config.code == AppConstants?.MIDDLENAME
                    }

                if (checkmiddlename) {

                    binding?.middlenametext?.visibility = View.VISIBLE


                    binding?.etmiddlename?.visibility = View.VISIBLE


                } else {


                    binding?.middlenametext?.visibility = View.GONE


                    binding?.etmiddlename?.visibility = View.GONE

                }


                val checkcom =
                    response?.body()?.responseContents!!.any {
                        it!!.facility_registration_config.code == AppConstants?.COMMUNITY
                    }

                if (checkcom) {

                    binding?.comlist?.visibility = View.VISIBLE

                    binding?.comtext?.visibility = View.VISIBLE


                } else {

                    binding?.comlist?.visibility = View.GONE

                    binding?.comtext?.visibility = View.GONE

                }


                val checkunit =
                    response?.body()?.responseContents!!.any {
                        it!!.facility_registration_config.code == AppConstants?.UNIT
                    }

                if (checkunit) {

                    binding?.unittext?.visibility = View.VISIBLE

                    binding?.unit?.visibility = View.VISIBLE


                } else {

                    binding?.unittext?.visibility = View.GONE

                    binding?.unit?.visibility = View.GONE

                }


                val checkaather =
                    response?.body()?.responseContents!!.any {
                        it!!.facility_registration_config.code == AppConstants?.AADHARNOR
                    }

                if (checkaather) {

                    binding?.aathertext?.visibility = View.VISIBLE

                    binding?.quickAather?.visibility = View.VISIBLE


                } else {


                    binding?.aathertext?.visibility = View.GONE

                    binding?.quickAather?.visibility = View.GONE

                }

                val checklastname =
                    response?.body()?.responseContents!!.any {
                        it!!.facility_registration_config.code == AppConstants?.LASTNAME
                    }

                if (checklastname) {

                    binding?.lastnametext?.visibility = View.VISIBLE


                    binding?.etLastname?.visibility = View.VISIBLE


                } else {


                    binding?.lastnametext?.visibility = View.GONE


                    binding?.etLastname?.visibility = View.GONE

                }


                val checkdrno =
                    response?.body()?.responseContents!!.any {
                        it!!.facility_registration_config.code == AppConstants?.DRNUMBNER
                    }

                if (checkdrno) {

                    binding?.drno?.visibility = View.VISIBLE


                    binding?.doorNo?.visibility = View.VISIBLE


                } else {


                    binding?.drno?.visibility = View.GONE


                    binding?.doorNo?.visibility = View.GONE

                }

                val checkstreet =
                    response?.body()?.responseContents!!.any {
                        it!!.facility_registration_config.code == AppConstants?.STNAMME
                    }

                if (checkstreet) {

                    binding?.streetname?.visibility = View.VISIBLE

                    binding?.quickAddress?.visibility = View.VISIBLE


                } else {


                    binding?.streetname?.visibility = View.GONE

                    binding?.quickAddress?.visibility = View.GONE

                }


                val checkcountry =

                    response?.body()?.responseContents!!.any {
                        it!!.facility_registration_config.code == AppConstants?.COUNTRY

                    }

                if (checkcountry) {

                    binding?.countrytext?.visibility = View.VISIBLE

                    binding?.countrylist?.visibility = View.VISIBLE


                } else {

                    binding?.countrytext?.visibility = View.GONE

                    binding?.countrylist?.visibility = View.GONE

                }

                val checkstate =
                    response?.body()?.responseContents!!.any {
                        it!!.facility_registration_config.code == AppConstants?.STATE
                    }

                if (checkstate) {

                    binding?.statetext?.visibility = View.VISIBLE

                    binding?.statelist?.visibility = View.VISIBLE


                } else {


                    binding?.statetext?.visibility = View.GONE

                    binding?.statelist?.visibility = View.GONE

                }

                val checkremark =
                    response?.body()?.responseContents!!.any {
                        it!!.facility_registration_config.code == AppConstants?.REMARK
                    }

                if (checkremark) {

                    binding?.remarknametext?.visibility = View.VISIBLE

                    binding?.etRemarkname?.visibility = View.VISIBLE


                } else {


                    binding?.remarknametext?.visibility = View.GONE

                    binding?.etRemarkname?.visibility = View.GONE

                }


                val checkdistict =
                    response?.body()?.responseContents!!.any {
                        it!!.facility_registration_config.code == AppConstants?.DISTIRICT
                    }

                if (checkdistict) {

                    binding?.distictlist?.visibility = View.VISIBLE

                    binding?.disticttext?.visibility = View.VISIBLE


                } else {


                    binding?.distictlist?.visibility = View.GONE

                    binding?.disticttext?.visibility = View.GONE

                }

                val checkzone =
                    response?.body()?.responseContents!!.any {
                        it!!.facility_registration_config.code == AppConstants?.TALUK
                    }

                if (checkzone) {

                    binding?.zonelist?.visibility = View.VISIBLE

                    binding?.zonetext?.visibility = View.VISIBLE


                } else {


                    binding?.zonelist?.visibility = View.GONE

                    binding?.zonetext?.visibility = View.GONE

                }


                val checkvillage =
                    response?.body()?.responseContents!!.any {
                        it!!.facility_registration_config.code == AppConstants.VILLAGE
                    }

                if (checkvillage) {

                    binding?.villagelist?.visibility = View.VISIBLE

                    binding?.villagetext?.visibility = View.VISIBLE


                } else {


                    binding?.villagelist?.visibility = View.GONE

                    binding?.villagetext?.visibility = View.GONE

                }


                val checkpin =
                    response.body()?.responseContents!!.any {
                        it.facility_registration_config.code == AppConstants.PINCODE
                    }

                if (checkpin) {

                    binding?.pincodetext?.visibility = View.VISIBLE

                    binding?.quickPincode?.visibility = View.VISIBLE


                } else {


                    binding?.pincodetext?.visibility = View.GONE

                    binding?.quickPincode?.visibility = View.GONE

                }


                val checkmatanity =
                    response.body()?.responseContents!!.any {
                        it.facility_registration_config.code == AppConstants.MATANITY
                    }

                if (checkmatanity) {

                    binding?.metanitylayout?.visibility = View.VISIBLE


                } else {


                    binding?.metanitylayout?.visibility = View.GONE


                }

                if ((checkdrno || checkstreet) || (checkdistict || checkzone) || (checkvillage || checkpin) || checkcountry) {

                    binding?.addressheader?.visibility = View.VISIBLE

                    addressenable = true

                } else {

                    binding?.addressheader?.visibility = View.GONE

                    addressenable = false

                }


            }

            override fun onBadRequest(response: Response<QuickelementRoleResponseModel>) {
                val gson = GsonBuilder().create()
                Log.i("", "Badddddddd")
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

*/


    val patientSearchRetrofitCallBack = object : RetrofitCallback<QuickSearchResponseModel> {
        override fun onSuccessfulResponse(response: Response<QuickSearchResponseModel>) {

            if (response.body()?.responseContents?.isNotEmpty()!!) {

                if (response.body()?.responseContents?.size == 1) {


                    updateId = 1

                    binding?.edtSeachPin?.setText("")

                    initViews(true)
                    binding?.lastpin?.visibility = View.VISIBLE
                    binding?.pinLayout?.visibility = View.VISIBLE
                    binding?.LastPin?.text = response.body()!!.responseContents!![0]!!.uhid
                    binding?.quickName!!.setText(response.body()!!.responseContents!![0]!!.first_name)
                    binding?.etmiddlename!!.setText(response.body()!!.responseContents!![0]!!.middle_name)
                    binding?.etLastname!!.setText(response.body()!!.responseContents!![0]!!.last_name)
                    binding?.quickAge!!.setText(response.body()!!.responseContents!![0]!!.age!!.toString())
                    binding?.quickMobile!!.setText(response.body()!!.responseContents!![0]!!.patient_detail?.mobile!!.toString())

                    Matanity = response.body()!!.responseContents!![0]!!.is_maternity




                    if (response.body()!!.responseContents!![0]!!.patient_detail?.address_line1 != null) {
                        binding?.doorNo!!.setText(response.body()!!.responseContents!![0]!!.patient_detail?.address_line1!!.toString())
                    }

                    if (response.body()!!.responseContents!![0]!!.patient_detail?.pincode != null) {
                        binding?.quickPincode!!.setText(response.body()!!.responseContents!![0]!!.patient_detail?.pincode!!.toString())
                    }
                    if (response.body()!!.responseContents!![0]!!.patient_detail?.aadhaar_number != null) {

                        binding?.quickAather!!.setText(response.body()!!.responseContents!![0]!!.patient_detail?.aadhaar_number!!.toString())

                    }


                    if (response.body()!!.responseContents!![0]!!.gender_uuid != null) {
                        val checkgender =
                            hashGenderSpinnerList.any { it.key == response.body()!!.responseContents!![0]!!.gender_uuid }
                        if (checkgender) {
                            binding?.qucikGender!!.setSelection(hashGenderSpinnerList.get(response.body()!!.responseContents!![0]!!.gender_uuid)!!)
                        } else {
                            binding?.qucikGender!!.setSelection(0)
                        }
                    }

                    if (response.body()!!.responseContents!![0]!!.period_uuid != null) {
                        val checkperiod =
                            hashPeriodSpinnerList.any { it.key == response.body()!!.responseContents!![0]!!.period_uuid }
                        if (checkperiod) {
                            binding?.qucikPeriod!!.setSelection(hashPeriodSpinnerList.get(response.body()!!.responseContents!![0]!!.period_uuid)!!)
                        } else {
                            binding?.qucikPeriod!!.setSelection(0)
                        }
                    }

                    selectBelongUuid =
                        response.body()!!.responseContents!![0]!!.patient_detail!!.taluk_uuid ?: 0

                    selectVillageUuid =
                        response.body()!!.responseContents!![0]!!.patient_detail!!.village_uuid ?: 0

                    selectNationalityUuid =
                        response.body()!!.responseContents!![0]!!.patient_detail!!.country_uuid ?: 0

                    selectStateUuid =
                        response.body()!!.responseContents!![0]!!.patient_detail!!.state_uuid ?: 0

                    selectDistictUuid =
                        response.body()!!.responseContents!![0]!!.patient_detail!!.district_uuid
                            ?: 0

                    viewModel?.getCovidNationalityList(
                        "nationality_type",
                        covidNationalityResponseCallback
                    )

                    if (selectDistictUuid != 0) {

                        viewModel!!.getTaluk(selectDistictUuid, getTalukSearchRetrofitCallback)

                    }


                    patientUUId = response.body()!!.responseContents!![0]!!.uuid

                    if (response.body()!!.responseContents!![0]!!.patient_detail!!.lab_to_facility_uuid != null) {

                        selectLabNameID =
                            response.body()!!.responseContents!![0]!!.patient_detail!!.lab_to_facility_uuid

                        if (selectLabNameID != 0) {

                            viewModel!!.getLocationMaster(
                                selectLabNameID!!,
                                LocationMasterResponseCallback
                            )

                        }

                    }
                    if (response.body()!!.responseContents!![0]!!.uhid != null) {

                        uhid = response.body()!!.responseContents!![0]!!.uhid!!
                    }

                    if (response.body()!!.responseContents!![0]!!.registered_date != "") {

                        registerDate = response.body()!!.responseContents!![0]!!.registered_date!!
                    }

                    val searchData = response.body()!!.responseContents!![0]

                    binding?.etFathername?.setText(searchData!!.patient_detail!!.father_name ?: "")

                    binding?.etFathername?.error = null

                    if (searchData!!.patient_detail!!.address_line2 != null) {

                        binding?.quickAddress!!.setText(searchData.patient_detail!!.address_line2!!.toString())

                    }

                    if (searchData.title_uuid != null) {
                        val titleuuid =
                            hashSalutaionSpinnerList.any { it.key == searchData.title_uuid }
                        if (titleuuid) {
                            binding?.salutation!!.setSelection(
                                hashSalutaionSpinnerList.get(
                                    searchData.title_uuid
                                )!!
                            )
                        } else {
                            binding?.salutation!!.setSelection(0)
                        }
                    }


                    if (searchData.professional_title_uuid != null) {
                        val titleuuid =
                            hashProfestioanlSpinnerList.any { it.key == searchData.professional_title_uuid }
                        if (titleuuid) {
                            binding?.profesitonal!!.setSelection(
                                hashProfestioanlSpinnerList.get(
                                    searchData.professional_title_uuid
                                )!!
                            )
                        } else {
                            binding?.profesitonal!!.setSelection(0)
                        }
                    }


                    if (searchData.suffix_uuid != null) {
                        val titleuuid =
                            hashSuffixSpinnerList.any { it.key == searchData.suffix_uuid }
                        if (titleuuid) {
                            binding?.suffixcode!!.setSelection(hashSuffixSpinnerList.get(searchData.suffix_uuid)!!)
                        } else {
                            binding?.suffixcode!!.setSelection(0)
                        }
                    }


                    selectdepartmentUUId = searchData.registred_facility_uuid
                    binding?.doorNo?.setText(searchData.patient_detail!!.address_line1 ?: "")


                } else {

                    val ft = childFragmentManager.beginTransaction()
                    val dialog = SearchPatientDialogFragment()
                    val bundle = Bundle()
                    bundle.putString("search", quicksearch)
                    bundle.putString("PIN", pinnumber)
                    bundle.putString("mobile", MobileNumber)
                    bundle.putString("aatherNo", aatherNo)
                    dialog.arguments = bundle
                    dialog.show(ft, "Tag")
                }
            } else {
                Toast.makeText(context!!, "No Record Found", Toast.LENGTH_SHORT).show()
            }


        }

        override fun onBadRequest(response: Response<QuickSearchResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: QuickSearchResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    QuickSearchResponseModel::class.java
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

    val updateQuickRegistrationRetrofitCallback =
        object : RetrofitCallback<QuickRegistrationUpdateResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<QuickRegistrationUpdateResponseModel>?) {

                Toast.makeText(requireContext(), "Successfully Updated", Toast.LENGTH_LONG).show()
                appPreferences?.saveString(
                    AppConstants.LASTPIN,
                    responseBody?.body()?.responseContent?.uhid
                )


                clearAll()

                initViews(false)
            }

            override fun onBadRequest(errorBody: Response<QuickRegistrationUpdateResponseModel>?) {

                val gson = GsonBuilder().create()
                val responseModel: QuickRegistrationUpdateResponseModel
                try {
                    responseModel = gson.fromJson(
                        errorBody!!.errorBody()!!.string(),
                        QuickRegistrationUpdateResponseModel::class.java
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


    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)

        if (childFragment is SearchPatientDialogFragment) {
            childFragment.setOnOrderProcessRefreshListener(this)
        }

    }

    override fun onRefreshOrderList(searchData: QuickSearchresponseContent) {


        initViews(true)

        binding?.edtSeachPin?.setText("")


        updateId = 1
        appPreferences?.saveString(AppConstants.LASTPIN, searchData.uhid)

        selectdepartmentUUId = searchData.registred_facility_uuid
        binding?.lastpin?.visibility = View.VISIBLE
        binding?.pinLayout?.visibility = View.VISIBLE
        binding?.LastPin?.text = searchData.uhid
        binding?.quickName!!.setText(searchData.first_name)
        binding?.etmiddlename!!.setText(searchData.middle_name)
        binding?.etLastname!!.setText(searchData.last_name)
        binding?.quickAge!!.setText(searchData.age!!.toString())
        binding?.quickMobile!!.setText(searchData.patient_detail?.mobile!!.toString())
        binding?.etFathername?.setText(searchData.patient_detail.father_name ?: "")
        binding?.etFathername?.error = null

        if (searchData.patient_detail.aadhaar_number != null) {
            binding?.quickAather!!.setText(searchData.patient_detail.aadhaar_number.toString())
        }

        if (searchData.patient_detail.address_line1 != null) {
            binding?.doorNo!!.setText(searchData.patient_detail.address_line1.toString())
        }
        if (searchData.patient_detail.pincode != null) {
            binding?.quickPincode!!.setText(searchData.patient_detail.pincode.toString())
        }
        patientUUId = searchData.uuid
        if (searchData.gender_uuid != null) {
            val checkgender = hashGenderSpinnerList.any { it.key == searchData.gender_uuid }
            if (checkgender) {
                binding?.qucikGender!!.setSelection(hashGenderSpinnerList.get(searchData.gender_uuid)!!)
            } else {
                binding?.qucikGender!!.setSelection(0)
            }
        }

        if (searchData.period_uuid != null) {

            val checkperiod = hashPeriodSpinnerList.any { it.key == searchData.period_uuid }

            if (checkperiod) {
                binding?.qucikPeriod!!.setSelection(hashPeriodSpinnerList.get(searchData.period_uuid)!!)
            } else {
                binding?.qucikPeriod!!.setSelection(0)
            }
        }

        selectBelongUuid = searchData.patient_detail.taluk_uuid ?: 0
        selectVillageUuid = searchData.patient_detail.village_uuid ?: 0
        selectNationalityUuid = searchData.patient_detail.country_uuid ?: 0
        selectStateUuid = searchData.patient_detail.state_uuid ?: 0
        selectDistictUuid = searchData.patient_detail.district_uuid ?: 0

        viewModel?.getCovidNationalityList(
            "nationality_type",
            covidNationalityResponseCallback
        )

        viewModel?.getCovidNationalityList(
            "nationality_type",
            covidNationalityResponseCallback
        )

        if (searchData.patient_detail.lab_to_facility_uuid != null) {
            selectLabNameID = searchData.patient_detail.lab_to_facility_uuid
            if (selectLabNameID != 0) {
                viewModel!!.getLocationMaster(selectLabNameID!!, LocationMasterResponseCallback)
            }
        }

        if (searchData.uhid != null) {
            uhid = searchData.uhid.toString()
        }

        if (searchData.registered_date != "") {
            registerDate = searchData.registered_date!!
        }

        if (searchData.patient_detail.address_line2 != null) {
            binding?.quickAddress!!.setText(searchData.patient_detail.address_line2.toString())
        }

        if (searchData.title_uuid != null) {
            val titleuuid =
                hashSalutaionSpinnerList.any { it.key == searchData.title_uuid }
            if (titleuuid) {
                binding?.salutation!!.setSelection(hashSalutaionSpinnerList.get(searchData.title_uuid)!!)
            } else {
                binding?.salutation!!.setSelection(0)
            }
        }

        if (searchData.professional_title_uuid != null) {
            val titleuuid =
                hashProfestioanlSpinnerList.any { it.key == searchData.professional_title_uuid }
            if (titleuuid) {
                binding?.profesitonal!!.setSelection(hashProfestioanlSpinnerList.get(searchData.professional_title_uuid)!!)
            } else {
                binding?.profesitonal!!.setSelection(0)
            }
        }

        if (searchData.suffix_uuid != null) {
            val titleuuid =
                hashSuffixSpinnerList.any { it.key == searchData.suffix_uuid }
            if (titleuuid) {
                binding?.suffixcode!!.setSelection(hashSuffixSpinnerList.get(searchData.suffix_uuid)!!)
            } else {
                binding?.suffixcode!!.setSelection(0)
            }
        }
    }

    fun String.getDateWithServerTimeStamp(): Date? {
        val dateFormat = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.getDefault()
        )
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")  // IMP !!!
        try {
            return dateFormat.parse(this)
        } catch (e: ParseException) {
            return null
        }
    }
}
