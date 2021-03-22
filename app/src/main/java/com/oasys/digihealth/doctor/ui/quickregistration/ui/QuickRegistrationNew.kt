package com.oasys.digihealth.doctor.ui.quickregistration.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.BuildConfig
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentNewquickRegistationBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.dashboard.model.*
import com.oasys.digihealth.doctor.ui.dashboard.model.registration.*
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.FavAddResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.EncounterErrorAPIClass
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.ErrorAPIClass
import com.oasys.digihealth.doctor.ui.home.HomeActivity
import com.oasys.digihealth.doctor.ui.quick_reg.model.*
import com.oasys.digihealth.doctor.ui.quick_reg.model.activitysession.ResponseActivitySession
import com.oasys.digihealth.doctor.ui.quick_reg.model.activitysession.ResponseContentsactivitysession
import com.oasys.digihealth.doctor.ui.quick_reg.model.request.Detail
import com.oasys.digihealth.doctor.ui.quick_reg.model.request.Header
import com.oasys.digihealth.doctor.ui.quick_reg.model.request.QuickRegistrationRequestModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.request.SaveOrderRequestModel
import com.oasys.digihealth.doctor.ui.quick_reg.model.session.ResponseSesionModule
import com.oasys.digihealth.doctor.ui.quick_reg.view.DialogPDFViewerActivity
import com.oasys.digihealth.doctor.ui.quick_reg.view.SearchPatientDialogFragment
import com.oasys.digihealth.doctor.ui.quickregistration.model.*
import com.oasys.digihealth.doctor.ui.quickregistration.model.response.QuickelementRoleResponseModel
import com.oasys.digihealth.doctor.ui.quickregistration.viewmodel.QuickRegistrationNewViewModel
import com.oasys.digihealth.doctor.ui.quickregistration.viewmodel.QuickRegistrationNewViewModelFactory
import com.oasys.digihealth.doctor.utils.Utils
import com.oasys.digihealth.doctor.utils.custom_views.CustomProgressDialog
import retrofit2.Response
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.time.ExperimentalTime

class QuickRegistrationNew : Fragment(), SearchPatientDialogFragment.OnOrderProcessListener {

    private var MobileNumber: String? = ""
    private var quicksearch: String? = ""
    private var pinnumber: String? = ""
    private var session_uuid: Int? = 0
    private var responseactivitysession: List<ResponseContentsactivitysession?>? = ArrayList()
    private var currentDateandTime: String? = null
    private var handler: Handler? = null
    private var sharepreferlastPin: String? = ""
    private var searchresponseData: ArrayList<QuickSearchresponseContent?> = ArrayList()
    private var currentPage = 0
    private var pageSize = 10

    private var schemaData: ArrayList<SchemaResponse> = ArrayList()
    private var selectschema = 0

    var searchData = QuickSearchresponseContent()
    var selectedState: String = ""
    var stateListfilteritem: ArrayList<State> = ArrayList()
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
    var binding: FragmentNewquickRegistationBinding? = null
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
    private var quickRegUatSaveRequest = RegistrationUatSaveRequest()
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
    var searchDob: String? = null
    private var sariStatus: Boolean = false
    private var iliStatus: Boolean = false
    private var casualtyBased: Boolean = false
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
                R.layout.fragment_newquick_registation,
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

        val args = arguments
        if (args == null) {

            binding?.pinLayout?.visibility = View.GONE

            //  Toast.makeText(activity, "arguments is null ", Toast.LENGTH_LONG).show()
        } else {

            var pinshow = args.getInt("PIN")

            if (pinshow == 0) {

                binding?.pinLayout?.visibility = View.GONE

            } else {

                val lastpin = appPreferences?.getString(AppConstants.LASTPIN)

                binding?.lastpinnumber?.text = lastpin
                binding?.pinLayout?.visibility = View.VISIBLE

            }

        }


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

        if (BuildConfig.FLAVOR == "puneuat" || BuildConfig.FLAVOR == "puneprod") {

            binding?.schematv?.visibility = View.VISIBLE

            binding?.schema?.visibility = View.VISIBLE

        } else {
            binding?.schematv?.visibility = View.GONE

            binding?.schema?.visibility = View.GONE

        }

        viewModel?.getTextMethod(facility_id!!, getTestMethdCallBack)
        //Privillage
        viewModel?.getPrevilage(facility_id!!, roleid, getPrivilageRetrofitCallback)

        viewModel!!.getLocationAPI(covidLocationResponseCallback)

        viewModel!!.getAll("suffix", suffixResponseCallback)

        viewModel!!.getAll("unit", unitResponseCallback)

        viewModel!!.getCommunity("community", getCommunityListRetrofitCallBack)

        viewModel?.getCovidNameTitleList(
            facility_id!!,
            covidSalutationResponseCallback
        )

        viewModel?.getActivityRole(facility_id!!, getActivityPrivilageRetrofitCallback)

        binding?.department?.imeOptions = EditorInfo.IME_ACTION_DONE

        initViews()
        listeners()


        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")

        tat_start_time = sdf.format(Date())

        viewModel!!.getDepartmentList(FavLabdepartmentRetrofitCallBack)


        binding?.searchDrawerCardView?.setOnClickListener {
            binding?.drawerLayout!!.openDrawer(GravityCompat.END)
        }

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

    private fun initViews() {

    }

    private fun listeners() {
        /* binding?.department?.setOnItemClickListener(object : OnItemClickListener() {
             fun onItemClick(
                 arg0: AdapterView<*>?,
                 arg1: View?,
                 arg2: Int,
                 arg3: Long
             ) {
                 val view: View = this.getCurrentFocus()
                 if (view != null) {
                     val inputManager: InputMethodManager =
                         this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                     inputManager.hideSoftInputFromWindow(
                         view.windowToken,
                         InputMethodManager.HIDE_NOT_ALWAYS
                     )
                 }
             }
         })*/


        binding?.print?.setOnClickListener {

            if (updateId == 1) {

                val pdfRequestModel = PrintQuickRequest()
                pdfRequestModel.componentName = "basic"
                pdfRequestModel.uuid = searchData.uuid!!
                pdfRequestModel.uhid = uhid
                pdfRequestModel.facilityName = facility_Name
                pdfRequestModel.firstName = binding?.quickName?.text.toString()
                pdfRequestModel.period = CovidPeriodList[selectPeriodUuid]!!
                pdfRequestModel.age = searchData.age!!
                pdfRequestModel.registered_date = registerDate
                pdfRequestModel.sari = sariStatus
                pdfRequestModel.ili = iliStatus
                pdfRequestModel.noSymptom = nosymptomsStatus
                pdfRequestModel.is_dob_auto_calculate = is_dob_auto_calculate
                pdfRequestModel.session = session_name!!
                pdfRequestModel.gender = CovidGenderList[selectGenderUuid]!!
                pdfRequestModel.mobile = binding!!.quickMobile.text.toString()


                if (!binding?.etFathername?.text?.trim().isNullOrEmpty()) {
                    pdfRequestModel.fatherName =
                        binding?.etFathername?.text?.trim().toString()

                }
                if (!binding?.quickAather?.text.toString().isNullOrEmpty()) {

                    pdfRequestModel.aadhaarNumber = binding?.quickAather?.text.toString()
                }

                if (addressenable!!) {

                    pdfRequestModel.addressDetails.doorNum = binding!!.doorNo.text.toString()
                    pdfRequestModel.addressDetails.streetName =
                        binding!!.quickAddress.text.toString()

                    if (selectNationalityUuid != 0) {
                        pdfRequestModel.addressDetails.country =
                            CovidNationalityList[selectNationalityUuid]!!
                    }
                    if (selectDistictUuid != 0) {


                        pdfRequestModel.addressDetails.district =
                            CovidDistictList[selectDistictUuid]!!


                    }

                    if (selectStateUuid != 0) {


                        try {
                            pdfRequestModel.addressDetails.state = CovidStateList[selectStateUuid]!!
                        } catch (e: Exception) {
                        }


                    }

                    if (selectVillageUuid != 0) {


                        pdfRequestModel.addressDetails.village =
                            CovidVillageList[selectVillageUuid]!!


                    }


                    if (selectBelongUuid != 0) {

                        pdfRequestModel.addressDetails.taluk = CovidBlockList[selectBelongUuid]!!


                    }

                    if (!binding?.quickPincode?.text.toString().isNullOrEmpty()) {


                        pdfRequestModel.addressDetails.pincode =
                            binding?.quickPincode?.text.toString()


                    }


                }
                /*        pdfRequestModel.visitNum =
                            responseBody.body()?.responseContent?.patient_visits!!.seqNum!!
        */

                pdfRequestModel.dob = searchDob!!

                if (selectSalutationUuid != 0) {

                    pdfRequestModel.salutation = SalutaionList[selectSalutationUuid]!!

                }
                if (selectProffestionalUuid != 0) {

                    pdfRequestModel.professional = ProfestioanlList[selectProffestionalUuid]!!
                }

                if (selectCoummityUuid != 0) {

                    pdfRequestModel.community = CommunityList[selectCoummityUuid]!!
                }

                pdfRequestModel.middleName = binding!!.etmiddlename.text.toString()

                pdfRequestModel.lastName = binding!!.etLastname.text.toString()


                if (selectSuffixUuid != 0) {

                    pdfRequestModel.suffixCode = SuffixList[selectSuffixUuid]!!
                }


                if (selectSuffixUuid != 0) {

                    pdfRequestModel.suffixCode = SuffixList[selectSuffixUuid]!!
                }

                pdfRequestModel.department = binding!!.department.text.toString()

                val bundle = Bundle()
                bundle.putParcelable(AppConstants.RESPONSECONTENT, pdfRequestModel)
                bundle.putInt(AppConstants.RESPONSENEXT, 190)
                bundle.putString("From", "Quick")
                bundle.putInt("next", onNext!!)

                val labtemplatedialog = QuickDialogPDFViewerActivity()

                labtemplatedialog.arguments = bundle

                (activity as HomeActivity).replaceFragment(labtemplatedialog)

            }

        }


        binding?.searchButton?.setOnClickListener {


            MobileNumber = binding?.quickMobileNum?.text?.toString()
            quicksearch = binding?.qucikSearch?.text?.toString()
            oldPin = binding?.quickExistPin?.text?.toString()
            pdsNumber = binding?.quickPds?.text?.toString()


            if ((quicksearch != "") || (MobileNumber != "") || (oldPin != "" || pdsNumber != "")) {


                if (pdsNumber?.length!! > 0) {


                    val ft = childFragmentManager.beginTransaction()
                    val dialog = SearchPatientDialogFragment()
                    val bundle = Bundle()
                    bundle.putString("PDS", pdsNumber)
                    dialog.arguments = bundle
                    dialog.show(ft, "Tag")
                    binding?.drawerLayout!!.closeDrawer(GravityCompat.END)
                    return@setOnClickListener


                } else {


                    if (oldPin?.trim()?.length!! > 0) {


                        if (oldPin?.trim()?.length!! < 13) {

                            val searchPatientRequestModelCovid = SearchPatientRequestModelCovid()

                            searchPatientRequestModelCovid.pageNo = currentPage
                            searchPatientRequestModelCovid.paginationSize = pageSize
                            searchPatientRequestModelCovid.sortField =
                                "patient_visits[0].registered_date"
                            searchPatientRequestModelCovid.sortOrder = "DESC"

                            searchPatientRequestModelCovid.aadhaar = oldPin

                            viewModel?.searchPatient(
                                searchPatientRequestModelCovid,
                                patientSearchRetrofitCallBack
                            )


                        } else {

                            viewModel?.searchOldPin(
                                oldPin!!,
                                oldPinSearchRetrofitCallBack
                            )
                        }

                    } else {


                        val searchPatientRequestModelCovid = SearchPatientRequestModelCovid()

                        searchPatientRequestModelCovid.pageNo = currentPage
                        searchPatientRequestModelCovid.paginationSize = pageSize
                        searchPatientRequestModelCovid.sortField =
                            "patient_visits[0].registered_date"
                        searchPatientRequestModelCovid.sortOrder = "DESC"



                        if (quicksearch?.trim()?.length!! > 2) {

                            searchPatientRequestModelCovid.searchKeyWord = quicksearch?.trim()



                            viewModel?.searchPatient(
                                searchPatientRequestModelCovid,
                                patientSearchRetrofitCallBack
                            )

                        } else if (MobileNumber?.trim()?.length!! > 0 && MobileNumber?.trim()?.length!! <= 10) {

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
                    }

                }

            } else {


                Toast.makeText(context, "Please enter any one felid", Toast.LENGTH_SHORT).show()

            }
            binding?.drawerLayout!!.closeDrawer(GravityCompat.END)

        }


        /*  binding?.searchButton?.setOnClickListener {


              MobileNumber = binding?.quickMobileNum?.text?.toString()
              quicksearch = binding?.qucikSearch?.text?.toString()
              pinnumber = binding?.quickPin?.text?.toString()
              pdsNumber = binding?.quickPds?.text?.toString()

              aatherNo = binding?.quickAatherSearch?.text?.toString()!!

              oldPin = binding?.quickExistPin?.text?.toString()

              if ((quicksearch != "" || pinnumber != "") || (MobileNumber != "" || pdsNumber != "") || (oldPin != "" || aatherNo != "")) {

                  if (pdsNumber?.length!! > 0) {
                      val ft = childFragmentManager.beginTransaction()
                      val dialog = SearchPatientDialogFragment()
                      val bundle = Bundle()
                      bundle.putString("PDS", pdsNumber)
                      dialog.setArguments(bundle)
                      dialog.show(ft, "Tag")
                      binding?.drawerLayout!!.closeDrawer(GravityCompat.END)
                      return@setOnClickListener
                  }

                  if (oldPin?.trim()?.length!! > 0) {
                      viewModel?.searchOldPin(
                          oldPin!!,
                          oldPinSearchRetrofitCallBack
                      )
                  } else {
                      viewModel?.searchPatient(
                          quicksearch!!,
                          pinnumber!!,
                          MobileNumber!!,
                          pageSize,
                          currentPage,
                          aatherNo!!,
                          patientSearchRetrofitCallBack
                      )
                  }

              } else {

                  Toast.makeText(context, "Please enter any one felid", Toast.LENGTH_SHORT).show()

              }
              binding?.drawerLayout!!.closeDrawer(GravityCompat.END)

          }
  */

        binding?.addressheader?.setOnClickListener {

            if (binding?.addressresultlayout?.visibility == View.VISIBLE) {
                binding?.addressresultlayout?.visibility = View.GONE

            } else {
                binding?.addressresultlayout?.visibility = View.VISIBLE
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


        binding?.advanceSearchText?.setOnClickListener {

            if (binding?.advanceSearchLayout?.visibility == View.VISIBLE) {
                binding?.advanceSearchLayout?.visibility = View.GONE


            } else {
                binding?.advanceSearchLayout?.visibility = View.VISIBLE


            }
        }

        binding!!.clear.setOnClickListener {

            binding!!.qucikSearch.setText("")

            binding!!.quickPin.setText("")

            binding!!.quickMobileNum.setText("")

            binding!!.quickExistPin.setText("")

            binding!!.quickPds.setText("")

            binding?.quickAatherSearch?.setText("")

        }

        viewModel!!.getActivitySession(facility_id!!, activitysessionResponseCallback)


        binding!!.clearCardView.setOnClickListener {
//            val ft =
//                fragmentManager!!.beginTransaction()
//            if (Build.VERSION.SDK_INT >= 26) {
//                ft.setReorderingAllowed(false)
//            }
//            ft.detach(this).attach(this).commit()


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

            oldPin = ""
            created_date = ""

            binding!!.qucikSearch.setText("")

            binding!!.quickPin.setText("")

            binding!!.quickMobile.setText("")

            binding!!.quickExistPin.setText("")

            binding!!.quickAge.setText("")
            binding?.department?.setText("")
            binding!!.quickPds.setText("")
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
            binding?.community?.setSelection(0)
            binding?.department?.setText("")
            binding?.unit?.setSelection(0)
            binding?.profesitonal?.setSelection(0)



            viewModel?.getTextMethod(facility_id!!, getTestMethdCallBack)
            //Privillage
            viewModel?.getPrevilage(facility_id!!, roleid, getPrivilageRetrofitCallback)



            viewModel!!.getLocationAPI(covidLocationResponseCallback)

            viewModel!!.getAll("suffix", suffixResponseCallback)

            viewModel!!.getAll("unit", unitResponseCallback)

            viewModel!!.getCommunity("community", getCommunityListRetrofitCallBack)

            viewModel?.getCovidNameTitleList(
                facility_id!!,
                covidSalutationResponseCallback
            )

        }

        //  binding?.listscroll?.requestChildFocus(binding!!.root, binding!!.root)

        viewModel!!.getApplicationRules(getApplicationRulesResponseCallback)


        binding?.switchCheck?.setOnCheckedChangeListener { _, isChecked ->

            Matanity = isChecked

        }

        binding?.salutation?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
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

        /*    binding?.salutation?.onItemSelectedListener =
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

                        selectSalutationUuid =
                            SalutaionList.filterValues { it == itemValue }.keys.toList()[0]

                        val selectedData=salutaioData[position]

                        if(selectedData.code=="1" || selectedData.code=="5"){

                            for(i in GenderlistData.indices){

                                if(GenderlistData[i]?.code=="1"){

                                    binding?.qucikGender?.setSelection(i)

                                    break
                                }
                            }

                        }
                        else if(selectedData.code=="2" || selectedData.code=="3"){

                            for(i in GenderlistData.indices){

                                if(GenderlistData[i]?.code=="2"){

                                    binding?.qucikGender?.setSelection(i)

                                    break
                                }
                            }

                        }


                    }

                }*/

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

        binding?.community?.onItemSelectedListener =
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
                    selectCoummityUuid =
                        CommunityList.filterValues { it == itemValue }.keys.toList()[0]

                }

            }

        binding?.unit?.onItemSelectedListener =
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
                    selectUnitUuid = UnitList.filterValues { it == itemValue }.keys.toList()[0]

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


        binding!!.department.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {

                val datasize = s.trim().length
                if (datasize in 2..7) {
                    val requests: GetAllDepartmentRequest = GetAllDepartmentRequest()

                    var attributeList: ArrayList<String> = ArrayList()

                    attributeList.add("uuid")
                    attributeList.add("code")
                    attributeList.add("name")
                    attributeList.add("end_age")
                    attributeList.add("start_age")
                    attributeList.add("is_speciality")

                    requests.attributes = attributeList
                    requests.registrationBased = true
                    requests.isClinical = true
                    requests.casualtyBased = casualtyBased
                    requests.facilityBased = true
                    requests.deptId = departmentUUId.toString()
                    requests.search = s.toString()
                    requests.pageNo = 0
                    requests.paginationSize = 100
                    requests.genderId = selectGenderUuid!!

                    viewModel!!.getAllDepartment(requests, favLabSearchentCallBack)

                }
            }
        })




        binding?.schema?.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {

                val datasize = s.trim().length
                if (datasize in 2..7) {
                    val requests: SchemaRequest = SchemaRequest()


                    requests.search = s.toString()
                    requests.pageNo = 0
                    requests.paginationSize = 100
                    requests.code_or_name = 0
                    requests.scheme_type_uuid = 0
                    requests.status = 0

                    viewModel!!.getAllSchema(requests, schemaSearchentCallBack)

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

            onNext = 0




            if (selectPeriodUuid == 0) {

                Toast.makeText(context, "Please Select Period", Toast.LENGTH_SHORT).show()

                return@setOnClickListener

            }


            if (!binding?.etRemarkname?.text.isNullOrEmpty() && binding?.etRemarkname?.text?.toString()?.length!! < 3) {

                Toast.makeText(context, "Remarks Minimum 3 letter", Toast.LENGTH_SHORT).show()

                return@setOnClickListener

            }

            if (selectDistictUuid == 0 && selectedState.equals("TAMIL NADU", true)) {

                Toast.makeText(
                    this.context,
                    "Please Select Distict",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener

            }



            if (BuildConfig.FLAVOR == "puneuat" || BuildConfig.FLAVOR == "puneprod") {

                if (selectschema == 0) {

                    Toast.makeText(context, "Please Select Schema", Toast.LENGTH_SHORT).show()

                    return@setOnClickListener

                }

            }

            if (!binding!!.quickName.text.toString().isNullOrEmpty()) {

                if (!binding!!.quickAge.text.toString().isNullOrEmpty()) {

                    if (!binding!!.quickMobile.text.toString().isNullOrEmpty()) {

                        if (selectGenderUuid != 0) {


                            if (selectdepartmentUUId != 0 && (!binding!!.department.text.toString()
                                    .isNullOrEmpty())
                            ) {

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

                                if (updateId == 0) {

                                    if (oldPin == "") {


                                        if (BuildConfig.FLAVOR == "puneuat" || BuildConfig.FLAVOR == "puneprod") {


                                            quickRegUatSaveRequest.patient_scheme_uuid =
                                                selectschema






                                            quickRegUatSaveRequest.first_name =
                                                binding!!.quickName.text.toString()
                                            quickRegUatSaveRequest.mobile =
                                                binding!!.quickMobile.text.toString()
                                            quickRegUatSaveRequest.age =
                                                binding!!.quickAge.text.toString().toInt()
                                            quickRegUatSaveRequest.gender_uuid =
                                                selectGenderUuid
                                            quickRegUatSaveRequest.session_uuid =
                                                session_uuid
                                            quickRegUatSaveRequest.department_uuid =
                                                selectdepartmentUUId.toString()

                                            quickRegUatSaveRequest.period_uuid =
                                                selectPeriodUuid
                                            quickRegUatSaveRequest.registred_facility_uuid =
                                                facility_id.toString()
                                            quickRegUatSaveRequest.is_dob_auto_calculate =
                                                1
                                            quickRegUatSaveRequest.isDrMobileApi = 1
                                            quickRegUatSaveRequest.country_uuid =
                                                selectNationalityUuid
                                            quickRegUatSaveRequest.state_uuid =
                                                selectStateUuid
                                            quickRegUatSaveRequest.pincode =
                                                binding!!.quickPincode.text.toString()
                                            quickRegUatSaveRequest.address_line2 =
                                                binding!!.quickAddress.text.toString()

                                            quickRegUatSaveRequest.address_line1 =
                                                binding!!.doorNo.text.toString()
                                            quickRegUatSaveRequest.district_uuid =
                                                selectDistictUuid
                                            quickRegUatSaveRequest.taluk_uuid =
                                                selectBelongUuid.toString().toString()
                                            quickRegUatSaveRequest.saveExists =
                                                alreadyExists

                                            if (is_dob_auto_calculate == 0) {


                                                DOB = utils!!.convertDateFormat(
                                                    binding?.dob?.text.toString(),
                                                    "dd-MM-yyyy",
                                                    "yyyy-MM-dd'T'HH:mm:ss.SSS"
                                                )

                                                quickRegUatSaveRequest.dob =
                                                    DOB
                                            } else {

                                                quickRegUatSaveRequest.dob =
                                                    DOB

                                            }
                                            quickRegUatSaveRequest.is_adult = isadult

                                            quickRegUatSaveRequest.middle_name =
                                                binding!!.etmiddlename.text.toString()

                                            quickRegUatSaveRequest.last_name =
                                                binding!!.etLastname.text.toString()

                                            quickRegUatSaveRequest.unit_uuid =
                                                selectUnitUuid

                                            quickRegUatSaveRequest.suffix_uuid =
                                                selectSuffixUuid

                                            quickRegUatSaveRequest.professional_title_uuid =
                                                selectProffestionalUuid

                                            quickRegUatSaveRequest.title_uuid =
                                                selectSalutationUuid

                                            quickRegUatSaveRequest.aadhaar_number =
                                                binding?.quickAather?.text.toString()

                                            quickRegUatSaveRequest.is_maternity =
                                                Matanity

                                            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")

                                            val dateInString = sdf.format(Date())
                                            quickRegUatSaveRequest.tat_start_time =
                                                tat_start_time

                                            quickRegUatSaveRequest.tat_end_time =
                                                dateInString

                                            quickRegUatSaveRequest.village_uuid =
                                                selectVillageUuid.toString()


                                            if (!binding?.etFathername?.text?.trim()
                                                    .isNullOrEmpty()
                                            ) {
                                                quickRegUatSaveRequest.father_name =
                                                    binding?.etFathername?.text?.trim().toString()

                                            }


                                            if (!binding?.etRemarkname?.text?.trim()
                                                    .isNullOrEmpty()
                                            ) {
                                                quickRegUatSaveRequest.remarks =
                                                    binding?.etRemarkname?.text?.trim().toString()

                                            }
                                            var req =
                                                Gson().toJson(quickRegistrationSaveResponseModel)
                                            Log.e("Data", req.toString())
                                            viewModel?.quickRegistrationSaveList(
                                                quickRegUatSaveRequest,
                                                saveQuickRegistrationRetrofitCallback
                                            )

                                        } else {


                                            quickRegistrationSaveResponseModel.first_name =
                                                binding!!.quickName.text.toString()
                                            quickRegistrationSaveResponseModel.mobile =
                                                binding!!.quickMobile.text.toString()
                                            quickRegistrationSaveResponseModel.age =
                                                binding!!.quickAge.text.toString().toInt()
                                            quickRegistrationSaveResponseModel.gender_uuid =
                                                selectGenderUuid
                                            quickRegistrationSaveResponseModel.session_uuid =
                                                session_uuid
                                            quickRegistrationSaveResponseModel.department_uuid =
                                                selectdepartmentUUId.toString()

                                            quickRegistrationSaveResponseModel.period_uuid =
                                                selectPeriodUuid
                                            quickRegistrationSaveResponseModel.registred_facility_uuid =
                                                facility_id.toString()
                                            quickRegistrationSaveResponseModel.is_dob_auto_calculate =
                                                1
                                            quickRegistrationSaveResponseModel.isDrMobileApi = 1
                                            quickRegistrationSaveResponseModel.country_uuid =
                                                selectNationalityUuid
                                            quickRegistrationSaveResponseModel.state_uuid =
                                                selectStateUuid
                                            quickRegistrationSaveResponseModel.pincode =
                                                binding!!.quickPincode.text.toString()
                                            quickRegistrationSaveResponseModel.address_line2 =
                                                binding!!.quickAddress.text.toString()

                                            quickRegistrationSaveResponseModel.address_line1 =
                                                binding!!.doorNo.text.toString()
                                            quickRegistrationSaveResponseModel.district_uuid =
                                                selectDistictUuid
                                            quickRegistrationSaveResponseModel.taluk_uuid =
                                                selectBelongUuid.toString().toString()
                                            quickRegistrationSaveResponseModel.saveExists =
                                                alreadyExists

                                            if (is_dob_auto_calculate == 0) {


                                                DOB = utils!!.convertDateFormat(
                                                    binding?.dob?.text.toString(),
                                                    "dd-MM-yyyy",
                                                    "yyyy-MM-dd'T'HH:mm:ss.SSS"
                                                )

                                                quickRegistrationSaveResponseModel.dob =
                                                    DOB
                                            } else {

                                                quickRegistrationSaveResponseModel.dob =
                                                    DOB

                                            }
                                            quickRegistrationSaveResponseModel.is_adult = isadult

                                            quickRegistrationSaveResponseModel.middle_name =
                                                binding!!.etmiddlename.text.toString()

                                            quickRegistrationSaveResponseModel.last_name =
                                                binding!!.etLastname.text.toString()

                                            quickRegistrationSaveResponseModel.unit_uuid =
                                                selectUnitUuid

                                            quickRegistrationSaveResponseModel.suffix_uuid =
                                                selectSuffixUuid

                                            quickRegistrationSaveResponseModel.professional_title_uuid =
                                                selectProffestionalUuid

                                            quickRegistrationSaveResponseModel.title_uuid =
                                                selectSalutationUuid

                                            quickRegistrationSaveResponseModel.aadhaar_number =
                                                binding?.quickAather?.text.toString()

                                            quickRegistrationSaveResponseModel.is_maternity =
                                                Matanity

                                            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")

                                            val dateInString = sdf.format(Date())
                                            quickRegistrationSaveResponseModel.tat_start_time =
                                                tat_start_time

                                            quickRegistrationSaveResponseModel.tat_end_time =
                                                dateInString

                                            quickRegistrationSaveResponseModel.village_uuid =
                                                selectVillageUuid.toString()


                                            if (!binding?.etFathername?.text?.trim()
                                                    .isNullOrEmpty()
                                            ) {
                                                quickRegistrationSaveResponseModel.father_name =
                                                    binding?.etFathername?.text?.trim().toString()

                                            }


                                            if (!binding?.etRemarkname?.text?.trim()
                                                    .isNullOrEmpty()
                                            ) {
                                                quickRegistrationSaveResponseModel.remarks =
                                                    binding?.etRemarkname?.text?.trim().toString()

                                            }


/*




                                        if(created_date=="") {
                                            quickRegistrationSaveResponseModel.created_date =
                                                created_date
                                        }
                                        quickRegistrationSaveResponseModel.old_pin=oldPin
*/


                                            var req =
                                                Gson().toJson(quickRegistrationSaveResponseModel)
                                            Log.e("Data", req.toString())
                                            viewModel?.quickRegistrationSaveList(
                                                quickRegistrationSaveResponseModel,
                                                saveQuickRegistrationRetrofitCallback
                                            )

                                        }


                                    } else {

                                        var quickregUseoldpin = QuickRegWItholdpinSaveReq()
                                        quickregUseoldpin.first_name =
                                            binding!!.quickName.text.toString()
                                        quickregUseoldpin.mobile =
                                            binding!!.quickMobile.text.toString()
                                        quickregUseoldpin.age =
                                            binding!!.quickAge.text.toString().toInt()
                                        quickregUseoldpin.gender_uuid =
                                            selectGenderUuid
                                        quickregUseoldpin.session_uuid =
                                            session_uuid
                                        quickregUseoldpin.department_uuid =
                                            selectdepartmentUUId.toString()
                                        quickregUseoldpin.period_uuid =
                                            selectPeriodUuid
                                        quickregUseoldpin.registred_facility_uuid =
                                            facility_id.toString()
                                        quickregUseoldpin.is_dob_auto_calculate =
                                            1
                                        quickregUseoldpin.isDrMobileApi = 1
                                        quickregUseoldpin.country_uuid =
                                            selectNationalityUuid
                                        quickregUseoldpin.state_uuid =
                                            selectStateUuid
                                        quickregUseoldpin.pincode =
                                            binding!!.quickPincode.text.toString()
                                        quickregUseoldpin.address_line2 =
                                            binding!!.quickAddress.text.toString()

                                        quickregUseoldpin.address_line1 =
                                            binding!!.doorNo.text.toString()
                                        quickregUseoldpin.district_uuid =
                                            selectDistictUuid
                                        quickregUseoldpin.taluk_uuid =
                                            selectBelongUuid.toString()
                                        quickregUseoldpin.saveExists =
                                            alreadyExists

                                        if (is_dob_auto_calculate == 0) {

                                            DOB = utils!!.convertDateFormat(
                                                binding?.dob?.text.toString(),
                                                "dd-MM-yyyy",
                                                "yyyy-MM-dd'T'HH:mm:ss.SSS"
                                            )

                                        } else {


                                        }
                                        quickregUseoldpin.dob =
                                            DOB
                                        quickregUseoldpin.is_adult = isadult

                                        quickregUseoldpin.middle_name =
                                            binding!!.etmiddlename.text.toString()

                                        quickregUseoldpin.last_name =
                                            binding!!.etLastname.text.toString()

                                        quickregUseoldpin.unit_uuid =
                                            selectUnitUuid

                                        quickregUseoldpin.suffix_uuid =
                                            selectSuffixUuid

                                        quickregUseoldpin.professional_title_uuid =
                                            selectProffestionalUuid

                                        quickregUseoldpin.title_uuid =
                                            selectSalutationUuid

                                        quickregUseoldpin.aadhaar_number =
                                            binding!!.quickAather.text.toString()

                                        if (created_date != "") {
                                            quickregUseoldpin.created_date =
                                                created_date
                                        }
                                        quickregUseoldpin.old_pin = oldPin

                                        quickregUseoldpin.is_maternity = Matanity

                                        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")

                                        val dateInString = sdf.format(Date())

                                        quickregUseoldpin.tat_start_time = tat_start_time

                                        quickregUseoldpin.tat_end_time = dateInString


                                        quickregUseoldpin.village_uuid =
                                            selectVillageUuid.toString()

                                        if (!binding?.etFathername?.text?.trim()
                                                .isNullOrEmpty()
                                        ) {
                                            quickregUseoldpin.father_name =
                                                binding?.etFathername?.text?.trim().toString()

                                        }

                                        if (!binding?.etRemarkname?.text?.trim()
                                                .isNullOrEmpty()
                                        ) {
                                            quickregUseoldpin.remarks =
                                                binding?.etRemarkname?.text?.trim().toString()

                                        }


                                        viewModel?.quickRegistrationUsingOldpinSaveList(
                                            quickregUseoldpin,
                                            saveQuickRegistrationRetrofitCallback
                                        )

                                    }


                                } else {

                                    if (oldPin == "") {


                                        val quickUpdateRequestModel =
                                            QuickRegistrationUpdateRequest()

                                        quickUpdateRequestModel.session_uuid = session_uuid!!

                                        quickUpdateRequestModel.is_dob_auto_calculate = 1
                                        quickUpdateRequestModel.country_uuid =
                                            selectNationalityUuid
                                        quickUpdateRequestModel.state_uuid = selectStateUuid
                                        quickUpdateRequestModel.pincode =
                                            binding!!.quickPincode.text.toString()
                                        quickUpdateRequestModel.address_line2 =
                                            binding!!.quickAddress.text.toString()
                                        quickUpdateRequestModel.district_uuid =
                                            selectDistictUuid
                                        quickUpdateRequestModel.taluk_uuid = selectBelongUuid
                                        quickUpdateRequestModel.village_uuid = selectVillageUuid
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
                                        quickUpdateRequestModel.session_uuid = session_uuid!!
                                        quickUpdateRequestModel.department_uuid =
                                            selectdepartmentUUId!!
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
                                            selectBelongUuid
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

                                        quickUpdateRequestModel.middle_name =
                                            binding!!.etmiddlename.text.toString()

                                        quickUpdateRequestModel.last_name =
                                            binding!!.etLastname.text.toString()

                                        quickUpdateRequestModel.unit_uuid =
                                            selectUnitUuid

                                        quickUpdateRequestModel.suffix_uuid =
                                            selectSuffixUuid

                                        quickUpdateRequestModel.professional_title_uuid =
                                            selectProffestionalUuid

                                        quickUpdateRequestModel.title_uuid =
                                            selectSalutationUuid

                                        quickUpdateRequestModel.aadhaar_number =
                                            binding!!.quickAather.text.toString()

                                        quickUpdateRequestModel.isDrMobileApi = 1

                                        quickUpdateRequestModel.is_maternity = Matanity



                                        if (!binding?.etFathername?.text?.trim()
                                                .isNullOrEmpty()
                                        ) {
                                            quickUpdateRequestModel.father_name =
                                                binding?.etFathername?.text?.trim().toString()

                                        }

                                        if (!binding?.etRemarkname?.text?.trim()
                                                .isNullOrEmpty()
                                        ) {
                                            quickUpdateRequestModel.remarks =
                                                binding?.etRemarkname?.text?.trim().toString()

                                        }

                                        viewModel?.quickRegistrationUpdateformQuick(
                                            quickUpdateRequestModel,
                                            updateQuickRegistrationRetrofitCallback
                                        )


                                    } else {

                                        val quickUpdateRequestModel =
                                            QuickRegistrationUpdateRequestWitholdPin()

                                        quickUpdateRequestModel.session_uuid = session_uuid!!

                                        quickUpdateRequestModel.is_dob_auto_calculate = 1
                                        quickUpdateRequestModel.country_uuid =
                                            selectNationalityUuid
                                        quickUpdateRequestModel.state_uuid = selectStateUuid
                                        quickUpdateRequestModel.pincode =
                                            binding!!.quickPincode.text.toString()
                                        quickUpdateRequestModel.address_line2 =
                                            binding!!.quickAddress.text.toString()
                                        quickUpdateRequestModel.district_uuid =
                                            selectDistictUuid
                                        quickUpdateRequestModel.taluk_uuid = selectBelongUuid
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
                                        quickUpdateRequestModel.session_uuid = session_uuid!!
                                        quickUpdateRequestModel.department_uuid =
                                            selectdepartmentUUId!!
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
                                            selectBelongUuid
                                        quickUpdateRequestModel.saveExists =
                                            alreadyExists

                                        quickUpdateRequestModel.village_uuid = selectVillageUuid

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

                                        quickUpdateRequestModel.middle_name =
                                            binding!!.etmiddlename.text.toString()

                                        quickUpdateRequestModel.last_name =
                                            binding!!.etLastname.text.toString()

                                        quickUpdateRequestModel.unit_uuid =
                                            selectUnitUuid

                                        quickUpdateRequestModel.suffix_uuid =
                                            selectSuffixUuid

                                        quickUpdateRequestModel.professional_title_uuid =
                                            selectProffestionalUuid

                                        quickUpdateRequestModel.title_uuid =
                                            selectSalutationUuid

                                        quickUpdateRequestModel.is_maternity = this.Matanity!!

                                        quickUpdateRequestModel.isDrMobileApi = 1
                                        if (created_date != "") {

                                            quickUpdateRequestModel.created_date = created_date
                                            quickUpdateRequestModel.old_pin = oldPin

                                        }

                                        quickUpdateRequestModel.village_uuid = selectVillageUuid

                                        if (!binding?.etFathername?.text?.trim()
                                                .isNullOrEmpty()
                                        ) {
                                            quickUpdateRequestModel.father_name =
                                                binding?.etFathername?.text?.trim().toString()

                                        }

                                        if (!binding?.etRemarkname?.text?.trim()
                                                .isNullOrEmpty()
                                        ) {
                                            quickUpdateRequestModel.remarks =
                                                binding?.etRemarkname?.text?.trim().toString()

                                        }

                                        viewModel?.quickRegistrationUpdateformQuick(
                                            quickUpdateRequestModel,
                                            updateQuickRegistrationRetrofitCallback
                                        )


                                    }

                                }

                                /*  } else {
                                  Toast.makeText(
                                      this.context,
                                      "Please Select Salutaion",
                                      Toast.LENGTH_SHORT
                                  ).show()

                              }*/


                            } else {

                                Toast.makeText(
                                    this.context,
                                    "Please Department",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }


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

        binding?.saveOrderCardView!!.setOnClickListener {

            onNext = 1


            if (selectPeriodUuid == 0) {

                Toast.makeText(context, "Please Select Period", Toast.LENGTH_SHORT).show()

                return@setOnClickListener

            }

            if (!binding!!.quickName.text.toString().isNullOrEmpty()) {

                if (!binding!!.quickAge.text.toString().isNullOrEmpty()) {

                    if (!binding!!.quickMobile.text.toString().isNullOrEmpty()) {

                        if (selectGenderUuid != 0) {

                            if (selectdepartmentUUId != 0 && (!binding!!.department.text.toString()
                                    .isNullOrEmpty())
                            ) {

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

                                if (updateId == 0) {

                                    if (oldPin == "") {

                                        quickRegistrationSaveResponseModel.first_name =
                                            binding!!.quickName.text.toString()
                                        quickRegistrationSaveResponseModel.mobile =
                                            binding!!.quickMobile.text.toString()
                                        quickRegistrationSaveResponseModel.age =
                                            binding!!.quickAge.text.toString().toInt()
                                        quickRegistrationSaveResponseModel.gender_uuid =
                                            selectGenderUuid
                                        quickRegistrationSaveResponseModel.session_uuid =
                                            session_uuid
                                        quickRegistrationSaveResponseModel.department_uuid =
                                            selectdepartmentUUId.toString()


                                        quickRegistrationSaveResponseModel.period_uuid =
                                            selectPeriodUuid
                                        quickRegistrationSaveResponseModel.registred_facility_uuid =
                                            facility_id.toString()
                                        quickRegistrationSaveResponseModel.is_dob_auto_calculate =
                                            1
                                        quickRegistrationSaveResponseModel.isDrMobileApi = 1
                                        quickRegistrationSaveResponseModel.country_uuid =
                                            selectNationalityUuid
                                        quickRegistrationSaveResponseModel.state_uuid =
                                            selectStateUuid
                                        quickRegistrationSaveResponseModel.pincode =
                                            binding!!.quickPincode.text.toString()
                                        quickRegistrationSaveResponseModel.address_line2 =
                                            binding!!.quickAddress.text.toString()

                                        quickRegistrationSaveResponseModel.address_line1 =
                                            binding!!.doorNo.text.toString()
                                        quickRegistrationSaveResponseModel.district_uuid =
                                            selectDistictUuid
                                        quickRegistrationSaveResponseModel.taluk_uuid =
                                            selectBelongUuid.toString()
                                        quickRegistrationSaveResponseModel.saveExists =
                                            alreadyExists

                                        if (is_dob_auto_calculate == 0) {

                                            DOB = utils!!.convertDateFormat(
                                                binding?.dob?.text.toString(),
                                                "dd-MM-yyyy",
                                                "yyyy-MM-dd'T'HH:mm:ss.SSS"
                                            )
                                        } else {


                                        }
                                        quickRegistrationSaveResponseModel.dob =
                                            DOB
                                        quickRegistrationSaveResponseModel.is_adult = isadult

                                        quickRegistrationSaveResponseModel.middle_name =
                                            binding!!.etmiddlename.text.toString()

                                        quickRegistrationSaveResponseModel.last_name =
                                            binding!!.etLastname.text.toString()

                                        quickRegistrationSaveResponseModel.unit_uuid =
                                            selectUnitUuid

                                        quickRegistrationSaveResponseModel.suffix_uuid =
                                            selectSuffixUuid

                                        quickRegistrationSaveResponseModel.professional_title_uuid =
                                            selectProffestionalUuid

                                        quickRegistrationSaveResponseModel.title_uuid =
                                            selectSalutationUuid

                                        quickRegistrationSaveResponseModel.aadhaar_number =
                                            binding?.quickAather?.text.toString()

                                        quickRegistrationSaveResponseModel.is_maternity = Matanity

                                        quickRegistrationSaveResponseModel.remarks =
                                            binding?.etRemarkname?.text?.trim().toString()

                                        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")

                                        val dateInString = sdf.format(Date())
                                        quickRegistrationSaveResponseModel.tat_start_time =
                                            tat_start_time

                                        quickRegistrationSaveResponseModel.tat_end_time =
                                            dateInString
/*

                                        if(created_date=="") {
                                            quickRegistrationSaveResponseModel.created_date =
                                                created_date
                                        }
                                        quickRegistrationSaveResponseModel.old_pin=oldPin
*/


                                        var req =
                                            Gson().toJson(quickRegistrationSaveResponseModel)
                                        Log.e("Data", req.toString())
                                        viewModel?.quickRegistrationSaveList(
                                            quickRegistrationSaveResponseModel,
                                            saveQuickRegistrationRetrofitCallback
                                        )
                                    } else {

                                        var quickregUseoldpin = QuickRegWItholdpinSaveReq()
                                        quickregUseoldpin.first_name =
                                            binding!!.quickName.text.toString()
                                        quickregUseoldpin.mobile =
                                            binding!!.quickMobile.text.toString()
                                        quickregUseoldpin.age =
                                            binding!!.quickAge.text.toString().toInt()
                                        quickregUseoldpin.gender_uuid =
                                            selectGenderUuid
                                        quickregUseoldpin.session_uuid =
                                            session_uuid
                                        quickregUseoldpin.department_uuid =
                                            selectdepartmentUUId.toString()
                                        quickregUseoldpin.period_uuid =
                                            selectPeriodUuid
                                        quickregUseoldpin.registred_facility_uuid =
                                            facility_id.toString()
                                        quickregUseoldpin.is_dob_auto_calculate =
                                            1
                                        quickregUseoldpin.isDrMobileApi = 1
                                        quickregUseoldpin.country_uuid =
                                            selectNationalityUuid
                                        quickregUseoldpin.state_uuid =
                                            selectStateUuid
                                        quickregUseoldpin.pincode =
                                            binding!!.quickPincode.text.toString()
                                        quickregUseoldpin.address_line2 =
                                            binding!!.quickAddress.text.toString()

                                        quickregUseoldpin.address_line1 =
                                            binding!!.doorNo.text.toString()
                                        quickregUseoldpin.district_uuid =
                                            selectDistictUuid
                                        quickregUseoldpin.taluk_uuid =
                                            selectBelongUuid.toString()
                                        quickregUseoldpin.saveExists =
                                            alreadyExists

                                        if (is_dob_auto_calculate == 0) {

                                            DOB = utils!!.convertDateFormat(
                                                binding?.dob?.text.toString(),
                                                "dd-MM-yyyy",
                                                "yyyy-MM-dd'T'HH:mm:ss.SSS"
                                            )

                                        } else {


                                        }
                                        quickregUseoldpin.dob =
                                            DOB
                                        quickregUseoldpin.is_adult = isadult

                                        quickregUseoldpin.middle_name =
                                            binding!!.etmiddlename.text.toString()

                                        quickregUseoldpin.last_name =
                                            binding!!.etLastname.text.toString()

                                        quickregUseoldpin.unit_uuid =
                                            selectUnitUuid

                                        quickregUseoldpin.suffix_uuid =
                                            selectSuffixUuid

                                        quickregUseoldpin.professional_title_uuid =
                                            selectProffestionalUuid

                                        quickregUseoldpin.title_uuid =
                                            selectSalutationUuid

                                        quickregUseoldpin.aadhaar_number =
                                            binding!!.quickAather.text.toString()

                                        if (created_date != "") {
                                            quickregUseoldpin.created_date =
                                                created_date
                                        }
                                        quickregUseoldpin.old_pin = oldPin

                                        quickregUseoldpin.is_maternity = Matanity

                                        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")

                                        val dateInString = sdf.format(Date())

                                        quickregUseoldpin.tat_start_time = tat_start_time

                                        quickregUseoldpin.tat_end_time = dateInString

                                        quickregUseoldpin.remarks =
                                            binding?.etRemarkname?.text?.trim().toString()


                                        viewModel?.quickRegistrationUsingOldpinSaveList(
                                            quickregUseoldpin,
                                            saveQuickRegistrationRetrofitCallback
                                        )

                                    }


                                } else {

                                    if (oldPin == "") {


                                        val quickUpdateRequestModel =
                                            QuickRegistrationUpdateRequest()

                                        quickUpdateRequestModel.session_uuid = session_uuid!!

                                        quickUpdateRequestModel.is_dob_auto_calculate = 1
                                        quickUpdateRequestModel.country_uuid = selectNationalityUuid
                                        quickUpdateRequestModel.state_uuid = selectStateUuid
                                        quickUpdateRequestModel.pincode =
                                            binding!!.quickPincode.text.toString()
                                        quickUpdateRequestModel.address_line2 =
                                            binding!!.quickAddress.text.toString()
                                        quickUpdateRequestModel.district_uuid = selectDistictUuid
                                        quickUpdateRequestModel.taluk_uuid = selectBelongUuid
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
                                        quickUpdateRequestModel.session_uuid = session_uuid!!
                                        quickUpdateRequestModel.department_uuid =
                                            selectdepartmentUUId!!
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
                                            selectBelongUuid
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

                                        quickUpdateRequestModel.middle_name =
                                            binding!!.etmiddlename.text.toString()

                                        quickUpdateRequestModel.last_name =
                                            binding!!.etLastname.text.toString()

                                        quickUpdateRequestModel.unit_uuid =
                                            selectUnitUuid

                                        quickUpdateRequestModel.suffix_uuid =
                                            selectSuffixUuid

                                        quickUpdateRequestModel.professional_title_uuid =
                                            selectProffestionalUuid

                                        quickUpdateRequestModel.title_uuid =
                                            selectSalutationUuid

                                        quickUpdateRequestModel.aadhaar_number =
                                            binding!!.quickAather.text.toString()

                                        quickUpdateRequestModel.isDrMobileApi = 1

                                        quickUpdateRequestModel.is_maternity = Matanity


                                        viewModel?.quickRegistrationUpdateformQuick(
                                            quickUpdateRequestModel,
                                            updateQuickRegistrationRetrofitCallback
                                        )


                                    } else {

                                        val quickUpdateRequestModel =
                                            QuickRegistrationUpdateRequestWitholdPin()

                                        quickUpdateRequestModel.session_uuid = session_uuid!!

                                        quickUpdateRequestModel.is_dob_auto_calculate = 1
                                        quickUpdateRequestModel.country_uuid = selectNationalityUuid
                                        quickUpdateRequestModel.state_uuid = selectStateUuid
                                        quickUpdateRequestModel.pincode =
                                            binding!!.quickPincode.text.toString()
                                        quickUpdateRequestModel.address_line2 =
                                            binding!!.quickAddress.text.toString()
                                        quickUpdateRequestModel.district_uuid = selectDistictUuid
                                        quickUpdateRequestModel.taluk_uuid = selectBelongUuid
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
                                        quickUpdateRequestModel.session_uuid = session_uuid!!
                                        quickUpdateRequestModel.department_uuid =
                                            selectdepartmentUUId!!
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
                                            selectBelongUuid
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

                                        quickUpdateRequestModel.middle_name =
                                            binding!!.etmiddlename.text.toString()

                                        quickUpdateRequestModel.last_name =
                                            binding!!.etLastname.text.toString()

                                        quickUpdateRequestModel.unit_uuid =
                                            selectUnitUuid

                                        quickUpdateRequestModel.suffix_uuid =
                                            selectSuffixUuid

                                        quickUpdateRequestModel.professional_title_uuid =
                                            selectProffestionalUuid

                                        quickUpdateRequestModel.title_uuid =
                                            selectSalutationUuid

                                        quickUpdateRequestModel.is_maternity = this.Matanity!!

                                        quickUpdateRequestModel.isDrMobileApi = 1
                                        if (created_date != "") {

                                            quickUpdateRequestModel.created_date = created_date
                                            quickUpdateRequestModel.old_pin = oldPin

                                        }


                                        viewModel?.quickRegistrationUpdateformQuick(
                                            quickUpdateRequestModel,
                                            updateQuickRegistrationRetrofitCallback
                                        )


                                    }

                                }

                                /*  } else {
                                      Toast.makeText(
                                          this.context,
                                          "Please Select Salutaion",
                                          Toast.LENGTH_SHORT
                                      ).show()

                                  }*/


                            } else {

                                Toast.makeText(
                                    this.context,
                                    "Please Department",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }


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


                    selectedState = stateListfilteritem.get(position).name
                    if (selectedState.equals("TAMIL NADU", true)) {
                        binding?.disticttext?.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.ic_asterisk,
                            0
                        )
                    } else {
                        binding?.disticttext?.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,
                            0,
                            0,
                            0
                        )
                    }

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

                StopTimer()


                if (casualtyBased != (uuid.code == "3")) {


                    binding!!.department.setText("")

                    selectdepartmentUUId = 0

                }

                casualtyBased = uuid.code == "3"

                session_uuid = uuid.uuid

                session_name = uuid.name
            }
        })

        /*  binding?.listscroll?.getViewTreeObserver()?.addOnScrollChangedListener(object : ViewTreeObserver.OnScrollChangedListener {
              override fun onScrollChanged() {

                  binding?.quickName?.error=null

              }
          })
  */

        binding?.etRemarkname?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length in 1 until 3) {
                    binding?.etRemarkname?.error = "Please enter atleast 3 chracters"
                } else {
                    binding?.etRemarkname?.error = null
                }
            }

        })
    }

    val favLabSearchentCallBack = object : RetrofitCallback<FavAddAllDepatResponseModel> {
        @SuppressLint("NewApi")
        override fun onSuccessfulResponse(responseBody: Response<FavAddAllDepatResponseModel>?) {
            Log.i("", "" + responseBody?.body()?.responseContents)

            setAdapter(responseBody?.body()?.responseContents)


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

    private fun setAdapter(responseContents: List<FavAddAllDepatResponseContent>?) {

        val responseContentAdapter = DepartmentSearchAdapter(
            this.requireContext(),
            R.layout.row_chief_complaint_search_result,
            responseContents as ArrayList<FavAddAllDepatResponseContent>
        )
        binding!!.department.threshold = 1
        binding!!.department.setAdapter(responseContentAdapter)
        binding!!.department.showDropDown()

        binding!!.department.setOnItemClickListener { parent, _, pos, id ->
            val selectedPoi = parent.adapter.getItem(pos) as FavAddAllDepatResponseContent?

            binding!!.department.setText(selectedPoi!!.name)

            selectdepartmentUUId = selectedPoi.uuid


            if (selectedPoi.is_speciality == 1 || selectedPoi.is_speciality == true) {

                binding?.unittext?.visibility = View.VISIBLE

                binding?.unit?.visibility = View.VISIBLE

            } else {


                binding?.unittext?.visibility = View.GONE

                binding?.unit?.visibility = View.GONE


            }


        }

    }

    val schemaNameCallBack = object : RetrofitCallback<SchemaNameResponce> {
        @SuppressLint("NewApi")
        override fun onSuccessfulResponse(responseBody: Response<SchemaNameResponce>?) {
            Log.i("", "" + responseBody?.body()?.responseContents)

            if (responseBody?.body()?.responseContents != null) {

                binding?.schema?.setText(responseBody.body()?.responseContents!!.name)

            }
        }

        override fun onBadRequest(errorBody: Response<SchemaNameResponce>?) {
            val gson = GsonBuilder().create()
            val responseModel: SchemaNameResponce
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    SchemaNameResponce::class.java
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

    val schemaSearchentCallBack = object : RetrofitCallback<SchemaResponseModel> {
        @SuppressLint("NewApi")
        override fun onSuccessfulResponse(responseBody: Response<SchemaResponseModel>?) {
            Log.i("", "" + responseBody?.body()?.responseContents)

            setSchemaAdapter(responseBody?.body()?.responseContents)


        }

        override fun onBadRequest(errorBody: Response<SchemaResponseModel>?) {
            val gson = GsonBuilder().create()
            val responseModel: SchemaResponseModel
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    SchemaResponseModel::class.java
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

    private fun setSchemaAdapter(responseContents: List<SchemaResponse>?) {

        schemaData = responseContents as ArrayList<SchemaResponse>

        val responseContentAdapter = SchemaSearchAdapter(
            this.requireContext(),
            R.layout.row_chief_complaint_search_result,
            responseContents
        )
        binding!!.schema.threshold = 1
        binding!!.schema.setAdapter(responseContentAdapter)
        binding!!.schema.showDropDown()

        binding!!.schema.setOnItemClickListener { parent, _, pos, id ->

            val selectedPoi = parent.adapter.getItem(pos) as SchemaResponse?

            binding!!.schema.setText(selectedPoi!!.name)

            selectschema = selectedPoi.uuid!!


        }

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

    private fun TimerStart() {

        timer = Timer()
        timerTask = MyTimerTask()
        timer?.schedule(timerTask, 1000, 1000)

    }

    private fun StopTimer() {

        timer?.cancel()
    }

    inner class MyTimerTask : TimerTask() {
        @ExperimentalTime
        override fun run() {
            val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

            val min = SimpleDateFormat("HH:mm", Locale.getDefault())
            val time = Date().time
            val date = Date(time - time % (24 * 60 * 60 * 1000))
            val currentDateMidnightTime = date.time


            var opEveEndTime: Long? = null


            try {


            } catch (e: ParseException) {

                e.printStackTrace()
            }
            val a = Date().time

            var times = Date()

            val optimechange = sdf.format(Date())

            val optime = sdf.parse(optimechange)

            val timestr = sdf.format(Date()).toString()



            if (optime == opEveEndTimeMin) {


                Handler(Looper.getMainLooper()).post {

                    customdialog = Dialog(requireContext())
                    customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    customdialog!!.setCancelable(false)
                    customdialog!!.setContentView(R.layout.revert_dialog)
                    val closeImageView =
                        customdialog!!.findViewById(R.id.closeImageView) as ImageView

                    closeImageView.setOnClickListener {
                        customdialog?.dismiss()
                    }

                    val drugNmae = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                    drugNmae.text =
                        "Current session has been expired, Do you want to continue for another $opExTime Min?"

                    val heading = customdialog!!.findViewById(R.id.heading) as TextView
                    heading.text = "Session Expiry Alert"

                    val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                    val noBtn = customdialog!!.findViewById(R.id.no) as CardView

                    yesBtn.setOnClickListener {


                        Log.i("timmer", "Before " + opEveEndTimeMin)

                        val cal = Calendar.getInstance()

                        cal.time = opEveEndTimeMin

                        cal.add(Calendar.MINUTE, opExTime!!)

                        opEveEndTimeMin = cal.time

                        TimesetStatus = false

                        Log.i("timmer", "After " + opEveEndTimeMin)

                        customdialog!!.dismiss()


                    }
                    noBtn.setOnClickListener {
                        customdialog!!.dismiss()

                        TimesetStatus = true
                    }




                    customdialog!!.show()

                }

            } else if (optime == opEveStartTime) {


                Handler(Looper.getMainLooper()).post {

                    customdialog = Dialog(requireContext())
                    customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    customdialog!!.setCancelable(false)
                    customdialog!!.setContentView(R.layout.revert_dialog)
                    val closeImageView =
                        customdialog!!.findViewById(R.id.closeImageView) as ImageView

                    closeImageView.setOnClickListener {
                        customdialog?.dismiss()
                    }

                    val drugNmae = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                    drugNmae.text =
                        "Current session has been expired, Do you want to continue for another $opExTime Min?"

                    val heading = customdialog!!.findViewById(R.id.heading) as TextView
                    heading.text = "Session Expiry Alert"

                    val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                    val noBtn = customdialog!!.findViewById(R.id.no) as CardView

                    yesBtn.setOnClickListener {


                        Log.i("timmer", "Before " + opEveStartTime)

                        val cal = Calendar.getInstance()

                        cal.time = opEveStartTime

                        cal.add(Calendar.MINUTE, opExTime!!)

                        opEveStartTime = cal.time

                        TimesetStatus = false

                        Log.i("timmer", "After " + opEveStartTime)

                        customdialog!!.dismiss()


                    }
                    noBtn.setOnClickListener {
                        customdialog!!.dismiss()

                        TimesetStatus = true
                    }




                    customdialog!!.show()

                }

            } else if (optime == opMorStartTime) {


                Handler(Looper.getMainLooper()).post {

                    customdialog = Dialog(requireContext())
                    customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    customdialog!!.setCancelable(false)
                    customdialog!!.setContentView(R.layout.revert_dialog)
                    val closeImageView =
                        customdialog!!.findViewById(R.id.closeImageView) as ImageView

                    closeImageView.setOnClickListener {
                        customdialog?.dismiss()
                    }

                    val drugNmae = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                    drugNmae.text =
                        "Current session has been expired, Do you want to continue for another $opExTime Min?"

                    val heading = customdialog!!.findViewById(R.id.heading) as TextView
                    heading.text = "Session Expiry Alert"

                    val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                    val noBtn = customdialog!!.findViewById(R.id.no) as CardView

                    yesBtn.setOnClickListener {


                        Log.i("timmer", "Before " + opMorStartTime)

                        val cal = Calendar.getInstance()

                        cal.time = opMorStartTime

                        cal.add(Calendar.MINUTE, opExTime!!)

                        opMorStartTime = cal.time

                        TimesetStatus = false

                        Log.i("timmer", "After " + opMorStartTime)

                        customdialog!!.dismiss()


                    }
                    noBtn.setOnClickListener {
                        customdialog!!.dismiss()

                        TimesetStatus = true
                    }




                    customdialog!!.show()

                }

            } else if (optime == opMorEndTime) {


                Handler(Looper.getMainLooper()).post {

                    customdialog = Dialog(requireContext())
                    customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    customdialog!!.setCancelable(false)
                    customdialog!!.setContentView(R.layout.revert_dialog)
                    val closeImageView =
                        customdialog!!.findViewById(R.id.closeImageView) as ImageView

                    closeImageView.setOnClickListener {
                        customdialog?.dismiss()
                    }

                    val drugNmae = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                    drugNmae.text =
                        "Current session has been expired, Do you want to continue for another $opExTime Min?"

                    val heading = customdialog!!.findViewById(R.id.heading) as TextView
                    heading.text = "Session Expiry Alert"

                    val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                    val noBtn = customdialog!!.findViewById(R.id.no) as CardView

                    yesBtn.setOnClickListener {


                        Log.i("timmer", "Before " + opMorEndTime)

                        val cal = Calendar.getInstance()

                        cal.time = opMorEndTime

                        cal.add(Calendar.MINUTE, opExTime!!)

                        opMorEndTime = cal.time

                        TimesetStatus = false

                        Log.i("timmer", "After " + opMorEndTime)

                        customdialog!!.dismiss()


                    }
                    noBtn.setOnClickListener {
                        customdialog!!.dismiss()

                        TimesetStatus = true
                    }




                    customdialog!!.show()

                }

            } else if (optime > opMorStartTime!! && optime < opMorEndTime!!) {

                Handler(Looper.getMainLooper()).post {


                    mAdapter?.setActiveSeation("1")

                    session_uuid = morning_session_uuid
                    binding?.buttonstatus?.text = morning_session_name
                    session_name = morning_session_name



                    if (casualtyBased) {

                        binding!!.department.setText("")

                        selectdepartmentUUId = 0

                    }

                    casualtyBased = false

                }

            } else if (optime > opEveStartTime!! && optime < opEveEndTimeMin!!) {
                Handler(Looper.getMainLooper()).post {

                    mAdapter?.setActiveSeation("2")
                    session_uuid = evening_session_uuid
                    binding?.buttonstatus?.text = evening_session_name
                    session_name = evening_session_name


                    if (casualtyBased) {

                        binding!!.department.setText("")

                        selectdepartmentUUId = 0

                    }

                    casualtyBased = false

                }
            } else {
                Handler(Looper.getMainLooper()).post {

                    mAdapter?.setActiveSeation("3")
                    session_uuid = case_session_uuid
                    binding?.buttonstatus?.text = case_session_name
                    session_name = case_session_name


                    if (!casualtyBased) {

                        binding!!.department.setText("")

                        selectdepartmentUUId = 0

                    }
                    casualtyBased = true

                }
            }

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

    val getCommunityListRetrofitCallBack = object : RetrofitCallback<ResponseSpicemanType> {
        override fun onSuccessfulResponse(response: Response<ResponseSpicemanType>) {

            val responseContents = Gson().toJson(response.body()?.responseContents)

            var responseData: ArrayList<ResponseSpicemanTypeContent> = ArrayList()

            var dummy: ResponseSpicemanTypeContent = ResponseSpicemanTypeContent()

            dummy.uuid = 0

            dummy.name = "Community"

            responseData.add(dummy)

            var responseDatas =
                response.body()!!.responseContents as List<ResponseSpicemanTypeContent>

            responseData.addAll(responseDatas)

            CommunityList = responseData.map { it.uuid!! to it.name!! }.toMap().toMutableMap()

            hashCommunitypinnerList.clear()

            for (i in responseData.indices) {

                hashCommunitypinnerList[responseData[i].uuid!!] = i
            }


            val adapter = ArrayAdapter<String>(
                context!!,
                R.layout.spinner_item,
                CommunityList.values.toMutableList()
            )

            adapter.setDropDownViewResource(R.layout.spinner_item)

            binding?.community!!.adapter = adapter


        }

        override fun onBadRequest(response: Response<ResponseSpicemanType>) {
            val gson = GsonBuilder().create()
            val responseModel: ResponseSpicemanType
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
//                viewModel!!.progress!!.value = 8
        }


    }

    val unitResponseCallback = object : RetrofitCallback<GetAllResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<GetAllResponseModel>?) {

            var responseData: ArrayList<GetAllResponse> = ArrayList()

            var dummy: GetAllResponse = GetAllResponse()

            dummy.uuid = 0

            dummy.name = "Unit"

            responseData.add(dummy)

            var responseDatas = responseBody!!.body()!!.responseContents

            responseData.addAll(responseDatas)

            UnitList = responseData.map { it.uuid to it.name }.toMap().toMutableMap()

            hashUnitSpinnerList.clear()

            for (i in responseData.indices) {

                hashUnitSpinnerList[responseData[i].uuid] = i
            }


            val adapter = ArrayAdapter<String>(
                context!!,
                R.layout.spinner_item,
                UnitList.values.toMutableList()
            )

            adapter.setDropDownViewResource(R.layout.spinner_item)

            binding?.unit!!.adapter = adapter


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

    var saveQuickRegistrationRetrofitCallback = object :
        RetrofitCallback<QuickRegistrationSaveResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<QuickRegistrationSaveResponseModel>?) {

            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                "Register Success"
            )

            Log.i("", "" + responseBody?.body()?.responseContent)

            appPreferences?.saveString(
                AppConstants.LASTPIN,
                responseBody?.body()?.responseContent?.uhid
            )

            val pdfRequestModel = PrintQuickRequest()
            pdfRequestModel.componentName = "basic"
            pdfRequestModel.uuid = responseBody?.body()?.responseContent?.uuid!!
            pdfRequestModel.uhid = responseBody.body()?.responseContent?.uhid!!
            pdfRequestModel.facilityName = facility_Name
            pdfRequestModel.firstName = responseBody.body()?.responseContent?.first_name!!
            pdfRequestModel.period = CovidPeriodList[selectPeriodUuid]!!
            pdfRequestModel.age = responseBody.body()?.responseContent?.age!!
            pdfRequestModel.registered_date =
                responseBody.body()?.responseContent?.registered_date!!
            pdfRequestModel.sari = sariStatus
            pdfRequestModel.ili = iliStatus
            pdfRequestModel.noSymptom = nosymptomsStatus
            pdfRequestModel.is_dob_auto_calculate = is_dob_auto_calculate
            pdfRequestModel.session = session_name!!
            pdfRequestModel.gender = CovidGenderList[selectGenderUuid]!!
            pdfRequestModel.mobile = binding!!.quickMobile.text.toString()

            if (!binding?.etFathername?.text?.trim().isNullOrEmpty()) {
                pdfRequestModel.fatherName =
                    binding?.etFathername?.text?.trim().toString()

            }
            if (!binding?.quickAather?.text.toString().isNullOrEmpty()) {

                pdfRequestModel.aadhaarNumber = binding?.quickAather?.text.toString()
            }

            if (addressenable!!) {

                pdfRequestModel.addressDetails.doorNum = binding!!.doorNo.text.toString()
                pdfRequestModel.addressDetails.streetName = binding!!.quickAddress.text.toString()

                if (selectNationalityUuid != 0) {
                    pdfRequestModel.addressDetails.country =
                        CovidNationalityList[selectNationalityUuid]!!
                }
                if (selectDistictUuid != 0) {


                    pdfRequestModel.addressDetails.district = CovidDistictList[selectDistictUuid]!!


                }

                if (selectStateUuid != 0) {


                    try {
                        pdfRequestModel.addressDetails.state = CovidStateList[selectStateUuid]!!
                    } catch (e: Exception) {
                    }


                }

                if (selectVillageUuid != 0) {


                    pdfRequestModel.addressDetails.village = CovidVillageList[selectVillageUuid]!!


                }


                if (selectBelongUuid != 0) {

                    pdfRequestModel.addressDetails.taluk = CovidBlockList[selectBelongUuid]!!


                }

                if (!binding?.quickPincode?.text.toString().isNullOrEmpty()) {


                    pdfRequestModel.addressDetails.pincode = binding?.quickPincode?.text.toString()


                }


            }
            pdfRequestModel.visitNum =
                responseBody.body()?.responseContent?.patient_visits!!.seqNum!!

            pdfRequestModel.dob = responseBody.body()?.responseContent?.dob!!

            if (selectSalutationUuid != 0) {

                pdfRequestModel.salutation = SalutaionList[selectSalutationUuid]!!

            }
            if (selectProffestionalUuid != 0) {

                pdfRequestModel.professional = ProfestioanlList[selectProffestionalUuid]!!
            }
            if (selectCoummityUuid != 0) {

                pdfRequestModel.community = CommunityList[selectCoummityUuid]!!
            }

            pdfRequestModel.middleName = binding!!.etmiddlename.text.toString()
            pdfRequestModel.lastName = binding!!.etLastname.text.toString()

            if (selectSuffixUuid != 0) {

                pdfRequestModel.suffixCode = SuffixList[selectSuffixUuid]!!
            }

            if (selectSuffixUuid != 0) {

                pdfRequestModel.suffixCode = SuffixList[selectSuffixUuid]!!
            }
            pdfRequestModel.department = binding!!.department.text.toString()

            val bundle = Bundle()
            bundle.putParcelable(AppConstants.RESPONSECONTENT, pdfRequestModel)
            bundle.putInt(AppConstants.RESPONSENEXT, 190)
            bundle.putString("From", "Quick")
            bundle.putInt("next", onNext!!)

            val labtemplatedialog = QuickDialogPDFViewerActivity()
            labtemplatedialog.arguments = bundle

            if (onNext == 0) {
                (activity as HomeActivity).replaceFragment(labtemplatedialog)
            } else if (onNext == 1) {
                (activity as HomeActivity).replaceFragmentNoBack(labtemplatedialog)
            }
        }

        override fun onBadRequest(response: Response<QuickRegistrationSaveResponseModel>) {
            Log.e("badreq", response.toString())
            val gson = GsonBuilder().create()
            val responseModel: QuickRegistrationSaveResponseModel
            var mError = ErrorAPIClass()
            try {
                mError = gson.fromJson(response.errorBody()!!.string(), ErrorAPIClass::class.java)

                Toast.makeText(context!!, mError.message, Toast.LENGTH_LONG).show()

                if (mError.message == "Patient already exists") {

                    saveAgain()
                }

            } catch (e: IOException) { // handle failure to read error
            }
        }

        override fun onServerError(response: Response<*>) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                "serverError"
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
                "Forbidden"
            )

        }

        override fun onFailure(failure: String) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                failure
            )
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    var saveQuickOrderRegistrationRetrofitCallback = object :
        RetrofitCallback<QuickRegistrationSaveResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<QuickRegistrationSaveResponseModel>?) {

            patientUUId = responseBody!!.body()!!.responseContent!!.uuid

            uhid = responseBody.body()!!.responseContent!!.uhid!!.toString()
            appPreferences?.saveString(AppConstants.LASTPIN, uhid)

            registerDate = responseBody.body()!!.responseContent!!.registered_date!!.toString()

            viewModel!!.createEncounter(
                createEncounterRetrofitCallback,
                responseBody.body()!!.responseContent!!.uuid!!
            )

        }

        override fun onBadRequest(response: Response<QuickRegistrationSaveResponseModel>) {
            Log.e("badreq", response.toString())
            val gson = GsonBuilder().create()
            val responseModel: QuickRegistrationSaveResponseModel
            var mError = ErrorAPIClass()
            try {
                mError = gson.fromJson(response.errorBody()!!.string(), ErrorAPIClass::class.java)

                Toast.makeText(context!!, mError.message, Toast.LENGTH_LONG).show()

                if (mError.message == "Patient already exists") {

                    saveOrderAgain()
                }

            } catch (e: IOException) { // handle failure to read error
            }
        }

        override fun onServerError(response: Response<*>) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                "serverError"
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
                "Forbidden"
            )

        }

        override fun onFailure(failure: String) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                failure
            )
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }

    }

    private fun saveOrderAgain() {

        alreadyExists = true

        customdialog = Dialog(requireContext())
        customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customdialog!!.setCancelable(false)
        customdialog!!.setContentView(R.layout.duplicate_add_dialog)
        val closeImageView = customdialog!!.findViewById(R.id.closeImageView) as ImageView
        closeImageView.setOnClickListener {

            customdialog?.dismiss()
        }

        val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
        val noBtn = customdialog!!.findViewById(R.id.no) as CardView
        yesBtn.setOnClickListener {

            quickRegistrationSaveResponseModel.saveExists = alreadyExists

            viewModel?.quickRegistrationSaveList(
                quickRegistrationSaveResponseModel,
                saveQuickOrderRegistrationRetrofitCallback
            )

            customdialog!!.dismiss()


        }
        noBtn.setOnClickListener {
            customdialog!!.dismiss()

            alreadyExists = false
        }
        customdialog!!.show()

    }

    val createEncounterRetrofitCallback = object : RetrofitCallback<CreateEncounterResponseModel> {
        override fun onSuccessfulResponse(response: Response<CreateEncounterResponseModel>) {

            encounter_doctor_id =
                response.body()!!.responseContents!!.encounterDoctor!!.uuid!!.toInt()

            encounter_id = response.body()!!.responseContents!!.encounter!!.uuid

            saveOrderAPICall()

        }

        override fun onBadRequest(response: Response<CreateEncounterResponseModel>) {
            Log.e("badreq", response.toString())
            val gson = GsonBuilder().create()
            val responseModel: QuickRegistrationSaveResponseModel
            var mError = EncounterErrorAPIClass()
            try {
                mError = gson.fromJson(
                    response.errorBody()!!.string(),
                    EncounterErrorAPIClass::class.java
                )


                if (mError.code == 400) {

                    encounter_doctor_id = mError.existingDetails.encounter_doctor_id

                    encounter_id = mError.existingDetails.encounter_id

                    saveOrderAPICall()

                }

            } catch (e: IOException) { // handle failure to read error
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

    @SuppressLint("SimpleDateFormat")
    private fun saveOrderAPICall() {

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")

        val dateInString = sdf.format(Date())

        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        val saveOrderRequestModel: SaveOrderRequestModel = SaveOrderRequestModel()

        val header: Header = Header()
        header.is_auto_accept = 1
        header.patient_uuid = patientUUId!!
        header.encounter_uuid = encounter_id!!
        header.encounter_type_uuid = 1
        header.lab_master_type_uuid = 1
        header.encounter_doctor_uuid = encounter_doctor_id!!
        header.doctor_uuid = userDataStoreBean?.uuid!!.toString()
        header.facility_uuid = facility_id.toString()
        header.department_uuid = departmentUUId.toString()
        header.sub_department_uuid = 0
        header.order_to_location_uuid = 1
        header.consultation_uuid = 0
        header.patient_treatment_uuid = 0
        header.order_status_uuid = 0
        header.treatment_plan_uuid = 0
        header.assign_to_location_uuid = locationId!!
        if (selectLabNameID != null) {
            header.to_facility_uuid = selectLabNameID!!
        }
        header.tat_session_start = dateInString
        header.tat_session_end = dateInString


        val detailsList: ArrayList<Detail> = ArrayList()

        val details: Detail = Detail()

        details.profile_uuid = ""
        details.test_master_uuid = gettest.uuid
        details.is_profile = false
        details.lab_master_type_uuid = 1
        details.to_department_uuid = gettest.department_uuid
        details.order_priority_uuid = getReference.uuid
        details.to_location_uuid = locationMasterX.uuid
        details.sample_type_uuid = sampleid!!
        details.type_of_method_uuid = radioid!!
        details.group_uuid = 0
        details.to_sub_department_uuid = 0
        details.tat_session_start = dateInString
        details.tat_session_end = dateInString
        details.is_active = true
        details.test_diseases_uuid = gettest.test_diseases_uuid
        details.is_approval_requried = true
        details.confidential_uuid = gettest.confidential_uuid

        details.is_confidential = true

        detailsList.add(details)

        saveOrderRequestModel.header = header

        saveOrderRequestModel.details = detailsList

        viewModel!!.getSaveOrder(saveOrderRequestModel, saveOrderRegistrationRetrofitCallback)


    }

    var saveOrderRegistrationRetrofitCallback = object :
        RetrofitCallback<SaveOrderResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<SaveOrderResponseModel>?) {

            utils?.showToast(
                R.color.positiveToast,
                binding?.mainLayout!!,
                "Register Success"

            )
            toLabActivity()

        }

        override fun onBadRequest(response: Response<SaveOrderResponseModel>) {
            Log.e("badreq", response.toString())
            val gson = GsonBuilder().create()
            val responseModel: SaveOrderResponseModel
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
                binding?.mainLayout!!,
                "serverError"
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
                "Forbidden"
            )

        }

        override fun onFailure(failure: String) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                failure
            )
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }

    }

    private fun saveAgain() {

        alreadyExists = true

        customdialog = Dialog(requireContext())
        customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customdialog!!.setCancelable(false)
        customdialog!!.setContentView(R.layout.duplicate_add_dialog)
        val closeImageView = customdialog!!.findViewById(R.id.closeImageView) as ImageView
        closeImageView.setOnClickListener {

            customdialog?.dismiss()
        }

        val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
        val noBtn = customdialog!!.findViewById(R.id.no) as CardView
        yesBtn.setOnClickListener {


            if (BuildConfig.FLAVOR == "puneuat" || BuildConfig.FLAVOR == "puneprod") {


                quickRegUatSaveRequest.saveExists = alreadyExists

                viewModel?.quickRegistrationSaveList(
                    quickRegUatSaveRequest,
                    saveQuickRegistrationRetrofitCallback
                )

            } else {


                quickRegistrationSaveResponseModel.saveExists = alreadyExists

                viewModel?.quickRegistrationSaveList(
                    quickRegistrationSaveResponseModel,
                    saveQuickRegistrationRetrofitCallback
                )


            }



            customdialog!!.dismiss()


        }
        noBtn.setOnClickListener {
            customdialog!!.dismiss()

            alreadyExists = false
        }
        customdialog!!.show()
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



        stateListfilteritem = responseContents

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

                val checkseach =
                    response.body()?.responseContents!!.any { it!!.code == quickPrevilege.search }

                if (checkseach) {
                    binding?.searchButton?.isClickable = true
                } else {
                    binding?.searchButton?.alpha = .5f
                    binding?.searchButton?.isClickable = false

                }

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

    val getActivityPrivilageRetrofitCallback =
        object : RetrofitCallback<QuickelementRoleResponseModel> {
            override fun onSuccessfulResponse(response: Response<QuickelementRoleResponseModel>) {

                val checksal =
                    response.body()?.responseContents!!.any {
                        it.facility_registration_config.code == AppConstants.SALUTAION
                    }

                if (checksal) {

                    binding?.salutaionText?.visibility = View.VISIBLE

                    binding?.salutaionList?.visibility = View.VISIBLE


                } else {

                    binding?.salutaionText?.visibility = View.GONE

                    binding?.salutaionList?.visibility = View.GONE

                }


                val checkprof =
                    response.body()?.responseContents!!.any {
                        it.facility_registration_config.code == AppConstants.PROFESTIONALSULUTAION
                    }


                if (checkprof || checksal) {


                    val tabletSize = resources.getBoolean(R.bool.isTablet)

                    if (tabletSize) {

                        binding?.SPList?.visibility = View.VISIBLE

                    }

                } else {


                    val tabletSize = resources.getBoolean(R.bool.isTablet)

                    if (tabletSize) {

                        binding?.SPList?.visibility = View.GONE

                    }


                }

                if (checkprof) {


                    val tabletSize = resources.getBoolean(R.bool.isTablet)

                    if (tabletSize) {

                        binding?.profestionallayout?.visibility = View.VISIBLE

                    } else {

                        binding?.proftext?.visibility = View.VISIBLE

                        binding?.proflist?.visibility = View.VISIBLE
                    }


                } else {

                    val tabletSize = resources.getBoolean(R.bool.isTablet)

                    if (tabletSize) {

                        binding?.profestionallayout?.visibility = View.GONE

                    } else {

                        binding?.proftext?.visibility = View.GONE

                        binding?.proflist?.visibility = View.GONE
                    }


                    /*   binding?.proftext?.visibility = View.GONE

                       binding?.proflist?.visibility = View.GONE*/

                }

                val checkFATHER =
                    response.body()?.responseContents!!.any {
                        it.facility_registration_config.code == AppConstants.FATHERNAME
                    }

                if (checkFATHER) {

                    binding?.fathernametext?.visibility = View.VISIBLE


                    binding?.etFathername?.visibility = View.VISIBLE


                } else {

                    binding?.fathernametext?.visibility = View.GONE

                    binding?.etFathername?.visibility = View.GONE

                }

                val checksuf =
                    response.body()?.responseContents!!.any {
                        it.facility_registration_config.code == AppConstants.SUFFIXCODE
                    }

                if (checksuf) {

                    binding?.suffixtext?.visibility = View.VISIBLE


                    binding?.suffixlist?.visibility = View.VISIBLE


                } else {

                    binding?.suffixtext?.visibility = View.GONE

                    binding?.suffixlist?.visibility = View.GONE

                }

                val checkdob =
                    response.body()?.responseContents!!.any {
                        it.facility_registration_config.code == AppConstants.DATEOFBIRTH
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
                    response.body()?.responseContents!!.any {
                        it.facility_registration_config.code == AppConstants.MIDDLENAME
                    }

                if (checkmiddlename) {

                    binding?.middlenametext?.visibility = View.VISIBLE


                    binding?.etmiddlename?.visibility = View.VISIBLE


                } else {


                    binding?.middlenametext?.visibility = View.GONE


                    binding?.etmiddlename?.visibility = View.GONE

                }


                val checkcom =
                    response.body()?.responseContents!!.any {
                        it.facility_registration_config.code == AppConstants.COMMUNITY
                    }

                if (checkcom) {

                    binding?.comlist?.visibility = View.VISIBLE

                    binding?.comtext?.visibility = View.VISIBLE


                } else {

                    binding?.comlist?.visibility = View.GONE

                    binding?.comtext?.visibility = View.GONE

                }


                val checkunit =
                    response.body()?.responseContents!!.any {
                        it.facility_registration_config.code == AppConstants.UNIT
                    }

                if (checkunit) {

                    binding?.unittext?.visibility = View.VISIBLE

                    binding?.unit?.visibility = View.VISIBLE


                } else {

                    binding?.unittext?.visibility = View.GONE

                    binding?.unit?.visibility = View.GONE

                }


                val checkaather =
                    response.body()?.responseContents!!.any {
                        it.facility_registration_config.code == AppConstants.AADHARNOR
                    }

                if (checkaather) {

                    binding?.aathertext?.visibility = View.VISIBLE

                    binding?.quickAather?.visibility = View.VISIBLE


                } else {


                    binding?.aathertext?.visibility = View.GONE

                    binding?.quickAather?.visibility = View.GONE

                }

                val checklastname =
                    response.body()?.responseContents!!.any {
                        it.facility_registration_config.code == AppConstants.LASTNAME
                    }

                if (checklastname) {

                    binding?.lastnametext?.visibility = View.VISIBLE


                    binding?.etLastname?.visibility = View.VISIBLE


                } else {


                    binding?.lastnametext?.visibility = View.GONE


                    binding?.etLastname?.visibility = View.GONE

                }


                val checkdrno =
                    response.body()?.responseContents!!.any {
                        it.facility_registration_config.code == AppConstants.DRNUMBNER
                    }

                if (checkdrno) {

                    binding?.drno?.visibility = View.VISIBLE


                    binding?.doorNo?.visibility = View.VISIBLE


                } else {


                    binding?.drno?.visibility = View.GONE


                    binding?.doorNo?.visibility = View.GONE

                }

                val checkstreet =
                    response.body()?.responseContents!!.any {
                        it.facility_registration_config.code == AppConstants.STNAMME
                    }

                if (checkstreet) {

                    binding?.streetname?.visibility = View.VISIBLE

                    binding?.quickAddress?.visibility = View.VISIBLE


                } else {


                    binding?.streetname?.visibility = View.GONE

                    binding?.quickAddress?.visibility = View.GONE

                }


                val checkcountry =

                    response.body()?.responseContents!!.any {
                        it.facility_registration_config.code == AppConstants.COUNTRY

                    }

                if (checkcountry) {

                    binding?.countrytext?.visibility = View.VISIBLE

                    binding?.countrylist?.visibility = View.VISIBLE


                } else {

                    binding?.countrytext?.visibility = View.GONE

                    binding?.countrylist?.visibility = View.GONE

                }

                val checkstate =
                    response.body()?.responseContents!!.any {
                        it.facility_registration_config.code == AppConstants.STATE
                    }

                if (checkstate) {

                    binding?.statetext?.visibility = View.VISIBLE

                    binding?.statelist?.visibility = View.VISIBLE


                } else {


                    binding?.statetext?.visibility = View.GONE

                    binding?.statelist?.visibility = View.GONE

                }

                val checkremark =
                    response.body()?.responseContents!!.any {
                        it.facility_registration_config.code == AppConstants.REMARK
                    }

                if (checkremark) {

                    binding?.remarknametext?.visibility = View.VISIBLE

                    binding?.etRemarkname?.visibility = View.VISIBLE


                } else {


                    binding?.remarknametext?.visibility = View.GONE

                    binding?.etRemarkname?.visibility = View.GONE

                }


                val checkdistict =
                    response.body()?.responseContents!!.any {
                        it.facility_registration_config.code == AppConstants.DISTIRICT
                    }

                if (checkdistict) {

                    binding?.distictlist?.visibility = View.VISIBLE

                    binding?.disticttext?.visibility = View.VISIBLE


                } else {


                    binding?.distictlist?.visibility = View.GONE

                    binding?.disticttext?.visibility = View.GONE

                }

                val checkzone =
                    response.body()?.responseContents!!.any {
                        it.facility_registration_config.code == AppConstants.TALUK
                    }

                if (checkzone) {

                    binding?.zonelist?.visibility = View.VISIBLE

                    binding?.zonetext?.visibility = View.VISIBLE


                } else {


                    binding?.zonelist?.visibility = View.GONE

                    binding?.zonetext?.visibility = View.GONE

                }


                val checkvillage =
                    response.body()?.responseContents!!.any {
                        it.facility_registration_config.code == AppConstants.VILLAGE
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

    val patientSearchRetrofitCallBack = object : RetrofitCallback<QuickSearchResponseModel> {
        override fun onSuccessfulResponse(response: Response<QuickSearchResponseModel>) {

            if (response.body()?.responseContents?.isNotEmpty()!!) {
//                viewModel?.errorTextVisibility?.value = 8
                updateId = 1

                if (response.body()?.responseContents?.size == 1) {
                    binding?.lastpin?.visibility = View.VISIBLE
                    binding?.pinLayout?.visibility = View.VISIBLE
                    binding?.lastpinnumber?.text = response.body()!!.responseContents!![0]!!.uhid
                    binding?.quickName!!.setText(response.body()!!.responseContents!![0]!!.first_name)
                    binding?.etmiddlename!!.setText(response.body()!!.responseContents!![0]!!.middle_name)
                    binding?.etLastname!!.setText(response.body()!!.responseContents!![0]!!.last_name)
                    binding?.quickAge!!.setText(response.body()!!.responseContents!![0]!!.age!!.toString())
                    binding?.quickMobile!!.setText(response.body()!!.responseContents!![0]!!.patient_detail?.mobile!!.toString())

                    binding?.switchCheck?.isChecked =
                        response.body()!!.responseContents!![0]!!.is_maternity!!

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
                    //binding?.qucikLabName!!.setText(response.body()!!.responseContents!![0]!!.last_name!!.toString())


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


/*

                    if (response.body()!!.responseContents!![0]!!.patient_detail!!.country_uuid != null) {
                        val checknationality =
                            hashNationalitySpinnerList.any { it!!.key == response.body()!!.responseContents!![0]!!.patient_detail!!.country_uuid }
                        if (checknationality) {
                            binding?.qucikCountry!!.setSelection(
                                hashNationalitySpinnerList.get(
                                    response.body()!!.responseContents!![0]!!.patient_detail!!.country_uuid
                                )!!
                            )
                        } else {
                            binding?.qucikCountry!!.setSelection(0)
                        }
                    }

                    if (response.body()!!.responseContents!![0]!!.patient_detail!!.state_uuid != null) {
                        val checkstate =
                            hashStateSpinnerList.any { it!!.key == response.body()!!.responseContents!![0]!!.patient_detail!!.state_uuid }
                        if (checkstate) {
                            binding?.qucikState!!.setSelection(hashStateSpinnerList.get(response.body()!!.responseContents!![0]!!.patient_detail!!.state_uuid)!!)
                        } else {
                            binding?.qucikState!!.setSelection(0)
                        }
                    }
                    if (response.body()!!.responseContents!![0]!!.patient_detail!!.district_uuid != null) {
                        val checkdistrict =
                            hashDistrictSpinnerList.any { it!!.key == response.body()!!.responseContents!![0]!!.patient_detail!!.district_uuid }
                        if (checkdistrict) {
                            binding?.qucikDistrict!!.setSelection(
                                hashDistrictSpinnerList.get(
                                    response.body()!!.responseContents!![0]!!.patient_detail!!.district_uuid
                                )!!
                            )
                        } else {
                            binding?.qucikDistrict!!.setSelection(0)
                        }
                    }


*/


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

                    searchData = response.body()!!.responseContents!![0]!!


                    searchDob = searchData.dob


                    if (BuildConfig.FLAVOR == "puneuat" || BuildConfig.FLAVOR == "puneprod") {

                        selectschema = searchData.patient_scheme_uuid ?: 0

                        if (selectschema != 0) {

                            viewModel?.getSchemaName(selectschema, schemaNameCallBack)


                        }

                    }


                    binding?.etFathername?.setText(searchData.patient_detail!!.father_name ?: "")

                    binding?.etFathername?.error = null

                    binding?.etRemarkname?.setText(searchData.patient_detail!!.remarks ?: "")

                    if (searchData.patient_detail!!.address_line2 != null) {

                        binding?.quickAddress!!.setText(searchData.patient_detail!!.address_line2!!.toString())

                    }

                    if (searchData.title_uuid != null) {
                        val titleuuid =
                            hashSalutaionSpinnerList.any { it.key == searchData.title_uuid }
                        if (titleuuid) {
                            binding?.salutation!!.setSelection(
                                hashSalutaionSpinnerList.get(
                                    searchData.title_uuid!!
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
                                    searchData.professional_title_uuid!!
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
                            binding?.suffixcode!!.setSelection(hashSuffixSpinnerList.get(searchData.suffix_uuid!!)!!)
                        } else {
                            binding?.suffixcode!!.setSelection(0)
                        }
                    }

                    if (searchData.patient_detail!!.community_uuid != null) {
                        val checkblockuuid =
                            hashCommunitypinnerList.any { it.key == searchData.patient_detail!!.community_uuid }
                        if (checkblockuuid) {
                            binding?.community!!.setSelection(hashCommunitypinnerList.get(searchData.patient_detail!!.community_uuid)!!)
                        } else {
                            binding?.community!!.setSelection(0)
                        }
                    }

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

    val oldPinSearchRetrofitCallBack = object : RetrofitCallback<QuickSearchResponseModel> {
        override fun onSuccessfulResponse(response: Response<QuickSearchResponseModel>) {
            if (response.body()!!.responseContent != null) {
                val searchDataOld = response.body()!!.responseContent!!
                if (response.body()!!.responseContent!!.crt_dt!!.isNotEmpty()) {
                    updateId = 0
                    val sdf1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val sdf = SimpleDateFormat("yyyy")
                    var createdDate = sdf1.parse(response.body()!!.responseContent!!.crt_dt!!)
                    var createdYear = sdf.format(createdDate)
                    var currentYear = sdf.format(Date())
                    var age =
                        (Integer.parseInt(currentYear) - Integer.parseInt(createdYear)) + response.body()!!.responseContent!!.age!!
                    binding?.quickAge!!.setText("" + age)
                    created_date = response.body()!!.responseContent!!.crt_dt!!
                    binding?.qucikPeriod!!.setSelection(hashPeriodSpinnerList.get(4)!!)

                    if (response.body()!!.responseContent!!.gender != null) {
                        when {
                            response.body()!!.responseContent!!.gender.equals("SXML") -> {
                                binding?.qucikGender!!.setSelection(hashGenderSpinnerList.get(1)!!)
                            }
                            response.body()!!.responseContent!!.gender.equals("SXFML") -> {
                                binding?.qucikGender!!.setSelection(hashGenderSpinnerList.get(2)!!)
                            }
                        }
                    } else {
                        binding?.qucikGender!!.setSelection(hashGenderSpinnerList.get(3)!!)
                    }

                    when {
                        response.body()!!.responseContent!!.title.equals("TIMRS") -> {
                            binding?.salutation!!.setSelection(hashSalutaionSpinnerList.get(2)!!)
                        }
                        response.body()!!.responseContent!!.title.equals("TIMR") -> {
                            binding?.salutation!!.setSelection(hashSalutaionSpinnerList.get(1)!!)
                        }
                        response.body()!!.responseContent!!.title.equals("TISEL") -> {
                            binding?.salutation!!.setSelection(hashSalutaionSpinnerList.get(3)!!)
                        }
                        response.body()!!.responseContent!!.title.equals("TIMS") -> {
                            binding?.salutation!!.setSelection(hashSalutaionSpinnerList.get(3)!!)
                        }
                        response.body()!!.responseContent!!.title.equals("TIDR") -> {
                            binding?.salutation!!.setSelection(hashSalutaionSpinnerList.get(7)!!)
                        }
                    }


                } else {
                    updateId = 1
                    uhid = response.body()!!.responseContent!!.uhid!!
                    binding?.quickAge!!.setText(response.body()!!.responseContent!!.age!!.toString())
                    created_date = response.body()!!.responseContent!!.created_date!!
                    if (response.body()!!.responseContent!!.period_uuid != null) {
                        val checkperiod =
                            hashPeriodSpinnerList.any { it.key == response.body()!!.responseContent!!.period_uuid }
                        if (checkperiod) {
                            binding?.qucikPeriod!!.setSelection(hashPeriodSpinnerList.get(response.body()!!.responseContent!!.period_uuid)!!)
                        } else {
                            binding?.qucikPeriod!!.setSelection(0)
                        }
                    }

                    if (response.body()!!.responseContent!!.gender_uuid != null) {
                        val checkgender =
                            hashGenderSpinnerList.any { it.key == response.body()!!.responseContent!!.gender_uuid }
                        if (checkgender) {
                            binding?.qucikGender!!.setSelection(hashGenderSpinnerList.get(response.body()!!.responseContent!!.gender_uuid)!!)
                        } else {
                            binding?.qucikGender!!.setSelection(0)
                        }
                    }
                    if (searchDataOld.title_uuid != null) {
                        val titleuuid =
                            hashSalutaionSpinnerList.any { it.key == searchDataOld.title_uuid }
                        if (titleuuid) {
                            binding?.salutation!!.setSelection(
                                hashSalutaionSpinnerList.get(
                                    searchDataOld.title_uuid
                                )!!
                            )
                        } else {
                            binding?.salutation!!.setSelection(0)
                        }
                    }
                }

                binding?.lastpin?.visibility = View.VISIBLE
                binding?.pinLayout?.visibility = View.VISIBLE
                binding?.lastpinnumber?.text = response.body()!!.responseContent!!.uhid
                binding?.quickName!!.setText(response.body()!!.responseContent!!.first_name)
                binding?.etmiddlename!!.setText(response.body()!!.responseContent!!.middle_name)
                binding?.etLastname!!.setText(response.body()!!.responseContent!!.last_name)
                binding?.quickMobile!!.setText(response.body()!!.responseContent!!.patient_detail?.mobile!!.toString())
                binding?.doorNo!!.setText(response.body()!!.responseContent!!.patient_detail?.address_line1!!.toString())
                binding?.quickPincode!!.setText(response.body()!!.responseContent!!.patient_detail?.pincode!!.toString())
                // binding?.qucikLabName!!.setText(response.body()!!.responseContent!!.last_name!!.toString())

                binding?.quickAddress!!.setText(searchDataOld.patient_detail!!.address_line2!!.toString())

                if (searchDataOld.patient_detail.aadhaar_number != null) {

                    binding?.quickAather!!.setText(searchDataOld.patient_detail.aadhaar_number.toString())

                }

                if (response.body()!!.responseContent!!.patient_detail!!.country_uuid != null) {
                    val checknationality =
                        hashNationalitySpinnerList.any { it.key == response.body()!!.responseContent!!.patient_detail!!.country_uuid }
                    if (checknationality) {
                        binding?.qucikCountry!!.setSelection(
                            hashNationalitySpinnerList.get(
                                response.body()!!.responseContent!!.patient_detail!!.country_uuid
                            )!!
                        )
                    } else {
                        binding?.qucikCountry!!.setSelection(0)
                    }
                }

                if (response.body()!!.responseContent!!.patient_detail!!.state_uuid != null) {
                    val checkstate =
                        hashStateSpinnerList.any { it.key == response.body()!!.responseContent!!.patient_detail!!.state_uuid }
                    if (checkstate) {
                        binding?.qucikState!!.setSelection(hashStateSpinnerList.get(response.body()!!.responseContent!!.patient_detail!!.state_uuid)!!)
                    } else {
                        binding?.qucikState!!.setSelection(0)
                    }
                }
                if (response.body()!!.responseContent!!.patient_detail!!.district_uuid != null) {
                    val checkdistrict =
                        hashDistrictSpinnerList.any { it.key == response.body()!!.responseContent!!.patient_detail!!.district_uuid }
                    if (checkdistrict) {
                        binding?.qucikDistrict!!.setSelection(
                            hashDistrictSpinnerList.get(
                                response.body()!!.responseContent!!.patient_detail!!.district_uuid
                            )!!
                        )
                    } else {
                        binding?.qucikDistrict!!.setSelection(0)
                    }
                }
                /*               if (response.body()!!.responseContent!!.patient_detail!!.taluk_uuid != null) {
                                   val checkblockuuid =
                                       hashBlockSpinnerList.any { it!!.key == response.body()!!.responseContent!!.patient_detail!!.taluk_uuid }
                                   if (checkblockuuid) {
                                       binding?.qucikBlock!!.setSelection(hashBlockSpinnerList.get(response.body()!!.responseContent!!.patient_detail!!.taluk_uuid)!!)
                                   } else {
                                       binding?.qucikBlock!!.setSelection(0)
                                   }
                               }*/
                patientUUId = response.body()!!.responseContent!!.uuid

                if (response.body()!!.responseContent!!.patient_detail!!.lab_to_facility_uuid != null) {

                    selectLabNameID =
                        response.body()!!.responseContent!!.patient_detail!!.lab_to_facility_uuid

                    if (selectLabNameID != 0) {

                        viewModel!!.getLocationMaster(
                            selectLabNameID!!,
                            LocationMasterResponseCallback
                        )

                    }

                }
                if (response.body()!!.responseContent!!.uhid != null) {

                    uhid = response.body()!!.responseContent!!.uhid!!
                }

                if (response.body()!!.responseContent!!.registered_date != "") {

                    registerDate = response.body()!!.responseContent!!.registered_date!!
                }


                if (searchDataOld.professional_title_uuid != null) {
                    val titleuuid =
                        hashProfestioanlSpinnerList.any { it.key == searchDataOld.professional_title_uuid }
                    if (titleuuid) {
                        binding?.profesitonal!!.setSelection(
                            hashProfestioanlSpinnerList.get(
                                searchDataOld.professional_title_uuid
                            )!!
                        )
                    } else {
                        binding?.profesitonal!!.setSelection(0)
                    }
                }


                if (searchDataOld.suffix_uuid != null) {
                    val titleuuid =
                        hashSuffixSpinnerList.any { it.key == searchDataOld.suffix_uuid }
                    if (titleuuid) {
                        binding?.suffixcode!!.setSelection(hashSuffixSpinnerList.get(searchDataOld.suffix_uuid)!!)
                    } else {
                        binding?.suffixcode!!.setSelection(0)
                    }
                }

                if (searchDataOld.patient_detail.community_uuid != null) {
                    val checkblockuuid =
                        hashCommunitypinnerList.any { it.key == searchDataOld.patient_detail.community_uuid }
                    if (checkblockuuid) {
                        binding?.community!!.setSelection(hashCommunitypinnerList.get(searchDataOld.patient_detail.community_uuid)!!)
                    } else {
                        binding?.community!!.setSelection(0)
                    }
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
                Log.e("UpdateMSG", responseBody!!.body()?.message.toString())
                Toast.makeText(requireContext(), "Successfully Updated", Toast.LENGTH_LONG).show()
                appPreferences?.saveString(
                    AppConstants.LASTPIN,
                    responseBody.body()?.responseContent?.uhid
                )
                val pdfRequestModel = PrintQuickRequest()
                pdfRequestModel.componentName = "basic"
                pdfRequestModel.uuid = responseBody.body()?.responseContent?.uuid!!
                pdfRequestModel.uhid = responseBody.body()?.responseContent?.uhid!!
                pdfRequestModel.facilityName = facility_Name
                pdfRequestModel.firstName = responseBody.body()?.responseContent?.first_name!!
                pdfRequestModel.period = CovidPeriodList[selectPeriodUuid]!!
                pdfRequestModel.age = responseBody.body()?.responseContent?.age!!
                //pdfRequestModel.registered_date = responseBody?.body()?.responseContent?.registered_date!!
                pdfRequestModel.registered_date = registerDate
                pdfRequestModel.sari = sariStatus
                pdfRequestModel.ili = iliStatus
                pdfRequestModel.noSymptom = nosymptomsStatus
                pdfRequestModel.session = session_name!!
                pdfRequestModel.gender = CovidGenderList[selectGenderUuid]!!
                pdfRequestModel.mobile = binding!!.quickMobile.text.toString()

                if (!binding?.etFathername?.text?.trim().isNullOrEmpty()) {
                    pdfRequestModel.fatherName =
                        binding?.etFathername?.text?.trim().toString()

                }

                if (!binding?.quickAather?.text.toString().isNullOrEmpty()) {

                    pdfRequestModel.aadhaarNumber = binding?.quickAather?.text.toString()

                }

                if (addressenable!!) {

                    pdfRequestModel.addressDetails.doorNum = binding!!.doorNo.text.toString()
                    pdfRequestModel.addressDetails.streetName =
                        binding!!.quickAddress.text.toString()

                    if (selectNationalityUuid != 0) {
                        pdfRequestModel.addressDetails.country =
                            CovidNationalityList[selectNationalityUuid]!!
                    }
                    if (selectDistictUuid != 0) {


                        pdfRequestModel.addressDetails.district =
                            CovidDistictList[selectDistictUuid]!!


                    }

                    if (selectStateUuid != 0) {

                        try {


                            pdfRequestModel.addressDetails.state = CovidStateList[selectStateUuid]!!

                        } catch (r: Exception) {


                        }
                    }

                    if (selectVillageUuid != 0) {


                        pdfRequestModel.addressDetails.village =
                            CovidVillageList[selectVillageUuid]!!


                    }


                    if (selectBelongUuid != 0) {

                        pdfRequestModel.addressDetails.taluk = CovidBlockList[selectBelongUuid]!!


                    }

                    if (!binding?.quickPincode?.text.toString().isNullOrEmpty()) {


                        pdfRequestModel.addressDetails.pincode =
                            binding?.quickPincode?.text.toString()


                    }


                }
                pdfRequestModel.visitNum =
                    responseBody.body()?.responseContent?.patient_visits!!.visit_number!!
                pdfRequestModel.dob = responseBody.body()?.responseContent?.dob!!

                if (selectSalutationUuid != 0) {

                    pdfRequestModel.salutation = SalutaionList[selectSalutationUuid]!!

                }
                if (selectProffestionalUuid != 0) {

                    pdfRequestModel.professional = ProfestioanlList[selectProffestionalUuid]!!
                }

                if (selectCoummityUuid != 0) {

                    pdfRequestModel.community = CommunityList[selectCoummityUuid]!!
                }

                pdfRequestModel.middleName = binding!!.etmiddlename.text.toString()

                pdfRequestModel.lastName = binding!!.etLastname.text.toString()


                if (selectSuffixUuid != 0) {

                    pdfRequestModel.suffixCode = SuffixList[selectSuffixUuid]!!
                }


                if (selectSuffixUuid != 0) {

                    pdfRequestModel.suffixCode = SuffixList[selectSuffixUuid]!!
                }

                pdfRequestModel.department = binding!!.department.text.toString()

                val bundle = Bundle()
                bundle.putParcelable(AppConstants.RESPONSECONTENT, pdfRequestModel)
                bundle.putInt(AppConstants.RESPONSENEXT, 190)
                bundle.putString("From", "Quick")

                bundle.putInt("next", onNext!!)

                val labtemplatedialog = QuickDialogPDFViewerActivity()

                labtemplatedialog.arguments = bundle

                (activity as HomeActivity).replaceFragment(labtemplatedialog)


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

    val updateQuicksaveorderRegistrationRetrofitCallback =
        object : RetrofitCallback<QuickRegistrationUpdateResponseModel> {

            override fun onSuccessfulResponse(responseBody: Response<QuickRegistrationUpdateResponseModel>?) {
                Log.e("UpdateMSG", responseBody!!.body()?.message.toString())

                patientUUId = responseBody.body()!!.responseContent.uuid

                viewModel!!.createEncounter(
                    createEncounterRetrofitCallback,
                    responseBody.body()!!.responseContent.uuid!!
                )
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

    private fun toLabActivity() {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        val dateInString = sdf.format(Date())
        val pdfRequestModel = PDFRequestModel()
        pdfRequestModel.componentName = "quick"
        pdfRequestModel.uuid = patientUUId
        pdfRequestModel.uhid = uhid
        pdfRequestModel.facilityName = facility_Name
        pdfRequestModel.firstName = binding!!.quickName.text.toString()
        pdfRequestModel.period = CovidPeriodList[selectPeriodUuid]
        pdfRequestModel.age = binding!!.quickAge.text.toString().toInt()
        pdfRequestModel.registered_date = registerDate
        pdfRequestModel.sari = sariStatus
        pdfRequestModel.ili = iliStatus
        pdfRequestModel.noSymptom = nosymptomsStatus
        if (ispublic!!) {
            pdfRequestModel.testMethod = "Rapid Diagnostic Test ( Blood for Covid 19 )"
        } else {
            pdfRequestModel.testMethod = "RT - PCR(Nasopharyngeal Swab for Covid 19)"
        }
        pdfRequestModel.gender = CovidGenderList[selectGenderUuid]
        pdfRequestModel.mobile = binding!!.quickMobile.text.toString()
        pdfRequestModel.addressDetails?.doorNum = binding!!.quickAddress.text.toString()
        pdfRequestModel.addressDetails?.country = CovidNationalityList[selectNationalityUuid]
        pdfRequestModel.addressDetails?.state = CovidStateList[selectStateUuid]
        pdfRequestModel.addressDetails?.pincode = binding!!.quickPincode.text.toString()
        pdfRequestModel.addressDetails?.district = CovidDistictList[selectDistictUuid]
        pdfRequestModel.addressDetails?.block = CovidBlockList[selectBelongUuid]
        val gson = Gson()
        val intent = Intent(requireContext(), DialogPDFViewerActivity::class.java)
        intent.putExtra(AppConstants.RESPONSECONTENT, gson.toJson(pdfRequestModel))
        if (selectLabNameID != 0) {
            intent.putExtra(AppConstants.RESPONSENEXT, 0)
        } else {
            intent.putExtra(AppConstants.RESPONSENEXT, 1)
        }
        requireActivity().finish()
        startActivity(intent)
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is SearchPatientDialogFragment) {
            childFragment.setOnOrderProcessRefreshListener(this)
        }
    }

    override fun onRefreshOrderList(res: QuickSearchresponseContent) {
        searchData = res
        updateId = 1
        appPreferences?.saveString(AppConstants.LASTPIN, searchData.uhid)
        binding?.lastpin?.visibility = View.VISIBLE
        binding?.pinLayout?.visibility = View.VISIBLE
        binding?.lastpinnumber?.text = searchData.uhid
        binding?.quickName!!.setText(searchData.first_name)
        binding?.etmiddlename!!.setText(searchData.middle_name)
        binding?.etLastname!!.setText(searchData.last_name)
        binding?.quickAge!!.setText(searchData.age!!.toString())
        binding?.quickMobile!!.setText(searchData.patient_detail?.mobile!!.toString())
        searchDob = searchData.dob
        binding?.etFathername?.setText(searchData.patient_detail!!.father_name ?: "")
        binding?.etFathername?.error = null
        binding?.etRemarkname?.setText(searchData.patient_detail!!.remarks ?: "")
        if (searchData.patient_detail!!.aadhaar_number != null) {
            binding?.quickAather!!.setText(searchData.patient_detail!!.aadhaar_number.toString())
        }

        if (searchData.patient_detail!!.address_line1 != null) {
            binding?.doorNo!!.setText(searchData.patient_detail!!.address_line1.toString())
        }
        if (searchData.patient_detail!!.pincode != null) {
            binding?.quickPincode!!.setText(searchData.patient_detail!!.pincode.toString())
        }
        patientUUId = searchData.uuid
        if (searchData.gender_uuid != null) {
            val checkgender = hashGenderSpinnerList.any { it.key == searchData.gender_uuid }
            if (checkgender) {
                binding?.qucikGender!!.setSelection(hashGenderSpinnerList.get(searchData.gender_uuid!!)!!)
            } else {
                binding?.qucikGender!!.setSelection(0)
            }
        }

        if (searchData.period_uuid != null) {

            val checkperiod = hashPeriodSpinnerList.any { it.key == searchData.period_uuid }

            if (checkperiod) {
                binding?.qucikPeriod!!.setSelection(hashPeriodSpinnerList.get(searchData.period_uuid!!)!!)
            } else {
                binding?.qucikPeriod!!.setSelection(0)
            }
        }

/*
        if (searchData.patient_detail.country_uuid != null) {
            val checknationality =
                hashNationalitySpinnerList.any { it!!.key == searchData.patient_detail.country_uuid }
            if (checknationality) {
                binding?.qucikCountry!!.setSelection(hashNationalitySpinnerList.get(searchData.patient_detail.country_uuid)!!)
            } else {
                binding?.qucikCountry!!.setSelection(0)
            }
        }

        if (searchData.patient_detail.state_uuid != null) {
            val checkstate =
                hashStateSpinnerList.any { it!!.key == searchData.patient_detail.state_uuid }
            if (checkstate) {
                binding?.qucikState!!.setSelection(hashStateSpinnerList.get(searchData.patient_detail.state_uuid)!!)
            } else {
                binding?.qucikState!!.setSelection(0)
            }
        }
        if (searchData.patient_detail.district_uuid != null) {
            val checkdistrict =
                hashDistrictSpinnerList.any { it!!.key == searchData.patient_detail.district_uuid }
            if (checkdistrict) {
                binding?.qucikDistrict!!.setSelection(hashDistrictSpinnerList.get(searchData.patient_detail.district_uuid)!!)
            } else {
                binding?.qucikDistrict!!.setSelection(0)
            }
        }
*/
        selectBelongUuid = searchData.patient_detail!!.taluk_uuid ?: 0
        selectVillageUuid = searchData.patient_detail!!.village_uuid ?: 0
        selectNationalityUuid = searchData.patient_detail!!.country_uuid ?: 0
        selectStateUuid = searchData.patient_detail!!.state_uuid ?: 0
        selectDistictUuid = searchData.patient_detail!!.district_uuid ?: 0

        viewModel?.getCovidNationalityList(
            "nationality_type",
            covidNationalityResponseCallback
        )
        viewModel?.getCovidNationalityList(
            "nationality_type",
            covidNationalityResponseCallback
        )

        if (searchData.patient_detail!!.lab_to_facility_uuid != null) {
            selectLabNameID = searchData.patient_detail!!.lab_to_facility_uuid
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

        if (searchData.patient_detail!!.address_line2 != null) {
            binding?.quickAddress!!.setText(searchData.patient_detail!!.address_line2.toString())
        }

        if (searchData.title_uuid != null) {
            val titleuuid =
                hashSalutaionSpinnerList.any { it.key == searchData.title_uuid }
            if (titleuuid) {
                binding?.salutation!!.setSelection(hashSalutaionSpinnerList.get(searchData.title_uuid!!)!!)
            } else {
                binding?.salutation!!.setSelection(0)
            }
        }

        if (searchData.professional_title_uuid != null) {
            val titleuuid =
                hashProfestioanlSpinnerList.any { it.key == searchData.professional_title_uuid }
            if (titleuuid) {
                binding?.profesitonal!!.setSelection(hashProfestioanlSpinnerList.get(searchData.professional_title_uuid!!)!!)
            } else {
                binding?.profesitonal!!.setSelection(0)
            }
        }

        if (searchData.suffix_uuid != null) {
            val titleuuid =
                hashSuffixSpinnerList.any { it.key == searchData.suffix_uuid }
            if (titleuuid) {
                binding?.suffixcode!!.setSelection(hashSuffixSpinnerList.get(searchData.suffix_uuid!!)!!)
            } else {
                binding?.suffixcode!!.setSelection(0)
            }
        }

        if (searchData.patient_detail!!.community_uuid != null) {
            val checkblockuuid =
                hashCommunitypinnerList.any { it.key == searchData.patient_detail!!.community_uuid }
            if (checkblockuuid) {
                binding?.community!!.setSelection(hashCommunitypinnerList.get(searchData.patient_detail!!.community_uuid)!!)
            } else {
                binding?.community!!.setSelection(0)
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

    val sessionResponseCallback = object : RetrofitCallback<ResponseSesionModule> {
        override fun onSuccessfulResponse(responseBody: Response<ResponseSesionModule>?) {
            if (responseBody?.body()?.responseContents != null) {
                mon_op_start_time = responseBody.body()?.responseContents?.mon_op_start_time!!
                mon_op_end_time = responseBody.body()?.responseContents?.mon_op_end_time!!
                Evn_op_start_time = responseBody.body()?.responseContents?.Evn_op_start_time!!
                Evn_op_end_time = responseBody.body()?.responseContents?.Evn_op_end_time!!
                val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                if (responseBody.body()?.responseContents?.op_extension_time != null && responseBody.body()?.responseContents?.op_extension_time != "") {
                    opExTime = responseBody.body()?.responseContents?.op_extension_time!!.toInt()
                } else {
                    opExTime = 0
                }
                opEveEndTimeMin = sdf.parse(Evn_op_end_time)
                opMorStartTime = sdf.parse(mon_op_start_time)
                opMorEndTime = sdf.parse(mon_op_end_time)
                opEveStartTime = sdf.parse(Evn_op_start_time)
                TimerStart()
            } else {
                mAdapter?.setActiveSeation("3")
                session_uuid = case_session_uuid
                binding?.buttonstatus?.text = case_session_name
                session_name = case_session_name
            }
        }

        override fun onBadRequest(errorBody: Response<ResponseSesionModule>?) {
            val gson = GsonBuilder().create()
            val responseModel: ResponseSesionModule
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    ResponseSesionModule::class.java
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

    val activitysessionResponseCallback = object : RetrofitCallback<ResponseActivitySession> {
        override fun onSuccessfulResponse(responseBody: Response<ResponseActivitySession>?) {
            responseactivitysession = responseBody?.body()?.responseContents
            mAdapter?.addAll(responseactivitysession)
            for (i in responseactivitysession!!.indices) {
                if (responseactivitysession!![i]!!.code == "1") {
                    binding!!.department.setText("")
                    selectdepartmentUUId = 0
                    casualtyBased = false
                    morning_session_uuid = responseactivitysession?.get(i)?.uuid!!
                    morning_session_name = responseactivitysession?.get(i)?.name!!
                } else if (responseactivitysession!![i]!!.code == "2") {
                    binding!!.department.setText("")
                    selectdepartmentUUId = 0
                    casualtyBased = false
                    evening_session_uuid = responseactivitysession?.get(i)?.uuid!!
                    evening_session_name = responseactivitysession?.get(i)?.name!!
                } else if (responseactivitysession!![i]!!.code == "3") {
                    case_session_uuid = responseactivitysession?.get(i)?.uuid!!
                    case_session_name = responseactivitysession?.get(i)?.name!!
                    binding!!.department.setText("")
                    selectdepartmentUUId = 0
                    casualtyBased = true
                }
            }
            viewModel!!.getSession(facility_id!!, sessionResponseCallback)

        }

        override fun onBadRequest(errorBody: Response<ResponseActivitySession>?) {
            val gson = GsonBuilder().create()
            val responseModel: ResponseActivitySession
            try {
                responseModel = gson.fromJson(
                    errorBody!!.errorBody()!!.string(),
                    ResponseActivitySession::class.java
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

    /*   val getTalukRetrofitCallback = object : RetrofitCallback<TalukListResponseModel> {

           override fun onSuccessfulResponse(responseBody: Response<TalukListResponseModel>?) {

               if(responseBody?.body()?.responseContents?.isNotEmpty()) {

                   talukAdapter(responseBody?.body()?.responseContents)

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

       private fun talukAdapter(responseContents: ArrayList<Taluk>?) {

       }
   */
}
