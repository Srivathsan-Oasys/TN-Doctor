package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.component.extention.*
import com.hmis_tn.doctor.config.AppConstants
import com.hmis_tn.doctor.config.AppPreferences
import com.hmis_tn.doctor.databinding.TreatmentKitChildFragmentBinding
import com.hmis_tn.doctor.db.UserDetailsRoomRepository
import com.hmis_tn.doctor.fire_base_analytics.AnalyticsManager
import com.hmis_tn.doctor.retrofitCallbacks.RetrofitCallback
import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.model.search_complaint.ComplaintSearchResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.diagnosis.model.DiagnosisListData
import com.hmis_tn.doctor.ui.emr_workflow.diagnosis.model.DiagnosisNewData
import com.hmis_tn.doctor.ui.emr_workflow.diagnosis.model.diagonosissearch.DiagonosisSearchResponse
import com.hmis_tn.doctor.ui.emr_workflow.diagnosis.model.diagonosissearch.ResponseContentsdiagonosissearch
import com.hmis_tn.doctor.ui.emr_workflow.diagnosis.view.DianosisCodeSearchResultAdapter
import com.hmis_tn.doctor.ui.emr_workflow.diagnosis.view.DianosisSearchResultAdapter
import com.hmis_tn.doctor.ui.emr_workflow.investigation.model.InvestigationLoationResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.investigation.model.InvestigationLocationResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.investigation.model.InvestigationTypeResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.investigation.model.InvestigationTypeResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.*
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.favresponse.FavSearch
import com.hmis_tn.doctor.ui.emr_workflow.lab.model.favresponse.FavSearchResponce
import com.hmis_tn.doctor.ui.emr_workflow.lab.ui.ClearFavParticularPositionListener
import com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesModel
import com.hmis_tn.doctor.ui.emr_workflow.model.favourite.FavouritesResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.model.templete.TempDetails
import com.hmis_tn.doctor.ui.emr_workflow.prescription.model.*
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.PrescriptionDurationAdapter
import com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.PrescriptionSearchResultAdapter
import com.hmis_tn.doctor.ui.emr_workflow.radiology.model.GetToLocationTestResponse
import com.hmis_tn.doctor.ui.emr_workflow.radiology.model.RadiologyTypeResponseContent
import com.hmis_tn.doctor.ui.emr_workflow.radiology.model.RadiologyTypeResponseModel
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.*
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.investigation_code_search.GetInvestigationCodeSearchReq
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.investigation_code_search.GetInvestigationCodeSearchResp
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.modify.*
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.modify.Header
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.modify.HeaderX
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.modify.HeaderXX
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.modify.PatientPrescription
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.radiology_code_search.GetRadiologyCodeSearchReq
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.radiology_code_search.GetRadiologyCodeSearchResp
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.request.*
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.request.Detail
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.response.FavAddToListresponseContents
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.response.TreatmentPrescRouteSpinnerResponse
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.response.TreatmentPrescRouteSpinnerresponseContent
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.search.*
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.treatmentKitPresModel.TKtPrescriptionData
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.treatmentKitPresModel.TKtPrescriptionListData
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.view_model.TreatmentKitViewModel
import com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.view_model.TreatmentKitViewModelFactory
import com.hmis_tn.doctor.ui.quick_reg.model.ResponseTestMethodContent
import com.hmis_tn.doctor.utils.custom_views.CustomProgressDialog
import com.hmis_tn.doctor.utils.Utils
import kotlinx.android.synthetic.main.treatment_kit_child_fragment.*
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.set

class TreatmentKitChildFragment : Fragment(),
    TreatmentKitFavouriteFragment.FavClickedListener,
    PrevTreatmentKitFragment.LabPrevClickedListener,
    PrevTreatmentKitFragment.LabModifyClickedListener,
    SaveOrderTreatmentFragment.RefreshClickedListener,
    PrevTreatmentKitFragment.PrevAddNewClickListener,
    CommentTKDialogFragment.CommandClickedListener {

    private val TAG = TreatmentKitChildFragment::class.java.simpleName

    private var customdialog: Dialog? = null
    lateinit var drugName: TextView
    var facility_id: Int = 0
    private var patient_id: Int = 0
    private var deparment_UUID: Int? = 0
    private var doctor_UUID: Int? = 0
    private var encounter_doctor_UUID: Int? = 0
    private var encounter_id: Int? = 0
    private var encounter_type: Int? = 0
    private var storemaster_Id: Int? = 0
    private var warduuid: Int = 0
    private var customProgressDialog: CustomProgressDialog? = null
    var binding: TreatmentKitChildFragmentBinding? = null
    private var searchPosition: Int? = 0
    var mCallbackTreatmentKitFavFragment: ClearFavParticularPositionListener? = null
    private var utils: Utils? = null
    private var responseContents: String? = ""
    var searchposition: Int = 0
    var viewModel: TreatmentKitViewModel? = null

    //    private var treatmentDiagnosisAdapter: TreatmentKitDiagnosisAdapter? = null
    //diagnosis
    private var treatmentDiagnosisAdapter: ITreatmentKitAdapter? = null
    var TKtDiagnosisAddNew = DiagnosisNewData()
    var isDiagnosisUpdate: Boolean = false
    var diagnosisPosition: Int = 0

    //    var treatmentprescriptionAdapter: TreatmentKitPrescriptionAdapter? = null
    var treatmentprescriptionAdapter: ITreatmentKitAdapter? = null

    //prescription
    var TKtPrescriptionAddNew: TKtPrescriptionData = TKtPrescriptionData()
    var InjectionList: ArrayList<InjectionDepartment> = ArrayList()
    var InstructionList: ArrayList<PresInstructionResponseContent> = ArrayList()
    var RouteList: ArrayList<TreatmentPrescRouteSpinnerresponseContent> = ArrayList()
    var freqencyList: ArrayList<PrescriptionFrequencyresponseContent> = ArrayList()
    var durationArrayList: ArrayList<PrescriptionDurationResponseContent> = ArrayList()
    var durationAdapter: PrescriptionDurationAdapter? = null


    private var instructionMap = mutableMapOf<Int, String>()
    private var routeNamesMap = mutableMapOf<Int, String>()
    private var frequencyMap = mutableMapOf<Int, String>()

    //    var labAdapter: TreatmentKitLabAdapter? = null
    var labAdapter: ITreatmentKitAdapter? = null

    //    var treatmentRadiologyAdapter: TreatmentKitRadiologyAdapter? = null
    var treatmentRadiologyAdapter: ITreatmentKitAdapter? = null

    //    var treatmentKitInvestigationAdapter: TreatmentKitInvestigationAdapter? = null
    var treatmentKitInvestigationAdapter: ITreatmentKitAdapter? = null

    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var selectedSearchPosition = -1
    lateinit var dropdownReferenceView: AppCompatAutoCompleteTextView
    private var appPreferences: AppPreferences? = null
    private lateinit var SpinnerToLocation: Spinner

    var diagSearch: Int = 0
    var labSearch: Int = 0
    var radioSearch: Int = 0
    var investSearch: Int = 0

    private var searchName = ""
    private val autoSearchRespContents = ArrayList<ResponseContent>()

    var gson: Gson? = Gson()


    private var priorityListFilter: ArrayList<LabTypeResponseContent?>? = ArrayList()
    private var priorityMutableNameList = mutableMapOf<Int, String>()
    private val priorityHashMapUUid: HashMap<Int, Int> = HashMap()

    private var orderLocationListFilter: ArrayList<LabToLocationResponse.LabToLocationContent?>? =
        ArrayList()
    private var orderToLocationList = mutableMapOf<Int, String>()
    private val orderLocationHashMap: HashMap<Int, Int> = HashMap()
    private var selectedLabData: FavouritesModel? = FavouritesModel()
    private var labMobileDataArrayList: ArrayList<FavouritesModel?>? = ArrayList()
    private var labModifyPostion: Int? = null
    private var isLabEditMode: Boolean = false

    //radiology
    private var selectedRadiologyData: FavouritesModel? = FavouritesModel()
    private var isRadiologyEditMode: Boolean = false

    private var radiologyTestMethodMap = mutableMapOf<Int, String>()
    private val hashMapRadiologyTestMethodMap: HashMap<Int, Int> = HashMap()

    private var radiologyTestMethodList: List<ResponseTestMethodContent?>? = listOf()

    private var radiologyPriorityListFilter: ArrayList<RadiologyTypeResponseContent?>? =
        ArrayList()
    private var radiologyPriorityMutableNameList = mutableMapOf<Int, String>()
    private val radiologyPriorityHashMapUUid: HashMap<Int, Int> = HashMap()

    private var radiologyTestTypeList: List<RadiologyTypeResponseContent?>? = listOf()
    private var typeNamesList = mutableMapOf<Int, String>()

    private var radiologyMobileDataArrayList: ArrayList<FavouritesModel?>? = ArrayList()
    private var radiologyModifyPostion: Int? = null

    private var radiologyOrderToLocationList = mutableMapOf<Int, String>()
    private val radiologyOrderToLocationHashMap: HashMap<Int, Int> = HashMap()

    private var radiologyOrderLocationListFilter: ArrayList<LabToLocationResponse.LabToLocationContent?>? =
        ArrayList()
    private val radiologyOrderLocationHashMap: HashMap<Int, Int> = HashMap()

    //investigation
    private var selectedInvestigationData: FavouritesModel? = FavouritesModel()
    private var isInvestigationEditMode: Boolean = false

    private var investigationTestMethodMap = mutableMapOf<Int, String>()
    private val hashMapInvestigationTestMethodMap: HashMap<Int, Int> = HashMap()

    private var investigationTestMethodList: List<ResponseTestMethodContent?>? = listOf()

    private var investigationPriorityListFilter: ArrayList<InvestigationTypeResponseContent?>? =
        ArrayList()
    private var investigationPriorityMutableNameList = mutableMapOf<Int, String>()
    private val investigationPriorityHashMapUUid: HashMap<Int, Int> = HashMap()

    private var investigationTestTypeList: List<InvestigationTypeResponseContent?>? = listOf()
    private var investigationTypeNamesList = mutableMapOf<Int, String>()

    private var investigationMobileDataArrayList: ArrayList<FavouritesModel?>? = ArrayList()
    private var investigationModifyPostion: Int? = null

    private var investigationOrderToLocationList = mutableMapOf<Int, String>()
    private val investigationOrderToLocationHashMap: HashMap<Int, Int> = HashMap()

    private var investigationOrderLocationListFilter: ArrayList<InvestigationLocationResponseContent?>? =
        ArrayList()
    private val investigationOrderLocationHashMap: HashMap<Int, Int> = HashMap()

    private var isModify = false
    private var treatmentKitResponsResponseContent = TreatmentKitResponsResponseContent()

    private val listNewDiagnosis = ArrayList<NewDiagnosi>()
    private val listRemoveDiagnosis = ArrayList<RemoveDetail>()

    private val listExistingInvestigation = ArrayList<NewDetailX>()
    private val headerInvestigation = Header()
    private val listNewDetailsInvestigation = ArrayList<NewDetailX>()
    private val listRemovedDetailsInvestigation = ArrayList<Int>()

    private val listExistingLab = ArrayList<NewDetailX>()
    private val headerLab = HeaderX()
    private val listNewDetailsLab = ArrayList<NewDetailX>()
    private val listRemovedDetailsLab = ArrayList<Int>()

    private val headerPrescription = HeaderXX()
    private val listDetailsPrescription = ArrayList<Any>()

    private val listExistingRadiology = ArrayList<NewDetailX>()
    private val listNewDetailsRadiology = ArrayList<NewDetailX>()
    private val listRemovedDetailsRadiology = ArrayList<Int>()

    private var treatmentKitFavouriteFragment: TreatmentKitFavouriteFragment? = null
    private var prevTreatmentKitFragment: PrevTreatmentKitFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.treatment_kit_child_fragment,
            container,
            false
        )

        viewModel = TreatmentKitViewModelFactory(
            requireActivity().application
        ).create(TreatmentKitViewModel::class.java)

        requireActivity().window
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        var userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        deparment_UUID = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)!!
        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        patient_id = appPreferences?.getInt(AppConstants.PATIENT_UUID)!!
        //doctor_UUID = appPreferences?.getInt(AppConstants.ENCOUNTER_DOCTOR_UUID)!!
        //encounter_id = appPreferences?.getInt(AppConstants.ENCOUNTER_UUID)!!
        encounter_type = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)!!
        storemaster_Id = appPreferences?.getInt(AppConstants.STOREMASTER_UUID)!!
        warduuid = appPreferences?.getInt(AppConstants.WARDUUID)!!

        utils = Utils(requireContext())

        customProgressDialog = CustomProgressDialog(requireContext())
        trackTreatmentAnalyticsVisit(utils!!.getEncounterType())

        initViews()
        listeners()

        viewModel?.getFrequencyDetails(getFrequencyRetrofitCallback, facility_id)
        viewModel?.getPrescriptionDuration(getPrescriptionDurationRetrofitCallBack, facility_id)
        viewModel?.getLabType(getLabTypeRetrofitCallback, facility_id)
        viewModel?.getRadiologyType(getRadiologyPriorityRetrofitCallback, facility_id)
        viewModel?.getInvestigationType(getInvestigationTypeRetrofitCallback, facility_id)
        viewModel?.getInvestigationToLocation(
            getInvestigationToLoctionRetrofitCallback,
            facility_id
        )

        return binding!!.root
    }

    private fun initViews() {
        binding?.mainLayout?.hideKeyboard()
        binding?.drawerLayout?.openDrawer(GravityCompat.END)
        setDiagnosisAdapter()
        setPrescriptionAdapter()
        setLabAdapter()
        setRadiologyAdapter()
        setInvestigationAdapter()
        setupViewPager(binding?.viewpager!!)
        binding?.viewpager!!.offscreenPageLimit = 2
        binding?.tabs!!.setupWithViewPager(binding?.viewpager!!)

//        binding?.drawerLayout?.drawerElevation = 0f

        binding?.drawerLayout?.setScrimColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )

        binding?.searchView?.requestFocus()

//      test ui for mobile
//        if (!isTablet(requireContext())) {
//            populateDiagnosisLayout()
//            populateRadiologyLayout()
//        }

        binding?.tvStoreName?.text =
            "Store Name: ${appPreferences?.getString(AppConstants.STOREMASTER_NAME)}"
    }

    private fun populateDiagnosisLayout() {
        val fav = FavouritesModel()
/*
        fav.diagnosis_code = "CODE"
        fav.itemAppendString = "Description"

        (treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter).addRow(fav)
        (treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter).notifyDataSetChanged()*/
    }

    private fun populateRadiologyLayout() {
        val fav = FavouritesModel()

        fav.test_master_code = "CODE"
        fav.itemAppendString = "Description"

        (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).addRow(fav)
        (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).notifyDataSetChanged()

    }

    private fun listeners() {
        binding?.favouriteDrawerCardView!!.setOnClickListener {
            binding?.drawerLayout!!.openDrawer(GravityCompat.END)
        }

        if (isTablet(requireContext())) {
            (labAdapter as TreatmentKitLabAdapter).setOnCommandClickListener(
                object : TreatmentKitLabAdapter.OnCommandClickListener {
                    override fun onCommandClick(position: Int, Command: String) {
                        commandItem(position, Command, "lab")
                    }
                })

            (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).setOnRadiologyCommandClickListener(
                object : TreatmentKitRadiologyAdapter.OnRadiologyCommandClickListener {
                    override fun onCommandClick(position: Int, Command: String) {
                        commandItem(position, Command, "radiology")
                    }
                })


            (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).setOnInvestigationCommandClickListener(
                object : TreatmentKitInvestigationAdapter.OnInvestigationCommandClickListener {
                    override fun onCommandClick(position: Int, Command: String) {
                        commandItem(position, Command, "investigation")
                    }
                })

            (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).setOnPrescriptionCommandClickListener(
                object : TreatmentKitPrescriptionAdapter.OnPrescriptionCommandClickListener {
                    override fun onCommandClick(position: Int, Command: String) {
                        commandItem(position, Command, "prescription")
                    }

                })
        } else {
            (labAdapter as TreatmentKitLabMobileAdapter).setOnCommandClickListener(
                object : TreatmentKitLabMobileAdapter.OnCommandClickListener {
                    override fun onCommandClick(position: Int, Command: String) {
                        commandItem(position, Command, "lab")
                    }
                })

            (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).setOnCommandClickListener(
                object : TreatmentKitRadiologyMobileAdapter.OnCommandClickListener {
                    override fun onCommandClick(position: Int, Command: String) {
                        commandItem(position, Command, "radiology")
                    }
                })


            (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).setOnCommandClickListener(
                object : TreatmentKitInvestigationMobileAdapter.OnCommandClickListener {
                    override fun onCommandClick(position: Int, Command: String) {
                        commandItem(position, Command, "investigation")
                    }
                })

            (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).setOnCommandClickListener(
                object : TreatmentKitPrescriptionMobileAdapter.OnCommandClickListener {
                    override fun onCommandClick(position: Int, Command: String) {
                        commandItem(position, Command, "prescription")
                    }
                })
        }

        binding?.saveTreatmentKit!!.setOnClickListener {

            trackTreatmentAnalyticsSaveStart(utils!!.getEncounterType())

            if (isTablet(requireContext())) {
                if ((labAdapter as TreatmentKitLabAdapter).getItems().size > 1 ||
                    (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).getAll().size > 1 ||
                    (treatmentDiagnosisAdapter as TreatmentKitDiagnosisAdapter).getall().size > 1 ||
                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).getItems().size > 1 ||
                    (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).getItems().size > 1
                ) {
                    val dialog = SaveOrderTreatmentFragment(AppConstants.SAVE_AS_TEMPLATE)
                    val ft = childFragmentManager.beginTransaction()
                    val bundle = Bundle()

                    val saveArrayLab = (labAdapter as TreatmentKitLabAdapter).getItems()
                    bundle.putParcelableArrayList(AppConstants.RESPONSECONTENT, saveArrayLab)

                    val saveArrayDiagnosis =
                        (treatmentDiagnosisAdapter as TreatmentKitDiagnosisAdapter).getall()

                    bundle.putParcelableArrayList(
                        AppConstants.DIAGNISISRESPONSECONTENT,
                        saveArrayDiagnosis
                    )


                    val savePrescription =
                        (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).getItems()
                    bundle.putParcelableArrayList(
                        AppConstants.PRESCRIPTIONRESPONSECONTENT,
                        savePrescription
                    )


                    val saveRadiology =
                        (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).getAll()
                    bundle.putParcelableArrayList(
                        AppConstants.RADIOLOGYRESPONSECONTENT,
                        saveRadiology
                    )

                    val saveInvestigation =
                        (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).getItems()
                    bundle.putParcelableArrayList(
                        AppConstants.INVESTIGATIONRESPONSECONTENT,
                        saveInvestigation
                    )
                    bundle.putInt("key", 0)
                    dialog.arguments = bundle
                    dialog.show(ft, "Tag")

                } else {
                    Toast.makeText(activity, "Enter any one widget details", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                if ((labAdapter as TreatmentKitLabMobileAdapter).getItems().size > 0 ||
                    (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).getAll().size > 0 ||
                    (treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter).getall().size > 0 ||
                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).getItems().size > 0 ||
                    (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).getItems().size > 0
                ) {
                    val dialog = SaveOrderTreatmentFragment(AppConstants.SAVE_AS_TEMPLATE)
                    val ft = childFragmentManager.beginTransaction()
                    val bundle = Bundle()

                    val saveArrayLab = (labAdapter as TreatmentKitLabMobileAdapter).getItems()
                    bundle.putParcelableArrayList(AppConstants.RESPONSECONTENT, saveArrayLab)

                    val saveArrayDiagnosis =
                        (treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter).getall()

                    bundle.putParcelableArrayList(
                        AppConstants.DIAGNISISRESPONSECONTENT,
                        saveArrayDiagnosis
                    )

                    val savePrescription =
                        (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).getItems()
                    bundle.putParcelableArrayList(
                        AppConstants.PRESCRIPTIONRESPONSECONTENT,
                        savePrescription
                    )

                    val saveRadiology =
                        (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).getAll()
                    bundle.putParcelableArrayList(
                        AppConstants.RADIOLOGYRESPONSECONTENT,
                        saveRadiology
                    )

                    val saveInvestigation =
                        (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).getItems()
                    bundle.putParcelableArrayList(
                        AppConstants.INVESTIGATIONRESPONSECONTENT,
                        saveInvestigation
                    )
                    bundle.putInt("key", 0)
                    dialog.arguments = bundle
                    dialog.show(ft, "Tag")
                } else {
                    Toast.makeText(activity, "Enter any one widget details", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        binding?.saveandOrder!!.setOnClickListener {

            if (isTablet(requireContext())) {
                if ((labAdapter as TreatmentKitLabAdapter).getItems().size > 1 ||
                    (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).getAll().size > 1 ||
                    (treatmentDiagnosisAdapter as TreatmentKitDiagnosisAdapter).getall().size > 1 ||
                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).getItems().size > 1 ||
                    (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).getItems().size > 1
                ) {
                    val dialog = SaveOrderTreatmentFragment(AppConstants.SAVE_AND_ORDER)
                    val ft = childFragmentManager.beginTransaction()
                    val bundle = Bundle()

                    val saveArrayLab = (labAdapter as TreatmentKitLabAdapter).getItems()
                    bundle.putParcelableArrayList(AppConstants.RESPONSECONTENT, saveArrayLab)

                    val saveArrayDiagnosis =
                        (treatmentDiagnosisAdapter as TreatmentKitDiagnosisAdapter).getall()
                    bundle.putParcelableArrayList(
                        AppConstants.DIAGNISISRESPONSECONTENT,
                        saveArrayDiagnosis
                    )

                    val savePrescription =
                        (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).getItems()
                    bundle.putParcelableArrayList(
                        AppConstants.PRESCRIPTIONRESPONSECONTENT,
                        savePrescription
                    )

                    val saveRadiology =
                        (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).getAll()
                    bundle.putParcelableArrayList(
                        AppConstants.RADIOLOGYRESPONSECONTENT,
                        saveRadiology
                    )

                    val saveInvestigation =
                        (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).getItems()
                    bundle.putParcelableArrayList(
                        AppConstants.INVESTIGATIONRESPONSECONTENT,
                        saveInvestigation
                    )

                    bundle.putInt("key", 1)
                    dialog.arguments = bundle
                    dialog.show(ft, "Tag")

                } else {
                    Toast.makeText(activity, "Enter any one widget details", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                if ((labAdapter as TreatmentKitLabMobileAdapter).getItems().size > 0 ||
                    (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).getAll().size > 0 ||
                    (treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter).getall().size > 0 ||
                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).getItems().size > 0 ||
                    (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).getItems().size > 0
                ) {
                    val dialog = SaveOrderTreatmentFragment(AppConstants.SAVE_AND_ORDER)
                    val ft = childFragmentManager.beginTransaction()
                    val bundle = Bundle()

                    val saveArrayLab = (labAdapter as TreatmentKitLabMobileAdapter).getItems()
                    bundle.putParcelableArrayList(AppConstants.RESPONSECONTENT, saveArrayLab)

                    val saveArrayDiagnosis =
                        (treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter).getall()
                    bundle.putParcelableArrayList(
                        AppConstants.DIAGNISISRESPONSECONTENT,
                        saveArrayDiagnosis
                    )

                    val savePrescription =
                        (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).getItems()
                    bundle.putParcelableArrayList(
                        AppConstants.PRESCRIPTIONRESPONSECONTENT,
                        savePrescription
                    )

                    val saveRadiology =
                        (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).getAll()
                    bundle.putParcelableArrayList(
                        AppConstants.RADIOLOGYRESPONSECONTENT,
                        saveRadiology
                    )

                    val saveInvestigation =
                        (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).getItems()
                    bundle.putParcelableArrayList(
                        AppConstants.INVESTIGATIONRESPONSECONTENT,
                        saveInvestigation
                    )

                    bundle.putInt("key", 1)
                    dialog.arguments = bundle
                    dialog.show(ft, "Tag")

                } else {
                    Toast.makeText(activity, "Enter any one widget details", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        binding?.ordercardView!!.setOnClickListener {
            if (isTablet(requireContext())) {
                trackTreatmentOrderStart(utils!!.getEncounterType())
                if ((treatmentDiagnosisAdapter as TreatmentKitDiagnosisAdapter).getall().size > 1) {
                    if ((labAdapter as TreatmentKitLabAdapter).getItems().size > 1 ||
                        (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).getAll().size > 1 ||
                        (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).getItems().size > 1 ||
                        (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).getItems().size > 1
                    ) {
                        viewModel?.getEncounter(
                            facility_id,
                            patient_id,
                            encounter_type!!,
                            fetchEncounterRetrofitCallBack
                        )
                    } else {
                        Toast.makeText(
                            activity,
                            "Enter Any One From Lab,Prescription,Investigation,Radiology..!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(activity, "Enter One Diagnosis..!", Toast.LENGTH_LONG).show()
                }
            } else {
                trackTreatmentOrderStart(utils!!.getEncounterType())
                if ((treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter).getall().size > 0) {

                    if ((labAdapter as TreatmentKitLabMobileAdapter).getItems().size > 0 ||
                        (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).getAll().size > 0 ||
                        (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).getItems().size > 0 ||
                        (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).getItems().size > 0
                    ) {
                        viewModel?.getEncounter(
                            facility_id,
                            patient_id,
                            encounter_type!!,
                            fetchEncounterRetrofitCallBack
                        )
                    } else {
                        Toast.makeText(
                            activity,
                            "Enter Any One From Lab,Prescription,Investigation,Radiology..!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(activity, "Enter One Diagnosis..!", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding?.clearCardView?.setOnClickListener {
            clearAllFields()
        }

        binding?.searchView?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length ?: 0 in 3 until 5) {
                    searchName = p0?.toString() ?: ""
                    callAutoSearch()
                }
            }
        })

        binding?.searchView?.setOnItemClickListener { adapterView, view, i, l ->
            var treatmentId = 0
            autoSearchRespContents.forEach {
                if (it.treatment_name == binding?.searchView?.text?.toString()) {
                    treatmentId = it.treatment_kit_id ?: 0
                }
            }
            callSearch(treatmentId)
        }

        binding?.llAddDiagnosis?.setOnClickListener {
            if (binding?.llAddDiagnosisDetail?.isvisible()!!) {
                hideDetailLayout(
                    requireContext(),
                    binding?.llAddDiagnosisDetail!!,
                    binding?.imgDownDiagnosis!!
                )
            } else {
                showDetailLayout(
                    requireContext(),
                    binding?.llAddDiagnosisDetail!!,
                    binding?.imgDownDiagnosis!!
                )
            }
        }

        binding?.llAddPrescription?.setOnClickListener {
            if (binding?.addMedicineDetailLayout?.isvisible()!!) {
                hideDetailLayout(
                    requireContext(),
                    binding?.addMedicineDetailLayout!!,
                    binding?.imgDownPrescription!!
                )
            } else {
                showDetailLayout(
                    requireContext(),
                    binding?.addMedicineDetailLayout!!,
                    binding?.imgDownPrescription!!
                )
            }
        }

        binding?.llAddLab?.setOnClickListener {
            if (binding?.llAddLabDetail?.isvisible()!!) {
                hideDetailLayout(requireContext(), binding?.llAddLabDetail!!, binding?.imgDownLab!!)
            } else {
                showDetailLayout(
                    requireContext(),
                    binding?.llAddLabDetail!!,
                    binding?.imgDownLab!!
                )
            }
        }

        binding?.llAddRadiology?.setOnClickListener {
            if (binding?.llAddRadiologyDetail?.isvisible()!!) {
                hideDetailLayout(
                    requireContext(),
                    binding?.llAddRadiologyDetail!!,
                    binding?.imgDownRadiology!!
                )
            } else {
                showDetailLayout(
                    requireContext(),
                    binding?.llAddRadiologyDetail!!,
                    binding?.imgDownRadiology!!
                )
            }
        }

        binding?.llAddInvestigation?.setOnClickListener {
            if (binding?.llAddInvestigationDetail?.isvisible()!!) {
                hideDetailLayout(
                    requireContext(),
                    binding?.llAddInvestigationDetail!!,
                    binding?.imgDownInvestigation!!
                )
            } else {
                showDetailLayout(
                    requireContext(),
                    binding?.llAddInvestigationDetail!!,
                    binding?.imgDownInvestigation!!
                )
            }
        }

        binding?.actvRadiologyCode?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 2) {
                    val body = GetRadiologyCodeSearchReq(s.toString())
                    viewModel?.getRadiologyCodeSearch(
                        facility_id,
                        body,
                        getRadiologyCodeSearchRespCallback
                    )
                }
            }
        })

        binding?.actvRadiologyTestName?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length ?: 0 > 2) {
                    viewModel?.getRadioSearchResult(
                        facility_id,
                        p0?.toString() ?: "",
                        getRadiologyComplaintSearchRetrofitCallBack
                    )
                }
            }

        })

        binding?.actvInvestigationCode?.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0?.length ?: 0 > 2) {
                        val body = GetInvestigationCodeSearchReq(p0?.toString() ?: "")
                        viewModel?.getInvestigationCodeSearch(
                            facility_id,
                            body,
                            getInvestigationCodeSearchRespCallback
                        )
                    }
                }
            }
        )

        binding?.actvInvestigationTestName?.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0?.length ?: 0 > 2) {
                        viewModel?.getInvestigationComplaintSearchResult(
                            facility_id,
                            p0?.toString() ?: "",
                            getInvestigationComplaintSearchRetrofitCallBack
                        )
                    }
                }
            }
        )
    }

    private fun clearAllFields() {
        mCallbackTreatmentKitFavFragment?.ClearAllData()
        binding?.searchView?.setText("")
        if (isTablet(requireContext())) {
            (treatmentDiagnosisAdapter as TreatmentKitDiagnosisAdapter).clearall()
            (treatmentDiagnosisAdapter as TreatmentKitDiagnosisAdapter).addRow(FavouritesModel())

            (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).clearall()
            (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).addRow(FavouritesModel())

            (labAdapter as TreatmentKitLabAdapter).clearall()
            (labAdapter as TreatmentKitLabAdapter).addRow(FavouritesModel())

            (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).clearall()
            (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).addRow(FavouritesModel())

            (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).clearall()
            (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).addRow(
                FavouritesModel()
            )
        } else {
            (treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter).clearall()

            (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).clearall()

            (labAdapter as TreatmentKitLabMobileAdapter).clearall()

            (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).clearall()

            (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).clearall()

            diagnosisClearAll()
            clearPrescriptionList()
            clearLabAddData()
            clearRadiology()
            clearInvestigation()
        }
    }

    private fun setupViewPager(viewPager: ViewPager) {

        val adapter = ViewPagerAdapter(childFragmentManager)
        treatmentKitFavouriteFragment = TreatmentKitFavouriteFragment()
        prevTreatmentKitFragment = PrevTreatmentKitFragment()
        adapter.addFragment(treatmentKitFavouriteFragment!!, "Favourite")
        adapter.addFragment(prevTreatmentKitFragment!!, "Prev. Kit")

        viewPager.adapter = adapter

    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) :
        FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val mFragmentList = java.util.ArrayList<Fragment>()
        private val mFragmentTitleList = java.util.ArrayList<String>()
        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }

    private fun callAutoSearch() {
        val body = AutoSearchReq(
            departmentId = deparment_UUID?.toString() ?: "",
            pageNo = 0,
            paginationSize = 100,
            searchKey = "filterbythree",
            searchValue = searchName
        )
        viewModel?.treatmentKitAutoSearch(facility_id, body, autoSearchRespCallback)
    }

    private fun callSearch(treatmentId: Int) {
        viewModel?.treatmentKitFavourite(
            facility_uuid = facility_id,
            treatmentId = treatmentId,
            favouriteId = "undefined",
            treatmentKitFavouriteRespCallback = treatmentKitFavouriteRespCallback
        )
    }

    private fun setSearchAdapter() {
        if (autoSearchRespContents.isNotEmpty()) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                autoSearchRespContents
            )
            binding?.searchView?.setAdapter(adapter)
            binding?.searchView?.showDropDown()
        }
    }

    private fun setDiagnosisAdapter() {
        if (isTablet(requireContext())) {

            treatmentDiagnosisAdapter =
                TreatmentKitDiagnosisAdapter(requireActivity(), ArrayList())
            binding?.saveDiagonosisRecyclerView?.adapter =
                treatmentDiagnosisAdapter as TreatmentKitDiagnosisAdapter
            (treatmentDiagnosisAdapter as TreatmentKitDiagnosisAdapter).addRow(FavouritesModel())

            (treatmentDiagnosisAdapter as TreatmentKitDiagnosisAdapter).setOnDeleteClickListener(
                object :
                    TreatmentKitDiagnosisAdapter.OnDeleteClickListener {
                    override fun onDeleteClick(favouritesModel: FavouritesModel?, position: Int) {
                        customdialog = Dialog(requireContext())
                        customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        customdialog!!.setCancelable(false)
                        customdialog!!.setContentView(R.layout.delete_cutsom_layout)
                        val closeImageView =
                            customdialog!!.findViewById(R.id.closeImageView) as ImageView

                        closeImageView.setOnClickListener {
                            customdialog?.dismiss()
                        }
                        drugName = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                        drugName.text =
                            "${drugName.text} '" + favouritesModel?.diagnosis_name + "(" + favouritesModel?.diagnosis_code + ")" + "' Record ?"
                        val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                        val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                        yesBtn.setOnClickListener {
                            (treatmentDiagnosisAdapter as TreatmentKitDiagnosisAdapter).deleteRow(
                                favouritesModel,
                                position
                            )

                            if (favouritesModel?.viewDiagnosisstatus == 1) {
                                favouritesModel.isSelected = false
                                mCallbackTreatmentKitFavFragment?.ClearFavParticularPosition(
                                    favouritesModel.isFavposition
                                )
                            }

                            customdialog!!.dismiss()
                            Toast.makeText(
                                requireContext(),
                                "${favouritesModel?.diagnosis_name} Deleted Successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                        noBtn.setOnClickListener {
                            customdialog!!.dismiss()
                        }
                        customdialog!!.show()

                    }
                })

            (treatmentDiagnosisAdapter as TreatmentKitDiagnosisAdapter).setOnSearchInitiatedListener(
                object : TreatmentKitDiagnosisAdapter.OnSearchDignisisInitiatedListener {
                    override fun onSearchInitiated(
                        query: String,
                        view: AppCompatAutoCompleteTextView,
                        position: Int,
                        search: Int
                    ) {
                        selectedSearchPosition = position
                        dropdownReferenceView = view
                        diagSearch = search
                        viewModel?.getDiagnosisComplaintSearchResult(
                            facility_id,
                            query,
                            getDignosisSearchRetrofitCallBack
                        )
                    }
                })
        } else {
            treatmentDiagnosisAdapter =
                TreatmentKitDiagnosisMobileAdapter(requireActivity(), ArrayList())
            binding?.saveDiagonosisRecyclerView?.adapter =
                treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter

            diagnosisInitView()

            (treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter).setOnDeleteClickListener(
                object : TreatmentKitDiagnosisMobileAdapter.OnDeleteClickListener {
                    override fun onDeleteClick(favouritesModel: DiagnosisListData?, position: Int) {

                        (treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter).deleteRowFromFavourites(
                            favouritesModel,
                            position
                        )
                    }
                })

            (treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter).onItemClick(
                object : TreatmentKitDiagnosisMobileAdapter.OnSelectItemCommunication {
                    override fun onClick(
                        position: Int,
                        selectedItem: Boolean,
                        favouritesModel: DiagnosisListData?
                    ) {
                        (treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter).updateSelectStatus(
                            position,
                            selectedItem
                        )

                        TKtDiagnosisAddNew = setDiagnosisNewData(favouritesModel)

                        binding?.actvDiagnosisCode?.setText(TKtDiagnosisAddNew.diagnosis_code)

                        binding?.actvDiagnosis?.setText(TKtDiagnosisAddNew.diagnosis_name)

                        if (selectedItem) {
                            diagnosisClearAll()
                            hideDetailLayout(
                                requireContext(),
                                binding?.llAddDiagnosisDetail!!,
                                binding?.imgDownDiagnosis!!
                            )
                            isDiagnosisUpdate = false
                        } else {

                            isDiagnosisUpdate = true
                            diagnosisPosition = position
                            showDetailLayout(
                                requireContext(),
                                binding?.llAddDiagnosisDetail!!,
                                binding?.imgDownDiagnosis!!
                            )
                        }
                    }
                })
        }
    }

    private fun diagnosisInitView() {

        binding?.actvDiagnosis?.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                if (s.length > 2 && s.length < 5) {

                    diagSearch = 1

                    viewModel?.getComplaintSearchResult(
                        s.toString(),
                        facility_id,
                        getDignosisSearchRetrofitCallBack
                    )
                }
            }
        })

        binding?.actvDiagnosisCode?.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                if (s.length > 2 && s.length < 5) {

                    diagSearch = 2

                    viewModel?.getComplaintSearchResult(
                        s.toString(),
                        facility_id,
                        getDignosisSearchRetrofitCallBack
                    )


                }
            }
        })


        binding?.cvDiagnosisAdd?.setOnClickListener {

            if (TKtDiagnosisAddNew.diagnosis_id == "") {

                Toast.makeText(requireContext(), "Please select fill all items", Toast.LENGTH_SHORT)
                    .show()
            }

            if (isDiagnosisUpdate) {

                (treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter).UpdateRow(
                    diagnosisPosition,
                    setDiagnosisListData(TKtDiagnosisAddNew)
                )

                isDiagnosisUpdate = false

                hideDetailLayout(
                    requireContext(),
                    binding?.llAddDiagnosisDetail!!,
                    binding?.imgDownDiagnosis!!
                )
                diagnosisClearAll()

            } else {

                (treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter).addRow(
                    setDiagnosisListData(TKtDiagnosisAddNew)
                )

                hideDetailLayout(
                    requireContext(),
                    binding?.llAddDiagnosisDetail!!,
                    binding?.imgDownDiagnosis!!
                )

                diagnosisClearAll()

            }

            if (isModify) {
                val newDiagnosi = NewDiagnosi(
                    body_site_uuid = 0,
                    category_uuid = 0,
                    condition_status_uuid = 0,
                    condition_type_uuid = 0,
                    consultation_uuid = 0,
                    department_uuid = treatmentKitResponsResponseContent.department_id.toString(),
                    diagnosis_uuid = treatmentKitResponsResponseContent.diagnosis.get(0).diagnosis_id,
                    encounter_doctor_uuid = treatmentKitResponsResponseContent.encounter_type_uuid.toString(),
                    encounter_type_uuid = treatmentKitResponsResponseContent.encounter_type,
                    encounter_uuid = treatmentKitResponsResponseContent.encounter_type_uuid.toString(),
                    facility_uuid = facility_id.toString(),
                    grade_uuid = 0,
                    patient_treatment_uuid = 0,
                    patient_uuid = treatmentKitResponsResponseContent.patient_id.toString(),
                    prescription_uuid = 0,
                    treatment_kit_uuid = treatmentKitResponsResponseContent.treatment_kit_uuid,
                    type_uuid = 0
                )
                listNewDiagnosis.add(newDiagnosi)
            }
        }


        binding?.cvDiagnosisClear?.setOnClickListener {

            diagnosisClearAll()
        }


    }

    private fun setDiagnosisNewData(responseContent: DiagnosisListData?): DiagnosisNewData {


        return DiagnosisNewData(

            drug_active = responseContent!!.drug_active,
            diagnosis_description = responseContent.diagnosis_description,
            diagnosis_code = responseContent.diagnosis_code,
            diagnosis_id = responseContent.diagnosis_id,
            diagnosis_name = responseContent.diagnosis_name,
            diagnosisUUiD = responseContent.diagnosisUUiD,
            favourite_active = responseContent.favourite_active!!,
            favourite_code = responseContent.favourite_code,
            favourite_type_id = responseContent.favourite_type_id,
            isModifiy = responseContent.isModifiy,
            isReadyForSave = responseContent.isReadyForSave,
            viewDiagnosisstatus = responseContent.viewDiagnosisstatus,
            isFavposition = responseContent.isFavposition,
            is_snomed = responseContent.is_snomed,
            commands = responseContent.commands
        )
    }

    private fun setDiagnosisListData(responseContent: DiagnosisNewData?): DiagnosisListData {


        return DiagnosisListData(

            drug_active = responseContent!!.drug_active,
            diagnosis_description = responseContent.diagnosis_description,
            diagnosis_code = responseContent.diagnosis_code,
            diagnosis_id = responseContent.diagnosis_id,
            diagnosis_name = responseContent.diagnosis_name,
            diagnosisUUiD = responseContent.diagnosisUUiD,
            favourite_active = responseContent.favourite_active!!,
            favourite_code = responseContent.favourite_code,
            favourite_type_id = responseContent.favourite_type_id,
            isModifiy = responseContent.isModifiy,
            isReadyForSave = responseContent.isReadyForSave,
            viewDiagnosisstatus = responseContent.viewDiagnosisstatus,
            isFavposition = responseContent.isFavposition,
            is_snomed = responseContent.is_snomed,
            commands = responseContent.commands
        )
    }

    fun diagnosisClearAll() {

        TKtDiagnosisAddNew = DiagnosisNewData()

        binding?.actvDiagnosis?.setText("")
        binding?.actvDiagnosisCode?.setText("")

    }

    private fun setPrescriptionAdapter() {
        if (isTablet(requireContext())) {
            treatmentprescriptionAdapter =
                TreatmentKitPrescriptionAdapter(requireActivity(), ArrayList())
            binding?.savePrescriptionRecyclerView?.adapter =
                (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter)
            (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).addRow(
                FavouritesModel()
            )
            (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).setOnDeleteClickListener(
                object :
                    TreatmentKitPrescriptionAdapter.OnDeleteClickListener {
                    override fun onDeleteClick(responseContent: FavouritesModel?, position: Int) {

                        customdialog = Dialog(requireContext())
                        customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        customdialog!!.setCancelable(false)
                        customdialog!!.setContentView(R.layout.delete_cutsom_layout)
                        val closeImageView =
                            customdialog!!.findViewById(R.id.closeImageView) as ImageView

                        closeImageView.setOnClickListener {
                            customdialog?.dismiss()
                        }
                        drugName = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                        drugName.text =
                            "${drugName.text} '" + responseContent?.itemAppendString + "(" + responseContent?.drug_code + ")" + "' Record ?"
                        val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                        val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                        yesBtn.setOnClickListener {
                            (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).deleteRow(
                                position
                            )
                            customdialog!!.dismiss()
                        }
                        noBtn.setOnClickListener {
                            customdialog!!.dismiss()
                        }
                        customdialog!!.show()

                    }
                })
            (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).setOnSearchInitiatedListener(
                object : TreatmentKitPrescriptionAdapter.OnSearchInitiatedListener {
                    override fun onSearchInitiated(
                        query: String,
                        view: AppCompatAutoCompleteTextView,
                        position: Int
                    ) {
                        dropdownReferenceView = view
                        selectedSearchPosition = position
                        viewModel?.getPrescriptionComplaintSearchResult(
                            query,
                            getPresriptionComplaintSearchRetrofitCallBack,
                            facility_id
                        )
                    }
                })
            viewModel?.getPrescriptionDuration(getPrescriptionDurationRetrofitCallBack, facility_id)
        } else {

            prescriptionInitViews()
            treatmentprescriptionAdapter =
                TreatmentKitPrescriptionMobileAdapter(requireActivity(), this, ArrayList())


            binding?.savePrescriptionRecyclerView?.adapter =
                (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter)


            (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).setOnRefreshClickListener(
                object : TreatmentKitPrescriptionMobileAdapter.OnRefreshClickListener {
                    override fun onRefreshClick() {


                        clearPrescriptionList()

                        hideDetailLayout(
                            requireContext(),
                            binding?.addMedicineDetailLayout!!,
                            binding?.imgDownPrescription!!
                        )

                    }
                })



            (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).setOnItemClickListener(
                object : TreatmentKitPrescriptionMobileAdapter.OnItemClickListener {
                    override fun onItemClick(
                        responseContent: TKtPrescriptionListData?,
                        position: Int
                    ) {

                        clearPrescriptionList()

                        showDetailLayout(
                            requireContext(),
                            binding?.addMedicineDetailLayout!!,
                            binding?.imgDownPrescription!!
                        )

                        //                    IsUpdateList=true

                        TKtPrescriptionAddNew = setPresNewData(responseContent)

                        //                    UpdatePosition=position
                        binding?.duration?.setText(TKtPrescriptionAddNew.drug_duration.toString())
                        binding?.PresAutoCompleteTextView?.setText(TKtPrescriptionAddNew.drug_name)
                        binding?.Strength?.setText(TKtPrescriptionAddNew.drug_strength)
                        durationAdapter?.updateSelectStatus(TKtPrescriptionAddNew.drug_period_id!!)

                        setFreqency(freqencyList)

                        for (i in freqencyList.indices) {
                            if (freqencyList[i].uuid == TKtPrescriptionAddNew.drug_frequency_id) {

                                binding?.frequencySpinner?.setSelection(i + 1)
                                break

                            }
                        }

                        setRoute(RouteList)
                        for (i in RouteList.indices) {
                            if (RouteList[i].uuid == TKtPrescriptionAddNew.drug_route_id) {
                                binding?.routeSpinner?.setSelection(i + 1)
                                break
                            }
                        }

                        if (TKtPrescriptionAddNew.drug_is_emar!!) {

                            setInjection(InjectionList)
                            for (i in InjectionList.indices) {
                                if (InjectionList[i].uuid == TKtPrescriptionAddNew.drug_instruction_id) {
                                    binding?.instructionSpinner?.setSelection(i + 1)
                                    break
                                }
                            }


                        } else {
                            setInstruction(InstructionList)
                            for (i in InstructionList.indices) {
                                if (InstructionList[i].uuid == TKtPrescriptionAddNew.drug_instruction_id) {
                                    binding?.instructionSpinner?.setSelection(i + 1)
                                    break
                                }
                            }

                        }

                    }
                })

            (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).setOnDeleteClickListener(
                object : TreatmentKitPrescriptionMobileAdapter.OnDeleteClickListener {
                    override fun onDeleteClick(
                        responseContent: TKtPrescriptionListData?,
                        position: Int
                    ) {

                        val check =
                            (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).deleteRow(
                                position
                            )


                    }
                })

        }
    }

    private fun setLabAdapter() {
        if (isTablet(requireContext())) {
            labAdapter = TreatmentKitLabAdapter(requireActivity(), ArrayList(), ArrayList())
            binding?.savelabRecyclerView?.adapter = (labAdapter as TreatmentKitLabAdapter)

            (labAdapter as TreatmentKitLabAdapter).addRow(FavouritesModel())
            (labAdapter as TreatmentKitLabAdapter).addTempleteRow(TempDetails())

            (labAdapter as TreatmentKitLabAdapter).setOnDeleteClickListener(
                object : TreatmentKitLabAdapter.OnDeleteClickListener {
                    override fun onDeleteClick(responseContent: FavouritesModel?, position: Int) {

                        customdialog = Dialog(requireContext())
                        customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        customdialog!!.setCancelable(false)
                        customdialog!!.setContentView(R.layout.delete_cutsom_layout)
                        val closeImageView =
                            customdialog!!.findViewById(R.id.closeImageView) as ImageView

                        closeImageView.setOnClickListener {
                            customdialog?.dismiss()
                        }
                        drugName = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                        drugName.text =
                            "${drugName.text} '" + responseContent?.itemAppendString + "(" + responseContent?.test_master_code + ")" + "' Record ?"
                        val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                        val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                        yesBtn.setOnClickListener {
                            val check = (labAdapter as TreatmentKitLabAdapter).deleteRow(position)
                            if (responseContent?.viewLabstatus == 1) {
                                mCallbackTreatmentKitFavFragment?.ClearFavParticularPosition(
                                    responseContent.isFavposition
                                )

                            }
                            customdialog!!.dismiss()


                        }
                        noBtn.setOnClickListener {
                            customdialog!!.dismiss()
                        }
                        customdialog!!.show()


                    }
                })
            (labAdapter as TreatmentKitLabAdapter).setOnSearchInitiatedListener(object :
                TreatmentKitLabAdapter.OnSearchInitiatedListener {
                override fun onSearchInitiated(
                    query: String,
                    view: AppCompatAutoCompleteTextView,
                    position: Int,
                    search: Int,
                    spinnerToLocation: Spinner
                ) {
                    dropdownReferenceView = view
                    searchposition = position
                    labSearch = search
                    SpinnerToLocation = spinnerToLocation
                    viewModel?.getLabComplaintSearchResult(
                        facility_id,
                        query,
                        getLabComplaintSearchRetrofitCallBack
                    )
                }
            })

            (labAdapter as TreatmentKitLabAdapter).setOnListItemClickListener(
                object : TreatmentKitLabAdapter.OnListItemClickListener {
                    override fun onListItemClick(
                        responseContent: FavouritesModel?,
                        position: Int,
                        spinnerToLocation: Spinner
                    ) {
                        SpinnerToLocation = spinnerToLocation
                        searchposition = position
                        viewModel?.getToLocationTest(
                            getLabToLoctionTestRetrofitCallback,
                            facility_id,
                            deparment_UUID,
                            responseContent!!.test_master_id
                        )

                    }
                })

        } else {
            initCompleteCompleteTextView()
            initLabAddView()
            setLabSpinnerClickListener()
            labAdapter = TreatmentKitLabMobileAdapter(requireActivity(), ArrayList())
            binding?.savelabRecyclerView?.adapter = (labAdapter as TreatmentKitLabMobileAdapter)

            (labAdapter as TreatmentKitLabMobileAdapter).setOnDeleteClickListener(
                object : TreatmentKitLabMobileAdapter.OnDeleteClickListener {
                    override fun onDeleteClick(responseContent: FavouritesModel?, position: Int) {
                        (labAdapter as TreatmentKitLabMobileAdapter).deleteRow(
                            position
                        )
                    }
                }
            )

            (labAdapter as TreatmentKitLabMobileAdapter).onItemClick(
                object : TreatmentKitLabMobileAdapter.OnSelectItemCommunication {
                    override fun onClick(
                        position: Int,
                        selectedItem: Boolean,
                        favouritesModel: FavouritesModel?
                    ) {
                        clearLabAddData()
                        (labAdapter as TreatmentKitLabMobileAdapter).updateSelectStatus(
                            position,
                            selectedItem
                        )
                        editLabItem(position, favouritesModel)

                    }

                })
        }
    }

    private fun setRadiologyAdapter() {
        if (isTablet(requireContext())) {
            treatmentRadiologyAdapter =
                TreatmentKitRadiologyAdapter(
                    requireActivity(),
                    ArrayList(), ArrayList()
                )
            binding?.saveRadiologyRecyclerView?.adapter =
                (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter)
            //expandableListView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
            binding?.saveRadiologyRecyclerView?.descendantFocusability =
                ViewGroup.FOCUS_BEFORE_DESCENDANTS

            (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).addRow(FavouritesModel())
            (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).addTempleteRow(TempDetails())



            (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).setOnDeleteClickListener(
                object :
                    TreatmentKitRadiologyAdapter.OnDeleteClickListener {
                    override fun onDeleteClick(responseContent: FavouritesModel?, position: Int) {

                        customdialog = Dialog(requireContext())
                        customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        customdialog!!.setCancelable(false)
                        customdialog!!.setContentView(R.layout.delete_cutsom_layout)
                        val closeImageView =
                            customdialog!!.findViewById(R.id.closeImageView) as ImageView

                        closeImageView.setOnClickListener {
                            customdialog?.dismiss()
                        }
                        drugName = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                        drugName.text =
                            "${drugName.text} '" + responseContent?.itemAppendString + "(" + responseContent?.test_master_code + ")" + "' Record ?"
                        val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                        val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                        yesBtn.setOnClickListener {

                            (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).deleteRow(
                                position
                            )

                            //mCallbacktreatmentKitFavFragment?.ClearFavParticularPosition(responseContent.isFavposition)


                            customdialog!!.dismiss()


                        }
                        noBtn.setOnClickListener {
                            customdialog!!.dismiss()
                        }
                        customdialog!!.show()
                    }
                })
            (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).setOnSearchInitiatedListener(
                object :
                    TreatmentKitRadiologyAdapter.OnSearchInitiatedListener {
                    override fun onSearchInitiated(
                        query: String,
                        view: AppCompatAutoCompleteTextView,
                        position: Int,
                        search: Int
                    ) {
                        dropdownReferenceView = view
                        searchPosition = position
                        radioSearch = search
                        viewModel?.getRadioSearchResult(
                            facility_id,
                            query,
                            getRadiologyComplaintSearchRetrofitCallBack
                        )
                    }
                })
        } else {
            initRadiologyAddView()
            setRadiologySpinnerClickListiner()
            treatmentRadiologyAdapter = TreatmentKitRadiologyMobileAdapter(
                requireActivity(),
                ArrayList(),
                ArrayList()
            )
            binding?.saveRadiologyRecyclerView?.adapter =
                (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter)

            (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).setOnDeleteClickListener(
                object : TreatmentKitRadiologyMobileAdapter.OnDeleteClickListener {
                    override fun onDeleteClick(responseContent: FavouritesModel?, position: Int) {
                        (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).deleteRow(
                            position
                        )
                    }
                }
            )

            (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).onItemClick(
                object : TreatmentKitRadiologyMobileAdapter.OnSelectItemCommunication {
                    override fun onClick(
                        position: Int,
                        selectedItem: Boolean,
                        favouritesModel: FavouritesModel?
                    ) {
                        clearRadiology()
                        (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).updateSelectStatus(
                            position,
                            selectedItem
                        )
                        editRadiologyItem(position, favouritesModel)

                    }

                })
        }
    }

    private fun initRadiologyAddView() {
        binding?.cvRadiologyAdd?.setOnClickListener {
            if (binding?.actvRadiologyCode?.text?.isEmpty()!! || binding?.actvRadiologyTestName?.text?.isEmpty()!!) {
                Toast.makeText(requireContext(), "Please Select TestName ", Toast.LENGTH_SHORT)
                    .show()
            } else if (selectedRadiologyData?.selectTypeUUID == 0) {
                Toast.makeText(requireContext(), "Please Select Priority", Toast.LENGTH_SHORT)
                    .show()
            } else if (selectedRadiologyData?.selectToLocationUUID == 0) {
                Toast.makeText(
                    requireContext(),
                    "Please Select Order To Location",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (selectedRadiologyData?.isEditableMode!!) {
                    selectedRadiologyData?.isEditableMode = false
                    selectedRadiologyData?.radiologyDataSelected = false
                    try {
                        radiologyMobileDataArrayList?.set(
                            radiologyModifyPostion!!,
                            selectedRadiologyData
                        )
                    } catch (e: Exception) {
                        radiologyMobileDataArrayList?.add(
                            radiologyModifyPostion!!,
                            selectedRadiologyData
                        )
                    }
                    (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).updateAdapter(
                        radiologyMobileDataArrayList!!
                    )
                    clearRadiology()
                    hideDetailLayout(
                        requireContext(),
                        binding?.llAddRadiologyDetail!!,
                        binding?.imgDownRadiology!!
                    )
                    if (isModify) {
                        val patientRadiologyDetail = NewDetailX(
                            doctor_uuid = treatmentKitResponsResponseContent.doctor_id.toString(),
                            encounter_type_uuid = treatmentKitResponsResponseContent.encounter_type,
                            encounter_uuid = treatmentKitResponsResponseContent.encounter_type_uuid.toString(),
                            from_department_uuid = treatmentKitResponsResponseContent.department_id.toString(),
                            is_ordered = true,
                            is_profile = false,
                            lab_master_type_uuid = 1,
                            order_priority_uuid = "2",
                            order_status_uuid = 1,
                            patient_uuid = treatmentKitResponsResponseContent.patient_id.toString(),
                            patient_work_order_by = 1,
                            sample_type_uuid = 23,
                            test_master_uuid = 1335,
                            to_department_uuid = 0,
                            to_location_uuid = 2668,
                            to_sub_department_uuid = 0,
                            type_of_method_uuid = 7
                        )
                        listNewDetailsRadiology.add(patientRadiologyDetail)
                    }
                } else {
                    val check =
                        radiologyMobileDataArrayList?.any { it!!.test_master_id == selectedRadiologyData?.test_master_id }
                    if (!check!!) {
                        radiologyMobileDataArrayList?.add(selectedRadiologyData!!)
                        (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).updateAdapter(
                            radiologyMobileDataArrayList!!
                        )
                        clearRadiology()
                        hideDetailLayout(
                            requireContext(),
                            binding?.llAddRadiologyDetail!!,
                            binding?.imgDownRadiology!!
                        )
                        if (isModify) {
                            val patientRadiologyDetail = NewDetailX(
                                doctor_uuid = treatmentKitResponsResponseContent.doctor_id.toString(),
                                encounter_type_uuid = treatmentKitResponsResponseContent.encounter_type,
                                encounter_uuid = treatmentKitResponsResponseContent.encounter_type_uuid.toString(),
                                from_department_uuid = treatmentKitResponsResponseContent.department_id.toString(),
                                is_ordered = true,
                                is_profile = false,
                                lab_master_type_uuid = 1,
                                order_priority_uuid = "2",
                                order_status_uuid = 1,
                                patient_uuid = treatmentKitResponsResponseContent.patient_id.toString(),
                                patient_work_order_by = 1,
                                sample_type_uuid = 23,
                                test_master_uuid = 1335,
                                to_department_uuid = 0,
                                to_location_uuid = 2668,
                                to_sub_department_uuid = 0,
                                type_of_method_uuid = 7
                            )
                            listExistingRadiology.add(patientRadiologyDetail)
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Already Item available in the list",
                            Toast.LENGTH_LONG
                        )?.show()
                    }
                }
            }
        }

        binding?.cvRadiologyClear?.setOnClickListener {
            clearRadiology()
        }
    }

    private fun setInvestigationAdapter() {
        if (isTablet(requireContext())) {
            treatmentKitInvestigationAdapter =
                TreatmentKitInvestigationAdapter(requireActivity(), ArrayList(), ArrayList())
            binding?.saveInvestigationRecyclerView?.adapter =
                (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter)
            binding?.saveInvestigationRecyclerView?.descendantFocusability =
                ViewGroup.FOCUS_BEFORE_DESCENDANTS
            (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).addRow(
                FavouritesModel()
            )
            //treatmentKitInvestigationAdapter?.addTempleteRow(TempDetails())

            (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).setOnDeleteClickListener(
                object :
                    TreatmentKitInvestigationAdapter.OnDeleteClickListener {
                    override fun onDeleteClick(responseContent: FavouritesModel?, position: Int) {

                        customdialog = Dialog(requireContext())
                        customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        customdialog!!.setCancelable(false)
                        customdialog!!.setContentView(R.layout.delete_cutsom_layout)
                        val closeImageView =
                            customdialog!!.findViewById(R.id.closeImageView) as ImageView

                        closeImageView.setOnClickListener {
                            customdialog?.dismiss()
                        }
                        drugName = customdialog!!.findViewById(R.id.addDeleteName) as TextView
                        drugName.text =
                            "${drugName.text} '" + responseContent?.chief_complaint_name + "(" + responseContent?.chief_complaint_code + ")" + "' Record ?"
                        val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
                        val noBtn = customdialog!!.findViewById(R.id.no) as CardView
                        yesBtn.setOnClickListener {
                            (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).deleteRow(
                                position
                            )
                            customdialog!!.dismiss()
                        }
                        noBtn.setOnClickListener {
                            customdialog!!.dismiss()
                        }
                        customdialog!!.show()
                    }
                })
            (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).setOnSearchInitiatedListener(
                object :
                    TreatmentKitInvestigationAdapter.OnSearchInitiatedListener {
                    override fun onSearchInitiated(
                        query: String,
                        view: AppCompatAutoCompleteTextView,
                        position: Int,
                        search: Int
                    ) {
                        dropdownReferenceView = view
                        searchPosition = position
                        investSearch = search
                        viewModel?.getInvestigationComplaintSearchResult(
                            facility_id,
                            query,
                            getInvestigationComplaintSearchRetrofitCallBack
                        )
                    }
                })
        } else {
            initInvestigationAddView()
            setInvestigationSpinnerClickListener()
            treatmentKitInvestigationAdapter = TreatmentKitInvestigationMobileAdapter(
                requireActivity(),
                ArrayList(),
                ArrayList()
            )
            binding?.saveInvestigationRecyclerView?.adapter =
                (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter)

            (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).onItemClick(
                object : TreatmentKitInvestigationMobileAdapter.OnSelectItemCommunication {
                    override fun onClick(
                        position: Int,
                        selectedItem: Boolean,
                        favouritesModel: FavouritesModel?
                    ) {
                        clearInvestigation()
                        (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).updateSelectStatus(
                            position,
                            selectedItem
                        )
                        editInvestigationItem(position, favouritesModel)
                    }
                })

            (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).setOnDeleteClickListener(
                object : TreatmentKitInvestigationMobileAdapter.OnDeleteClickListener {
                    override fun onDeleteClick(responseContent: FavouritesModel?, position: Int) {
                        (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).deleteRow(
                            position
                        )
                    }

                }
            )
        }
    }

    private fun initInvestigationAddView() {
        binding?.cvInvestigationAdd?.setOnClickListener {
            if (binding?.actvInvestigationCode?.text?.isEmpty()!! || binding?.actvInvestigationTestName?.text?.isEmpty()!!) {
                Toast.makeText(requireContext(), "Please Select TestName ", Toast.LENGTH_SHORT)
                    .show()
            } else if (selectedInvestigationData?.selectTypeUUID == 0) {
                Toast.makeText(requireContext(), "Please Select Priority", Toast.LENGTH_SHORT)
                    .show()
            } else if (selectedInvestigationData?.selectToLocationUUID == 0) {
                Toast.makeText(
                    requireContext(),
                    "Please Select Order To Location",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (selectedInvestigationData?.isEditableMode!!) {
                    selectedInvestigationData?.isEditableMode = false
                    selectedInvestigationData?.investigationDataSelected = false
                    investigationMobileDataArrayList?.set(
                        investigationModifyPostion!!,
                        selectedInvestigationData
                    )
                    (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).updateAdapter(
                        investigationMobileDataArrayList!!
                    )
                    clearInvestigation()
                    hideDetailLayout(
                        requireContext(),
                        binding?.llAddInvestigationDetail!!,
                        binding?.imgDownInvestigation!!
                    )
                    if (isModify) {
                        val patientInvestigationDetail = NewDetailX(
                            doctor_uuid = treatmentKitResponsResponseContent.doctor_id.toString(),
                            encounter_type_uuid = treatmentKitResponsResponseContent.encounter_type,
                            encounter_uuid = treatmentKitResponsResponseContent.encounter_type_uuid.toString(),
                            from_department_uuid = treatmentKitResponsResponseContent.department_id.toString(),
                            is_ordered = true,
                            is_profile = false,
                            lab_master_type_uuid = 1,
                            order_priority_uuid = "2",
                            order_status_uuid = 1,
                            patient_uuid = treatmentKitResponsResponseContent.patient_id.toString(),
                            patient_work_order_by = 1,
                            sample_type_uuid = 23,
                            test_master_uuid = 1335,
                            to_department_uuid = 0,
                            to_location_uuid = 2668,
                            to_sub_department_uuid = 0,
                            type_of_method_uuid = 7
                        )
                        listNewDetailsInvestigation.add(patientInvestigationDetail)
                    }
                } else {
                    val check =
                        investigationMobileDataArrayList?.any { it!!.test_master_id == selectedInvestigationData?.test_master_id }
                    if (!check!!) {
                        investigationMobileDataArrayList?.add(selectedInvestigationData!!)
                        (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).updateAdapter(
                            investigationMobileDataArrayList!!
                        )
                        clearInvestigation()
                        hideDetailLayout(
                            requireContext(),
                            binding?.llAddInvestigationDetail!!,
                            binding?.imgDownInvestigation!!
                        )
                        if (isModify) {
                            val patientInvestigationDetail = NewDetailX(
                                doctor_uuid = treatmentKitResponsResponseContent.doctor_id.toString(),
                                encounter_type_uuid = treatmentKitResponsResponseContent.encounter_type,
                                encounter_uuid = treatmentKitResponsResponseContent.encounter_type_uuid.toString(),
                                from_department_uuid = treatmentKitResponsResponseContent.department_id.toString(),
                                is_ordered = true,
                                is_profile = false,
                                lab_master_type_uuid = 1,
                                order_priority_uuid = "2",
                                order_status_uuid = 1,
                                patient_uuid = treatmentKitResponsResponseContent.patient_id.toString(),
                                patient_work_order_by = 1,
                                sample_type_uuid = 23,
                                test_master_uuid = 1335,
                                to_department_uuid = 0,
                                to_location_uuid = 2668,
                                to_sub_department_uuid = 0,
                                type_of_method_uuid = 7
                            )
                            listExistingInvestigation.add(patientInvestigationDetail)
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Already Item available in the list",
                            Toast.LENGTH_LONG
                        )?.show()
                    }
                }
            }
        }

        binding?.cvInvestigationClear?.setOnClickListener {
            clearInvestigation()
        }
    }

    private fun prescriptionInitViews() {
        routeNamesMap.put(0, "")
        instructionMap.put(0, "")
        frequencyMap.put(0, "")

        setDummyInSpinner(binding?.routeSpinner, routeNamesMap)
        setDummyInSpinner(binding?.frequencySpinner, instructionMap)
        setDummyInSpinner(binding?.instructionSpinner, frequencyMap)

        binding?.clearPresCardView?.setOnClickListener {

            clearPrescriptionList()
        }

        binding?.PresAutoCompleteTextView?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 2 && s.length < 5) {

                    viewModel?.getPrescriptionComplaintSearchResult(
                        s.toString(),
                        getPresriptionComplaintSearchRetrofitCallBack,
                        facility_id
                    )

                }
            }
        })


        binding?.routeSpinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent?.getItemAtPosition(pos).toString()
                    TKtPrescriptionAddNew.drug_route_id =
                        routeNamesMap.filterValues { it == itemValue }.keys.toList()[0]


                    TKtPrescriptionAddNew.drug_route_name = itemValue

                    Log.i(
                        "LabType",
                        "name = " + itemValue + "uuid=" + routeNamesMap.filterValues { it == itemValue }.keys.toList()[0]
                    )
                }
            }

        binding?.instructionSpinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent?.getItemAtPosition(pos).toString()


                    TKtPrescriptionAddNew.drug_instruction_id =
                        instructionMap.filterValues { it == itemValue }.keys.toList()[0]

                    TKtPrescriptionAddNew.drug_instruction_name = itemValue

                    Log.i(
                        "LabType",
                        "name = " + itemValue + "uuid=" + instructionMap.filterValues { it == itemValue }.keys.toList()[0]
                    )
                }
            }

        binding?.frequencySpinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent?.getItemAtPosition(pos).toString()
                    TKtPrescriptionAddNew.drug_frequency_id =
                        frequencyMap.filterValues { it == itemValue }.keys.toList()[0]

                    if (freqencyList.isNotEmpty()) {

                        if (pos != 0) {

                            TKtPrescriptionAddNew.perday_quantity =
                                freqencyList[pos - 1].perdayquantity.toString()

                        }
                    }
                    TKtPrescriptionAddNew.drug_frequency_name = itemValue

                    Log.i(
                        "LabType",
                        "name = " + itemValue + "uuid=" + frequencyMap.filterValues { it == itemValue }.keys.toList()[0]
                    )
                }
            }


        binding?.addCardView?.setOnClickListener {

            if (isModify) {
                listDetailsPrescription.add(PatientPrescription().details!!)
            }

            if (TKtPrescriptionAddNew.drug_id == 0) {

                Toast.makeText(requireContext(), "Please enter valid Drug name", Toast.LENGTH_SHORT)
                    .show()

                return@setOnClickListener
            }

            if (binding?.duration?.text.isNullOrEmpty()) {

                Toast.makeText(requireContext(), "Please enter Duration", Toast.LENGTH_SHORT).show()

                return@setOnClickListener
            }

            if (TKtPrescriptionAddNew.drug_period_id == 0) {

                Toast.makeText(requireContext(), "Please Select Duration", Toast.LENGTH_SHORT)
                    .show()

                return@setOnClickListener

            }

            if (TKtPrescriptionAddNew.drug_route_id == 0) {

                Toast.makeText(requireContext(), "Please Select Route", Toast.LENGTH_SHORT).show()

                return@setOnClickListener

            }

            if (TKtPrescriptionAddNew.drug_frequency_id == 0) {

                Toast.makeText(requireContext(), "Please Select Frequency", Toast.LENGTH_SHORT)
                    .show()

                return@setOnClickListener

            }

            if (TKtPrescriptionAddNew.drug_instruction_id == 0) {

                Toast.makeText(requireContext(), "Please Select Instruction", Toast.LENGTH_SHORT)
                    .show()

                return@setOnClickListener

            }

            if (TKtPrescriptionAddNew.drug_period_name == "") {

                Toast.makeText(
                    requireContext(),
                    "Please Select Duration Period",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener

            }

            TKtPrescriptionAddNew.drug_duration = binding?.duration?.text.toString().toInt()

            when (TKtPrescriptionAddNew.drug_period_name) {
                "Days" -> {
                    TKtPrescriptionAddNew.drug_quantity =
                        (TKtPrescriptionAddNew.perday_quantity!!.toDouble() * TKtPrescriptionAddNew.drug_duration!!.toInt()).toString()
                }
                "Weeks" -> {
                    TKtPrescriptionAddNew.drug_quantity =
                        (TKtPrescriptionAddNew.perday_quantity!!.toDouble() * TKtPrescriptionAddNew.drug_duration!!.toInt() * 7).toString()
                }
                "Months" -> {
                    TKtPrescriptionAddNew.drug_quantity =
                        (TKtPrescriptionAddNew.perday_quantity!!.toDouble() * TKtPrescriptionAddNew.drug_duration!!.toInt() * 30).toString()
                }
                "Years" -> {
                    TKtPrescriptionAddNew.drug_quantity =
                        (TKtPrescriptionAddNew.perday_quantity!!.toDouble() * TKtPrescriptionAddNew.drug_duration!!.toInt() * 365).toString()
                }
                else -> {
                    TKtPrescriptionAddNew.drug_quantity =
                        (TKtPrescriptionAddNew.perday_quantity!!.toDouble() * TKtPrescriptionAddNew.drug_duration!!.toInt()).toString()
                }
            }


            val checkdata =
                (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).getItems()

            val injId: Int = TKtPrescriptionAddNew.drug_instruction_id!!

            for (k in checkdata.indices) {

                if (checkdata[k]!!.drug_active!!) {

                    val ck = InjectionList.any { it.uuid == checkdata[k]!!.drug_instruction_id }

                    if (checkdata[k]!!.drug_instruction_id != 0 && ck) {

                        if (injId != checkdata[k]!!.drug_instruction_id) {

                            Toast.makeText(
                                requireContext(),
                                "Injection Room must be same",
                                Toast.LENGTH_SHORT
                            ).show()

                            return@setOnClickListener
                        }


                    }

                }


            }

            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)



            if (TKtPrescriptionAddNew.Update) {

                val sendData = setPresListData(TKtPrescriptionAddNew)

                (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).Update(
                    sendData
                )


            } else {

                val sendData = setPresListData(TKtPrescriptionAddNew)

                (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).addDummy(
                    sendData
                )

            }
            clearPrescriptionList()

            hideDetailLayout(
                requireContext(),
                binding?.addMedicineDetailLayout!!,
                binding?.imgDownPrescription!!
            )

        }


    }

    private fun setSearchPrescription(responseContents: List<PrescriptionSearchResponseContent?>) {


        val responseContentAdapter = PrescriptionSearchResultAdapter(
            requireContext(),
            R.layout.row_chief_complaint_search_result,
            responseContents
        )
        binding?.PresAutoCompleteTextView?.threshold = 1
        binding?.PresAutoCompleteTextView?.setAdapter(responseContentAdapter)
        binding?.PresAutoCompleteTextView?.showDropDown()
        binding?.PresAutoCompleteTextView?.setOnItemClickListener { parent, _, position, id ->


            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)


            val selectedPoi = parent.adapter.getItem(position) as PrescriptionSearchResponseContent?

            val st = FavouritesModel()

            val check = checkPrescription(selectedPoi?.uuid!!)


            if (!check) {

                binding?.PresAutoCompleteTextView?.setText(selectedPoi.name)
                TKtPrescriptionAddNew.drug_name = selectedPoi.name
                TKtPrescriptionAddNew.drug_id = selectedPoi.uuid
                TKtPrescriptionAddNew.store_master_uuid =
                    selectedPoi.stock_item?.store_master_uuid
                TKtPrescriptionAddNew.drug_strength = selectedPoi.strength
                TKtPrescriptionAddNew.drug_period_code = selectedPoi.code.toString()
                TKtPrescriptionAddNew.drug_is_emar = selectedPoi.is_emar
                binding?.Strength?.setText(selectedPoi.strength ?: "")

                setFreqency(freqencyList)

                setRoute(RouteList)

                if (selectedPoi.is_emar!!) {

                    setInjection(InjectionList)
                } else {
                    setInstruction(InstructionList)
                }


            } else {

                binding?.PresAutoCompleteTextView?.setText("")

                Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_SHORT)
                    ?.show()

            }

        }


    }

    private fun setFreqency(data: ArrayList<PrescriptionFrequencyresponseContent>) {

        frequencyMap.putAll(data.map { it.uuid!! to it.name }.toMap().toMutableMap())

        val frequencyAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            frequencyMap.values.toMutableList()
        )

        frequencyAdapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.frequencySpinner?.adapter = frequencyAdapter

    }

    private fun setRoute(data: ArrayList<TreatmentPrescRouteSpinnerresponseContent>) {

        routeNamesMap.putAll(data.map { it.uuid!! to it.name }.toMap().toMutableMap())

        val routeAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            routeNamesMap.values.toMutableList()
        )
        routeAdapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.routeSpinner?.adapter = routeAdapter

    }

    private fun setInjection(data: ArrayList<InjectionDepartment>) {

        instructionMap.clear()

        instructionMap.put(0, "")

        instructionMap.putAll(data.map { it.uuid to it.store_name }.toMap().toMutableMap())

        val routeAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            instructionMap.values.toMutableList()
        )
        routeAdapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.instructionSpinner?.adapter = routeAdapter

    }

    private fun setInstruction(data: ArrayList<PresInstructionResponseContent>) {


        instructionMap.clear()

        instructionMap.put(0, "")

        instructionMap.putAll(data.map { it.uuid to it.name }.toMap().toMutableMap())

        val routeAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            instructionMap.values.toMutableList()
        )
        routeAdapter.setDropDownViewResource(R.layout.spinner_item)
        binding?.instructionSpinner?.adapter = routeAdapter

    }

    private fun setDummyInSpinner(spinner: AppCompatSpinner?, Mapdata: MutableMap<Int, String>) {

        val Adapter =
            ArrayAdapter<String>(
                requireContext(),
                R.layout.spinner_item,
                Mapdata.values.toMutableList()
            )
        Adapter.setDropDownViewResource(R.layout.spinner_item)
        spinner?.adapter = Adapter


    }

    private fun checkPrescription(drug_id: Int): Boolean {


        return (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).getItems()
            .any { it!!.drug_id == drug_id }

    }

    private fun clearPrescriptionList() {

        TKtPrescriptionAddNew = TKtPrescriptionData()
        binding?.PresAutoCompleteTextView?.setText("")
        binding?.Strength?.setText("")
        binding?.duration?.setText("")
        routeNamesMap.clear()
        instructionMap.clear()
        frequencyMap.clear()
        routeNamesMap.put(0, "")
        instructionMap.put(0, "")
        frequencyMap.put(0, "")

        durationAdapter?.updateSelectStatus(0)

        setDummyInSpinner(binding?.routeSpinner, routeNamesMap)
        setDummyInSpinner(binding?.frequencySpinner, instructionMap)
        setDummyInSpinner(binding?.instructionSpinner, frequencyMap)


    }

    private fun setPresListData(responseContent: TKtPrescriptionData?): TKtPrescriptionListData {


        return TKtPrescriptionListData(
            drug_active = responseContent!!.drug_active,
            drug_duration = responseContent.drug_duration!!,
            drug_code = responseContent.drug_code,
            drug_quantity = responseContent.drug_quantity,
            perday_quantity = responseContent.perday_quantity,

            drug_frequency_id = responseContent.drug_frequency_id,
            drug_frequency_name = responseContent.drug_frequency_name,
            drug_frequency_code = responseContent.drug_frequency_code,

            drug_instruction_code = responseContent.drug_instruction_code,
            drug_instruction_id = responseContent.drug_instruction_id,
            drug_instruction_name = responseContent.drug_instruction_name,

            drug_id = responseContent.drug_id,
            store_master_uuid = responseContent.store_master_uuid,
            drug_name = responseContent.drug_name,

            drug_period_code = responseContent.drug_period_code,
            drug_period_id = responseContent.drug_period_id,
            drug_period_name = responseContent.drug_period_name,

            drug_route_id = responseContent.drug_route_id,
            drug_route_name = responseContent.drug_route_name!!,


            collapse = responseContent.collapse,
            Update = responseContent.Update,
            drug_is_emar = responseContent.drug_is_emar
        )

    }

    private fun setPresNewData(responseContent: TKtPrescriptionListData?): TKtPrescriptionData {


        return TKtPrescriptionData(
            drug_active = responseContent!!.drug_active,
            drug_duration = responseContent.drug_duration!!,
            drug_code = responseContent.drug_code,
            drug_quantity = responseContent.drug_quantity,
            perday_quantity = responseContent.perday_quantity,

            drug_frequency_id = responseContent.drug_frequency_id,
            drug_frequency_name = responseContent.drug_frequency_name,
            drug_frequency_code = responseContent.drug_frequency_code,

            drug_instruction_code = responseContent.drug_instruction_code,
            drug_instruction_id = responseContent.drug_instruction_id,
            drug_instruction_name = responseContent.drug_instruction_name,

            drug_id = responseContent.drug_id,
            store_master_uuid = responseContent.store_master_uuid,
            drug_name = responseContent.drug_name,

            drug_period_code = responseContent.drug_period_code,
            drug_period_id = responseContent.drug_period_id,
            drug_period_name = responseContent.drug_period_name,

            drug_route_id = responseContent.drug_route_id,
            drug_route_name = responseContent.drug_route_name!!,


            collapse = responseContent.collapse,
            Update = responseContent.Update,
            drug_is_emar = responseContent.drug_is_emar
        )

    }

    private fun setDurationPeriod(responseContents: List<PrescriptionDurationResponseContent>) {

        durationArrayList = responseContents as ArrayList<PrescriptionDurationResponseContent>

        val gridLayoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL, true
        )
        binding?.durationRecyclerView?.layoutManager = gridLayoutManager

        durationAdapter = PrescriptionDurationAdapter(requireContext(), responseContents)

        binding?.durationRecyclerView?.adapter = durationAdapter

        durationAdapter?.setOnItemClickListener(
            object : PrescriptionDurationAdapter.OnItemClickListener {

                override fun onItemClick(durationID: Int, poss: Int) {

                    durationAdapter?.updateSelectStatus((durationID))

                    TKtPrescriptionAddNew.drug_period_name = durationArrayList[poss].code
                    TKtPrescriptionAddNew.drug_period_id = durationID

                }
            })
    }

    override fun sendFavAddInLab(
        faverAddmodel: FavAddToListresponseContents?,
        position: Int,
        selected: Boolean,
        favouriteID: Int
    ) {
        if (isTablet(requireContext())) {
            if (!selected) {
                for (i in faverAddmodel?.diagnosis_details!!.indices) {
                    val favmodel = FavouritesModel()
                    favmodel.itemAppendString = faverAddmodel.diagnosis_details[i]?.diagnosis_name
                    favmodel.diagnosis_id =
                        faverAddmodel.diagnosis_details[i]?.diagnosis_id?.toString()
                    favmodel.diagnosis_code =
                        faverAddmodel.diagnosis_details[i]?.diagnosis_code?.toString()
                    favmodel.diagnosis_description =
                        faverAddmodel.diagnosis_details[i]?.diagnosis_description?.toString()
                    favmodel.favourite_id = favouriteID
                    (treatmentDiagnosisAdapter as TreatmentKitDiagnosisAdapter).addFavouritesInRow(
                        favmodel
                    )
                }

                for (i in faverAddmodel.lab_details!!.indices) {
                    val favmodel = FavouritesModel()
                    favmodel.itemAppendString = faverAddmodel.lab_details[i]?.lab_name
                    favmodel.test_master_code = faverAddmodel.lab_details[i]?.lab_code
                    favmodel.lab_description = faverAddmodel.lab_details[i]?.lab_description
                    favmodel.test_master_id = faverAddmodel.lab_details[i]?.lab_id
                    favmodel.favourite_id = favouriteID
                    (labAdapter as TreatmentKitLabAdapter).addFavouritesInRow(favmodel)
                }

                for (i in faverAddmodel.radiology_details!!.indices) {
                    val favmodel = FavouritesModel()
                    favmodel.itemAppendString = faverAddmodel.radiology_details[i]?.radiology_name
                    favmodel.test_master_code = faverAddmodel.radiology_details[i]?.radiology_code
                    favmodel.radiology_description =
                        faverAddmodel.radiology_details[i]?.radiology_description
                    favmodel.test_master_id = faverAddmodel.radiology_details[i]?.radiology_id
                    favmodel.favourite_id = favouriteID
                    (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).addFavouritesInRow(
                        favmodel
                    )
                }

                for (i in faverAddmodel.investigation_details!!.indices) {
                    val favmodel = FavouritesModel()
                    favmodel.itemAppendString =
                        faverAddmodel.investigation_details[i]?.investigation_name.toString()
                    favmodel.test_master_code =
                        faverAddmodel.investigation_details[i]?.investigation_code.toString()
                    favmodel.investigation_description =
                        faverAddmodel.investigation_details[i]?.investigation_description.toString()
                    favmodel.test_master_id =
                        faverAddmodel.investigation_details[i]?.investigation_id
                    favmodel.favourite_id = favouriteID
                    (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).addFavouritesInRow(
                        favmodel
                    )
                }


                for (i in faverAddmodel.drug_details!!.indices) {
                    val favmodel = FavouritesModel()
                    favmodel.itemAppendString = faverAddmodel.drug_details[i]?.drug_name
                    favmodel.drug_code = faverAddmodel.drug_details[i]?.drug_code
                    favmodel.drug_id = faverAddmodel.drug_details[i]?.drug_id
                    favmodel.drug_quantity = faverAddmodel.drug_details[i]?.drug_quantity
                    favmodel.drug_duration = faverAddmodel.drug_details[i]?.drug_duration?.toInt()
                    favmodel.drug_route_name = faverAddmodel.drug_details[i]?.drug_route_name
                    favmodel.drug_route_id = faverAddmodel.drug_details[i]?.drug_route_id
                    favmodel.drug_frequency_id =
                        faverAddmodel.drug_details[i]?.drug_frequency_id.toString()
                    favmodel.drug_period_id = faverAddmodel.drug_details[i]?.drug_period_id
                    favmodel.drug_instruction_id =
                        faverAddmodel.drug_details[i]?.drug_instruction_id
                    favmodel.favourite_id = favouriteID
                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).addFavouritesInRow(
                        favmodel
                    )
                }

            } else {
                for (i in faverAddmodel?.diagnosis_details!!.indices) {
                    (treatmentDiagnosisAdapter as TreatmentKitDiagnosisAdapter).deleteRowFromFavourites(
                        favouriteID
                    )
                }

                for (i in faverAddmodel.lab_details!!.indices) {
                    (labAdapter as TreatmentKitLabAdapter).deleteRowFromFavourites(favouriteID)
                }

                for (i in faverAddmodel.radiology_details!!.indices) {
                    (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).deleteRowFromFavourites(
                        favouriteID
                    )
                }

                for (i in faverAddmodel.investigation_details!!.indices) {
                    (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).deleteRowFromFavourites(
                        favouriteID
                    )
                }

                for (i in faverAddmodel.drug_details!!.indices) {
                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).deleteRowFromFavourites(
                        favouriteID
                    )
                }
            }
        } else {
            if (!selected) {

                appPreferences?.saveInt(
                    AppConstants.PREF_TKT_UUID,
                    faverAddmodel?.treatment_id ?: 0
                )

                for (i in faverAddmodel?.diagnosis_details!!.indices) {

                    val favmodel = DiagnosisListData()
                    favmodel.diagnosis_name = faverAddmodel.diagnosis_details[i]?.diagnosis_name
                    favmodel.diagnosis_id =
                        faverAddmodel.diagnosis_details[i]?.diagnosis_id?.toString()
                    favmodel.diagnosis_code =
                        faverAddmodel.diagnosis_details[i]?.diagnosis_code?.toString()
                    favmodel.diagnosis_description =
                        faverAddmodel.diagnosis_details[i]?.diagnosis_description?.toString()
//                    favmodel.favourite_id = favouriteID
                    (treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter).addFavouritesInRow(
                        favmodel
                    )
                }

                for (i in faverAddmodel.drug_details!!.indices) {
                    val favmodel = TKtPrescriptionListData()
                    favmodel.drug_name = faverAddmodel.drug_details[i]?.drug_name
                    favmodel.drug_code = faverAddmodel.drug_details[i]?.drug_code
                    favmodel.drug_id = faverAddmodel.drug_details[i]?.drug_id
                    favmodel.drug_quantity = faverAddmodel.drug_details[i]?.drug_quantity
                    favmodel.drug_duration = faverAddmodel.drug_details[i]?.drug_duration?.toInt()
                    favmodel.drug_route_name = faverAddmodel.drug_details[i]?.drug_route_name
                    favmodel.drug_route_id = faverAddmodel.drug_details[i]?.drug_route_id
                    favmodel.drug_frequency_id = faverAddmodel.drug_details[i]?.drug_frequency_id
                    favmodel.drug_period_id = faverAddmodel.drug_details[i]?.drug_period_id
                    favmodel.drug_instruction_id =
                        faverAddmodel.drug_details[i]?.drug_instruction_id
//                    favmodel.favourite_id = favouriteID
                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).addRow(
                        favmodel
                    )
                }

                for (i in faverAddmodel.lab_details!!.indices) {
                    val favmodel = FavouritesModel()
//                    favmodel.itemAppendString = faverAddmodel.lab_details[i]?.lab_name
//                    favmodel.test_master_code = faverAddmodel.lab_details[i]?.lab_code
//                    favmodel.lab_description = faverAddmodel.lab_details[i]?.lab_description
//                    favmodel.test_master_id = faverAddmodel.lab_details[i]?.lab_id
//                    favmodel.favourite_id = favouriteID

                    favmodel.lab_name = faverAddmodel.lab_details[i]?.lab_name
                    favmodel.lab_code = faverAddmodel.lab_details[i]?.lab_code
                    favmodel.lab_description = faverAddmodel.lab_details[i]?.lab_description
                    favmodel.lab_id = faverAddmodel.lab_details[i]?.lab_id
                    favmodel.selectTypeUUID = faverAddmodel.lab_details[i]?.order_priority_uuid ?: 0
                    favmodel.selectTypeName =
                        getLabPriorityKeyFromValue(favmodel.selectTypeUUID)
                    favmodel.selectToLocationUUID =
                        faverAddmodel.lab_details[i]?.order_to_location_uuid ?: 0
                    favmodel.selectedLocationName =
                        getLabLocationKeyFromValue(favmodel.selectToLocationUUID)
                    labMobileDataArrayList?.add(favmodel)

                    (labAdapter as TreatmentKitLabMobileAdapter).addRow(favmodel)
                }

                for (i in faverAddmodel.radiology_details!!.indices) {
                    val favmodel = FavouritesModel()
//                    favmodel.itemAppendString = faverAddmodel.radiology_details[i]?.radiology_name
//                    favmodel.test_master_code = faverAddmodel.radiology_details[i]?.radiology_code
//                    favmodel.radiology_description =
//                        faverAddmodel.radiology_details[i]?.radiology_description
//                    favmodel.test_master_id = faverAddmodel.radiology_details[i]?.radiology_id
//                    favmodel.favourite_id = favouriteID

                    favmodel.radiology_name = faverAddmodel.radiology_details[i]?.radiology_name
                    favmodel.radiology_code = faverAddmodel.radiology_details[i]?.radiology_code
                    favmodel.radiology_description =
                        faverAddmodel.radiology_details[i]?.radiology_description
                    favmodel.radiology_id = faverAddmodel.radiology_details[i]?.radiology_id
                    favmodel.selectTypeUUID =
                        faverAddmodel.radiology_details[i]?.order_priority_uuid ?: 0
                    favmodel.selectTypeName =
                        getRadiologyPriorityKeyFromValue(favmodel.selectTypeUUID)
                    favmodel.selectToLocationUUID =
                        faverAddmodel.radiology_details[i]?.order_to_location_uuid ?: 0
                    favmodel.selectedLocationName =
                        getRadiologyLocationKeyFromValue(favmodel.selectToLocationUUID)
                    radiologyMobileDataArrayList?.add(favmodel)
                    (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).addRow(
                        favmodel
                    )
                }

                for (i in faverAddmodel.investigation_details!!.indices) {
                    val favmodel = FavouritesModel()
//                    favmodel.itemAppendString =
//                        faverAddmodel.investigation_details[i]?.investigation_name.toString()
//                    favmodel.test_master_code =
//                        faverAddmodel.investigation_details[i]?.investigation_code.toString()
//                    favmodel.investigation_description =
//                        faverAddmodel.investigation_details[i]?.investigation_description.toString()
//                    favmodel.test_master_id =
//                        faverAddmodel.investigation_details[i]?.investigation_id
//                    favmodel.favourite_id = favouriteID

                    favmodel.investigation_name =
                        faverAddmodel.investigation_details[i]?.investigation_name
                    favmodel.investigation_code =
                        faverAddmodel.investigation_details[i]?.investigation_code
                    favmodel.investigation_description =
                        faverAddmodel.investigation_details[i]?.investigation_description
                    favmodel.investigation_id =
                        faverAddmodel.investigation_details[i]?.investigation_id
                    favmodel.selectTypeUUID =
                        faverAddmodel.investigation_details[i]?.order_priority_uuid ?: 0
                    favmodel.selectTypeName =
                        getInvestigationPriorityKeyFromValue(favmodel.selectTypeUUID)
                    favmodel.selectToLocationUUID =
                        faverAddmodel.investigation_details[i]?.order_to_location_uuid ?: 0
                    favmodel.selectedLocationName =
                        getInvestigationLocationKeyFromValue(favmodel.selectToLocationUUID)
                    investigationMobileDataArrayList?.add(favmodel)
                    (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).addRow(
                        favmodel
                    )
                }

            } else {
                for (i in faverAddmodel?.diagnosis_details!!.indices) {
                    (treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter).deleteRow(
                        diagnosCode = faverAddmodel.diagnosis_details[i]?.diagnosis_code
                    )
                }

                for (i in faverAddmodel.drug_details!!.indices) {
                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).deleteRow(
                        drugCode = faverAddmodel.drug_details[i]?.drug_code
                    )
                }

                for (i in faverAddmodel.lab_details!!.indices) {
                    (labAdapter as TreatmentKitLabMobileAdapter).deleteRow(
                        labCode = faverAddmodel.lab_details[i]?.lab_code
                    )
                }

                for (i in faverAddmodel.radiology_details!!.indices) {
                    (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).deleteRow(
                        radiologyCode = faverAddmodel.radiology_details[i]?.radiology_code
                    )
                }

                for (i in faverAddmodel.investigation_details!!.indices) {
                    (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).deleteRow(
                        investigationCode = faverAddmodel.investigation_details[i]?.investigation_code
                    )
                }
            }
        }
    }

    override fun sendPrevtoChild(responseContent: TreatmentKitResponsResponseContent?) {

        //Log.e("TKCHildF",responseContents.toString())

        if (isTablet(requireContext())) {
            for (i in responseContent?.diagnosis?.indices!!) {

                val favmodel = FavouritesModel()
                favmodel.itemAppendString = responseContent.diagnosis[i].diagnosis_name
                favmodel.diagnosis_id = responseContent.diagnosis[i].diagnosis_id.toString()
                favmodel.diagnosis_code = responseContent.diagnosis[i].diagnosis_code
                favmodel.diagnosis_description =
                    responseContent.diagnosis[i].diagnosis_description
                //favmodel.favourite_id = favouriteID
                (treatmentDiagnosisAdapter as TreatmentKitDiagnosisAdapter).addFavouritesInRow(
                    favmodel
                )
            }

            for (i in responseContent.labDetails.indices) {
                val favmodel = FavouritesModel()
                favmodel.itemAppendString = responseContent.labDetails[i].lab_name
                favmodel.test_master_code = responseContent.labDetails[i].lab_code
                //favmodel.lab_description = responseContent.labDetails[i]!!.lab_description
                favmodel.selectToLocationUUID = responseContent.labDetails[i].to_location_uuid
                favmodel.test_master_id = responseContent.labDetails[i].test_master_uuid
                //favmodel.favourite_id = favouriteID
                (labAdapter as TreatmentKitLabAdapter).addFavouritesInRow(favmodel)
            }

            for (i in responseContent.radilogyDetails.indices) {
                val favmodel = FavouritesModel()
                favmodel.itemAppendString = responseContent.radilogyDetails[i].lab_name
                favmodel.test_master_code = responseContent.radilogyDetails[i].lab_code
                //favmodel.radiology_description = responseContent.radilogyDetails[i]!!.radiology_description
                favmodel.test_master_id = responseContent.radilogyDetails[i].test_master_uuid
                //favmodel.favourite_id = favouriteID
                (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).addFavouritesInRow(
                    favmodel
                )
            }

            for (i in responseContent.InvestigationDetails.indices) {
                val favmodel = FavouritesModel()
                favmodel.itemAppendString = responseContent.InvestigationDetails[i].lab_name
                favmodel.test_master_code = responseContent.InvestigationDetails[i].lab_code
                favmodel.test_master_id = responseContent.InvestigationDetails[i].test_master_uuid

                (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).addFavouritesInRow(
                    favmodel
                )
            }

            for (i in responseContent.drugDetails.indices) {
                val favmodel: FavouritesModel = FavouritesModel()
                favmodel.itemAppendString = responseContent.drugDetails[i].drug_name
                favmodel.drug_id = responseContent.drugDetails[i].uuid
                favmodel.drug_route_id = responseContent.drugDetails[i].drug_route_id

                (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).addFavouritesInRow(
                    favmodel
                )

            }
        } else {
            for (i in responseContent?.diagnosis?.indices!!) {

                val diagnosisListData = DiagnosisListData()
                diagnosisListData.diagnosis_name = responseContent.diagnosis[i].diagnosis_name
                diagnosisListData.diagnosis_id =
                    responseContent.diagnosis[i].diagnosis_id.toString()
                diagnosisListData.diagnosis_code = responseContent.diagnosis[i].diagnosis_code
                diagnosisListData.diagnosis_description =
                    responseContent.diagnosis[i].diagnosis_description
                //favmodel.favourite_id = favouriteID
                (treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter).addFavouritesInRow(
                    diagnosisListData
                )
            }

            for (i in responseContent.labDetails.indices) {
                val favmodel = FavouritesModel()
                favmodel.itemAppendString = responseContent.labDetails[i].lab_name
                favmodel.test_master_code = responseContent.labDetails[i].lab_code
                favmodel.lab_name = responseContent.labDetails[i].lab_name
                favmodel.lab_code = responseContent.labDetails[i].lab_code
                //favmodel.lab_description = responseContent.labDetails[i]!!.lab_description
                favmodel.selectToLocationUUID = responseContent.labDetails[i].to_location_uuid
                favmodel.selectedLocationName = responseContent.labDetails[i].location_name
                favmodel.test_master_id = responseContent.labDetails[i].test_master_uuid
                favmodel.selectTypeUUID = responseContent.labDetails[i].priority_uuid
                favmodel.selectTypeName = responseContent.labDetails[i].prority_name
                //favmodel.favourite_id = favouriteID
                (labAdapter as TreatmentKitLabMobileAdapter).addRow(favmodel)
            }

            for (i in responseContent.radilogyDetails.indices) {
                val favmodel = FavouritesModel()
                favmodel.itemAppendString = responseContent.radilogyDetails[i].lab_name
                favmodel.test_master_code = responseContent.radilogyDetails[i].lab_code
                favmodel.radiology_name = responseContent.radilogyDetails[i].lab_name
                favmodel.radiology_code = responseContent.radilogyDetails[i].lab_code
                //favmodel.radiology_description = responseContent.radilogyDetails[i]!!.radiology_description
                favmodel.test_master_id = responseContent.radilogyDetails[i].test_master_uuid
                favmodel.selectToLocationUUID =
                    responseContent.radilogyDetails[i].to_location_uuid ?: 0
                favmodel.selectedLocationName =
                    responseContent.radilogyDetails[i].location_name ?: ""
                favmodel.test_master_id = responseContent.radilogyDetails[i].test_master_uuid
                favmodel.selectTypeUUID = responseContent.radilogyDetails[i].priority_uuid ?: 0
                favmodel.selectTypeName = responseContent.radilogyDetails[i].prority_name
                //favmodel.favourite_id = favouriteID
                (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).addRow(
                    favmodel
                )
            }

            for (i in responseContent.InvestigationDetails.indices) {
                val favmodel = FavouritesModel()
                favmodel.itemAppendString = responseContent.InvestigationDetails[i].lab_name
                favmodel.test_master_code = responseContent.InvestigationDetails[i].lab_code
                favmodel.investigation_name = responseContent.InvestigationDetails[i].lab_name
                favmodel.investigation_code = responseContent.InvestigationDetails[i].lab_code
                favmodel.test_master_id = responseContent.InvestigationDetails[i].test_master_uuid
                favmodel.selectToLocationUUID =
                    responseContent.InvestigationDetails[i].to_location_uuid ?: 0
                favmodel.selectedLocationName =
                    responseContent.InvestigationDetails[i].location_name ?: ""
                favmodel.test_master_id = responseContent.InvestigationDetails[i].test_master_uuid
                favmodel.selectTypeUUID = responseContent.InvestigationDetails[i].priority_uuid ?: 0
                favmodel.selectTypeName = responseContent.InvestigationDetails[i].prority_name

                (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).addRow(
                    favmodel
                )
            }

            for (i in responseContent.drugDetails.indices) {
                val favmodel = TKtPrescriptionListData()
                favmodel.drug_name = responseContent.drugDetails[i].drug_name
                favmodel.drug_id = responseContent.drugDetails[i].uuid
                favmodel.drug_route_id = responseContent.drugDetails[i].drug_route_id

                (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).addRow(
                    favmodel
                )
            }
        }
        binding?.drawerLayout!!.closeDrawer(GravityCompat.END)
    }

    override fun sendModify(responseContent: TreatmentKitResponsResponseContent?) {

        //Log.e("TKCHildF",responseContents.toString())

        isModify = true
        treatmentKitResponsResponseContent.order_id = responseContent?.order_id ?: 0
        treatmentKitResponsResponseContent.treatment_kit_uuid =
            responseContent?.treatment_kit_uuid ?: 0
        treatmentKitResponsResponseContent.department_id = responseContent?.department_id ?: 0
        treatmentKitResponsResponseContent.diagnosis.add(Diagnosi())
        treatmentKitResponsResponseContent.diagnosis.get(0).diagnosis_id =
            if (responseContent?.diagnosis?.isEmpty() == true)
                0
            else
                responseContent?.diagnosis?.get(0)?.diagnosis_id ?: 0
        treatmentKitResponsResponseContent.encounter_type_uuid =
            responseContent?.encounter_type_uuid ?: 0
        treatmentKitResponsResponseContent.encounter_type = responseContent?.encounter_type ?: ""
        treatmentKitResponsResponseContent.patient_id = responseContent?.patient_id ?: 0
        treatmentKitResponsResponseContent.doctor_id = responseContent?.doctor_id ?: 0

        if (isTablet(requireContext())) {
            for (i in responseContent?.diagnosis?.indices!!) {

                val favmodel = FavouritesModel()
                favmodel.itemAppendString = responseContent.diagnosis[i].diagnosis_name
                favmodel.diagnosis_id = responseContent.diagnosis[i].diagnosis_id.toString()
                favmodel.diagnosis_code = responseContent.diagnosis[i].diagnosis_code
                favmodel.diagnosis_description =
                    responseContent.diagnosis[i].diagnosis_description
                //favmodel.favourite_id = favouriteID
                (treatmentDiagnosisAdapter as TreatmentKitDiagnosisAdapter).addFavouritesInRow(
                    favmodel
                )
            }

            for (i in responseContent.labDetails.indices) {
                val favmodel = FavouritesModel()
                favmodel.itemAppendString = responseContent.labDetails[i].lab_name
                favmodel.test_master_code = responseContent.labDetails[i].lab_code
                //favmodel.lab_description = responseContent.labDetails[i]!!.lab_description
                favmodel.selectToLocationUUID = responseContent.labDetails[i].to_location_uuid
                favmodel.test_master_id = responseContent.labDetails[i].test_master_uuid
                //favmodel.favourite_id = favouriteID
                (labAdapter as TreatmentKitLabAdapter).addFavouritesInRow(favmodel)
            }

            for (i in responseContent.radilogyDetails.indices) {
                val favmodel = FavouritesModel()
                favmodel.itemAppendString = responseContent.radilogyDetails[i].lab_name
                favmodel.test_master_code = responseContent.radilogyDetails[i].lab_code
                //favmodel.radiology_description = responseContent.radilogyDetails[i]!!.radiology_description
                favmodel.test_master_id = responseContent.radilogyDetails[i].test_master_uuid
                //favmodel.favourite_id = favouriteID
                (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).addFavouritesInRow(
                    favmodel
                )
            }

            for (i in responseContent.InvestigationDetails.indices) {
                val favmodel = FavouritesModel()
                favmodel.itemAppendString = responseContent.InvestigationDetails[i].lab_name
                favmodel.test_master_code = responseContent.InvestigationDetails[i].lab_code
                favmodel.test_master_id = responseContent.InvestigationDetails[i].test_master_uuid

                (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).addFavouritesInRow(
                    favmodel
                )
            }

            for (i in responseContent.drugDetails.indices) {
                val favmodel: FavouritesModel = FavouritesModel()
                favmodel.itemAppendString = responseContent.drugDetails[i].drug_name
                favmodel.drug_id = responseContent.drugDetails[i].uuid
                favmodel.drug_route_id = responseContent.drugDetails[i].drug_route_id

                (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).addFavouritesInRow(
                    favmodel
                )

            }
        } else {
            for (i in responseContent?.diagnosis?.indices!!) {

                val diagnosisListData = DiagnosisListData()
                diagnosisListData.diagnosis_name = responseContent.diagnosis[i].diagnosis_name
                diagnosisListData.diagnosis_id =
                    responseContent.diagnosis[i].diagnosis_id.toString()
                diagnosisListData.diagnosis_code = responseContent.diagnosis[i].diagnosis_code
                diagnosisListData.diagnosis_description =
                    responseContent.diagnosis[i].diagnosis_description
                //favmodel.favourite_id = favouriteID
                (treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter).addFavouritesInRow(
                    diagnosisListData
                )
            }

            for (i in responseContent.labDetails.indices) {
                val favmodel = FavouritesModel()
                favmodel.itemAppendString = responseContent.labDetails[i].lab_name
                favmodel.test_master_code = responseContent.labDetails[i].lab_code
                favmodel.lab_name = responseContent.labDetails[i].lab_name
                favmodel.lab_code = responseContent.labDetails[i].lab_code
                //favmodel.lab_description = responseContent.labDetails[i]!!.lab_description
                favmodel.selectToLocationUUID = responseContent.labDetails[i].to_location_uuid
                favmodel.selectedLocationName = responseContent.labDetails[i].location_name
                favmodel.test_master_id = responseContent.labDetails[i].test_master_uuid
                favmodel.selectTypeUUID = responseContent.labDetails[i].priority_uuid
                favmodel.selectTypeName = responseContent.labDetails[i].prority_name
                //favmodel.favourite_id = favouriteID
                (labAdapter as TreatmentKitLabMobileAdapter).addRow(favmodel)
            }

            for (i in responseContent.radilogyDetails.indices) {
                val favmodel = FavouritesModel()
                favmodel.itemAppendString = responseContent.radilogyDetails[i].lab_name
                favmodel.test_master_code = responseContent.radilogyDetails[i].lab_code
                favmodel.radiology_name = responseContent.radilogyDetails[i].lab_name
                favmodel.radiology_code = responseContent.radilogyDetails[i].lab_code
                //favmodel.radiology_description = responseContent.radilogyDetails[i]!!.radiology_description
                favmodel.test_master_id = responseContent.radilogyDetails[i].test_master_uuid
                favmodel.selectToLocationUUID =
                    responseContent.radilogyDetails[i].to_location_uuid ?: 0
                favmodel.selectedLocationName =
                    responseContent.radilogyDetails[i].location_name ?: ""
                favmodel.test_master_id = responseContent.radilogyDetails[i].test_master_uuid
                favmodel.selectTypeUUID = responseContent.radilogyDetails[i].priority_uuid ?: 0
                favmodel.selectTypeName = responseContent.radilogyDetails[i].prority_name
                //favmodel.favourite_id = favouriteID
                (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).addRow(
                    favmodel
                )
            }

            for (i in responseContent.InvestigationDetails.indices) {
                val favmodel = FavouritesModel()
                favmodel.itemAppendString = responseContent.InvestigationDetails[i].lab_name
                favmodel.test_master_code = responseContent.InvestigationDetails[i].lab_code
                favmodel.investigation_name = responseContent.InvestigationDetails[i].lab_name
                favmodel.investigation_code = responseContent.InvestigationDetails[i].lab_code
                favmodel.test_master_id = responseContent.InvestigationDetails[i].test_master_uuid
                favmodel.selectToLocationUUID =
                    responseContent.InvestigationDetails[i].to_location_uuid ?: 0
                favmodel.selectedLocationName =
                    responseContent.InvestigationDetails[i].location_name ?: ""
                favmodel.test_master_id = responseContent.InvestigationDetails[i].test_master_uuid
                favmodel.selectTypeUUID = responseContent.InvestigationDetails[i].priority_uuid ?: 0
                favmodel.selectTypeName = responseContent.InvestigationDetails[i].prority_name

                (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).addRow(
                    favmodel
                )
            }

            for (i in responseContent.drugDetails.indices) {
                val favmodel = TKtPrescriptionListData()
                favmodel.drug_name = responseContent.drugDetails[i].drug_name
                favmodel.drug_id = responseContent.drugDetails[i].uuid
                favmodel.drug_route_id = responseContent.drugDetails[i].drug_route_id

                (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).addRow(
                    favmodel
                )
            }
        }
        binding?.drawerLayout!!.closeDrawer(GravityCompat.END)
    }

    private fun populateSearchedValues(responseContents: ResponseContents) {
        if (isTablet(requireContext())) {
            responseContents.diagnosis_details?.forEach { diagnosisDetail: DiagnosisDetail ->
                val favmodel = FavouritesModel()
                favmodel.itemAppendString = diagnosisDetail.diagnosis_name
                favmodel.diagnosis_id = diagnosisDetail.diagnosis_id.toString()
                favmodel.diagnosis_code = diagnosisDetail.diagnosis_code.toString()
                favmodel.diagnosis_description = diagnosisDetail.diagnosis_description.toString()
                (treatmentDiagnosisAdapter as TreatmentKitDiagnosisAdapter).addFavouritesInRow(
                    favmodel
                )
            }
            responseContents.drug_details?.forEach { drugDetail ->
                val favmodel = FavouritesModel()
                favmodel.itemAppendString = drugDetail.drug_name
                favmodel.drug_id = drugDetail.drug_id
                favmodel.drug_route_id = drugDetail.drug_route_id
                (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).addFavouritesInRow(
                    favmodel
                )
            }
            responseContents.lab_details?.forEach { labDetail ->
                val favmodel = FavouritesModel()
                favmodel.itemAppendString = labDetail.lab_name
                favmodel.test_master_code = labDetail.lab_code
                favmodel.selectToLocationUUID = labDetail.order_to_location_uuid ?: 0
                favmodel.test_master_id = labDetail.lab_id
                (labAdapter as TreatmentKitLabAdapter).addFavouritesInRow(favmodel)
            }
            responseContents.radiology_details?.forEach { radiologyDetail: RadiologyDetail ->
                val favmodel = FavouritesModel()
                favmodel.itemAppendString = radiologyDetail.radiology_name
                favmodel.test_master_code = radiologyDetail.radiology_code
                favmodel.test_master_id = radiologyDetail.radiology_id
                (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).addFavouritesInRow(
                    favmodel
                )
            }
            responseContents.investigation_details?.forEach { investigationDetails: InvestigationDetails ->
                val favmodel = FavouritesModel()
                favmodel.itemAppendString = investigationDetails.investigation_name
                favmodel.test_master_code = investigationDetails.investigation_code
                favmodel.test_master_id = investigationDetails.investigation_id
                (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).addFavouritesInRow(
                    favmodel
                )
            }
        } else {
            val treatmentKitUuid = responseContents.treatment_id
            val appPreferences =
                activity?.application?.let {
                    AppPreferences.getInstance(
                        it,
                        AppConstants.SHARE_PREFERENCE_NAME
                    )
                }
            appPreferences?.saveInt(AppConstants.PREF_TKT_UUID, treatmentKitUuid ?: 0)
            responseContents.diagnosis_details?.forEach { diagnosisDetail: DiagnosisDetail ->
                val diagnosisListData = DiagnosisListData()
//                diagnosisListData.itemAppendString = diagnosisDetail.diagnosis_name
                diagnosisListData.diagnosis_name = diagnosisDetail.diagnosis_name
                diagnosisListData.diagnosis_id = diagnosisDetail.diagnosis_id.toString()
                diagnosisListData.diagnosis_code = diagnosisDetail.diagnosis_code.toString()
                diagnosisListData.diagnosis_description =
                    diagnosisDetail.diagnosis_description.toString()
                (treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter).addFavouritesInRow(
                    diagnosisListData
                )
            }
            responseContents.drug_details?.forEach { drugDetail ->
                val tKtPrescriptionListData = TKtPrescriptionListData()
                tKtPrescriptionListData.drug_name = drugDetail.drug_name
                tKtPrescriptionListData.drug_id = drugDetail.drug_id
                tKtPrescriptionListData.drug_route_id = drugDetail.drug_route_id
                (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).addRow(
                    tKtPrescriptionListData
                )
            }
            responseContents.lab_details?.forEach { labDetail ->
                val favmodel: FavouritesModel = FavouritesModel()
                favmodel.itemAppendString = labDetail.lab_name
                favmodel.test_master_name = labDetail.lab_name
                favmodel.test_master_code = labDetail.lab_code
                favmodel.selectTypeUUID = labDetail.order_priority_uuid!!
                favmodel.selectToLocationUUID = labDetail.order_to_location_uuid ?: 0
                favmodel.selectTypeName = priorityMutableNameList[favmodel.selectTypeUUID]
                favmodel.selectedLocationName = orderToLocationList[favmodel.selectToLocationUUID]!!
                favmodel.test_master_id = labDetail.lab_id
                labMobileDataArrayList?.add(favmodel)
            }
            labMobileDataArrayList?.let {
                (labAdapter as TreatmentKitLabMobileAdapter).updateAdapter(
                    it
                )
            }
            responseContents.radiology_details?.forEach { radiologyDetail: RadiologyDetail ->
                val favmodel: FavouritesModel = FavouritesModel()
                favmodel.itemAppendString = radiologyDetail.radiology_name
                favmodel.test_master_name = radiologyDetail.radiology_name
                favmodel.test_master_code = radiologyDetail.radiology_code
                favmodel.test_master_id = radiologyDetail.radiology_id
                favmodel.selectTypeUUID = radiologyDetail.order_priority_uuid!!
                favmodel.selectToLocationUUID = radiologyDetail.order_to_location_uuid ?: 0
                favmodel.selectTypeName = radiologyPriorityMutableNameList[favmodel.selectTypeUUID]
                favmodel.selectedLocationName =
                    radiologyOrderToLocationList[favmodel.selectToLocationUUID].toString()
                (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).addRow(favmodel)
            }
            responseContents.investigation_details?.forEach { investigationDetails: InvestigationDetails ->
                val favmodel: FavouritesModel = FavouritesModel()
                favmodel.itemAppendString = investigationDetails.investigation_name
                favmodel.test_master_name = investigationDetails.investigation_name
                favmodel.test_master_code = investigationDetails.investigation_code
                favmodel.test_master_id = investigationDetails.investigation_id
                favmodel.selectToLocationUUID = investigationDetails.order_to_location_uuid ?: 0
                favmodel.selectTypeUUID = investigationDetails.order_priority_uuid!!
                favmodel.selectTypeName =
                    investigationPriorityMutableNameList[favmodel.selectTypeUUID]
                favmodel.selectedLocationName =
                    investigationOrderToLocationList[favmodel.selectToLocationUUID].toString()
                (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).addRow(
                    favmodel
                )
            }
        }
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        if (childFragment is TreatmentKitFavouriteFragment) {
            childFragment.setOnTextClickedListener(this)
        }
        if (childFragment is SaveOrderTreatmentFragment) {

            childFragment.setOnRefreshClickedListener(this)
        }

        if (childFragment is ClearFavParticularPositionListener) {
            mCallbackTreatmentKitFavFragment = childFragment
        }

        if (childFragment is PrevTreatmentKitFragment) {
            childFragment.setOnTextClickedListener(this)
        }

        if (childFragment is PrevTreatmentKitFragment) {
            childFragment.setOnModifyClickedListener(this)
        }

        if (childFragment is PrevTreatmentKitFragment) {
            childFragment.setAddnewItemListener(this)
        }

        if (childFragment is CommentTKDialogFragment) {
            childFragment.setOnTextClickedListener(this)
        }
    }

    override fun refresh() {
        prevTreatmentKitFragment?.prevLabRecords()
        if (isTablet(requireContext())) {
            (labAdapter as TreatmentKitLabAdapter).clearall()
            (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).clearall()
            (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).clearall()
            (treatmentDiagnosisAdapter as TreatmentKitDiagnosisAdapter).clearall()
            (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).clearall()

            setLabAdapter()
            setPrescriptionAdapter()
            setRadiologyAdapter()
            setInvestigationAdapter()
            setDiagnosisAdapter()

            viewModel?.getFrequencyDetails(getFrequencyRetrofitCallback, facility_id)
            viewModel?.getPrescriptionDuration(getPrescriptionDurationRetrofitCallBack, facility_id)
            viewModel?.getLabType(getLabTypeRetrofitCallback, facility_id)
            viewModel?.getRadiologyType(getRadiologyPriorityRetrofitCallback, facility_id)
            viewModel?.getInvestigationType(getInvestigationTypeRetrofitCallback, facility_id)
            viewModel?.getInvestigationToLocation(
                getInvestigationToLoctionRetrofitCallback,
                facility_id
            )
        } else {
            (labAdapter as TreatmentKitLabMobileAdapter).clearall()
            (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).clearall()
            (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).clearall()
            (treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter).clearall()
            (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).clearall()

            setLabAdapter()
            setPrescriptionAdapter()
            setRadiologyAdapter()
            setInvestigationAdapter()
            setDiagnosisAdapter()

            viewModel?.getFrequencyDetails(getFrequencyRetrofitCallback, facility_id)
            viewModel?.getPrescriptionDuration(getPrescriptionDurationRetrofitCallBack, facility_id)
            viewModel?.getLabType(getLabTypeRetrofitCallback, facility_id)
            viewModel?.getRadiologyType(getRadiologyPriorityRetrofitCallback, facility_id)
            viewModel?.getInvestigationType(getInvestigationTypeRetrofitCallback, facility_id)
            viewModel?.getInvestigationToLocation(
                getInvestigationToLoctionRetrofitCallback,
                facility_id
            )
        }
    }

    private fun trackTreatmentAnalyticsVisit(type: String) {
        AnalyticsManager.getAnalyticsManager().trackTreatmentVisit(type)
    }

    private fun trackTreatmentAnalyticsSaveStart(type: String) {
        AnalyticsManager.getAnalyticsManager().trackTreatmentSaveOrderStart(type)
    }

    private fun trackTreatmentOrderStart(type: String) {
        AnalyticsManager.getAnalyticsManager().trackTreatmentOrderStart(type)
    }

    fun trackTreatmentOrderComplete(type: String?, status: String?, message: String?) {
        AnalyticsManager.getAnalyticsManager().trackTreatmentOrderComplete(type, status, message)
    }

    override fun addnewItem() {
        clearAllFields()
        binding?.drawerLayout!!.closeDrawer(GravityCompat.END)
    }

    fun commandItem(position: Int, Command: String, widget: String) {
        val dialog = CommentTKDialogFragment()
        val ft = childFragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putInt("position", position)
        bundle.putString("commands", Command)
        bundle.putString("widget", widget)
        dialog.arguments = bundle
        dialog.show(ft, "Tag")
    }

    override fun sendCommandPosData(position: Int, command: String, widget: String) {
        if (isTablet(requireContext())) {
            if (widget.equals("lab")) {
                (labAdapter as TreatmentKitLabAdapter).addCommands(position, command)
            }
            if (widget.equals("radiology")) {
                (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).addCommands(
                    position,
                    command
                )
            }
            if (widget.equals("investigation")) {
                (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).addCommands(
                    position,
                    command
                )
            }
            if (widget.equals("prescription")) {
                (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).addCommands(
                    position,
                    command
                )
            }
        } else {
            if (widget.equals("lab")) {
                (labAdapter as TreatmentKitLabMobileAdapter).addCommands(position, command)
            }
            if (widget.equals("radiology")) {
                (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).addCommands(
                    position,
                    command
                )
            }
            if (widget.equals("investigation")) {
                (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).addCommands(
                    position,
                    command
                )
            }
//            if (widget.equals("prescription")) {
//                (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).addCommands(
//                    position,
//                    command
//                )
//            }
        }
    }

    fun order() {
        if (isModify) {
            modifyOrder()
        } else {
            if (isTablet(requireContext())) {
                orderTab()
            } else {
                orderMobile()
            }
        }
    }

    private fun modifyOrder() {
        val body = UpdateTreatmentKitPreviousOrderReq()

        listNewDiagnosis.forEach { body.patientDiagnosis?.get(0)?.new_diagnosis?.add(it) }
        listRemoveDiagnosis.forEach { body.patientDiagnosis?.get(0)?.remove_details?.add(it) }

        listExistingInvestigation.forEach { body.patientInvestigation?.existing_details?.add(it) }
        headerInvestigation.let { body.patientInvestigation?.header = it }
        listNewDetailsInvestigation.forEach { body.patientInvestigation?.new_details?.add(it) }
        listRemovedDetailsInvestigation.forEach { body.patientInvestigation?.removed_details?.add(it) }

        listExistingLab.forEach { body.patientLab?.existing_details?.add(it) }
        headerLab.let { body.patientLab?.header = it }
        listNewDetailsLab.forEach { body.patientLab?.new_details?.add(it) }
        listRemovedDetailsLab.forEach { body.patientLab?.removed_details?.add(it) }

        listDetailsPrescription.forEach { body.patientPrescription?.details?.add(it) }
        headerPrescription.let { body.patientPrescription?.header = it }

        listExistingRadiology.forEach { body.patientRadiology?.existing_details?.add(it) }
        listNewDetailsRadiology.forEach { body.patientRadiology?.new_details?.add(it) }
        listRemovedDetailsRadiology.forEach { body.patientRadiology?.removed_details?.add(it) }

        body.patientTreatment?.treatment_kit_uuid =
            treatmentKitResponsResponseContent.treatment_kit_uuid
        body.patientTreatment?.uuid = treatmentKitResponsResponseContent.order_id

        viewModel?.modifyTreatmentKit(
            facility_id,
            treatmentKitResponsResponseContent.order_id,
            body,
            updateTreatmentKitPreviousOrderRespCallback
        )
    }

    private fun orderTab() {
        val tkOrderRequestModel: TKOrderRequestModel = TKOrderRequestModel()

        tkOrderRequestModel.patientTreatment?.facility_uuid = facility_id.toString()
        tkOrderRequestModel.patientTreatment?.department_uuid = deparment_UUID.toString()
        tkOrderRequestModel.patientTreatment?.patient_uuid = patient_id.toString()
        tkOrderRequestModel.patientTreatment?.encounter_uuid = encounter_id
        tkOrderRequestModel.patientTreatment?.encounter_type_uuid = encounter_type
        tkOrderRequestModel.patientTreatment?.consultation_uuid = 0
        tkOrderRequestModel.patientTreatment?.treatment_kit_uuid = 0
        tkOrderRequestModel.patientTreatment?.treatment_status_uuid = 0
        tkOrderRequestModel.patientTreatment?.comments = "test comments"

        if ((treatmentDiagnosisAdapter as TreatmentKitDiagnosisAdapter).getall().size > 1) {

            val reqDiagnosis: ArrayList<PatientDiagnosis> = ArrayList()
            reqDiagnosis.clear()

            val diagList = (treatmentDiagnosisAdapter as TreatmentKitDiagnosisAdapter).getall().size
            for (i in 0 until diagList) {

                val treatmentKitDiagOrder: PatientDiagnosis = PatientDiagnosis()
                treatmentKitDiagOrder.facility_uuid =
                    facility_id.toString()
                treatmentKitDiagOrder.department_uuid =
                    deparment_UUID.toString()
                treatmentKitDiagOrder.patient_uuid =
                    patient_id.toString()
                treatmentKitDiagOrder.encounter_uuid = encounter_id
                treatmentKitDiagOrder.encounter_type_uuid = encounter_type
                treatmentKitDiagOrder.consultation_uuid = 0
                treatmentKitDiagOrder.diagnosis_uuid =
                    ((treatmentDiagnosisAdapter as TreatmentKitDiagnosisAdapter).getall()
                        .get(i)?.diagnosisUUiD)
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

            reqDiagnosis.removeAt(reqDiagnosis.size - 1)
            tkOrderRequestModel.patientDiagnosis = reqDiagnosis
        }

        if ((labAdapter as TreatmentKitLabAdapter).getAll().size > 1) {

            tkOrderRequestModel.patientLab?.header?.patient_uuid = patient_id.toString()
            tkOrderRequestModel.patientLab?.header?.encounter_uuid = encounter_id
            tkOrderRequestModel.patientLab?.header?.encounter_type_uuid = encounter_type
            tkOrderRequestModel.patientLab?.header?.lab_master_type_uuid = 1
            tkOrderRequestModel.patientLab?.header?.doctor_uuid = doctor_UUID.toString()
            tkOrderRequestModel.patientLab?.header?.department_uuid = deparment_UUID.toString()
            tkOrderRequestModel.patientLab?.header?.facility_uuid = facility_id.toString()
            tkOrderRequestModel.patientLab?.header?.sub_department_uuid = 0
            tkOrderRequestModel.patientLab?.header?.order_to_location_uuid = 1
            tkOrderRequestModel.patientLab?.header?.consultation_uuid = 0
            tkOrderRequestModel.patientLab?.header?.patient_treatment_uuid = 0
            tkOrderRequestModel.patientLab?.header?.treatment_plan_uuid = 0
            tkOrderRequestModel.patientLab?.header?.order_status_uuid = 0
            tkOrderRequestModel.patientLab?.header?.application_type_uuid = 10

            //wardidbind for ip

            if (encounter_type == AppConstants.TYPE_IN_PATIENT) {
                tkOrderRequestModel.patientLab?.header?.ward_uuid = warduuid
            }

            val requestLabDetail: ArrayList<Detail?> = ArrayList()
            requestLabDetail.clear()

            val labList = (labAdapter as TreatmentKitLabAdapter).getAll().size
            for (i in 0 until labList) {
                val labDetail: Detail = Detail()
                labDetail.profile_uuid = 1
                if ((labAdapter as TreatmentKitLabAdapter).getAll()[i]!!.test_master_id == 0) {
                    labDetail.test_master_uuid =
                        (labAdapter as TreatmentKitLabAdapter).getAll()[i]!!.profile_master_uuid
                } else {
                    labDetail.test_master_uuid =
                        (labAdapter as TreatmentKitLabAdapter).getAll()[i]!!.test_master_id
                }
                labDetail.test_master_uuid =
                    (labAdapter as TreatmentKitLabAdapter).getAll()[i]?.test_master_id
                labDetail.is_profile = false
                labDetail.lab_master_type_uuid = 1
                labDetail.to_department_uuid = 1
                labDetail.order_priority_uuid =
                    (labAdapter as TreatmentKitLabAdapter).getAll()[i]?.selectTypeUUID.toString()
                labDetail.to_location_uuid =
                    (labAdapter as TreatmentKitLabAdapter).getAll()[i]?.selectToLocationUUID.toString()
                labDetail.group_uuid = 0
                labDetail.to_sub_department_uuid = 0
                labDetail.patient_work_order_by = 0
                labDetail.comments = (labAdapter as TreatmentKitLabAdapter).getAll()[i]?.commands
                labDetail.encounter_type_uuid = encounter_type
                labDetail.is_active = 1
                labDetail.status = 1
                labDetail.application_type_uuid = 10

                if (encounter_type == AppConstants.TYPE_IN_PATIENT) {

                    labDetail.ward_uuid = warduuid
                }


                requestLabDetail.add(labDetail)
            }
            requestLabDetail.removeAt(requestLabDetail.size - 1)
            tkOrderRequestModel.patientLab!!.details = requestLabDetail
        }


        if ((treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).getAll().size > 1) {
            tkOrderRequestModel.patientRadiology?.header?.patient_uuid = patient_id.toString()
            tkOrderRequestModel.patientRadiology?.header?.encounter_uuid = encounter_id
            tkOrderRequestModel.patientRadiology?.header?.encounter_type_uuid = encounter_type
            tkOrderRequestModel.patientRadiology?.header?.lab_master_type_uuid = 1
            tkOrderRequestModel.patientRadiology?.header?.consultation_uuid = 0
            tkOrderRequestModel.patientRadiology?.header?.patient_treatment_uuid = 0
            tkOrderRequestModel.patientRadiology?.header?.treatment_plan_uuid = 0
            tkOrderRequestModel.patientRadiology?.header?.doctor_uuid = doctor_UUID.toString()
            tkOrderRequestModel.patientRadiology?.header?.department_uuid =
                deparment_UUID.toString()
            tkOrderRequestModel.patientRadiology?.header?.sub_department_uuid = 0
            tkOrderRequestModel.patientRadiology?.header?.order_to_location_uuid = 1

            val requestRadiologyDetail: ArrayList<DetailXX?> = ArrayList()
            requestRadiologyDetail.clear()


            val radioList =
                (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).getAll().size
            for (i in 0 until radioList) {
                val radiologyDetail: DetailXX = DetailXX()

                radiologyDetail.profile_uuid = 1
                if ((treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).getAll()[i]!!.test_master_id == 0) {
                    radiologyDetail.test_master_uuid =
                        (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).getAll()[i]!!.profile_master_uuid
                } else {
                    radiologyDetail.test_master_uuid =
                        (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).getAll()[i]!!.test_master_id
                }

                radiologyDetail.is_profile = true
                radiologyDetail.lab_master_type_uuid = 2
                radiologyDetail.to_department_uuid = 0
                radiologyDetail.order_priority_uuid =
                    (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).getAll()[i]!!.selectTypeUUID.toString()
                radiologyDetail.to_location_uuid =
                    (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).getAll()[i]!!.selectToLocationUUID.toString()
                radiologyDetail.group_uuid = 0
                radiologyDetail.to_sub_department_uuid = 0
                radiologyDetail.details_comments =
                    (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).getAll()[i]!!.commands
                radiologyDetail.encounter_type_uuid = encounter_type

                requestRadiologyDetail.add(radiologyDetail)

            }
            requestRadiologyDetail.removeAt(requestRadiologyDetail.size - 1)
            tkOrderRequestModel.patientRadiology?.details = requestRadiologyDetail

        }

        if ((treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).getItems().size > 1) {
            tkOrderRequestModel.patientPrescription?.header?.dispense_status_uuid = 1
            tkOrderRequestModel.patientPrescription?.header?.prescription_status_uuid = 2
            tkOrderRequestModel.patientPrescription?.header?.store_master_uuid = storemaster_Id
            tkOrderRequestModel.patientPrescription?.header?.patient_uuid = patient_id.toString()
            tkOrderRequestModel.patientPrescription?.header?.department_uuid =
                deparment_UUID.toString()
            tkOrderRequestModel.patientPrescription?.header?.doctor_uuid = doctor_UUID.toString()
            tkOrderRequestModel.patientPrescription?.header?.encounter_uuid = encounter_id
            tkOrderRequestModel.patientPrescription?.header?.encounter_type_uuid = encounter_type
            tkOrderRequestModel.patientPrescription?.header?.treatment_kit_uuid = 0
            tkOrderRequestModel.patientPrescription?.header?.notes = ""


            val requestPrescriptionDetail: ArrayList<com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.request.DetailX?> =
                ArrayList()
            requestPrescriptionDetail.clear()

            val prescList =
                (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).getItems().size
            for (i in 0 until prescList) {
                val prescriptionDetail: com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.request.DetailX =
                    com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.request.DetailX()

                prescriptionDetail.item_master_uuid =
                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).getItems()[i]?.test_master_id
                prescriptionDetail.drug_route_uuid =
                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).getItems()[i]?.drug_route_id.toString()
                prescriptionDetail.drug_frequency_uuid =
                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).getItems()[i]?.drug_frequency_id.toString()
                prescriptionDetail.duration =
                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).getItems()[i]?.duration.toString()
                prescriptionDetail.duration_period_uuid =
                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).getItems()[i]?.durationPeriodId.toString()
                prescriptionDetail.drug_instruction_uuid =
                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).getItems()[i]?.drug_instruction_id.toString()
                prescriptionDetail.prescribed_quantity = ""

                requestPrescriptionDetail.add(prescriptionDetail)
            }
            requestPrescriptionDetail.removeAt(requestPrescriptionDetail.size - 1)
            tkOrderRequestModel.patientPrescription?.details = requestPrescriptionDetail
        }

        if ((treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).getItems().size > 1) {

            tkOrderRequestModel.patientInvestigation?.header?.patient_uuid = patient_id.toString()
            tkOrderRequestModel.patientInvestigation?.header?.encounter_uuid = encounter_id
            tkOrderRequestModel.patientInvestigation?.header?.encounter_type_uuid = encounter_type
            tkOrderRequestModel.patientInvestigation?.header?.lab_master_type_uuid = 1
            tkOrderRequestModel.patientInvestigation?.header?.consultation_uuid = 0
            tkOrderRequestModel.patientInvestigation?.header?.patient_treatment_uuid = 0
            tkOrderRequestModel.patientInvestigation?.header?.treatment_plan_uuid = 0
            tkOrderRequestModel.patientInvestigation?.header?.doctor_uuid = doctor_UUID.toString()
            tkOrderRequestModel.patientInvestigation?.header?.department_uuid =
                deparment_UUID.toString()
            tkOrderRequestModel.patientInvestigation?.header?.sub_department_uuid = 0
            tkOrderRequestModel.patientInvestigation?.header?.order_to_location_uuid = 1


            val requestInvestigationDetail: ArrayList<DetailXXX?> = ArrayList()
            requestInvestigationDetail.clear()


            val investList =
                (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).getItems().size
            for (i in 0 until investList) {
                val invesDetail: DetailXXX = DetailXXX()
                invesDetail.profile_uuid = 1
                if ((treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).getItems()[i]!!.test_master_id == 0) {
                    invesDetail.test_master_uuid =
                        (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).getItems()[i]!!.profile_master_uuid
                } else {
                    invesDetail.test_master_uuid =
                        (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).getItems()[i]!!.test_master_id
                }
                invesDetail.is_profile = true
                invesDetail.lab_master_type_uuid = 2
                invesDetail.to_department_uuid = 0
                invesDetail.order_priority_uuid =
                    (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).getItems()[i]!!.selectTypeUUID.toString()
                invesDetail.to_location_uuid =
                    (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).getItems()[i]!!.selectToLocationUUID.toString()
                invesDetail.group_uuid = 0
                invesDetail.to_sub_department_uuid = 0
                invesDetail.details_comments =
                    (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).getItems()[i]!!.commands
                invesDetail.encounter_type_uuid = encounter_type

                requestInvestigationDetail.add(invesDetail)
            }
            requestInvestigationDetail.removeAt(requestInvestigationDetail.size - 1)
            tkOrderRequestModel.patientInvestigation?.details = requestInvestigationDetail
        }

        val responseobject = Gson().toJson(tkOrderRequestModel)
        Log.e("RequestOrder", responseobject.toString())

        viewModel?.createOrderTreatmentKit(
            facility_id,
            tkOrderRequestModel,
            orderSaveRetrofitCallback
        )
    }

    fun isLoading(st: Boolean) {

        if (st) {

            customProgressDialog!!.show()

        } else {

            customProgressDialog!!.dismiss()
        }
    }

    private fun orderMobile() {
        val treatmentKitUuid: Int = appPreferences?.getInt(AppConstants.PREF_TKT_UUID) ?: 0
        isLoading(true)
        val tkOrderRequestModel: TKOrderRequestModel = TKOrderRequestModel()

        tkOrderRequestModel.patientTreatment?.facility_uuid = facility_id.toString()
        tkOrderRequestModel.patientTreatment?.department_uuid = deparment_UUID.toString()
        tkOrderRequestModel.patientTreatment?.patient_uuid = patient_id.toString()
        tkOrderRequestModel.patientTreatment?.encounter_uuid = encounter_id
        tkOrderRequestModel.patientTreatment?.encounter_type_uuid = encounter_type
        tkOrderRequestModel.patientTreatment?.consultation_uuid = 0
        tkOrderRequestModel.patientTreatment?.treatment_kit_uuid = treatmentKitUuid
        tkOrderRequestModel.patientTreatment?.treatment_status_uuid = 0
        tkOrderRequestModel.patientTreatment?.comments = "test comments"

        if ((treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter).getall().size > 0) {
            val reqDiagnosis: ArrayList<PatientDiagnosis> = ArrayList()
            reqDiagnosis.clear()

            val diagList =
                (treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter).getall().size
            for (i in 0 until diagList) {
                val treatmentKitDiagOrder: PatientDiagnosis = PatientDiagnosis()
                treatmentKitDiagOrder.facility_uuid = facility_id.toString()
                treatmentKitDiagOrder.department_uuid = deparment_UUID.toString()
                treatmentKitDiagOrder.patient_uuid = patient_id.toString()
                treatmentKitDiagOrder.encounter_uuid = encounter_id
                treatmentKitDiagOrder.encounter_type_uuid = encounter_type
                treatmentKitDiagOrder.consultation_uuid = 0
                treatmentKitDiagOrder.diagnosis_uuid =
                    ((treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter).getall()
                        .get(i)?.diagnosisUUiD)
                treatmentKitDiagOrder.condition_type_uuid = 0
                treatmentKitDiagOrder.condition_status_uuid = 0
                treatmentKitDiagOrder.category_uuid = 0
                treatmentKitDiagOrder.type_uuid = 0
                treatmentKitDiagOrder.grade_uuid = 0
                treatmentKitDiagOrder.body_site_uuid = 0
                treatmentKitDiagOrder.prescription_uuid = 0
                treatmentKitDiagOrder.patient_treatment_uuid = 0
                treatmentKitDiagOrder.encounter_doctor_uuid = doctor_UUID

                reqDiagnosis.add(treatmentKitDiagOrder)
            }
            tkOrderRequestModel.patientDiagnosis = reqDiagnosis
        }

        if ((labAdapter as TreatmentKitLabMobileAdapter).getAll().size > 0) {
            tkOrderRequestModel.patientLab?.header?.patient_uuid = patient_id.toString()
            tkOrderRequestModel.patientLab?.header?.encounter_uuid = encounter_id
            tkOrderRequestModel.patientLab?.header?.encounter_type_uuid = encounter_type
            tkOrderRequestModel.patientLab?.header?.lab_master_type_uuid = 1
            tkOrderRequestModel.patientLab?.header?.doctor_uuid = doctor_UUID.toString()
            tkOrderRequestModel.patientLab?.header?.department_uuid = deparment_UUID.toString()
            tkOrderRequestModel.patientLab?.header?.facility_uuid = facility_id.toString()
            tkOrderRequestModel.patientLab?.header?.sub_department_uuid = 0
            tkOrderRequestModel.patientLab?.header?.order_to_location_uuid = 1
            tkOrderRequestModel.patientLab?.header?.consultation_uuid = 0
            tkOrderRequestModel.patientLab?.header?.patient_treatment_uuid = 0
            tkOrderRequestModel.patientLab?.header?.treatment_plan_uuid = 0
            tkOrderRequestModel.patientLab?.header?.treatment_kit_uuid = treatmentKitUuid
            tkOrderRequestModel.patientLab?.header?.order_status_uuid = 0
            tkOrderRequestModel.patientLab?.header?.application_type_uuid = 10

            //wardidbind for ip

            if (encounter_type == AppConstants.TYPE_IN_PATIENT) {
                tkOrderRequestModel.patientLab?.header?.ward_uuid = warduuid
            }

            val requestLabDetail: ArrayList<Detail?> = ArrayList()
            requestLabDetail.clear()

            val labList = (labAdapter as TreatmentKitLabMobileAdapter).getAll().size
            for (i in 0 until labList) {
                val labDetail: Detail = Detail()
                labDetail.profile_uuid = 1
                if ((labAdapter as TreatmentKitLabMobileAdapter).getAll()[i]!!.test_master_id == 0) {
                    labDetail.test_master_uuid =
                        (labAdapter as TreatmentKitLabMobileAdapter).getAll()[i]!!.profile_master_uuid
                } else {
                    labDetail.test_master_uuid =
                        (labAdapter as TreatmentKitLabMobileAdapter).getAll()[i]!!.test_master_id
                }
                labDetail.test_master_uuid =
                    (labAdapter as TreatmentKitLabMobileAdapter).getAll()[i]?.test_master_id
                labDetail.is_profile = false
                labDetail.lab_master_type_uuid = 1
                labDetail.to_department_uuid = 1
                labDetail.order_priority_uuid =
                    (labAdapter as TreatmentKitLabMobileAdapter).getAll()[i]?.selectTypeUUID.toString()
                labDetail.to_location_uuid =
                    (labAdapter as TreatmentKitLabMobileAdapter).getAll()[i]?.selectToLocationUUID.toString()
                labDetail.group_uuid = 0
                labDetail.to_sub_department_uuid = 0
                labDetail.patient_work_order_by = 0
                labDetail.comments =
                    (labAdapter as TreatmentKitLabMobileAdapter).getAll()[i]?.commands
                labDetail.encounter_type_uuid = encounter_type
                labDetail.is_active = 1
                labDetail.status = 1
                labDetail.application_type_uuid = 10

                if (encounter_type == AppConstants.TYPE_IN_PATIENT) {
                    labDetail.ward_uuid = warduuid
                }
                requestLabDetail.add(labDetail)
            }
//            requestLabDetail.removeAt(requestLabDetail.size - 1)
            tkOrderRequestModel.patientLab!!.details = requestLabDetail
        }

        if ((treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).getAll().size > 0) {
            tkOrderRequestModel.patientRadiology?.header?.patient_uuid = patient_id.toString()
            tkOrderRequestModel.patientRadiology?.header?.encounter_uuid = encounter_id
            tkOrderRequestModel.patientRadiology?.header?.encounter_type_uuid = encounter_type
            tkOrderRequestModel.patientRadiology?.header?.lab_master_type_uuid = 1
            tkOrderRequestModel.patientRadiology?.header?.consultation_uuid = 0
            tkOrderRequestModel.patientRadiology?.header?.patient_treatment_uuid = 0
            tkOrderRequestModel.patientRadiology?.header?.treatment_plan_uuid = 0
            tkOrderRequestModel.patientRadiology?.header?.doctor_uuid = doctor_UUID.toString()
            tkOrderRequestModel.patientRadiology?.header?.department_uuid =
                deparment_UUID.toString()
            tkOrderRequestModel.patientRadiology?.header?.sub_department_uuid = 0
            tkOrderRequestModel.patientRadiology?.header?.order_to_location_uuid = 1
            tkOrderRequestModel.patientRadiology?.header?.encounter_doctor_uuid =
                encounter_doctor_UUID ?: 0
            tkOrderRequestModel.patientRadiology?.header?.treatment_kit_uuid = treatmentKitUuid

            val requestRadiologyDetail: ArrayList<DetailXX?> = ArrayList()
            requestRadiologyDetail.clear()

            val radioList =
                (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).getAll().size
            for (i in 0 until radioList) {
                val radiologyDetail: DetailXX = DetailXX()

                radiologyDetail.profile_uuid = 1
                if ((treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).getAll()[i]!!.test_master_id == 0) {
                    radiologyDetail.test_master_uuid =
                        (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).getAll()[i]!!.profile_master_uuid
                } else {
                    radiologyDetail.test_master_uuid =
                        (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).getAll()[i]!!.test_master_id
                }

                radiologyDetail.is_profile = true
                radiologyDetail.lab_master_type_uuid = 2
                radiologyDetail.to_department_uuid = 0
                radiologyDetail.order_priority_uuid =
                    (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).getAll()[i]!!.selectTypeUUID.toString()
                radiologyDetail.to_location_uuid =
                    (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).getAll()[i]!!.selectToLocationUUID.toString()
                radiologyDetail.group_uuid = 0
                radiologyDetail.to_sub_department_uuid = 0
                radiologyDetail.details_comments =
                    (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).getAll()[i]!!.commands
                radiologyDetail.encounter_type_uuid = encounter_type

                requestRadiologyDetail.add(radiologyDetail)
            }
//            requestRadiologyDetail.removeAt(requestRadiologyDetail.size - 1)
            tkOrderRequestModel.patientRadiology?.details = requestRadiologyDetail
        }

        if ((treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).getItems().size > 0) {
            tkOrderRequestModel.patientPrescription?.header?.dispense_status_uuid = 1
            tkOrderRequestModel.patientPrescription?.header?.prescription_status_uuid = 2
            tkOrderRequestModel.patientPrescription?.header?.store_master_uuid = storemaster_Id
            tkOrderRequestModel.patientPrescription?.header?.patient_uuid = patient_id.toString()
            tkOrderRequestModel.patientPrescription?.header?.department_uuid =
                deparment_UUID.toString()
            tkOrderRequestModel.patientPrescription?.header?.doctor_uuid = doctor_UUID.toString()
            tkOrderRequestModel.patientPrescription?.header?.encounter_uuid = encounter_id
            tkOrderRequestModel.patientPrescription?.header?.encounter_type_uuid = encounter_type
            tkOrderRequestModel.patientPrescription?.header?.treatment_kit_uuid = treatmentKitUuid
            tkOrderRequestModel.patientPrescription?.header?.notes = ""
            tkOrderRequestModel.patientPrescription?.header?.encounter_doctor_uuid =
                encounter_doctor_UUID ?: 0
//            tkOrderRequestModel.patientPrescription?.header?.injection_room_uuid =

            val requestPrescriptionDetail: ArrayList<com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.request.DetailX?> =
                ArrayList()
            requestPrescriptionDetail.clear()

            val prescList =
                (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).getItems().size
            for (i in 0 until prescList) {
                val prescriptionDetail =
                    com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.request.DetailX()

                prescriptionDetail.item_master_uuid =
                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).getItems()[i]?.drug_id
                prescriptionDetail.drug_route_uuid =
                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).getItems()[i]?.drug_route_id.toString()
                prescriptionDetail.drug_frequency_uuid =
                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).getItems()[i]?.drug_frequency_id.toString()
                prescriptionDetail.duration =
                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).getItems()[i]?.drug_duration.toString()
                prescriptionDetail.duration_period_uuid =
                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).getItems()[i]?.drug_period_id.toString()
                prescriptionDetail.drug_instruction_uuid =
                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).getItems()[i]?.drug_instruction_id.toString()
                prescriptionDetail.prescribed_quantity =
                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).getItems()[i]?.drug_quantity.toString()
                prescriptionDetail.is_emar =
                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).getItems()[i]?.drug_is_emar
                        ?: false


                requestPrescriptionDetail.add(prescriptionDetail)
            }
//            requestPrescriptionDetail.removeAt(requestPrescriptionDetail.size - 1)
            tkOrderRequestModel.patientPrescription?.details = requestPrescriptionDetail
        }

        if ((treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).getItems().size > 0) {
            tkOrderRequestModel.patientInvestigation?.header?.patient_uuid = patient_id.toString()
            tkOrderRequestModel.patientInvestigation?.header?.encounter_uuid = encounter_id
            tkOrderRequestModel.patientInvestigation?.header?.encounter_type_uuid = encounter_type
            tkOrderRequestModel.patientInvestigation?.header?.lab_master_type_uuid = 1
            tkOrderRequestModel.patientInvestigation?.header?.consultation_uuid = 0
            tkOrderRequestModel.patientInvestigation?.header?.patient_treatment_uuid = 0
            tkOrderRequestModel.patientInvestigation?.header?.treatment_plan_uuid = 0
            tkOrderRequestModel.patientInvestigation?.header?.treatment_kit_uuid = treatmentKitUuid
            tkOrderRequestModel.patientInvestigation?.header?.doctor_uuid = doctor_UUID.toString()
            tkOrderRequestModel.patientInvestigation?.header?.department_uuid =
                deparment_UUID.toString()
            tkOrderRequestModel.patientInvestigation?.header?.sub_department_uuid = 0
            tkOrderRequestModel.patientInvestigation?.header?.order_to_location_uuid = 1

            val requestInvestigationDetail: ArrayList<DetailXXX?> = ArrayList()
            requestInvestigationDetail.clear()

            val investList =
                (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).getItems().size
            for (i in 0 until investList) {
                val invesDetail: DetailXXX = DetailXXX()
                invesDetail.profile_uuid = 1
                if ((treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).getItems()[i]!!.test_master_id == 0) {
                    invesDetail.test_master_uuid =
                        (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).getItems()[i]!!.profile_master_uuid
                } else {
                    invesDetail.test_master_uuid =
                        (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).getItems()[i]!!.test_master_id
                }
                invesDetail.is_profile = true
                invesDetail.lab_master_type_uuid = 2
                invesDetail.to_department_uuid = 0
                invesDetail.order_priority_uuid =
                    (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).getItems()[i]!!.selectTypeUUID.toString()
                invesDetail.to_location_uuid =
                    (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).getItems()[i]!!.selectToLocationUUID.toString()
                invesDetail.group_uuid = 0
                invesDetail.to_sub_department_uuid = 0
                invesDetail.details_comments =
                    (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).getItems()[i]!!.commands
                invesDetail.encounter_type_uuid = encounter_type

                requestInvestigationDetail.add(invesDetail)
            }
//            requestInvestigationDetail.removeAt(requestInvestigationDetail.size - 1)
            tkOrderRequestModel.patientInvestigation?.details = requestInvestigationDetail
        }

        val responseobject = Gson().toJson(tkOrderRequestModel)
        Log.e("RequestOrder", responseobject.toString())

        viewModel?.createOrderTreatmentKit(
            facility_id,
            tkOrderRequestModel,
            orderSaveRetrofitCallback
        )
    }

    fun showDetailLayout(context: Context, layout: View, img: ImageView) {
        hideAll(context)
        slideDown(context, layout)
        layout.show()
        img.rotation = 270F
    }

    fun hideDetailLayout(context: Context, layout: View, img: ImageView) {
        slideDown(context, layout)
        layout.hide()
        img.rotation = 90F
    }

    private fun hideAll(context: Context) {
        slideDown(context, binding?.llAddDiagnosisDetail!!)
        slideDown(context, binding?.addMedicineDetailLayout!!)
        slideDown(context, binding?.llAddLabDetail!!)
        slideDown(context, binding?.llAddRadiologyDetail!!)
        slideDown(context, binding?.llAddInvestigationDetail!!)

        binding?.llAddDiagnosisDetail?.hide()
        binding?.addMedicineDetailLayout?.hide()
        binding?.llAddLabDetail?.hide()
        binding?.llAddRadiologyDetail?.hide()
        binding?.llAddInvestigationDetail?.hide()

        binding?.imgDownDiagnosis?.rotation = 90F
        binding?.imgDownPrescription?.rotation = 90F
        binding?.imgDownLab?.rotation = 90F
        binding?.imgDownRadiology?.rotation = 90F
        binding?.imgDownInvestigation?.rotation = 90F
    }

    private fun initCompleteCompleteTextView() {
        binding?.actLabTestName?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (binding?.actLabTestName?.hasFocus()!!) {
                    if (s.length > 2) {
                        viewModel?.getLabComplaintSearchResult(
                            facility_id,
                            s.toString(),
                            getLabComplaintSearchRetrofitCallBack
                        )
                    }

                }
            }
        })
        binding?.actLabCode?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 2) {
                    viewModel?.getCodeSearchResult(
                        facility_id,
                        s.toString(),
                        getCodeSearchRetrofitCallBack
                    )
                }
            }
        })
    }

    fun setTestNameorCodeLabAdapter(responseContents: ArrayList<FavSearch>, isFromCode: Boolean) {
        val responseContentAdapter = LabSearchResultAdapter(
            requireContext(),
            R.layout.row_chief_complaint_search_result,
            responseContents,
            true
        )
        if (isFromCode) {
            binding?.actLabCode?.threshold = 1
            binding?.actLabCode?.setAdapter(responseContentAdapter)
            binding?.actLabCode?.showDropDown()
        } else {
            binding?.actLabTestName?.threshold = 1
            binding?.actLabTestName?.setAdapter(responseContentAdapter)
            binding?.actLabTestName?.showDropDown()
        }

        binding?.actLabTestName?.setOnItemClickListener { parent, _, pos, id ->
            val selectedPoi = parent.adapter.getItem(pos) as FavSearch?
            binding?.actLabTestName?.clearFocus()
            binding?.actLabTestName?.setText(selectedPoi?.name)
            binding?.actLabCode?.setText(selectedPoi?.code)
            setOtherLabData(selectedPoi)
            selectedLabData?.test_master_code = selectedPoi?.code
            selectedLabData?.test_master_name = selectedPoi?.name
            selectedLabData?.test_master_id = selectedPoi?.uuid
            selectedLabData?.lab_code = selectedPoi?.code
            selectedLabData?.lab_name = selectedPoi?.name
            selectedLabData?.lab_id = selectedPoi?.uuid
        }

        binding?.actLabCode?.setOnItemClickListener { parent, _, pos, id ->
            val selectedPoi = parent.adapter.getItem(pos) as FavSearch?
            binding?.actLabCode?.clearFocus()
            binding?.actLabCode?.setText(selectedPoi?.code)
            binding?.actLabTestName?.setText(selectedPoi?.name)
            setOtherLabData(selectedPoi)
            selectedLabData?.test_master_code = selectedPoi?.code
            selectedLabData?.test_master_name = selectedPoi?.name
            selectedLabData?.test_master_id = selectedPoi?.uuid
            selectedLabData?.lab_code = selectedPoi?.code
            selectedLabData?.lab_name = selectedPoi?.name
            selectedLabData?.lab_id = selectedPoi?.uuid
        }

    }

    private fun setOtherLabData(
        data: FavSearch?
    ) {
        this.selectedLabData = selectedLabData
        val adapterLabPriority =
            ArrayAdapter<String>(
                requireContext(),
                R.layout.spinner_item,
                priorityMutableNameList.values.toMutableList()
            )
        adapterLabPriority.setDropDownViewResource(R.layout.spinner_item)
        binding?.spinnerLabPriority?.adapter = adapterLabPriority
        if (!isLabEditMode) {
            binding?.spinnerLabPriority?.setSelection(1)
            selectedLabData?.selectTypeUUID = priorityHashMapUUid.get(2)!!
            selectedLabData?.selectTypeName =
                priorityMutableNameList.get(selectedLabData?.selectTypeUUID)
        } else {
            priorityHashMapUUid.get(selectedLabData?.selectTypeUUID)?.let {
                binding?.spinnerLabPriority?.setSelection(
                    it
                )
            }
        }


        val adapterLocation =
            ArrayAdapter<String>(
                requireContext(),
                R.layout.spinner_item,
                orderToLocationList.values.toMutableList()
            )
        adapterLocation.setDropDownViewResource(R.layout.spinner_item)
        binding?.spinnerLabOrderLocation?.adapter = adapterLocation
        if (!isLabEditMode) {
            binding?.spinnerLabOrderLocation?.setSelection(0)

            viewModel?.getToLocationTest(
                getLabToLoctionTestRetrofitCallback,
                facility_id,
                appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)!!,
                data?.uuid
            )
        } else {
            orderLocationHashMap.get(selectedLabData?.selectToLocationUUID)?.let {
                binding?.spinnerLabOrderLocation?.setSelection(
                    it
                )
            }
            isLabEditMode = false
        }
    }

    fun setTestNameorCodeRadiologyAdapter(
        responseContents: ArrayList<FavSearch>,
        isFromCode: Boolean
    ) {
        val responseContentAdapter = LabSearchResultAdapter(
            requireContext(),
            R.layout.row_chief_complaint_search_result,
            responseContents,
            true
        )
        if (isFromCode) {
            binding?.actvRadiologyCode?.threshold = 1
            binding?.actvRadiologyCode?.setAdapter(responseContentAdapter)
            binding?.actvRadiologyTestName?.dismissDropDown()
            binding?.actvRadiologyCode?.showDropDown()
        } else {
            binding?.actvRadiologyTestName?.threshold = 1
            binding?.actvRadiologyTestName?.setAdapter(responseContentAdapter)
            binding?.actvRadiologyCode?.dismissDropDown()
            binding?.actvRadiologyTestName?.showDropDown()
        }

        binding?.actvRadiologyTestName?.setOnItemClickListener { parent, _, pos, id ->
            val selectedPoi = parent.adapter.getItem(pos) as FavSearch?
            binding?.actvRadiologyTestName?.clearFocus()
            binding?.actvRadiologyTestName?.setText(selectedPoi?.name)
            binding?.actvRadiologyCode?.setText(selectedPoi?.code)
            setOtherRadiologyData(selectedPoi)
            selectedRadiologyData?.test_master_code = selectedPoi?.code
            selectedRadiologyData?.test_master_name = selectedPoi?.name
            selectedRadiologyData?.test_master_id = selectedPoi?.uuid
            selectedRadiologyData?.radiology_code = selectedPoi?.code
            selectedRadiologyData?.radiology_name = selectedPoi?.name
            selectedRadiologyData?.radiology_id = selectedPoi?.uuid
        }

        binding?.actvRadiologyCode?.setOnItemClickListener { parent, _, pos, id ->
            val selectedPoi = parent.adapter.getItem(pos) as FavSearch?
            binding?.actvRadiologyCode?.clearFocus()
            binding?.actvRadiologyCode?.setText(selectedPoi?.code)
            binding?.actvRadiologyTestName?.setText(selectedPoi?.name)
            setOtherRadiologyData(selectedPoi)
            selectedRadiologyData?.test_master_code = selectedPoi?.code
            selectedRadiologyData?.test_master_name = selectedPoi?.name
            selectedRadiologyData?.test_master_id = selectedPoi?.uuid
            selectedRadiologyData?.radiology_code = selectedPoi?.code
            selectedRadiologyData?.radiology_name = selectedPoi?.name
            selectedRadiologyData?.radiology_id = selectedPoi?.uuid
        }

    }

    private fun setOtherRadiologyData(
        data: FavSearch?
    ) {
        this.selectedRadiologyData = selectedRadiologyData
        val adapterRadiologyPriority =
            ArrayAdapter<String>(
                requireContext(),
                R.layout.spinner_item,
                radiologyPriorityMutableNameList.values.toMutableList()
            )
        adapterRadiologyPriority.setDropDownViewResource(R.layout.spinner_item)
        binding?.spinnerRadiologyPriority?.adapter = adapterRadiologyPriority
        if (!isRadiologyEditMode) {
            binding?.spinnerRadiologyPriority?.setSelection(1)
            selectedRadiologyData?.selectTypeUUID = radiologyPriorityHashMapUUid.get(2)!!
            selectedRadiologyData?.selectTypeName =
                radiologyPriorityMutableNameList.get(selectedRadiologyData?.selectTypeUUID)
        } else {
//            radiologyPriorityHashMapUUid.get(selectedRadiologyData?.selectTypeUUID)?.let {
//                binding?.spinnerRadiologyTestMethod?.setSelection(
//                    it
//                )
//            }
            radiologyPriorityHashMapUUid.get(selectedRadiologyData?.selectTypeUUID)?.let {
                binding?.spinnerRadiologyPriority?.setSelection(
                    it
                )
            }
        }

        val adapterLocation =
            ArrayAdapter<String>(
                requireContext(),
                R.layout.spinner_item,
                radiologyOrderToLocationList.values.toMutableList()
            )
        adapterLocation.setDropDownViewResource(R.layout.spinner_item)
        binding?.spinnerRadiologyOrderLocation?.adapter = adapterLocation
        if (!isRadiologyEditMode) {
            binding?.spinnerRadiologyOrderLocation?.setSelection(0)

            viewModel?.getToLocationTest(
                getLabToLoctionTestRetrofitCallback,
                facility_id,
                appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)!!,
                data?.uuid
            )
        } else {
            radiologyOrderLocationHashMap.get(selectedLabData?.selectToLocationUUID)?.let {
                binding?.spinnerRadiologyOrderLocation?.setSelection(
                    it
                )
            }
            isRadiologyEditMode = false
        }
    }

    fun setTestNameorCodeInvestigationAdapter(
        responseContents: ArrayList<FavSearch>,
        isFromCode: Boolean
    ) {
        val responseContentAdapter = LabSearchResultAdapter(
            requireContext(),
            R.layout.row_chief_complaint_search_result,
            responseContents,
            true
        )
        if (isFromCode) {
            binding?.actvInvestigationCode?.threshold = 1
            binding?.actvInvestigationCode?.setAdapter(responseContentAdapter)
            binding?.actvInvestigationTestName?.dismissDropDown()
            binding?.actvInvestigationCode?.showDropDown()
        } else {
            binding?.actvInvestigationTestName?.threshold = 1
            binding?.actvInvestigationTestName?.setAdapter(responseContentAdapter)
            binding?.actvInvestigationCode?.dismissDropDown()
            binding?.actvInvestigationTestName?.showDropDown()
        }

        binding?.actvInvestigationTestName?.setOnItemClickListener { parent, _, pos, id ->
            val selectedPoi = parent.adapter.getItem(pos) as FavSearch?
            binding?.actvInvestigationTestName?.clearFocus()
            binding?.actvInvestigationTestName?.setText(selectedPoi?.name)
            binding?.actvInvestigationCode?.setText(selectedPoi?.code)
            setOtherInvestigationData(selectedPoi)
            selectedInvestigationData?.test_master_code = selectedPoi?.code
            selectedInvestigationData?.test_master_name = selectedPoi?.name
            selectedInvestigationData?.test_master_id = selectedPoi?.uuid
            selectedInvestigationData?.investigation_code = selectedPoi?.code
            selectedInvestigationData?.investigation_name = selectedPoi?.name
            selectedInvestigationData?.investigation_id = selectedPoi?.uuid
        }

        binding?.actvInvestigationCode?.setOnItemClickListener { parent, _, pos, id ->
            val selectedPoi = parent.adapter.getItem(pos) as FavSearch?
            binding?.actvInvestigationCode?.clearFocus()
            binding?.actvInvestigationCode?.setText(selectedPoi?.code)
            binding?.actvInvestigationTestName?.setText(selectedPoi?.name)
            setOtherInvestigationData(selectedPoi)
            selectedInvestigationData?.test_master_code = selectedPoi?.code
            selectedInvestigationData?.test_master_name = selectedPoi?.name
            selectedInvestigationData?.test_master_id = selectedPoi?.uuid
            selectedInvestigationData?.investigation_code = selectedPoi?.code
            selectedInvestigationData?.investigation_name = selectedPoi?.name
            selectedInvestigationData?.investigation_id = selectedPoi?.uuid
        }

    }

    private fun setOtherInvestigationData(
        data: FavSearch?
    ) {
        this.selectedInvestigationData = selectedInvestigationData
        val adapterInvestigationPriority =
            ArrayAdapter<String>(
                requireContext(),
                R.layout.spinner_item,
                investigationPriorityMutableNameList.values.toMutableList()
            )
        adapterInvestigationPriority.setDropDownViewResource(R.layout.spinner_item)
        binding?.spinnerInvestigationPriority?.adapter = adapterInvestigationPriority
        if (!isInvestigationEditMode) {
            binding?.spinnerInvestigationPriority?.setSelection(1)
            selectedInvestigationData?.selectTypeUUID = investigationPriorityHashMapUUid.get(2)!!
            selectedInvestigationData?.selectTypeName =
                investigationPriorityMutableNameList.get(selectedInvestigationData?.selectTypeUUID)
        } else {
//            radiologyPriorityHashMapUUid.get(selectedRadiologyData?.selectTypeUUID)?.let {
//                binding?.spinnerRadiologyTestMethod?.setSelection(
//                    it
//                )
//            }
            investigationPriorityHashMapUUid.get(selectedInvestigationData?.selectTypeUUID)?.let {
                binding?.spinnerInvestigationPriority?.setSelection(
                    it
                )
            }
        }

        val adapterLocation =
            ArrayAdapter<String>(
                requireContext(),
                R.layout.spinner_item,
                investigationOrderToLocationList.values.toMutableList()
            )
        adapterLocation.setDropDownViewResource(R.layout.spinner_item)
        binding?.spinnerInvestigationOrderLocation?.adapter = adapterLocation
        if (!isInvestigationEditMode) {
            binding?.spinnerInvestigationOrderLocation?.setSelection(0)

            viewModel?.getToLocationTest(
                getLabToLoctionTestRetrofitCallback,
                facility_id,
                appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)!!,
                data?.uuid
            )
        } else {
            investigationOrderLocationHashMap.get(selectedLabData?.selectToLocationUUID)?.let {
                binding?.spinnerLabOrderLocation?.setSelection(
                    it
                )
            }
            isInvestigationEditMode = false
        }
    }

    fun editLabItem(
        position: Int,
        favouritesModel: FavouritesModel?
    ) {
        labModifyPostion = position
        selectedLabData = FavouritesModel()
        selectedLabData = favouritesModel!!
        selectedLabData!!.isEditableMode = true
        isLabEditMode = true
        binding?.actLabTestName?.setText(selectedLabData?.lab_name)
        binding?.actLabCode?.setText(selectedLabData?.lab_code)
        binding?.spinnerLabPriority?.setSelection(
            positionFromLabPrioritySpinner(
                selectedLabData?.selectTypeUUID ?: 0
            )
        )
        binding?.spinnerLabOrderLocation?.setSelection(
            positionFromLabOrderToLocationSpinner(
                selectedLabData?.selectToLocationUUID ?: 0
            )
        )
        setOtherLabData(null)

    }

    fun editRadiologyItem(
        position: Int,
        favouritesModel: FavouritesModel?
    ) {
        radiologyModifyPostion = position
        selectedRadiologyData = FavouritesModel()
        selectedRadiologyData = favouritesModel!!
        selectedRadiologyData!!.isEditableMode = true
        isRadiologyEditMode = true
        binding?.actvRadiologyTestName?.setText(selectedRadiologyData?.radiology_name)
        binding?.actvRadiologyCode?.setText(selectedRadiologyData?.radiology_code)
        binding?.spinnerRadiologyPriority?.setSelection(
            positionFromRadiologyPrioritySpinner(
                selectedRadiologyData?.selectTypeUUID ?: 0
            )
        )
        binding?.spinnerRadiologyOrderLocation?.setSelection(
            positionFromRadiologyOrderToLocationSpinner(
                selectedRadiologyData?.selectToLocationUUID ?: 0
            )
        )
        setOtherRadiologyData(null)
    }

    fun editInvestigationItem(
        position: Int,
        favouritesModel: FavouritesModel?
    ) {
        investigationModifyPostion = position
        selectedInvestigationData = FavouritesModel()
        selectedInvestigationData = favouritesModel!!
        selectedInvestigationData!!.isEditableMode = true
        isInvestigationEditMode = true
        binding?.actvInvestigationTestName?.setText(selectedInvestigationData?.investigation_name)
        binding?.actvInvestigationCode?.setText(selectedInvestigationData?.investigation_code)
        binding?.spinnerInvestigationPriority?.setSelection(
            positionFromInvestigationPrioritySpinner(
                selectedInvestigationData?.selectTypeUUID ?: 0
            )
        )
        binding?.spinnerInvestigationOrderLocation?.setSelection(
            positionFromInvestigationOrderToLocationSpinner(
                selectedInvestigationData?.selectToLocationUUID ?: 0
            )
        )
        setOtherInvestigationData(null)
    }

    fun setOrderTolocationTestMethod(toLocationUuid: Int) {
        val check =
            this.orderLocationHashMap.any { it.key == toLocationUuid }
        if (check) {
            binding?.spinnerLabOrderLocation?.setSelection(orderLocationHashMap[toLocationUuid]!!)
            selectedLabData?.selectToLocationUUID = toLocationUuid
            selectedLabData?.selectedLocationName =
                orderToLocationList.get(selectedLabData?.selectToLocationUUID)!!
        }
    }

    fun setRadiologyOrderTolocationTestMethod(toLocationUuid: Int) {
        val check =
            this.orderLocationHashMap.any { it.key == toLocationUuid }
        if (check) {
            binding?.spinnerRadiologyOrderLocation?.setSelection(radiologyOrderToLocationHashMap[toLocationUuid]!!)
            selectedRadiologyData?.selectToLocationUUID = toLocationUuid
            selectedRadiologyData?.selectedLocationName =
                radiologyOrderToLocationList.get(selectedRadiologyData?.selectToLocationUUID)!!
        }
    }

    private fun initLabAddView() {
        binding?.cvLabAdd?.setOnClickListener {
            if (binding?.actLabCode?.text?.isEmpty()!! || binding?.actLabTestName?.text?.isEmpty()!!) {
                Toast.makeText(requireContext(), "Please Select  TestName ", Toast.LENGTH_SHORT)
                    .show()
            } else if (selectedLabData?.selectTypeUUID == 0) {
                Toast.makeText(requireContext(), "Please Select Priority", Toast.LENGTH_SHORT)
                    .show()
            } else if (selectedLabData?.selectToLocationUUID == 0) {
                Toast.makeText(
                    requireContext(),
                    "Please Select Order To Location",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (selectedLabData?.isEditableMode!!) {
                    selectedLabData?.isEditableMode = false
                    selectedLabData?.labDataSelected = false
                    labMobileDataArrayList?.set(labModifyPostion!!, selectedLabData)
                    (labAdapter as TreatmentKitLabMobileAdapter).updateAdapter(
                        labMobileDataArrayList!!
                    )
                    clearLabAddData()
                    hideDetailLayout(
                        requireContext(),
                        binding?.llAddLabDetail!!,
                        binding?.imgDownLab!!
                    )
                    if (isModify) {
                        val patientLabDetail = NewDetailX(
                            doctor_uuid = treatmentKitResponsResponseContent.doctor_id.toString(),
                            encounter_type_uuid = treatmentKitResponsResponseContent.encounter_type,
                            encounter_uuid = treatmentKitResponsResponseContent.encounter_type_uuid.toString(),
                            from_department_uuid = treatmentKitResponsResponseContent.department_id.toString(),
                            is_ordered = true,
                            is_profile = false,
                            lab_master_type_uuid = 1,
                            order_priority_uuid = "2",
                            order_status_uuid = 1,
                            patient_uuid = treatmentKitResponsResponseContent.patient_id.toString(),
                            patient_work_order_by = 1,
                            sample_type_uuid = 23,
                            test_master_uuid = 1335,
                            to_department_uuid = 0,
                            to_location_uuid = 2668,
                            to_sub_department_uuid = 0,
                            type_of_method_uuid = 7
                        )
                        listNewDetailsLab.add(patientLabDetail)
                    }
                } else {
                    val check =
                        labMobileDataArrayList?.any { it!!.test_master_id == selectedLabData?.test_master_id }
                    if (!check!!) {
                        labMobileDataArrayList?.add(selectedLabData!!)
                        (labAdapter as TreatmentKitLabMobileAdapter).updateAdapter(
                            labMobileDataArrayList!!
                        )
                        clearLabAddData()
                        hideDetailLayout(
                            requireContext(),
                            binding?.llAddLabDetail!!,
                            binding?.imgDownLab!!
                        )
                        if (isModify) {
                            val patientLabDetail = NewDetailX(
                                doctor_uuid = treatmentKitResponsResponseContent.doctor_id.toString(),
                                encounter_type_uuid = treatmentKitResponsResponseContent.encounter_type,
                                encounter_uuid = treatmentKitResponsResponseContent.encounter_type_uuid.toString(),
                                from_department_uuid = treatmentKitResponsResponseContent.department_id.toString(),
                                is_ordered = true,
                                is_profile = false,
                                lab_master_type_uuid = 1,
                                order_priority_uuid = "2",
                                order_status_uuid = 1,
                                patient_uuid = treatmentKitResponsResponseContent.patient_id.toString(),
                                patient_work_order_by = 1,
                                sample_type_uuid = 23,
                                test_master_uuid = 1335,
                                to_department_uuid = 0,
                                to_location_uuid = 2668,
                                to_sub_department_uuid = 0,
                                type_of_method_uuid = 7
                            )
                            listExistingLab.add(patientLabDetail)
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Already Item available in the list",
                            Toast.LENGTH_LONG
                        )?.show()
                    }
                }
            }
        }

        binding?.cvLabClear?.setOnClickListener {
            clearLabAddData()
        }
    }

    private fun setLabSpinnerClickListener() {
        binding?.spinnerLabOrderLocation?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent?.getItemAtPosition(pos).toString()
                    selectedLabData?.selectToLocationUUID =
                        orderToLocationList.filterValues { it == itemValue }.keys.toList()[0]
                    selectedLabData?.selectedLocationName =
                        orderToLocationList.get(selectedLabData?.selectToLocationUUID)!!
                }

            }



        binding?.spinnerLabPriority?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent?.getItemAtPosition(pos).toString()
                    selectedLabData?.selectTypeUUID =
                        priorityMutableNameList.filterValues { it == itemValue }.keys.toList()[0]
                    selectedLabData?.selectTypeName =
                        priorityMutableNameList.get(selectedLabData?.selectTypeUUID)
                }
            }
    }

    fun clearLabAddData() {
        binding?.actLabTestName?.setText("")
        binding?.actLabCode?.setText("")
        binding?.spinnerLabOrderLocation?.setSelection(0)
        binding?.spinnerLabPriority?.setSelection(2)
        selectedLabData = FavouritesModel()
    }

    private fun clearRadiology() {
        binding?.actvRadiologyCode?.setText("")
        binding?.actvRadiologyTestName?.setText("")
        binding?.spinnerRadiologyPriority?.setSelection(0)
        binding?.spinnerRadiologyOrderLocation?.setSelection(0)
    }

    private fun setRadiologyPriorityAdapter(responseContents: List<RadiologyTypeResponseContent?>?) {
        val data = RadiologyTypeResponseContent()
        data.uuid = 0
        data.name = "select Type"
        radiologyPriorityListFilter?.add(data)
        responseContents?.let { radiologyPriorityListFilter?.addAll(it) }
        radiologyPriorityMutableNameList =
            radiologyPriorityListFilter?.map { it?.uuid!! to it.name }!!.toMap()
                .toMutableMap()
        radiologyPriorityHashMapUUid.clear()
        for (i in radiologyPriorityListFilter?.indices!!) {
            radiologyPriorityHashMapUUid[i] =
                radiologyPriorityListFilter?.get(i)?.uuid ?: 0
        }
//        val adapter = ArrayAdapter(
//            requireContext(),
//            R.layout.spinner_item,
//            radiologyPriorityListFilter!!
//        )
//        adapter.setDropDownViewResource(R.layout.spinner_item)
//        binding?.spinnerRadiologyPriority?.adapter = adapter

//        typeNamesList = responseContents?.map { it?.uuid!! to it.name!! }!!.toMap().toMutableMap()
//        radiologyTestTypeList = responseContents
    }

    private fun setRadiologySpinnerClickListiner() {
        binding?.spinnerRadiologyOrderLocation?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent?.getItemAtPosition(pos).toString()
                    selectedRadiologyData?.selectToLocationUUID =
                        radiologyOrderToLocationList.filterValues { it == itemValue }.keys.toList()[0]
                    selectedRadiologyData?.selectedLocationName =
                        radiologyOrderToLocationList.get(selectedRadiologyData?.selectToLocationUUID)!!
                }
            }

        binding?.spinnerRadiologyPriority?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent?.getItemAtPosition(pos).toString()
                    selectedRadiologyData?.selectTypeUUID =
                        radiologyPriorityMutableNameList.filterValues { it == itemValue }.keys.toList()[0]
                    selectedRadiologyData?.selectTypeName =
                        radiologyPriorityMutableNameList.get(selectedRadiologyData?.selectTypeUUID)
                }
            }
    }

    private fun positionFromLabPrioritySpinner(uuid: Int): Int {
        radiologyPriorityHashMapUUid.entries.forEach { t: MutableMap.MutableEntry<Int, Int>? ->
            if (uuid == t?.value) {
                return t.key
            }
        }
        return 0
    }

    private fun positionFromLabOrderToLocationSpinner(uuid: Int): Int {
        radiologyOrderLocationHashMap.entries.forEach { t: MutableMap.MutableEntry<Int, Int>? ->
            if (uuid == t?.value) {
                return t.key
            }
        }
        return 0
    }

    private fun positionFromRadiologyPrioritySpinner(uuid: Int): Int {
        radiologyPriorityHashMapUUid.entries.forEach { t: MutableMap.MutableEntry<Int, Int>? ->
            if (uuid == t?.value) {
                return t.key
            }
        }
        return 0
    }

    private fun positionFromRadiologyOrderToLocationSpinner(uuid: Int): Int {
        radiologyOrderLocationHashMap.entries.forEach { t: MutableMap.MutableEntry<Int, Int>? ->
            if (uuid == t?.value) {
                return t.key
            }
        }
        return 0
    }

    private fun positionFromInvestigationPrioritySpinner(uuid: Int): Int {
        radiologyPriorityHashMapUUid.entries.forEach { t: MutableMap.MutableEntry<Int, Int>? ->
            if (uuid == t?.value) {
                return t.key
            }
        }
        return 0
    }

    private fun positionFromInvestigationOrderToLocationSpinner(uuid: Int): Int {
        radiologyOrderLocationHashMap.entries.forEach { t: MutableMap.MutableEntry<Int, Int>? ->
            if (uuid == t?.value) {
                return t.key
            }
        }
        return 0
    }

    private fun clearInvestigation() {
        binding?.actvInvestigationCode?.setText("")
        binding?.actvInvestigationTestName?.setText("")
        binding?.spinnerInvestigationPriority?.setSelection(0)
        binding?.spinnerInvestigationOrderLocation?.setSelection(0)
    }

    private fun setInvestigationPriorityAdapter(responseContents: List<InvestigationTypeResponseContent?>?) {
        val data = InvestigationTypeResponseContent()
        data.uuid = 0
        data.name = "select Type"
        investigationPriorityListFilter?.add(data)
        responseContents?.let { investigationPriorityListFilter?.addAll(it) }
        investigationPriorityMutableNameList =
            investigationPriorityListFilter?.map { it?.uuid!! to it.name }!!.toMap()
                .toMutableMap()
        investigationPriorityHashMapUUid.clear()
        for (i in investigationPriorityListFilter?.indices!!) {
            investigationPriorityHashMapUUid[i] =
                investigationPriorityListFilter?.get(i)?.uuid ?: 0
        }
    }

    private fun setInvestigationSpinnerClickListener() {
        binding?.spinnerInvestigationOrderLocation?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent?.getItemAtPosition(pos).toString()
                    selectedInvestigationData?.selectToLocationUUID =
                        investigationOrderToLocationList.filterValues { it == itemValue }.keys.toList()[0]
                    selectedInvestigationData?.selectedLocationName =
                        investigationOrderToLocationList.get(selectedInvestigationData?.selectToLocationUUID)!!
                }
            }

        binding?.spinnerInvestigationPriority?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val itemValue = parent?.getItemAtPosition(pos).toString()
                    selectedInvestigationData?.selectTypeUUID =
                        investigationPriorityMutableNameList.filterValues { it == itemValue }.keys.toList()[0]
                    selectedInvestigationData?.selectTypeName =
                        investigationPriorityMutableNameList.get(selectedInvestigationData?.selectTypeUUID)
                }
            }
    }

    private fun getLabPriorityKeyFromValue(
        uuid: Int
    ): String {
        priorityListFilter?.forEach { labTypeResponseContent ->
            if (labTypeResponseContent?.uuid ?: 0 == uuid)
                return labTypeResponseContent?.name ?: ""
        }
        return ""
    }

    private fun getLabLocationKeyFromValue(
        uuid: Int
    ): String {
        orderLocationListFilter?.forEach { labTypeResponseContent ->
            if (labTypeResponseContent?.uuid ?: 0 == uuid)
                return labTypeResponseContent?.location_name ?: ""
        }
        return ""
    }

    private fun getRadiologyPriorityKeyFromValue(
        uuid: Int
    ): String {
        radiologyPriorityListFilter?.forEach {
            if (it?.uuid ?: 0 == uuid)
                return it?.name ?: ""
        }
        return ""
    }

    private fun getRadiologyLocationKeyFromValue(
        uuid: Int
    ): String {
        radiologyOrderLocationListFilter?.forEach {
            if (it?.uuid ?: 0 == uuid)
                return it?.location_name ?: ""
        }
        return ""
    }

    private fun getInvestigationPriorityKeyFromValue(
        uuid: Int
    ): String {
        investigationPriorityListFilter?.forEach {
            if (it?.uuid ?: 0 == uuid)
                return it?.name ?: ""
        }
        return ""
    }

    private fun getInvestigationLocationKeyFromValue(
        uuid: Int
    ): String {
        investigationOrderLocationListFilter?.forEach {
            if (it?.uuid ?: 0 == uuid)
                return it?.location_name ?: ""
        }
        return ""
    }

    private val updateTreatmentKitPreviousOrderRespCallback =
        object : RetrofitCallback<UpdateTreatmentKitPreviousOrderResp> {
            override fun onSuccessfulResponse(responseBody: Response<UpdateTreatmentKitPreviousOrderResp>?) {
                if (responseBody?.body()?.code == 200) {
                    utils?.showToast(
                        R.color.positiveToast,
                        binding?.mainLayout!!,
                        responseBody.body()?.message ?: ""
                    )
                }
            }

            override fun onBadRequest(response: Response<UpdateTreatmentKitPreviousOrderResp>) {
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

    val getInvestigationComplaintSearchRetrofitCallBack =
        object : RetrofitCallback<TreatmentkitSearchResponseModel> {
            override fun onSuccessfulResponse(response: Response<TreatmentkitSearchResponseModel>) {
                responseContents = Gson().toJson(response.body()?.responseContents)
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    if (isTablet(requireContext())) {
                        (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).setAdapter(
                            dropdownReferenceView,
                            (response.body()?.responseContents as ArrayList<TreatmentkitSearchresponseContent>?)!!,
                            searchPosition, investSearch
                        )
                    } else {
//                        (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).setAdapter(
//                            dropdownReferenceView,
//                            (response.body()?.responseContents as ArrayList<TreatmentkitSearchresponseContent>?)!!,
//                            searchPosition, investSearch
//                        )

                        val favSearchList = ArrayList<FavSearch>()
                        response.body()?.responseContents?.forEach {
                            val fav = FavSearch(
                                code = it?.code ?: "",
                                department_uuid = it?.department_uuid ?: 0,
                                is_active = it?.is_active ?: false,
                                name = it?.name ?: "",
                                status = it?.status ?: false,
                                sub_department_uuid = it?.sub_department_uuid ?: 0,
                                type = it?.type ?: "",
                                uuid = it?.uuid ?: 0
                            )
                            favSearchList.add(fav)
                        }


                        setTestNameorCodeInvestigationAdapter(
                            favSearchList,
                            false
                        )
                    }
                }

            }

            override fun onBadRequest(response: Response<TreatmentkitSearchResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: TreatmentkitSearchResponseModel
                try {

                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        TreatmentkitSearchResponseModel::class.java
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

    val getRadiologyComplaintSearchRetrofitCallBack =
        object : RetrofitCallback<FavSearchResponce> {
            override fun onSuccessfulResponse(response: Response<FavSearchResponce>) {
                responseContents = Gson().toJson(response.body()?.responseContents)

                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    if (isTablet(requireContext())) {
                        (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).setAdapter(
                            dropdownReferenceView,
                            response.body()?.responseContents!!, searchPosition,
                            radioSearch
                        )
                    } else {
//                        (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).setAdapter(
//                            dropdownReferenceView,
//                            response.body()?.responseContents!!,
//                            searchPosition,
//                            radioSearch
//                        )

                        setTestNameorCodeRadiologyAdapter(
                            response.body()?.responseContents!!,
                            false
                        )
                    }
                }

            }

            override fun onBadRequest(response: Response<FavSearchResponce>) {
                val gson = GsonBuilder().create()
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
                viewModel!!.progress.value = 8
            }
        }

    val getPresriptionComplaintSearchRetrofitCallBack =
        object : RetrofitCallback<PrescriptionSearchResponseModel> {
            override fun onSuccessfulResponse(response: Response<PrescriptionSearchResponseModel>) {
                responseContents = Gson().toJson(response.body()?.responseContents)
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    if (isTablet(requireContext())) {
                        (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).setPrescriptionAdapter(
                            dropdownReferenceView,
                            response.body()?.responseContents,
                            selectedSearchPosition
                        )
                    } else {

                        setSearchPrescription(response.body()?.responseContents!!)

                    }

                }
            }

            override fun onBadRequest(response: Response<PrescriptionSearchResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: PrescriptionSearchResponseModel
                try {

                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        PrescriptionSearchResponseModel::class.java
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

    var getDignosisSearchRetrofitCallBack =
        object : RetrofitCallback<DiagonosisSearchResponse> {
            override fun onSuccessfulResponse(response: Response<DiagonosisSearchResponse>?) {

                responseContents = Gson().toJson(response?.body()?.responseContents)
                if (response?.body()?.responseContents?.isNotEmpty()!!) {
                    if (isTablet(requireContext())) {
                        (treatmentDiagnosisAdapter as TreatmentKitDiagnosisAdapter).setAdapter(
                            dropdownReferenceView,
                            response.body()?.responseContents!!,
                            selectedSearchPosition,
                            diagSearch
                        )
                    } else {
                        /*  (treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter).setAdapter(
                              dropdownReferenceView,
                              response.body()?.responseContents!!,
                              selectedSearchPosition,
                              diagSearch
                          )*/
                        setDiagnosisSearchData(response.body()?.responseContents!!)
                    }

                }
            }

            override fun onBadRequest(response: Response<DiagonosisSearchResponse>) {
                val gson = GsonBuilder().create()
                val responseModel: FavouritesResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        FavouritesResponseModel::class.java
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

    private fun setDiagnosisSearchData(responseContents: List<ResponseContentsdiagonosissearch>) {


        if (diagSearch == 1) {


            val responseContentAdapter = DianosisSearchResultAdapter(
                requireContext(),
                R.layout.row_chief_complaint_search_result,
                responseContents
            )
            binding?.actvDiagnosis!!.threshold = 1
            binding?.actvDiagnosis!!.setAdapter(responseContentAdapter)
            binding?.actvDiagnosis!!.showDropDown()
            binding?.actvDiagnosis!!.setOnItemClickListener { parent, _, position, id ->

                val selectedPoi =
                    parent.adapter.getItem(position) as ResponseContentsdiagonosissearch?

                val getData =
                    (treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter).getall()

                val check = getData.any { it!!.diagnosis_id == selectedPoi?.uuid.toString() }

                if (!check) {

                    binding?.actvDiagnosis!!.setText(selectedPoi?.name)
                    binding?.actvDiagnosisCode?.setText(selectedPoi?.code)

                    //   var AddNew=DiagnosisNewData()

                    TKtDiagnosisAddNew.diagnosis_name = selectedPoi?.name
                    TKtDiagnosisAddNew.is_snomed = 0
                    TKtDiagnosisAddNew.diagnosis_id = selectedPoi?.uuid.toString()
                    TKtDiagnosisAddNew.diagnosis_code = selectedPoi?.code.toString()


                } else {
                    binding?.actvDiagnosis!!.setText("")

                    Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)
                        ?.show()

                }
            }

        } else {

            val responseContentAdapter = DianosisCodeSearchResultAdapter(
                requireContext(),
                R.layout.row_chief_complaint_search_result,
                responseContents
            )
            binding?.actvDiagnosisCode!!.threshold = 1
            binding?.actvDiagnosisCode!!.setAdapter(responseContentAdapter)
            binding?.actvDiagnosisCode!!.showDropDown()
            binding?.actvDiagnosisCode!!.setOnItemClickListener { parent, _, position, id ->

                val selectedPoi =
                    parent.adapter.getItem(position) as ResponseContentsdiagonosissearch?

                val getData =
                    (treatmentDiagnosisAdapter as TreatmentKitDiagnosisMobileAdapter).getall()

                val check = getData.any { it!!.diagnosis_id == selectedPoi?.uuid.toString() }

                if (!check) {

                    binding?.actvDiagnosis!!.setText(selectedPoi?.name)
                    binding?.actvDiagnosisCode?.setText(selectedPoi?.code)

                    //   var AddNew=DiagnosisNewData()

                    TKtDiagnosisAddNew.diagnosis_name = selectedPoi?.name
                    TKtDiagnosisAddNew.is_snomed = 0
                    TKtDiagnosisAddNew.diagnosis_id = selectedPoi?.uuid.toString()
                    TKtDiagnosisAddNew.diagnosis_code = selectedPoi?.code.toString()


                } else {
                    binding?.actvDiagnosisCode!!.setText("")

                    Toast.makeText(context, "Already Item available in the list", Toast.LENGTH_LONG)
                        ?.show()

                }
            }
        }
    }

    val getLabComplaintSearchRetrofitCallBack =
        object : RetrofitCallback<FavSearchResponce> {
            override fun onSuccessfulResponse(response: Response<FavSearchResponce>) {
                responseContents = Gson().toJson(response.body()?.responseContents)
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    if (isTablet(requireContext())) {
                        (labAdapter as TreatmentKitLabAdapter).setAdapter(
                            dropdownReferenceView,
                            response.body()?.responseContents!!,
                            searchposition,
                            labSearch,
                            SpinnerToLocation
                        )
                    } else {
                        setTestNameorCodeLabAdapter(response.body()?.responseContents!!, false)
                    }
                }
            }

            override fun onBadRequest(response: Response<FavSearchResponce>) {
                val gson = GsonBuilder().create()
                val responseModel: ComplaintSearchResponseModel
                try {

                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        ComplaintSearchResponseModel::class.java
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

    val getCodeSearchRetrofitCallBack =
        object : RetrofitCallback<FavSearchResponce> {
            override fun onSuccessfulResponse(response: Response<FavSearchResponce>) {
                setTestNameorCodeLabAdapter(response.body()?.responseContents!!, true)
            }

            override fun onBadRequest(response: Response<FavSearchResponce>) {
                val gson = GsonBuilder().create()
                val responseModel: ComplaintSearchResponseModel
                try {

                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        ComplaintSearchResponseModel::class.java
                    )
                    utils?.showToast(
                        R.color.negativeToast,
                        mainLayout!!,
                        responseModel.message!!
                    )
                } catch (e: Exception) {
                    utils?.showToast(
                        R.color.negativeToast,
                        mainLayout!!,
                        getString(R.string.something_went_wrong)
                    )
                    e.printStackTrace()
                }
            }

            override fun onServerError(response: Response<*>) {
                utils?.showToast(
                    R.color.negativeToast, mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onUnAuthorized() {
                utils?.showToast(
                    R.color.negativeToast,
                    mainLayout!!,
                    getString(R.string.unauthorized)
                )
            }

            override fun onForbidden() {
                utils?.showToast(
                    R.color.negativeToast,
                    mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(failure: String) {
                utils?.showToast(R.color.negativeToast, mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    val getFrequencyRetrofitCallback =
        object : RetrofitCallback<PrescriptionFrequencyResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<PrescriptionFrequencyResponseModel>?) {

                if (isTablet(requireContext())) {
                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).setFrequencyList(
                        responseBody?.body()?.responseContents
                    )

                } else {
                    /*  (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).setFrequencyList(
                          responseBody?.body()?.responseContents
                      )*/

                    freqencyList =
                        responseBody?.body()?.responseContents as ArrayList<PrescriptionFrequencyresponseContent>
                }
                viewModel?.getRouteDetails(getRouteRetrofitCallback, facility_id)
            }

            override fun onBadRequest(errorBody: Response<PrescriptionFrequencyResponseModel>?) {
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

    val getRouteRetrofitCallback =
        object : RetrofitCallback<TreatmentPrescRouteSpinnerResponse> {
            override fun onSuccessfulResponse(responseBody: Response<TreatmentPrescRouteSpinnerResponse>?) {

                if (isTablet(requireContext())) {
                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).setadapterTypeValue(
                        responseBody?.body()?.responseContents
                    )

                } else {
                    /*   (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).setadapterTypeValue(
                           responseBody?.body()?.responseContents
                       )*/

                    RouteList =
                        responseBody?.body()?.responseContents!! as ArrayList<TreatmentPrescRouteSpinnerresponseContent>
                }
                viewModel?.getInstructionDetails(getInstructionRetrofitCallback, facility_id)
            }

            override fun onBadRequest(errorBody: Response<TreatmentPrescRouteSpinnerResponse>?) {

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

    val getInjectionRetrofitCallback =
        object : RetrofitCallback<InjectionDepartMentResponce> {
            override fun onSuccessfulResponse(responseBody: Response<InjectionDepartMentResponce>?) {

                if (isTablet(requireContext())) {
                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).setInjectionList(
                        responseBody?.body()?.responseContents
                    )
                } else {
                    /*      (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).setInjectionList(
                              responseBody?.body()?.responseContents
                          )*/

                    InjectionList =
                        responseBody?.body()?.responseContents!! as ArrayList<InjectionDepartment>


                }
            }

            override fun onBadRequest(errorBody: Response<InjectionDepartMentResponce>?) {
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

    val getInstructionRetrofitCallback =
        object : RetrofitCallback<PresInstructionResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<PresInstructionResponseModel>?) {

                if (isTablet(requireContext())) {
                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).setInstructionList(
                        responseBody?.body()?.responseContents
                    )

                    viewModel?.getInjectionDetails(getInjectionRetrofitCallback, facility_id)

                } else {
//                    (treatmentprescriptionAdapter as TreatmentKitPrescriptionMobileAdapter).setInstructionList(
//                        responseBody?.body()?.responseContents
//                    )

                    InstructionList =
                        responseBody?.body()?.responseContents!! as ArrayList<PresInstructionResponseContent>

                    viewModel?.getInjectionDetails(getInjectionRetrofitCallback, facility_id)

                }
            }

            override fun onBadRequest(errorBody: Response<PresInstructionResponseModel>?) {
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

    private val getPrescriptionDurationRetrofitCallBack =
        object : RetrofitCallback<PrescriptionDurationResponseModel> {
            override fun onSuccessfulResponse(response: Response<PrescriptionDurationResponseModel>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    if (isTablet(requireContext())) {
                        (treatmentprescriptionAdapter as TreatmentKitPrescriptionAdapter).setDuration(
                            response.body()?.responseContents!!
                        )
                    } else {


                        setDurationPeriod(response.body()?.responseContents!!)
                    }
                }
            }

            override fun onBadRequest(response: Response<PrescriptionDurationResponseModel>) {
                val gson = GsonBuilder().create()
                val responseModel: PrescriptionDurationResponseModel
                try {

                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
                        PrescriptionDurationResponseModel::class.java
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

    private val getRadiologyCodeSearchRespCallback =
        object : RetrofitCallback<GetRadiologyCodeSearchResp> {
            override fun onSuccessfulResponse(responseBody: Response<GetRadiologyCodeSearchResp>?) {

                val favList = ArrayList<FavSearch>()
                responseBody?.body()?.responseContents?.forEach {
                    val fav = FavSearch(
                        code = it.code ?: "",
                        department_uuid = it.department_uuid ?: 0,
                        is_active = it.is_active ?: false,
                        name = it.name ?: "",
                        status = it.status ?: false,
                        sub_department_uuid = it.sub_department_uuid ?: 0,
                        type = it.type ?: "",
                        uuid = it.uuid ?: 0,
                        type_of_method_uuid = it.type_of_method_uuid ?: 0,
                        sample_type_uuid = it.sample_type_uuid ?: 0
                    )
                    favList.add(fav)
                }
                setTestNameorCodeRadiologyAdapter(favList, true)
            }

            override fun onBadRequest(response: Response<GetRadiologyCodeSearchResp>) {
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

    private val getInvestigationCodeSearchRespCallback =
        object : RetrofitCallback<GetInvestigationCodeSearchResp> {
            override fun onSuccessfulResponse(responseBody: Response<GetInvestigationCodeSearchResp>?) {

                val favList = ArrayList<FavSearch>()
                responseBody?.body()?.responseContents?.forEach {
                    val fav = FavSearch(
                        code = it.code ?: "",
                        department_uuid = it.department_uuid ?: 0,
                        is_active = it.is_active ?: false,
                        name = it.name ?: "",
                        status = it.status ?: false,
                        sub_department_uuid = it.sub_department_uuid ?: 0,
                        type = it.type ?: "",
                        uuid = it.uuid ?: 0,
                        type_of_method_uuid = it.type_of_method_uuid ?: 0,
                        sample_type_uuid = it.sample_type_uuid ?: 0
                    )
                    favList.add(fav)
                }
                setTestNameorCodeInvestigationAdapter(favList, true)
            }

            override fun onBadRequest(response: Response<GetInvestigationCodeSearchResp>) {
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

    //getEncounterid
    private val fetchEncounterRetrofitCallBack =
        object : RetrofitCallback<FectchEncounterResponseModel> {
            override fun onSuccessfulResponse(response: Response<FectchEncounterResponseModel>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    val encounterResponseContent = response.body()?.responseContents!!
                    doctor_UUID = encounterResponseContent.get(0)?.encounter_doctors?.get(0)?.uuid!!
                    encounter_id = encounterResponseContent.get(0)?.uuid!!
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_DOCTOR_UUID, doctor_UUID!!)
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_UUID, encounter_id!!)
                    order()

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

    val createEncounterRetrofitCallback =
        object : RetrofitCallback<CreateEncounterResponseModel> {
            override fun onSuccessfulResponse(response: Response<CreateEncounterResponseModel>) {

                doctor_UUID =
                    response.body()?.responseContents?.encounterDoctor?.doctor_uuid?.toInt()
                encounter_doctor_UUID =
                    response.body()?.responseContents?.encounterDoctor?.uuid!!.toInt()
                encounter_id = response.body()?.responseContents?.encounter?.uuid!!.toInt()
                patient_id =
                    response.body()?.responseContents?.encounterDoctor?.patient_uuid!!.toInt()

                order()
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

    val getLabToLoctionTestRetrofitCallback =
        object : RetrofitCallback<GetToLocationTestResponse> {
            override fun onSuccessfulResponse(responseBody: Response<GetToLocationTestResponse>?) {
                if (isTablet(requireContext())) {
                    Log.e("spinnerData", responseBody?.body()?.responseContents.toString())
                    (labAdapter as TreatmentKitLabAdapter).setToLocation(
                        responseBody?.body()?.responseContents,
                        SpinnerToLocation,
                        searchposition
                    )
                } else {
                    responseBody?.body()?.responseContents?.to_location_uuid?.let {
                        setOrderTolocationTestMethod(
                            it
                        )
                    }
                }
            }

            override fun onBadRequest(errorBody: Response<GetToLocationTestResponse>?) {
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
            }

        }

    private val autoSearchRespCallback =
        object : RetrofitCallback<AutoSearchResp> {
            override fun onSuccessfulResponse(responseBody: Response<AutoSearchResp>?) {
                if (responseBody?.body()?.code ?: 0 == 200) {
                    responseBody?.body()?.responseContents?.let {
                        autoSearchRespContents.clear()
                        autoSearchRespContents.addAll(it)
                        setSearchAdapter()
                    }
                }
            }

            override fun onBadRequest(errorBody: Response<AutoSearchResp>?) {
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
            }
        }

    private val treatmentKitFavouriteRespCallback =
        object : RetrofitCallback<TreatmentKitFavouriteResp> {
            override fun onSuccessfulResponse(responseBody: Response<TreatmentKitFavouriteResp>?) {
                if (responseBody?.body()?.code ?: 0 == 200) {
                    responseBody?.body()?.responseContents?.let { responseContents ->
                        populateSearchedValues(responseContents)
                    }
                }
            }

            override fun onBadRequest(errorBody: Response<TreatmentKitFavouriteResp>?) {
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
            }
        }

    private val getLabTypeRetrofitCallback =
        object : RetrofitCallback<LabTypeResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<LabTypeResponseModel>?) {
                if (isTablet(requireContext())) {
                    (labAdapter as TreatmentKitLabAdapter).setadapterTypeValue(responseBody?.body()?.responseContents)
                    viewModel?.getLabToLocation(getLabToLoctionRetrofitCallback, facility_id)
                } else {
                    val data = LabTypeResponseContent()
                    data.uuid = 0
                    data.name = "select Type"
                    priorityListFilter?.add(data)
                    responseBody?.body()?.responseContents?.let { priorityListFilter?.addAll(it) }
                    priorityMutableNameList =
                        priorityListFilter?.map { it?.uuid!! to it.name!! }!!.toMap()
                            .toMutableMap()
                    priorityHashMapUUid.clear()
                    for (i in priorityListFilter?.indices!!) {
                        priorityHashMapUUid[i] =
                            priorityListFilter?.get(i)?.uuid!!
                    }
                    (labAdapter as TreatmentKitLabMobileAdapter).setadapterTypeValue(responseBody?.body()?.responseContents)
                    viewModel?.getLabToLocation(getLabToLoctionRetrofitCallback, facility_id)
                }
            }

            override fun onBadRequest(errorBody: Response<LabTypeResponseModel>?) {

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

    val getLabToLoctionRetrofitCallback =
        object : RetrofitCallback<LabToLocationResponse> {

            override fun onSuccessfulResponse(responseBody: Response<LabToLocationResponse>?) {
                if (isTablet(requireContext())) {
                    (labAdapter as TreatmentKitLabAdapter).setToLocationList(responseBody?.body()?.responseContents)
                    viewModel?.getToLocation(getRadiologyToLoctionRetrofitCallback, facility_id)
                } else {
                    val data = LabToLocationResponse.LabToLocationContent()
                    data.uuid = 0
                    data.location_name = "selectLocation"
                    orderLocationListFilter?.add(data)
                    orderLocationListFilter?.addAll(responseBody?.body()?.responseContents!!)
                    orderToLocationList =
                        orderLocationListFilter?.map { it?.uuid!! to it.location_name!! }!!
                            .toMap().toMutableMap()
                    orderLocationHashMap.clear()
                    for (i in orderLocationListFilter?.indices!!) {
                        orderLocationHashMap[orderLocationListFilter?.get(i)!!.uuid!!] = i
                    }
                    (labAdapter as TreatmentKitLabMobileAdapter).setToLocationList(responseBody?.body()?.responseContents)
                    viewModel?.getToLocation(getRadiologyToLoctionRetrofitCallback, facility_id)
                }


            }

            override fun onBadRequest(errorBody: Response<LabToLocationResponse>?) {
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

    private val getRadiologyPriorityRetrofitCallback =
        object : RetrofitCallback<RadiologyTypeResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<RadiologyTypeResponseModel>?) {
                if (isTablet(requireContext())) {
                    (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).setadapterTypeValue(
                        responseBody?.body()?.responseContents
                    )
                    viewModel?.getToLocation(getRadiologyToLoctionRetrofitCallback, facility_id)
                } else {
                    val data = RadiologyTypeResponseContent()
                    data.uuid = 0
                    data.name = "select Type"
                    radiologyPriorityListFilter?.add(data)
                    responseBody?.body()?.responseContents?.let {
                        radiologyPriorityListFilter?.addAll(
                            it
                        )
                    }
                    radiologyPriorityMutableNameList =
                        radiologyPriorityListFilter?.map { it?.uuid!! to it.name }!!.toMap()
                            .toMutableMap()
                    radiologyPriorityHashMapUUid.clear()
                    for (i in radiologyPriorityListFilter?.indices!!) {
                        radiologyPriorityHashMapUUid[i] =
                            radiologyPriorityListFilter?.get(i)?.uuid!!
                    }
                    setRadiologyPriorityAdapter(responseBody?.body()?.responseContents)
                    viewModel?.getToLocation(getRadiologyToLoctionRetrofitCallback, facility_id)
                }
            }

            override fun onBadRequest(errorBody: Response<RadiologyTypeResponseModel>?) {
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
            }

        }

    val getRadiologyToLoctionRetrofitCallback =
        object : RetrofitCallback<LabToLocationResponse> {
            override fun onSuccessfulResponse(responseBody: Response<LabToLocationResponse>?) {
                if (isTablet(requireContext())) {
                    (treatmentRadiologyAdapter as TreatmentKitRadiologyAdapter).setToLocationList(
                        responseBody?.body()?.responseContents
                    )
                } else {
                    val data = LabToLocationResponse.LabToLocationContent()
                    data.uuid = 0
                    data.location_name = "select Location"
                    radiologyOrderLocationListFilter?.add(data)
                    radiologyOrderLocationListFilter?.addAll(responseBody?.body()?.responseContents!!)
                    radiologyOrderToLocationList =
                        radiologyOrderLocationListFilter?.map { it?.uuid!! to it.location_name!! }!!
                            .toMap().toMutableMap()
                    radiologyOrderLocationHashMap.clear()
                    for (i in orderLocationListFilter?.indices!!) {
                        radiologyOrderLocationHashMap[orderLocationListFilter?.get(i)!!.uuid!!] = i
                    }

                    (treatmentRadiologyAdapter as TreatmentKitRadiologyMobileAdapter).setToLocationList(
                        responseBody?.body()?.responseContents
                    )
//                    viewModel?.getToLocation(getRadiologyToLoctionRetrofitCallback, facility_id)
                }
            }

            override fun onBadRequest(errorBody: Response<LabToLocationResponse>?) {
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
            }

        }

    private val getInvestigationTypeRetrofitCallback =
        object : RetrofitCallback<InvestigationTypeResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<InvestigationTypeResponseModel>?) {
                if (isTablet(requireContext())) {
                    (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).setadapterTypeValue(
                        responseBody?.body()?.responseContents
                    )
                } else {
                    setInvestigationPriorityAdapter(responseBody?.body()?.responseContents)
                    viewModel?.getInvestigationToLocation(
                        getInvestigationToLoctionRetrofitCallback,
                        facility_id
                    )
                }
            }

            override fun onBadRequest(errorBody: Response<InvestigationTypeResponseModel>?) {

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

            }
        }

    val getInvestigationToLoctionRetrofitCallback =
        object : RetrofitCallback<InvestigationLoationResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<InvestigationLoationResponseModel>?) {
                if (isTablet(requireContext())) {
                    (treatmentKitInvestigationAdapter as TreatmentKitInvestigationAdapter).setToLocationList(
                        responseBody?.body()?.responseContents
                    )
                } else {
                    val data = InvestigationLocationResponseContent()
                    data.uuid = 0
                    data.location_name = "select Location"
                    investigationOrderLocationListFilter?.add(data)
                    investigationOrderLocationListFilter?.addAll(responseBody?.body()?.responseContents!!)
                    investigationOrderToLocationList =
                        investigationOrderLocationListFilter?.map { it?.uuid!! to it.location_name }!!
                            .toMap().toMutableMap()
                    investigationOrderLocationHashMap.clear()
                    for (i in orderLocationListFilter?.indices!!) {
                        investigationOrderLocationHashMap[orderLocationListFilter?.get(i)?.uuid
                            ?: 0] = i
                    }

                    (treatmentKitInvestigationAdapter as TreatmentKitInvestigationMobileAdapter).setToLocationList(
                        responseBody?.body()?.responseContents
                    )
                }
            }

            override fun onBadRequest(errorBody: Response<InvestigationLoationResponseModel>?) {
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
            }

        }

    private val orderSaveRetrofitCallback =
        object : RetrofitCallback<OrderSaveTreatmentKitResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<OrderSaveTreatmentKitResponseModel>?) {
                trackTreatmentOrderComplete(utils?.getEncounterType(), "success", "")
                responseBody?.body()?.message?.let { Log.e("OrderSaveSuccess", it) }
                Toast.makeText(activity, responseBody?.body()?.message, Toast.LENGTH_LONG).show()
                refresh()
            }

            override fun onBadRequest(response: Response<OrderSaveTreatmentKitResponseModel>) {
                trackTreatmentOrderComplete(
                    utils!!.getEncounterType(),
                    "failure",
                    response.message()
                )
                val gson = GsonBuilder().create()
                val responseModel: OrderSaveTreatmentKitResponseModel
                try {
                    responseModel = gson.fromJson(
                        response.errorBody()!!.string(),
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
                trackTreatmentOrderComplete(
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
                trackTreatmentOrderComplete(
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
                trackTreatmentOrderComplete(
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
                trackTreatmentOrderComplete(utils!!.getEncounterType(), "failure", failure)
                utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
                isLoading(false)
            }

        }

}
