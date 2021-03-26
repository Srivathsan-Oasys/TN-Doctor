package com.hmis_tn.doctor.ui.detailed_registration.ui

//import androidx.appcompat.widget.Toolbar

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.FragementDetailedRegistrationBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.dashboard.model.*
import com.hmis_tn.doctor.ui.dashboard.model.registration.*
import com.hmis_tn.doctor.ui.detailedRegistration.model.*
import com.hmis_tn.doctor.ui.detailedRegistration.ui.PatientAllReferralsAdapter
import com.hmis_tn.doctor.ui.detailedRegistration.ui.PatientAllVisitsAdapter
import com.hmis_tn.doctor.ui.detailedRegistration.viewModel.DetailedRegistrationViewModel
import com.hmis_tn.doctor.ui.detailedRegistration.viewModel.DetailedRegistrationViewModelFactory
import com.hmis_tn.doctor.ui.emr_workflow.history.immunization.model.ImmunizationInstitutionResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.history.immunization.model.ImmunizationInstitutionresponseContent
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.FavAddAllDepatResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.ErrorAPIClass
import com.hmis_tn.doctor.ui.home.HomeActivity
import com.hmis_tn.doctor.ui.quick_reg.model.*
import com.hmis_tn.doctor.ui.quick_reg.model.activitysession.ResponseActivitySession
import com.hmis_tn.doctor.ui.quick_reg.model.activitysession.ResponseContentsactivitysession
import com.hmis_tn.doctor.ui.quick_reg.model.request.QuickRegistrationRequestModel
import com.hmis_tn.doctor.ui.quick_reg.model.session.ResponseSesionModule
import com.hmis_tn.doctor.ui.quick_reg.view.SearchPatientDialogFragment
import com.hmis_tn.doctor.ui.quickregistration.model.*
import com.hmis_tn.doctor.ui.quickregistration.ui.DepartmentSearchAdapter
import com.hmis_tn.doctor.ui.quickregistration.ui.QuickDialogPDFViewerActivity
import com.hmis_tn.doctor.ui.quickregistration.ui.SessionAdapter
import com.hmis_tn.doctor.utils.Utils
import com.hmis_tn.doctor.utils.custom_views.CustomProgressDialog
import retrofit2.Response
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.time.ExperimentalTime

class DetailedRegistration : Fragment(), SearchPatientDialogFragment.OnOrderProcessListener {
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

    private var ReferralsAdapter: PatientAllReferralsAdapter? = null

    private var VisitsAdapter: PatientAllVisitsAdapter? = null

    var salutaioData: ArrayList<SalutationresponseContent> = ArrayList()

    var GenderlistData: ArrayList<GenderresponseContent?> = ArrayList()

    private var mAdapter: SessionAdapter? = null

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
    var binding: FragementDetailedRegistrationBinding? = null
    var utils: Utils? = null
    private var viewModel: DetailedRegistrationViewModel? = null

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


    private var IncomeList = mutableMapOf<Int, String>()
    private var hashIncomeSpinnerList: HashMap<Int, Int> = HashMap()
    private var selectIncomeUuid: Int? = 0


    private var OccupationList = mutableMapOf<Int, String>()
    private var hashOccupationSpinnerList: HashMap<Int, Int> = HashMap()
    private var selectOccupationUuid: Int? = 0

    private var InstitutionList = mutableMapOf<Int, String>()
    private var hashInstitutionSpinnerList: HashMap<Int, Int> = HashMap()
    private var selectInstitutionUuid: Int? = 0


    private var ReferralReasonList = mutableMapOf<Int, String>()
    private var hashReferralReasonSpinnerList: HashMap<Int, Int> = HashMap()
    private var selectReferralReasonUuid: Int? = 0

    private var ReligionList = mutableMapOf<Int, String>()
    private var hashReligionSpinnerList: HashMap<Int, Int> = HashMap()
    private var selectReligionUuid: Int? = 0

    private var TreatmentPlanList = mutableMapOf<Int, String>()
    private var hashTreatmentPlanSpinnerList: HashMap<Int, Int> = HashMap()
    private var selectTreatmentPlanUuid: Int? = 0

    private var RefferalTypeList = mutableMapOf<Int, String>()
    private var hashRefferalTypeSpinnerList: HashMap<Int, Int> = HashMap()
    private var selectRefferalTypeUuid: Int? = 0

    private var ComplicationList = mutableMapOf<Int, String>()
    private var hashComplicationSpinnerList: HashMap<Int, Int> = HashMap()
    private var selectComplicationUuid: Int? = 0


    private var institutionNameList = mutableMapOf<Int, String>()
    private var selectInstitutionNameUuid: Int? = 0


    private var selectdepartmentRefuralUUId: Int? = 0

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
    var opMorStartTime: Date? = null
    var opMorEndTime: Date? = null
    var opEveStartTime: Date? = null
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
                R.layout.fragement_detailed_registration,
                container,
                false
            )

        viewModel = DetailedRegistrationViewModelFactory(
            requireActivity().application
        )
            .create(DetailedRegistrationViewModel::class.java)
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

            //  Toast.makeText(activity, "arguments is null ", Toast.LENGTH_LONG).show()
        } else {

            var pinshow = args.getInt("PIN")

            if (pinshow == 0) {

                //    binding?.pinLayout?.visibility = View.GONE

            } else {

                val lastpin = appPreferences?.getString(AppConstants.LASTPIN)

                // binding?.lastpinnumber?.setText(lastpin)
                //binding?.pinLayout?.visibility = View.VISIBLE

            }

        }




        customProgressDialog = CustomProgressDialog(requireContext())
        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })
/*

        viewModel!!.progress.observe(viewLifecycleOwner,
            Observer { progress ->
                if (progress == View.VISIBLE) {
                    customProgressDialog!!.show()
                } else if (progress == View.GONE) {
                    customProgressDialog!!.dismiss()
                }
            })
*/


        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")

        tat_start_time = sdf.format(Date())

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

        setSeasion()


        binding?.visitHistoryHeader?.setOnClickListener {
            if (binding?.visitHistoryResultlayout?.visibility == View.GONE) {
                binding?.visitHistoryResultlayout?.visibility = View.VISIBLE
            } else {
                binding?.visitHistoryResultlayout?.visibility = View.GONE
            }
        }


        binding?.patientInfoHeader?.setOnClickListener {

            if (binding?.patientInfoResultlayout?.visibility == View.GONE) {

                binding?.patientInfoResultlayout?.visibility = View.VISIBLE


            } else {

                binding?.patientInfoResultlayout?.visibility = View.GONE

            }


        }

        binding?.patientDetailsHeader?.setOnClickListener {

            if (binding?.patientDetailsResultlayout?.visibility == View.GONE) {

                binding?.patientDetailsResultlayout?.visibility = View.VISIBLE


            } else {

                binding?.patientDetailsResultlayout?.visibility = View.GONE

            }


        }


        binding?.addressheader?.setOnClickListener {

            if (binding?.addressresultlayout?.visibility == View.GONE) {

                binding?.addressresultlayout?.visibility = View.VISIBLE


            } else {

                binding?.addressresultlayout?.visibility = View.GONE

            }

        }

        binding?.referalDetailsHeader?.setOnClickListener {

            if (binding?.referalDetailsResultlayout?.visibility == View.GONE) {

                binding?.referalDetailsResultlayout?.visibility = View.VISIBLE


            } else {

                binding?.referalDetailsResultlayout?.visibility = View.GONE

            }

        }

        binding?.referalHistoryHeader?.setOnClickListener {

            if (binding?.referalHistoryResultlayout?.visibility == View.GONE) {

                binding?.referalHistoryResultlayout?.visibility = View.VISIBLE


            } else {

                binding?.referalHistoryResultlayout?.visibility = View.GONE

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



                        if (quicksearch?.trim()?.length!! > 0) {

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

                                        quickRegistrationSaveResponseModel.is_maternity = Matanity

                                        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")

                                        val dateInString = sdf.format(Date())
                                        quickRegistrationSaveResponseModel.tat_start_time =
                                            tat_start_time

                                        quickRegistrationSaveResponseModel.tat_end_time =
                                            dateInString

                                        quickRegistrationSaveResponseModel.village_uuid =
                                            selectVillageUuid.toString()


                                        if (!binding?.etFathername?.text?.trim().isNullOrEmpty()) {
                                            quickRegistrationSaveResponseModel.father_name =
                                                binding?.etFathername?.text?.trim().toString()

                                        }


                                        if (!binding?.etRemarkname?.text?.trim().isNullOrEmpty()) {
                                            quickRegistrationSaveResponseModel.remarks =
                                                binding?.etRemarkname?.text?.trim().toString()

                                        }


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


                                        quickregUseoldpin.village_uuid =
                                            selectVillageUuid.toString()

                                        if (!binding?.etFathername?.text?.trim().isNullOrEmpty()) {
                                            quickregUseoldpin.father_name =
                                                binding?.etFathername?.text?.trim().toString()

                                        }

                                        if (!binding?.etRemarkname?.text?.trim().isNullOrEmpty()) {
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
                                        quickUpdateRequestModel.country_uuid = selectNationalityUuid
                                        quickUpdateRequestModel.state_uuid = selectStateUuid
                                        quickUpdateRequestModel.pincode =
                                            binding!!.quickPincode.text.toString()
                                        quickUpdateRequestModel.address_line2 =
                                            binding!!.quickAddress.text.toString()
                                        quickUpdateRequestModel.district_uuid = selectDistictUuid
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



                                        if (!binding?.etFathername?.text?.trim().isNullOrEmpty()) {
                                            quickUpdateRequestModel.father_name =
                                                binding?.etFathername?.text?.trim().toString()

                                        }

                                        if (!binding?.etRemarkname?.text?.trim().isNullOrEmpty()) {
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

                                        if (!binding?.etFathername?.text?.trim().isNullOrEmpty()) {
                                            quickUpdateRequestModel.father_name =
                                                binding?.etFathername?.text?.trim().toString()

                                        }

                                        if (!binding?.etRemarkname?.text?.trim().isNullOrEmpty()) {
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



        binding?.InstitutionReferal!!.requestFocus()
        binding?.InstitutionReferal!!.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 2 && s.length < 5) {
                    //  customProgressDialog!!.show()
                    viewModel?.getInstitutionSearchResult(
                        facility_id,
                        s.toString(),
                        getInstitutionSearchRetrofitCallBack
                    )

                }
            }
        })


        binding?.InstitutionReferal!!.onItemClickListener =
            object : AdapterView.OnItemClickListener {
                override fun onItemClick(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val itemValue = parent!!.getItemAtPosition(position).toString()

                    selectInstitutionNameUuid =
                        institutionNameList.filterValues { it == itemValue }.keys.toList()[0]

                    //Log.e("InsSelectData",selectInstitutionUuid.toString())
                }

            }

        APICall()

        return binding!!.root
    }

    private fun APICall() {

        viewModel?.getCovidNameTitleList(
            facility_id!!,
            SalutationResponseCallback
        )

        viewModel!!.getAll("suffix", suffixResponseCallback)

        viewModel?.getCovidPeriodList(facility_id!!, PeriodResponseCallback)

        viewModel!!.getApplicationRules(getApplicationRulesResponseCallback)


        viewModel!!.getCommunity("community", getCommunityListRetrofitCallBack)

        viewModel?.getCovidGenderList(facility_id!!, covidGenderResponseCallback)

        viewModel!!.getAll("unit", unitResponseCallback)

        viewModel!!.getAll("income", incomeResponseCallback)

        viewModel!!.getAll("occupation", occupationResponseCallback)

        viewModel!!.getAll("facility_type", InstitutionResponseCallback)

        viewModel!!.getCommunity("referral_reason", getReferralReasonListRetrofitCallBack)

        viewModel!!.getCommunity("religion", getReligionListRetrofitCallBack)

        viewModel!!.getCommunity("treatment_plan", getTreatmentPlanListRetrofitCallBack)

        viewModel!!.getCommunity("refferal_type", getRefferalTypeListRetrofitCallBack)

        viewModel!!.getCommunity("complication", getcomplicationListRetrofitCallBack)


        viewModel!!.getFaciltyLocation(facilityLocationResponseCallback)


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
                    requests.casualtyBased = false

                    requests.deptId = departmentUUId.toString()
                    requests.search = s.toString()
                    requests.pageNo = 0
                    requests.paginationSize = 10
                    requests.genderId = selectGenderUuid!!

                    viewModel!!.getAllDepartment(requests, DepartmentSearchCallBack)

                }
            }
        })


        binding!!.departmentReferal.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {

                val datasize = s.trim().length
                if (datasize in 2..7) {
                    val requests = GetAllDepartmentRequestModel()

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
                    requests.casualtyBased = false
                    requests.search = s.toString()
                    requests.pageNo = 0
                    if (selectInstitutionNameUuid != 0) {
                        requests.facilityId = selectInstitutionNameUuid

                    } else {

                        requests.facilityId = facility_id

                    }
                    requests.paginationSize = 10


                    viewModel!!.getAllDepartmentAll(requests, DepartmentSearchRefuralCallBack)

                }
            }
        })


    }

    private fun setSeasion() {

        linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding?.sessionlist!!.layoutManager = linearLayoutManager

        mAdapter = SessionAdapter(requireContext(), ArrayList())
        binding?.sessionlist!!.adapter = mAdapter



        mAdapter?.setOnPrintClickListener(object : SessionAdapter.OnPrintClickListener {
            override fun onPrintClick(uuid: ResponseContentsactivitysession) {

                StopTimer()

                session_uuid = uuid.uuid

                session_name = uuid.name
            }
        })

        viewModel!!.getActivitySession(facility_id!!, activitySeasionResponseCallback)

    }


    //seassion handling

    val activitySeasionResponseCallback = object : RetrofitCallback<ResponseActivitySession> {
        override fun onSuccessfulResponse(responseBody: Response<ResponseActivitySession>?) {

            responseactivitysession = responseBody?.body()?.responseContents

            mAdapter?.addAll(responseactivitysession)

            for (i in responseactivitysession!!.indices) {

                if (responseactivitysession!![i]!!.code == "1") {

                    morning_session_uuid = responseactivitysession?.get(i)?.uuid!!

                    morning_session_name = responseactivitysession?.get(i)?.name!!

                } else if (responseactivitysession!![i]!!.code == "2") {

                    evening_session_uuid = responseactivitysession?.get(i)?.uuid!!

                    evening_session_name = responseactivitysession?.get(i)?.name!!

                } else if (responseactivitysession!![i]!!.code == "3") {

                    case_session_uuid = responseactivitysession?.get(i)?.uuid!!

                    case_session_name = responseactivitysession?.get(i)?.name!!

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

    //salutaion
    val SalutationResponseCallback =
        object : RetrofitCallback<CovidSalutationTitleResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<CovidSalutationTitleResponseModel>?) {

                val res = responseBody?.body()?.responseContents
                //     A1selectSalutationUuid = responseBody?.body()?.responseContents?.get(0)!!.uuid!!
                setSaluationProfession(res)

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

    private fun setSaluationProfession(res: List<SalutationresponseContent?>?) {


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
            requireContext(),
            R.layout.spinner_item,
            SalutaionList.values.toMutableList()
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)

        binding?.salutation!!.adapter = adapter



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



        ProfestioanlList =
            professtionalData.map { it.uuid!! to it.name!! }.toMap().toMutableMap()

        hashProfestioanlSpinnerList.clear()

        for (i in professtionalData.indices) {

            hashProfestioanlSpinnerList[professtionalData[i].uuid!!] = i
        }


        val adapter2 = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            ProfestioanlList.values.toMutableList()
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.profesitonal!!.adapter = adapter2


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


    }


    val suffixResponseCallback = object : RetrofitCallback<GetAllResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<GetAllResponseModel>?) {

            var responseDatas = responseBody!!.body()!!.responseContents

            setSuffix(responseDatas)


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

    private fun setSuffix(responseDatas: List<GetAllResponse>) {

        var responseData: ArrayList<GetAllResponse> = ArrayList()


        var dummy: GetAllResponse = GetAllResponse()

        dummy.uuid = 0

        dummy.name = "Suffix Code"

        responseData.add(dummy)


        responseData.addAll(responseDatas)

        SuffixList = responseData.map { it.uuid to it.name }.toMap().toMutableMap()

        hashSuffixSpinnerList.clear()

        for (i in responseData.indices) {

            hashSuffixSpinnerList[responseData[i].uuid] = i
        }


        val adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            SuffixList.values.toMutableList()
        )

        adapter.setDropDownViewResource(R.layout.spinner_item)

        binding?.suffixcode!!.adapter = adapter


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

    val PeriodResponseCallback = object : RetrofitCallback<CovidPeriodResponseModel> {
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


    }


    val DepartmentSearchCallBack = object : RetrofitCallback<FavAddAllDepatResponseModel> {
        @SuppressLint("NewApi")
        override fun onSuccessfulResponse(responseBody: Response<FavAddAllDepatResponseModel>?) {
            Log.i("", "" + responseBody?.body()?.responseContents)

            setDepartmentAdapter(responseBody?.body()?.responseContents)


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


    private fun setDepartmentAdapter(responseContents: List<FavAddAllDepatResponseContent>?) {

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


            if (selectedPoi.is_speciality == 1) {

                binding?.unittext?.visibility = View.VISIBLE

                binding?.unit?.visibility = View.VISIBLE

            } else {


                binding?.unittext?.visibility = View.GONE

                binding?.unit?.visibility = View.GONE


            }


        }

    }

    val getCommunityListRetrofitCallBack = object : RetrofitCallback<ResponseSpicemanType> {
        override fun onSuccessfulResponse(response: Response<ResponseSpicemanType>) {

            val responseContents = Gson().toJson(response.body()?.responseContents)


            setCommunity(response.body()?.responseContents)


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

    private fun setCommunity(responseDatas: ArrayList<ResponseSpicemanTypeContent?>?) {


        var responseData: ArrayList<ResponseSpicemanTypeContent?>? = ArrayList()

        var dummy: ResponseSpicemanTypeContent = ResponseSpicemanTypeContent()

        dummy.uuid = 0

        dummy.name = "Community"

        responseData?.add(dummy)

        /*

        var responseDatas =
              response!!.body()!!.responseContents as List<ResponseSpicemanTypeContent>
  */
        responseData?.addAll(responseDatas!!)

        CommunityList = responseData?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        hashCommunitypinnerList.clear()

        for (i in responseData.indices) {

            hashCommunitypinnerList[responseData[i]!!.uuid!!] = i
        }


        val adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            CommunityList.values.toMutableList()
        )

        adapter.setDropDownViewResource(R.layout.spinner_item)

        binding?.community!!.adapter = adapter

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


    }


    val covidGenderResponseCallback = object : RetrofitCallback<CovidGenderResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<CovidGenderResponseModel>?) {

            selectGenderUuid = responseBody?.body()?.responseContents?.get(0)!!.uuid!!
            setGender(responseBody.body()?.responseContents)

            //viewModel!!.getFaciltyLocation(facilityLocationResponseCallback)
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


    }


    val unitResponseCallback = object : RetrofitCallback<GetAllResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<GetAllResponseModel>?) {


            var responseDatas = responseBody!!.body()!!.responseContents

            setUnit(responseDatas)


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

    private fun setUnit(responseDatas: List<GetAllResponse>) {

        var responseData: ArrayList<GetAllResponse> = ArrayList()

        var dummy: GetAllResponse = GetAllResponse()

        dummy.uuid = 0

        dummy.name = "Unit"

        responseData.add(dummy)


        responseData.addAll(responseDatas)

        UnitList = responseData.map { it.uuid to it.name }.toMap().toMutableMap()

        hashUnitSpinnerList.clear()

        for (i in responseData.indices) {

            hashUnitSpinnerList[responseData[i].uuid] = i
        }


        val adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            UnitList.values.toMutableList()
        )

        adapter.setDropDownViewResource(R.layout.spinner_item)

        binding?.unit!!.adapter = adapter

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

    }


    val incomeResponseCallback = object : RetrofitCallback<GetAllResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<GetAllResponseModel>?) {


            var responseDatas = responseBody!!.body()!!.responseContents

            setIncome(responseDatas)


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


    private fun setIncome(responseDatas: List<GetAllResponse>) {

        var responseData: ArrayList<GetAllResponse> = ArrayList()

        var dummy: GetAllResponse = GetAllResponse()

        dummy.uuid = 0

        dummy.name = "Income"

        responseData.add(dummy)


        responseData.addAll(responseDatas)

        IncomeList = responseData.map { it.uuid to it.name }.toMap().toMutableMap()

        hashIncomeSpinnerList.clear()

        for (i in responseData.indices) {

            hashIncomeSpinnerList[responseData[i].uuid] = i
        }


        val adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            IncomeList.values.toMutableList()
        )

        adapter.setDropDownViewResource(R.layout.spinner_item)

        binding?.IncomeSpinner!!.adapter = adapter

        binding?.IncomeSpinner?.onItemSelectedListener =
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
                    selectIncomeUuid = IncomeList.filterValues { it == itemValue }.keys.toList()[0]

                }

            }

    }


    val occupationResponseCallback = object : RetrofitCallback<GetAllResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<GetAllResponseModel>?) {


            var responseDatas = responseBody!!.body()!!.responseContents

            setOccupation(responseDatas)


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


    private fun setOccupation(responseDatas: List<GetAllResponse>) {

        var responseData: ArrayList<GetAllResponse> = ArrayList()

        var dummy: GetAllResponse = GetAllResponse()

        dummy.uuid = 0

        dummy.name = "Occupation"

        responseData.add(dummy)

        responseData.addAll(responseDatas)

        OccupationList = responseData.map { it.uuid to it.name }.toMap().toMutableMap()

        hashOccupationSpinnerList.clear()


        for (i in responseData.indices) {

            hashOccupationSpinnerList[responseData[i].uuid] = i
        }


        val adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            OccupationList.values.toMutableList()
        )

        adapter.setDropDownViewResource(R.layout.spinner_item)

        binding?.OccupencySpinner!!.adapter = adapter

        binding?.OccupencySpinner?.onItemSelectedListener =
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
                    selectOccupationUuid =
                        OccupationList.filterValues { it == itemValue }.keys.toList()[0]

                }

            }

    }

    val InstitutionResponseCallback = object : RetrofitCallback<GetAllResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<GetAllResponseModel>?) {

            var responseDatas = responseBody!!.body()!!.responseContents

            setInstitution(responseDatas)


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

    private fun setInstitution(responseDatas: List<GetAllResponse>) {

        var responseData: ArrayList<GetAllResponse> = ArrayList()

        var dummy: GetAllResponse = GetAllResponse()

        dummy.uuid = 0

        dummy.name = getString(R.string.Institution_title)

        responseData.add(dummy)


        responseData.addAll(responseDatas)


        InstitutionList = responseData.map { it.uuid to it.name }.toMap().toMutableMap()

        hashInstitutionSpinnerList.clear()

        for (i in responseData.indices) {

            hashInstitutionSpinnerList[responseData[i].uuid] = i
        }


        val adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            InstitutionList.values.toMutableList()
        )

        adapter.setDropDownViewResource(R.layout.spinner_item)

        binding?.InstitutionTypeSpinner!!.adapter = adapter

        binding?.InstitutionTypeSpinner?.onItemSelectedListener =
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
                    selectInstitutionUuid =
                        InstitutionList.filterValues { it == itemValue }.keys.toList()[0]

                }

            }

    }


    val getReferralReasonListRetrofitCallBack = object : RetrofitCallback<ResponseSpicemanType> {
        override fun onSuccessfulResponse(response: Response<ResponseSpicemanType>) {

            setReferralReason(response.body()?.responseContents)

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

    private fun setReferralReason(responseDatas: ArrayList<ResponseSpicemanTypeContent?>?) {


        var responseData: ArrayList<ResponseSpicemanTypeContent?>? = ArrayList()

        var dummy: ResponseSpicemanTypeContent = ResponseSpicemanTypeContent()

        dummy.uuid = 0

        dummy.name = getString(R.string.ReasonforReferral)

        responseData?.add(dummy)

        responseData?.addAll(responseDatas!!)


        ReferralReasonList = responseData?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        hashReferralReasonSpinnerList.clear()

        for (i in responseData.indices) {

            hashReferralReasonSpinnerList[responseData[i]!!.uuid!!] = i
        }


        val adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            ReferralReasonList.values.toMutableList()
        )

        adapter.setDropDownViewResource(R.layout.spinner_item)

        binding?.ReferralReasonTypeSpinner!!.adapter = adapter

        binding?.ReferralReasonTypeSpinner?.onItemSelectedListener =
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
                    selectReferralReasonUuid =
                        ReferralReasonList.filterValues { it == itemValue }.keys.toList()[0]

                }

            }


    }


    val getReligionListRetrofitCallBack = object : RetrofitCallback<ResponseSpicemanType> {
        override fun onSuccessfulResponse(response: Response<ResponseSpicemanType>) {

            setReligion(response.body()?.responseContents)

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

    private fun setReligion(responseDatas: ArrayList<ResponseSpicemanTypeContent?>?) {


        var responseData: ArrayList<ResponseSpicemanTypeContent?>? = ArrayList()

        var dummy: ResponseSpicemanTypeContent = ResponseSpicemanTypeContent()

        dummy.uuid = 0

        dummy.name = getString(R.string.Religion)

        responseData?.add(dummy)

        responseData?.addAll(responseDatas!!)

        ReligionList = responseData?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        hashReligionSpinnerList.clear()

        for (i in responseData.indices) {

            hashReligionSpinnerList[responseData[i]!!.uuid!!] = i
        }


        val adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            ReligionList.values.toMutableList()
        )

        adapter.setDropDownViewResource(R.layout.spinner_item)

        binding?.ReligionPlan!!.adapter = adapter

        binding?.ReligionPlan?.onItemSelectedListener =
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
                    selectReligionUuid =
                        ReligionList.filterValues { it == itemValue }.keys.toList()[0]

                }

            }


    }


    val getTreatmentPlanListRetrofitCallBack = object : RetrofitCallback<ResponseSpicemanType> {
        override fun onSuccessfulResponse(response: Response<ResponseSpicemanType>) {

            setTreatmentPlan(response.body()?.responseContents)

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

    private fun setTreatmentPlan(responseDatas: ArrayList<ResponseSpicemanTypeContent?>?) {


        var responseData: ArrayList<ResponseSpicemanTypeContent?>? = ArrayList()

        var dummy: ResponseSpicemanTypeContent = ResponseSpicemanTypeContent()

        dummy.uuid = 0

        dummy.name = "Treatment Plan"

        responseData?.add(dummy)

        responseData?.addAll(responseDatas!!)

        TreatmentPlanList = responseData?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        hashTreatmentPlanSpinnerList.clear()

        for (i in responseData.indices) {

            hashTreatmentPlanSpinnerList[responseData[i]!!.uuid!!] = i
        }


        val adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            TreatmentPlanList.values.toMutableList()
        )

        adapter.setDropDownViewResource(R.layout.spinner_item)

        binding?.TreatmentPlan!!.adapter = adapter

        binding?.TreatmentPlan?.onItemSelectedListener =
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
                    selectTreatmentPlanUuid =
                        TreatmentPlanList.filterValues { it == itemValue }.keys.toList()[0]

                }

            }


    }

    val getRefferalTypeListRetrofitCallBack = object : RetrofitCallback<ResponseSpicemanType> {
        override fun onSuccessfulResponse(response: Response<ResponseSpicemanType>) {

            setRefferalType(response.body()?.responseContents)

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

    private fun setRefferalType(responseDatas: ArrayList<ResponseSpicemanTypeContent?>?) {


        var responseData: ArrayList<ResponseSpicemanTypeContent?>? = ArrayList()

        var dummy: ResponseSpicemanTypeContent = ResponseSpicemanTypeContent()

        dummy.uuid = 0

        dummy.name = "Refferal Type"

        responseData?.add(dummy)

        responseData?.addAll(responseDatas!!)

        RefferalTypeList = responseData?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        hashRefferalTypeSpinnerList.clear()

        for (i in responseData.indices) {

            hashRefferalTypeSpinnerList[responseData[i]!!.uuid!!] = i
        }


        val adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            RefferalTypeList.values.toMutableList()
        )

        adapter.setDropDownViewResource(R.layout.spinner_item)

        binding?.ReferalTypeSpinner!!.adapter = adapter

        binding?.ReferalTypeSpinner?.onItemSelectedListener =
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
                    selectRefferalTypeUuid =
                        RefferalTypeList.filterValues { it == itemValue }.keys.toList()[0]

                }

            }


    }


    val getcomplicationListRetrofitCallBack = object : RetrofitCallback<ResponseSpicemanType> {
        override fun onSuccessfulResponse(response: Response<ResponseSpicemanType>) {

            setComplication(response.body()?.responseContents)

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

    private fun setComplication(responseDatas: ArrayList<ResponseSpicemanTypeContent?>?) {


        var responseData: ArrayList<ResponseSpicemanTypeContent?>? = ArrayList()

        var dummy: ResponseSpicemanTypeContent = ResponseSpicemanTypeContent()

        dummy.uuid = 0

        dummy.name = "Complication"

        responseData?.add(dummy)

        responseData?.addAll(responseDatas!!)

        ComplicationList = responseData?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()

        hashComplicationSpinnerList.clear()

        for (i in responseData.indices) {

            hashComplicationSpinnerList[responseData[i]!!.uuid!!] = i
        }


        val adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            ComplicationList.values.toMutableList()
        )

        adapter.setDropDownViewResource(R.layout.spinner_item)

        binding?.ComplicationTypeSpinner!!.adapter = adapter

        binding?.ComplicationTypeSpinner?.onItemSelectedListener =
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
                    selectComplicationUuid =
                        ComplicationList.filterValues { it == itemValue }.keys.toList()[0]

                }

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

                        viewModel!!.getTaluk(selectDistictUuid, getTalukRetrofitCallback)

                    }


                    patientUUId = response.body()!!.responseContents!![0]!!.uuid

                    if (response.body()!!.responseContents!![0]!!.patient_detail!!.lab_to_facility_uuid != null) {

                        selectLabNameID =
                            response.body()!!.responseContents!![0]!!.patient_detail!!.lab_to_facility_uuid

                        /*        if (selectLabNameID != 0) {

                                    viewModel!!.getLocationMaster(
                                        selectLabNameID!!,
                                        LocationMasterResponseCallback
                                    )

                                }*/

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

                    binding?.etRemarkname?.setText(searchData!!.patient_detail!!.remarks ?: "")

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

                    if (searchData.patient_detail!!.community_uuid != null) {
                        val checkblockuuid =
                            hashCommunitypinnerList.any { it.key == searchData.patient_detail.community_uuid }
                        if (checkblockuuid) {
                            binding?.community!!.setSelection(hashCommunitypinnerList.get(searchData.patient_detail.community_uuid)!!)
                        } else {
                            binding?.community!!.setSelection(0)
                        }
                    }

                    binding?.doorNo?.setText(searchData.patient_detail.address_line1 ?: "")


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
                val searchData = response.body()!!.responseContent!!
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

                binding?.quickAddress!!.setText(searchData.patient_detail!!.address_line2!!.toString())

                if (searchData.patient_detail.aadhaar_number != null) {

                    binding?.quickAather!!.setText(searchData.patient_detail.aadhaar_number.toString())

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

                    /*  if (selectLabNameID != 0) {

                          viewModel!!.getLocationMaster(
                              selectLabNameID!!,
                              LocationMasterResponseCallback
                          )

                      }*/

                }
                if (response.body()!!.responseContent!!.uhid != null) {

                    uhid = response.body()!!.responseContent!!.uhid!!
                }

                if (response.body()!!.responseContent!!.registered_date != "") {

                    registerDate = response.body()!!.responseContent!!.registered_date!!
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

                if (searchData.patient_detail.community_uuid != null) {
                    val checkblockuuid =
                        hashCommunitypinnerList.any { it.key == searchData.patient_detail.community_uuid }
                    if (checkblockuuid) {
                        binding?.community!!.setSelection(hashCommunitypinnerList.get(searchData.patient_detail.community_uuid)!!)
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
                    session_name = morning_session_name


                }

            } else if (optime > opEveStartTime!! && optime < opEveEndTimeMin!!) {
                Handler(Looper.getMainLooper()).post {

                    mAdapter?.setActiveSeation("2")
                    session_uuid = evening_session_uuid
                    session_name = evening_session_name


                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    mAdapter?.setActiveSeation("3")
                    session_uuid = case_session_uuid
                    session_name = case_session_name

                }
            }

        }
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)

        if (childFragment is SearchPatientDialogFragment) {
            childFragment.setOnOrderProcessRefreshListener(this)
        }

    }

    override fun onRefreshOrderList(searchData: QuickSearchresponseContent) {

        updateId = 1
        appPreferences?.saveString(AppConstants.LASTPIN, searchData.uhid)


        viewModel?.setReferrals(facility_id, searchData.uuid.toString(), getAllReferralCallBack)

        val request: GetPatientAllVisitsRequest? = GetPatientAllVisitsRequest()
        request?.pageNo = 0
        request?.paginationSize = 10
        request?.patientId = searchData.uuid

        viewModel?.getVisits(facility_id, request!!, getAllVisitsCallBack)

        binding?.lastpin?.visibility = View.VISIBLE
        binding?.pinLayout?.visibility = View.VISIBLE
        binding?.lastpinnumber?.text = searchData.uhid
        binding?.quickName!!.setText(searchData.first_name)
        binding?.etmiddlename!!.setText(searchData.middle_name)
        binding?.etLastname!!.setText(searchData.last_name)
        binding?.quickAge!!.setText(searchData.age!!.toString())
        binding?.quickMobile!!.setText(searchData.patient_detail?.mobile!!.toString())


        binding?.etFathername?.setText(searchData.patient_detail.father_name ?: "")
        binding?.etFathername?.error = null
        binding?.etRemarkname?.setText(searchData.patient_detail.remarks ?: "")

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

            /*   if (selectLabNameID != 0) {

                   viewModel!!.getLocationMaster(selectLabNameID!!, LocationMasterResponseCallback)

               }*/

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

        if (searchData.patient_detail.community_uuid != null) {
            val checkblockuuid =
                hashCommunitypinnerList.any { it.key == searchData.patient_detail.community_uuid }
            if (checkblockuuid) {
                binding?.community!!.setSelection(hashCommunitypinnerList.get(searchData.patient_detail.community_uuid)!!)
            } else {
                binding?.community!!.setSelection(0)
            }
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

                    setTaluk(ArrayList())

                    setVillage(ArrayList())
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

            } catch (e: IOException) {

                // handle failure to read error

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

            quickRegistrationSaveResponseModel.saveExists = alreadyExists

            viewModel?.quickRegistrationSaveList(
                quickRegistrationSaveResponseModel,
                saveQuickRegistrationRetrofitCallback
            )

            customdialog!!.dismiss()


        }
        noBtn.setOnClickListener {
            customdialog!!.dismiss()

            alreadyExists = false
        }
        customdialog!!.show()
    }


    val getInstitutionSearchRetrofitCallBack =
        object : RetrofitCallback<ImmunizationInstitutionResponseModel> {

            override fun onSuccessfulResponse(response: Response<ImmunizationInstitutionResponseModel>) {

                if (response.body()?.responseContents?.isNotEmpty()!!) {

                    setInstitutionSeason(response.body()?.responseContents!!)


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

                //     customProgressDialog!!.dismiss()
            }

        }


    fun setInstitutionSeason(responseContents: List<ImmunizationInstitutionresponseContent?>) {

        institutionNameList =
            responseContents.map { it?.uuid!! to it.name!! }.toMap().toMutableMap()
        val adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            institutionNameList.values.toMutableList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.InstitutionReferal!!.threshold = 1
        binding?.InstitutionReferal!!.setAdapter(adapter)

    }


    val DepartmentSearchRefuralCallBack = object : RetrofitCallback<FavAddAllDepatResponseModel> {
        @SuppressLint("NewApi")
        override fun onSuccessfulResponse(responseBody: Response<FavAddAllDepatResponseModel>?) {
            Log.i("", "" + responseBody?.body()?.responseContents)

            setDepartmentRefuralAdapter(responseBody?.body()?.responseContents)


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

    private fun setDepartmentRefuralAdapter(responseContents: List<FavAddAllDepatResponseContent>?) {

        val responseContentAdapter2 = DepartmentSearchAdapter(
            this.requireContext(),
            R.layout.row_chief_complaint_search_result,
            responseContents as ArrayList<FavAddAllDepatResponseContent>
        )
        binding!!.departmentReferal.threshold = 1
        binding!!.departmentReferal.setAdapter(responseContentAdapter2)
        binding!!.departmentReferal.showDropDown()

        binding!!.departmentReferal.setOnItemClickListener { parent, _, pos, id ->
            val selectedPoi = parent.adapter.getItem(pos) as FavAddAllDepatResponseContent?

            binding!!.departmentReferal.setText(selectedPoi!!.name)

            selectdepartmentRefuralUUId = selectedPoi.uuid


        }

    }

    val getAllReferralCallBack = object : RetrofitCallback<GetPatientAllReferralsResponse> {
        @SuppressLint("NewApi")
        override fun onSuccessfulResponse(responseBody: Response<GetPatientAllReferralsResponse>?) {
            Log.i("", "" + responseBody?.body()?.responseContents)

            setReferralsAdapter(responseBody?.body()?.responseContents)


        }

        override fun onBadRequest(errorBody: Response<GetPatientAllReferralsResponse>?) {
            val gson = GsonBuilder().create()
            val responseModel: GetPatientAllReferralsResponse
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    GetPatientAllReferralsResponse::class.java
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

    fun setReferralsAdapter(responseContent: List<GetPatientAllReferral?>?) {

        val layoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        binding?.refuralHistoryRecyclerView!!.layoutManager = layoutmanager
        ReferralsAdapter = PatientAllReferralsAdapter(requireContext(), ArrayList())
        binding?.refuralHistoryRecyclerView!!.adapter = ReferralsAdapter
        ReferralsAdapter?.clearAll()
        ReferralsAdapter?.addAll(responseContent)

    }


    val getAllVisitsCallBack = object : RetrofitCallback<GetPatientAllVisitsResponse> {
        @SuppressLint("NewApi")
        override fun onSuccessfulResponse(responseBody: Response<GetPatientAllVisitsResponse>?) {

            Log.i("", "" + responseBody?.body()?.responseContents)

            setVisitsAdapter(responseBody?.body()?.responseContents)


        }

        override fun onBadRequest(errorBody: Response<GetPatientAllVisitsResponse>?) {
            val gson = GsonBuilder().create()
            val responseModel: GetPatientAllVisitsResponse
            try {
                responseModel = gson.fromJson(
                    errorBody?.errorBody()!!.string(),
                    GetPatientAllVisitsResponse::class.java
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


    fun setVisitsAdapter(responseContent: List<GetPatientAllVisit?>?) {

        val layoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        binding?.visitHistoryRecyclerView!!.layoutManager = layoutmanager
        VisitsAdapter = PatientAllVisitsAdapter(requireContext(), ArrayList())
        binding?.visitHistoryRecyclerView!!.adapter = VisitsAdapter
        VisitsAdapter?.clearAll()
        VisitsAdapter?.addAll(responseContent)
    }
}
