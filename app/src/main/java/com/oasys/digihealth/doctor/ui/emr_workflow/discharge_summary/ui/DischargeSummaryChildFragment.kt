package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentDischargeSummaryChildBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.fire_base_analytics.AnalyticsManager
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.discharge_model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.discharge_model.DischargeResult
import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.previous_model.DischargeSummaryPreviousResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.ui.adapter.*
import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.view_model.DischargeSummaryViewModel
import com.oasys.digihealth.doctor.ui.home.HomeActivity
import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.nurse_desk.NurseDeskDischargeSummaryResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.nurse_desk.revertresponse.RevertRequest
import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.nurse_desk.revertresponse.RevertResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.save_model.Detail
import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.save_model.Headers
import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.save_model.SaveRequestModel
import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.save_model.SaveResponseModel
import com.oasys.digihealth.doctor.ui.quick_reg.view.DialogPDFViewerActivity
import com.oasys.digihealth.doctor.utils.LogUtils
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DischargeSummaryChildFragment : Fragment(), View.OnClickListener {

    private var viewModel: DischargeSummaryViewModel? = null
    private var utils: Utils? = null
    private var facilityId: Int = -1
    private var patientId: Int = -1
    private var encounterId: Int = -1
    private var doctorID: Int? = 0
    private var appPreferences: AppPreferences? = null
    private var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var allergyUuid: Int? = 0
    private var prescriptionUUIDS: Int? = 0
    private var prescriptionUUID: Int? = 0
    private var diagnosisUUIDS: Int? = 0
    private var labUUIDS: Int? = 0
    private var labUUID: Int? = 0

    private var radiologyUUIDS: Int? = 0
    private var vitalsUUIDS: Int? = 0
    private var investigationUUIDS: Int? = 0
    private var chiefComplaintsUUIDS: Int? = 0
    var downloadZipFileTask: DialogPDFViewerActivity.DownloadZipFileTask? =
        null

    private var binding: FragmentDischargeSummaryChildBinding? = null

    private var dischargeSummaryPreviousAdapter: DischargeSummaryPreviousAdapter? = null

    private var adapterAllergy: DischargeSummaryAllergyTypeAdapter? = null
    private var adapterPrescription: DischargeSummaryPrescriptionTypeAdapter? = null
    private var adapterLab: DischargeSummaryLabTypeAdapter? = null
    private var adapterRadiology: DischargeSummaryRadiologyTypeAdapter? = null
    private var adapterDiagnosis: DischargeSummaryDiagnosisTypeAdapter? = null
    private var adapterVital: DischargeSummaryVitalTypeAdapter? = null
    private var adapterInvestigation: DischargeSummaryInvestigationTypeAdapter? = null
    private var adapterChiefComplaint: DischargeChiefComplaintAdapter? = null

    private var encounterResponseContent = listOf<EncounterResponseContent>()
    private var encounterUUID: Int? = 0
    private var encounterDoctorUUID: Int? = 0
    private var encounterType: Int? = null
    private var encounterTypeId: Int? = null

    private var departmentId: Int? = null
    private var templateText: String? = ""
    private var isAlive: Boolean? = true
    private val detailsList = ArrayList<Detail>()
    private val header: Headers? = Headers()

    private var allergyList: List<AllergyDetail> = listOf()
    private var allergyAPIList: ArrayList<AllergyDetail> = ArrayList()

    private var prescriptionList: List<PrescriptionDetail> =
        listOf()
    private var prescriptionAPIList: ArrayList<PrescriptionDetail> =
        ArrayList()

    private var diagnosisList: List<DiagnosisDetail> = listOf()
    private var diagnosisAPIList: ArrayList<DiagnosisDetail> = ArrayList()

    private var labList: List<LabDetail> =
        listOf()
    private var labAPIList: ArrayList<LabDetail> =
        ArrayList()

    private var investigationList: List<InvestigationDetails> = listOf()
    private var investigationAPIList: ArrayList<InvestigationDetails> = ArrayList()

    private var chiefComplaintList: List<ChiefComplaintsDetails> = listOf()
    private var chiefComplaintAPIList: ArrayList<ChiefComplaintsDetails> = ArrayList()


    private var radioLogyList: List<RadiologyDetail> = listOf()
    private var radioLogyAPIList: ArrayList<RadiologyDetail> = ArrayList()

    private var vitalList: List<VitalDetail> = listOf()
    private var vitalAPIList: ArrayList<VitalDetail> = ArrayList()

    private var listDSTypeItems: ArrayList<DischargeTypeList?> = ArrayList()
    private var resDSTypeMap = mutableMapOf<Int, String>()

    private var listDSDeathTypeItems: ArrayList<DischargeDeathTypeList?> = ArrayList()
    private var resDSDeathTypeMap = mutableMapOf<Int, String>()

    private var listDSNoteTemplateItems: ArrayList<ResDischargeSummaryNoteTemplate?> = ArrayList()
    private var resDSNoteTemplateMap = mutableMapOf<Int, String>()


    private var listDSDoctorNameItems: ArrayList<DischargeSummaryDoctorRes?> =
        ArrayList()
    private var resDSDoctorNameMap = mutableMapOf<Int, String>()


    private var mYear: Int? = null
    private var mMonth: Int? = null
    private var mDay: Int? = null
    private var mHour: Int? = null
    private var mMinute: Int? = null
    private var mSecond: Int? = null

    private var firstName = ""
    private var titleName = ""

    private var dischargeAPIDate = ""
    private var generateAPIDate = ""

    private var customdialog: Dialog? = null

    private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())

    var dischargeResult: DischargeResult? = null


    lateinit var drugNmae: TextView


    /*
    * For Save and Approve request body
    *
    * */

    var allergy: Allergy = Allergy()
    var diagnosis: Diagnosis = Diagnosis()
    var investigation: Investigation = Investigation()
    var lab: Lab = Lab()
    var prescription: Prescription = Prescription()
    var radiology: Radiology = Radiology()
    var vitals: Vitals = Vitals()
    var chiefComplaintsDetails: ResChiefComplaintsData = ResChiefComplaintsData()

    var encounterTypeUUID: Int = 0
    var admissionUUID: Int = 0
    var admissionStatusUUID: Int = 0
    var dischargeTypeUUID: Int = 0
    var deathTypeUUID: Int = 0
    var comments: String = ""
    var admissionRequestUUID: Int = 0
    var noteTemplateUUID: Int = 0
    var isSaved: Boolean = false
    var isApproval: Boolean = false

    var data: NurseDeskDischargeSummaryResponseContent = NurseDeskDischargeSummaryResponseContent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        utils = Utils(requireActivity())
        AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)?.let {
            appPreferences =
                AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
            userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
            val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()
            facilityId = it.getInt(AppConstants.FACILITY_UUID)
            patientId = it.getInt(AppConstants.PATIENT_UUID)
            doctorID = userDataStoreBean?.uuid!!

            encounterType = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)
            encounterTypeId = appPreferences?.getInt(AppConstants.ENCOUNTER_UUID)

            departmentId = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
            titleName = userDataStoreBean.title?.name ?: ""
            firstName = userDataStoreBean.first_name ?: ""

            trackDischargeSummaryVisit(utils?.getEncounterType())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Utils(requireContext()).setCalendarLocale("en", requireContext())

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_discharge_summary_child,
            container,
            false
        )

        initMain()

        return binding?.root
    }

    private fun initMain() {
        viewModel = ViewModelProvider(this)[DischargeSummaryViewModel::class.java]
        encounterId = encounterUUID!!
        binding?.viewModel = viewModel



        if (Utils.isNetworkConnected(requireContext())) {
            viewModel?.getEncounter(
                facilityId,
                patientId,
                doctorID!!,
                encounterType!!,
                departmentId!!,
                fetchEncounterRetrofitCallBack
            )
            viewModel?.getDischargeSummaryTypeList(callbackGetDSTypeRetrofit)
            viewModel?.getDischargeSummaryDeathType(callbackGetDSDeathTypeRetrofit)
//            viewModel?.getDischargeSummaryDefaultTemplate(callbackGetDSDefaultTemplate)
            viewModel?.getDoctorName(callbackGetDSDoctorNameRetrofit)

            viewModel?.getDischargeNoteTemplate(callbackGetDSNoteTemplate)
        } else {
            alertDialog(getString(R.string.no_internet))
        }

        initUI()
        clickListener()
        drawerListeners()
        initSwitchListener()
        initPreviousDischargeSummaryList()
        initDischargeSummaryAllergyType()
        initDischargeSummaryDiagnosisType()
        initDischargeSummaryPrescriptionType()
        initDischargeSummaryLabType()
        initDischargeSummaryRadioLogyType()
        initDischargeSummaryVitalType()
        initDischargeSummaryInvestigationType()
        initDischargeSummaryChiefComplaintType()

        binding?.cvRevertDischargeSummary?.visibility = View.VISIBLE

        // By default date and time added
        val sdf = SimpleDateFormat("dd-mm-yyy HH:mm:ss", Locale.getDefault())
        binding?.etDischargeSummaryDate?.setText(sdf.format(Date().time))
        dischargeAPIDate = formatter.format(Date().time)
        generateAPIDate = formatter.format(Date().time)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initUI() {
        binding!!.etTemplateNote.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                if (binding!!.etTemplateNote.hasFocus()) {
                    v.parent.requestDisallowInterceptTouchEvent(true)
                    when (event.action and MotionEvent.ACTION_MASK) {
                        MotionEvent.ACTION_SCROLL -> {
                            v.parent.requestDisallowInterceptTouchEvent(false)
                            return true
                        }
                    }
                }
                return false
            }
        })
        binding?.etDischargeSummaryDeathPlace?.isVisible = false
        binding?.tvDischargeSummaryDeathPlaceLabel?.isVisible = false
    }

    private fun clickListener() {
        binding?.also {
            it.etDischargeSummaryDate.setOnClickListener(this)

            it.cvPrintDischargeSummary.setOnClickListener(this)
            it.cvSaveDischargeSummary.setOnClickListener(this)
            it.cvApproveDischargeSummary.setOnClickListener(this)
            it.cvClearDischargeSummary.setOnClickListener(this)
            it.cvCancelDischargeSummary?.setOnClickListener(this)
            it.cvRevertDischargeSummary?.setOnClickListener(this)

            it.layoutAllergy.ivDeleteAllergy.setOnClickListener(this)
            it.layoutDiagonosis.ivDeleteDiagnosis.setOnClickListener(this)
            it.layoutPrescription.ivPrescriptionDelete.setOnClickListener(this)
            it.layoutLab.ivLabDelete.setOnClickListener(this)
            it.layoutRadioLogy.ivRadiologyDelete.setOnClickListener(this)
            it.layoutVital.ivVitalDelete.setOnClickListener(this)
            it.layoutInvestigation.ivInvestigationDelete.setOnClickListener(this)
            it.layoutChiefComplaint?.ivChiefComplaintDelete?.setOnClickListener(this)



            it.layoutAllergy.ivAllergyExpandList.setOnClickListener(this)
            it.layoutDiagonosis.ivDeleteExpandList.setOnClickListener(this)
            it.layoutPrescription.ivPrescriptionExpandList.setOnClickListener(this)
            it.layoutLab.ivLabExpandList.setOnClickListener(this)
            it.layoutRadioLogy.ivRadiologyExpandList.setOnClickListener(this)
            it.layoutVital.ivVitalExpandList.setOnClickListener(this)
            it.layoutInvestigation.ivInvestigationExpandList.setOnClickListener(this)
            it.layoutChiefComplaint?.ivChiefComplaintExpandList?.setOnClickListener(this)
        }
    }

    private fun drawerListeners() {
        binding?.also { binding ->
            binding.DSDrawerCardView.setOnClickListener {
                binding.drawerLayout.openDrawer(GravityCompat.END)
            }
            binding.cvCancel.setOnClickListener {
                binding.drawerLayout.closeDrawer(GravityCompat.END)
            }
        }
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (menuVisible) {
            trackDischargeSummaryPreviousDischargeSummary(utils?.getEncounterType())
        }
    }

    private fun initSwitchListener() {
        // by Default switch on...
        binding?.swDischargeSummary?.isChecked = true
        binding?.swDischargeSummary?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding?.swDischargeSummary?.thumbDrawable?.setColorFilter(
                    Color.parseColor("#00b28a"),
                    PorterDuff.Mode.MULTIPLY
                )
            } else {
                binding?.swDischargeSummary?.thumbDrawable?.setColorFilter(
                    Color.parseColor("#ff0000"),
                    PorterDuff.Mode.MULTIPLY
                )
            }
            isAlive = isChecked
            if (Utils(requireContext()).isTablet(requireContext())) {
                if (isChecked) {
                    binding?.llDischargeType?.visibility = View.VISIBLE
                    binding?.llDeathType?.visibility = View.GONE
                    binding?.llDeathView?.visibility = View.GONE
                    binding?.tvDischargeSummaryDeathPlaceLabel?.visibility = View.GONE
                    binding?.etDischargeSummaryDeathPlace?.visibility = View.GONE
                } else {
                    binding?.llDischargeType?.visibility = View.GONE
                    binding?.llDeathType?.visibility = View.VISIBLE
                    binding?.llDeathView?.visibility = View.VISIBLE
                    binding?.tvDischargeSummaryDeathPlaceLabel?.visibility = View.VISIBLE
                    binding?.etDischargeSummaryDeathPlace?.visibility = View.VISIBLE
                }
            } else {
                binding?.spDeathType?.isVisible = !isChecked
                binding?.deathTypeText?.isVisible = !isChecked
                binding?.spDischargeType?.isVisible = isChecked
                binding?.etDischargeSummaryDeathPlace?.isVisible = !isChecked
                binding?.tvDischargeSummaryDeathPlaceLabel?.isVisible = !isChecked
                binding?.dischargeTxtView?.isVisible = isChecked
            }
        }
    }

    private fun initPreviousDischargeSummaryList() {
        binding?.rvDischargeSummaryPrevious?.apply {
            dischargeSummaryPreviousAdapter = DischargeSummaryPreviousAdapter(
                onDSPreviousListener = {
                    //TODO EDIT PREVIOUS DATA SUMMARY
                },
                onDSPreviousPrintListener = {
                    //TODO PRINT PREVIOUS DATA SUMMARY
                }
            )
            this.adapter = dischargeSummaryPreviousAdapter
        }
    }

    private fun initDischargeSummaryAllergyType() {
        binding?.layoutAllergy?.rvAllergy?.apply {
            adapterAllergy = DischargeSummaryAllergyTypeAdapter(
                onClickDeleteListener = { allergyDetail ->
                    if (allergyAPIList.contains(allergyDetail))
                        allergyAPIList.remove(allergyDetail)
                    if (allergyAPIList.isEmpty())
                        deleteAllergyView(false)
                }
            )
            this.adapter = adapterAllergy
        }
    }

    private fun initDischargeSummaryDiagnosisType() {
        binding?.layoutDiagonosis?.rvDiagonosis?.apply {
            adapterDiagnosis = DischargeSummaryDiagnosisTypeAdapter(
                onDeleteDiagnosisListener = {
                    if (diagnosisAPIList.contains(it))
                        diagnosisAPIList.remove(it)
                    if (diagnosisAPIList.isEmpty())
                        deleteDiagnosisView(false)
                }
            )
            adapter = adapterDiagnosis
        }
    }

    private fun initDischargeSummaryPrescriptionType() {
        binding?.layoutPrescription?.rvPrescription?.apply {
            adapterPrescription = DischargeSummaryPrescriptionTypeAdapter(
                onDeletePrescriptionListener = {
                    if (prescriptionAPIList.contains(it))
                        prescriptionAPIList.remove(it)
                    if (prescriptionAPIList.isEmpty())
                        deletePrescriptionView(false)
                }
            )
            adapter = adapterPrescription
        }
    }

    private fun initDischargeSummaryLabType() {
        binding?.layoutLab?.rvLab?.apply {
            //Used Prescription Adapter for checking UI
            adapterLab = DischargeSummaryLabTypeAdapter(onDeleteLabListener = {
                if (labAPIList.contains(it))
                    labAPIList.remove(it)
                if (labAPIList.isEmpty())
                    deleteLabView(false)
            })
            adapter = adapterLab
        }
    }

    private fun initDischargeSummaryRadioLogyType() {
        binding?.layoutRadioLogy?.rvRadiology?.apply {
            adapterRadiology = DischargeSummaryRadiologyTypeAdapter(
                onDeleteRadioLogyListener = {
                    if (radioLogyAPIList.contains(it))
                        radioLogyAPIList.remove(it)
                    if (radioLogyAPIList.isEmpty())
                        deleteRadioLogyView(false)
/*
                    if (radioLogyAPIList.isEmpty())
                        hideAndShowRadioLogyView(false)
*/
                }
            )
            adapter = adapterRadiology
        }
    }

    private fun initDischargeSummaryVitalType() {
        binding?.layoutVital?.rvVitalMain?.apply {
            adapterVital = DischargeSummaryVitalTypeAdapter(
                onDeleteVitalListener = {
                    if (vitalAPIList.contains(it))
                        vitalAPIList.remove(it)
                    if (vitalAPIList.isEmpty())
                        deleteVitalView(false)
/*
                    if (vitalAPIList.isEmpty())
                        hideAndShowVitalView(false)
*/
                }
            )
            adapter = adapterVital
        }
    }

    private fun initDischargeSummaryInvestigationType() {
        binding?.layoutInvestigation?.rvInvestigationMain?.apply {
            adapterInvestigation = DischargeSummaryInvestigationTypeAdapter(requireContext(),
                onDeleteInvestigationListener = {
                    if (investigationAPIList.contains(it))
                        investigationAPIList.remove(it)
                    if (investigationAPIList.isEmpty())
                        deleteInvestigationView(false)
                }
            )
            adapter = adapterInvestigation
        }
    }

    private fun initDischargeSummaryChiefComplaintType() {
        binding?.layoutChiefComplaint?.rvChiefComplaintMain?.apply {

            adapterChiefComplaint = DischargeChiefComplaintAdapter(requireContext(),
                onDeleteComplaintsListener = {
                    if (chiefComplaintAPIList.contains(it))
                        chiefComplaintAPIList.remove(it)
                    if (chiefComplaintAPIList.isEmpty())
                        deleteChiefComplaintView(false)
                }
            )
            adapter = adapterChiefComplaint

        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.etDischargeSummaryDate -> openDatePicker()
            R.id.cvPrintDischargeSummary -> pdfDownload()
            R.id.cvSaveDischargeSummary -> saveData(true)
            R.id.cvApproveDischargeSummary -> saveData(false)
            R.id.cvClearDischargeSummary -> clearData()
            R.id.cvRevertDischargeSummary -> Revert()
//            R.id.cvCancelDischargeSummary -> cancel()
            R.id.ivDeleteAllergy -> {
                alertDialogDelete(getString(R.string.allergy_label))
            }
            R.id.ivDeleteDiagnosis -> {
                alertDialogDelete(getString(R.string.diagnosis))
            }
            R.id.ivPrescriptionDelete -> {
                alertDialogDelete(getString(R.string.prescription))
            }
            R.id.ivLabDelete -> {
                alertDialogDelete(getString(R.string.lab_label))
            }
            R.id.ivRadiologyDelete -> {
                alertDialogDelete(getString(R.string.radiology))
            }
            R.id.ivVitalDelete -> {
                alertDialogDelete(getString(R.string.vitals))
            }
            R.id.ivInvestigationDelete -> {
                alertDialogDelete(getString(R.string.investigation))
            }
            R.id.ivChiefComplaintDelete -> {
                alertDialogDelete(getString(R.string.chiefComplient))
            }
            R.id.ivAllergyExpandList -> expandAndCollapseAllergy()
            R.id.ivDeleteExpandList -> expandAndCollapseDiagnosis()
            R.id.ivPrescriptionExpandList -> expandAndCollapsePrescription()
            R.id.ivLabExpandList -> expandAndCollapseLab()
            R.id.ivRadiologyExpandList -> expandAndCollapseRadiology()
            R.id.ivVitalExpandList -> expandAndCollapseVitals()
            R.id.ivInvestigationExpandList -> expandAndCollapseInvestigation()
            R.id.ivChiefComplaintExpandList -> expandAndCollapseChiefComplaints()
        }
    }

    private fun expandAndCollapseAllergy() {
        if (binding?.layoutAllergy?.cvAllergy?.visibility == View.VISIBLE) {
            binding?.layoutAllergy?.ivAllergyExpandList?.setImageResource(R.drawable.ic_discharge_summary_right_arrow)
            binding?.layoutAllergy?.cvAllergy?.visibility = View.GONE
        } else {
            binding?.layoutAllergy?.ivAllergyExpandList?.setImageResource(R.drawable.ic_discharge_summary_down_arrow)
            binding?.layoutAllergy?.cvAllergy?.visibility = View.VISIBLE
        }
    }

    private fun expandAndCollapseDiagnosis() {
        if (binding?.layoutDiagonosis?.cvDiagnosis?.visibility == View.VISIBLE) {
            binding?.layoutDiagonosis?.ivDeleteExpandList?.setImageResource(R.drawable.ic_discharge_summary_right_arrow)
            binding?.layoutDiagonosis?.cvDiagnosis?.visibility = View.GONE
        } else {
            binding?.layoutDiagonosis?.ivDeleteExpandList?.setImageResource(R.drawable.ic_discharge_summary_down_arrow)
            binding?.layoutDiagonosis?.cvDiagnosis?.visibility = View.VISIBLE
        }
    }

    private fun expandAndCollapsePrescription() {
        if (binding?.layoutPrescription?.cvPrescription?.visibility == View.VISIBLE) {
            binding?.layoutPrescription?.ivPrescriptionExpandList?.setImageResource(R.drawable.ic_discharge_summary_right_arrow)
            binding?.layoutPrescription?.cvPrescription?.visibility = View.GONE
        } else {
            binding?.layoutPrescription?.ivPrescriptionExpandList?.setImageResource(R.drawable.ic_discharge_summary_down_arrow)
            binding?.layoutPrescription?.cvPrescription?.visibility = View.VISIBLE
        }
    }

    private fun expandAndCollapseLab() {
        if (binding?.layoutLab?.cvLab?.visibility == View.VISIBLE) {
            binding?.layoutLab?.ivLabExpandList?.setImageResource(R.drawable.ic_discharge_summary_right_arrow)
            binding?.layoutLab?.cvLab?.visibility = View.GONE
        } else {
            binding?.layoutLab?.ivLabExpandList?.setImageResource(R.drawable.ic_discharge_summary_down_arrow)
            binding?.layoutLab?.cvLab?.visibility = View.VISIBLE
        }
    }

    private fun expandAndCollapseRadiology() {
        if (binding?.layoutRadioLogy?.cvRadiology?.visibility == View.VISIBLE) {
            binding?.layoutRadioLogy?.ivRadiologyExpandList?.setImageResource(R.drawable.ic_discharge_summary_right_arrow)
            binding?.layoutRadioLogy?.cvRadiology?.visibility = View.GONE
        } else {
            binding?.layoutRadioLogy?.ivRadiologyExpandList?.setImageResource(R.drawable.ic_discharge_summary_down_arrow)
            binding?.layoutRadioLogy?.cvRadiology?.visibility = View.VISIBLE
        }
    }

    private fun expandAndCollapseVitals() {
        if (binding?.layoutVital?.cvVitals?.visibility == View.VISIBLE) {
            binding?.layoutVital?.ivVitalExpandList?.setImageResource(R.drawable.ic_discharge_summary_right_arrow)
            binding?.layoutVital?.cvVitals?.visibility = View.GONE
        } else {
            binding?.layoutVital?.ivVitalExpandList?.setImageResource(R.drawable.ic_discharge_summary_down_arrow)
            binding?.layoutVital?.cvVitals?.visibility = View.VISIBLE
        }
    }

    private fun expandAndCollapseInvestigation() {
        if (binding?.layoutInvestigation?.cvInvestigation?.visibility == View.VISIBLE) {
            binding?.layoutInvestigation?.ivInvestigationExpandList?.setImageResource(R.drawable.ic_discharge_summary_right_arrow)
            binding?.layoutInvestigation?.cvInvestigation?.visibility = View.GONE
        } else {
            binding?.layoutInvestigation?.ivInvestigationExpandList?.setImageResource(R.drawable.ic_discharge_summary_down_arrow)
            binding?.layoutInvestigation?.cvInvestigation?.visibility = View.VISIBLE
        }
    }

    private fun expandAndCollapseChiefComplaints() {
        if (binding?.layoutChiefComplaint?.cvChiefComplaint?.visibility == View.VISIBLE) {
            binding?.layoutChiefComplaint?.ivChiefComplaintExpandList?.setImageResource(R.drawable.ic_discharge_summary_right_arrow)
            binding?.layoutChiefComplaint?.cvChiefComplaint?.visibility = View.GONE
        } else {
            binding?.layoutChiefComplaint?.ivChiefComplaintExpandList?.setImageResource(R.drawable.ic_discharge_summary_down_arrow)
            binding?.layoutChiefComplaint?.cvChiefComplaint?.visibility = View.VISIBLE
        }
    }


    private fun deleteAllergyView(isVisible: Boolean) {
        binding?.layoutAllergy?.root?.isVisible = isVisible
    }

    private fun deleteDiagnosisView(isVisible: Boolean) {
        binding?.layoutDiagonosis?.root?.isVisible = isVisible
    }

    private fun deletePrescriptionView(isVisible: Boolean) {
        binding?.layoutPrescription?.root?.isVisible = isVisible
    }

    private fun deleteLabView(isVisible: Boolean) {
        binding?.layoutLab?.root?.isVisible = isVisible
    }

    private fun deleteRadioLogyView(isVisible: Boolean) {
        binding?.layoutRadioLogy?.root?.isVisible = isVisible
    }

    private fun deleteVitalView(isVisible: Boolean) {
        binding?.layoutVital?.root?.isVisible = isVisible
    }

    private fun deleteInvestigationView(isVisible: Boolean) {
        binding?.layoutInvestigation?.root?.isVisible = isVisible
    }

    private fun deleteChiefComplaintView(isVisible: Boolean) {
        binding?.layoutChiefComplaint?.root?.isVisible = isVisible
    }


    private fun Revert() {

        customdialog = Dialog(requireContext())
        customdialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customdialog!!.setCancelable(false)
        customdialog!!.setContentView(R.layout.revert_dialog)
        val closeImageView = customdialog!!.findViewById(R.id.closeImageView) as ImageView

        closeImageView.setOnClickListener {
            customdialog?.dismiss()
        }


        drugNmae = customdialog!!.findViewById(R.id.addDeleteName) as TextView
        drugNmae.text = "Are you sure you want to Revert ?"
        val yesBtn = customdialog!!.findViewById(R.id.yes) as CardView
        val noBtn = customdialog!!.findViewById(R.id.no) as CardView
        yesBtn.setOnClickListener {

            val revert = RevertRequest()

            revert.admission_uuid = admissionUUID

            revert.encounter_type_uuid = encounterTypeUUID

            revert.encounter_uuid = encounterUUID

            revert.facility_uuid = facilityId.toString()

            revert.patient_uuid = patientId

            viewModel?.revertData(facilityId, revert, callbackRevert)


        }
        noBtn.setOnClickListener {
            customdialog!!.dismiss()
        }
        customdialog!!.show()

    }

    @SuppressLint("SetTextI18n")
    private fun openDatePicker() {
        val sdf = SimpleDateFormat("dd-MM-yyy HH:mm:ss", Locale.getDefault())
        val calendarDisplay: Calendar = Calendar.getInstance()
        val calendar: Calendar = Calendar.getInstance()
        mYear = calendar.get(Calendar.YEAR)
        mMonth = calendar.get(Calendar.MONTH)
        mDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this.requireActivity(),
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

                val cal = Calendar.getInstance()
                mHour = cal[Calendar.HOUR_OF_DAY]
                mMinute = cal[Calendar.MINUTE]
                mSecond = cal[Calendar.SECOND]

                calendarDisplay.set(Calendar.YEAR, year)
                calendarDisplay.set(Calendar.MONTH, monthOfYear + 1)
                calendarDisplay.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val timePickerDialog = TimePickerDialog(
                    this.activity,
                    TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                        calendarDisplay.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendarDisplay.set(Calendar.MINUTE, minute)
                        val displayDischargeDate = sdf.format(calendarDisplay.time)
                        //Passing to API
                        dischargeAPIDate = formatter.format(calendarDisplay.time)
                        generateAPIDate = formatter.format(Date().time)
                        //Display the UI Date
                        binding?.etDischargeSummaryDate?.setText(displayDischargeDate)
                    },
                    mHour!!,
                    mMinute!!,
                    false
                )
                timePickerDialog.show()
            },
            mYear!!,
            mMonth!!,
            mDay!!
        )
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    private fun setDischargeTypeSpinner(listDSTypeItems: ArrayList<DischargeTypeList?>) {
        resDSTypeMap = listDSTypeItems.map { it?.uuid!! to it.name!! }.toMap().toMutableMap()
        val adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                resDSTypeMap.values.toMutableList()
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spDischargeType!!.adapter = adapter

        binding?.spDischargeType?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //TODO NOTHING
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    dischargeTypeUUID = listDSTypeItems[position]?.uuid!!
                }
            }
    }

    private fun setDischargeDeathTypeSpinner(listDSDeathTypeItems: ArrayList<DischargeDeathTypeList?>) {
        resDSDeathTypeMap =
            listDSDeathTypeItems.map { it?.uuid!! to it.name!! }.toMap().toMutableMap()
        val adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                resDSDeathTypeMap.values.toMutableList()
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spDeathType!!.adapter = adapter

        binding?.spDeathType?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //TODO NOTHING
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    deathTypeUUID = listDSTypeItems[position]?.uuid!!
                }
            }
    }

    private fun setDischargeDoctorNameTypeSpinner(listDSDoctorNameItems: ArrayList<DischargeSummaryDoctorRes?>) {
        if (!listDSDoctorNameItems.isNullOrEmpty()) {
            resDSDoctorNameMap =
                listDSDoctorNameItems.map { it?.uuid!! to (it.user_name ?: "") }.toMap()
                    .toMutableMap()
        }
        val adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                resDSDoctorNameMap.values.toMutableList()
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spDischargeSummaryDoctorName?.adapter = adapter

        binding?.spDischargeSummaryDoctorName?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //TODO NOTHING
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                }
            }
    }


    private fun setDischargeNoteTemplateSpinner(listDSNoteTemplateItems: ArrayList<ResDischargeSummaryNoteTemplate?>) {
        resDSNoteTemplateMap =
            listDSNoteTemplateItems.map { it?.nt_uuid!! to it.nt_name!! }.toMap().toMutableMap()
        val adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                resDSNoteTemplateMap.values.toMutableList()
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spTemplate!!.adapter = adapter
        binding?.spTemplate?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO NOTHING
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                noteTemplateUUID = listDSNoteTemplateItems[position]?.nt_uuid!!

                templateText =
                    Html.fromHtml(listDSNoteTemplateItems[position]?.nt_data_template ?: "")
                        .toString()
                binding?.etTemplateNote?.setText(templateText)
                binding?.etTemplateNote?.movementMethod = ScrollingMovementMethod()
            }
        }
    }

    private val callbackRevert =
        object : RetrofitCallback<RevertResponseModel> {

            override fun onSuccessfulResponse(responseBody: Response<RevertResponseModel>?) {

                //    alertDialog(responseBody?.body()?.message.toString())

                toast(responseBody?.body()?.message.toString())
                initMain()

//                val frag = DischargeSummaryChildFragment()
//                val fragmentTransaction = fragmentManager?.beginTransaction()
//                fragmentTransaction!!.replace(R.id.fragmentContainer, frag)
//                fragmentTransaction.commit()
            }

            override fun onBadRequest(errorBody: Response<RevertResponseModel>?) {
            }

            override fun onServerError(response: Response<*>) {
                toast(getString(R.string.something_went_wrong))
            }

            override fun onUnAuthorized() {
                toast(getString(R.string.unauthorized))
            }

            override fun onForbidden() {
                toast(getString(R.string.something_went_wrong))
            }

            override fun onFailure(failure: String) {
                toast(failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }


    private val callbackGetDischargeSummaryRetrofit =
        object :
            RetrofitCallback<DischargeSummaryListResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<DischargeSummaryListResponseModel>?) {
                LogUtils.logE("resDischargeSummary", "____" + responseBody?.body()?.toString())

                responseBody?.body()?.discharge_result?.let {
                    dischargeResult = it


                    if (!responseBody.body()?.discharge_result!!.diagnosis.diagnosis_details.isNullOrEmpty()) {
                        diagnosisList =
                            responseBody.body()?.discharge_result!!.diagnosis.diagnosis_details
                        adapterDiagnosis?.setData(diagnosisList)
                        diagnosisUUIDS =
                            responseBody.body()?.discharge_result!!.diagnosis.diagnosis_details[0].diagnosis_uuid

                        diagnosisAPIList = diagnosisList as ArrayList<DiagnosisDetail>
                    }

                    if (!responseBody.body()?.discharge_result!!.radiology.radiology_details.isNullOrEmpty()) {
                        radioLogyList =
                            responseBody.body()?.discharge_result!!.radiology.radiology_details as ArrayList<RadiologyDetail>
                        adapterRadiology?.setData(radioLogyList as ArrayList<RadiologyDetail>)
                        radiologyUUIDS =
                            responseBody.body()?.discharge_result!!.radiology.radiology_details[0].uuid
                        radioLogyAPIList = radioLogyList as ArrayList<RadiologyDetail>

                    }
                    if (!responseBody.body()?.discharge_result!!.lab.lab_details.isNullOrEmpty()) {
                        labList = responseBody.body()?.discharge_result!!.lab.lab_details
                        adapterLab?.setData(labList)
                        labUUIDS =
                            responseBody.body()?.discharge_result!!.lab.lab_details[0].uuid
                        labUUID =
                            responseBody.body()?.discharge_result!!.lab.lab_details[0].patient_order_test_details[0].uuid

                        labAPIList =
                            labList as ArrayList<LabDetail>
                    }

                    if (!responseBody.body()?.discharge_result!!.vitals.vital_details.isNullOrEmpty()) {
                        vitalList =
                            responseBody.body()?.discharge_result!!.vitals.vital_details as ArrayList<VitalDetail>
                        adapterVital?.setData(vitalList as ArrayList<VitalDetail>)
                        vitalsUUIDS =
                            responseBody.body()?.discharge_result!!.vitals.vital_details[0].patient_uuid

                        vitalAPIList = vitalList as ArrayList<VitalDetail>
                    }

                    if (!responseBody.body()?.discharge_result!!.investigation.investigation_details.isNullOrEmpty()) {
                        investigationList =
                            responseBody.body()?.discharge_result!!.investigation.investigation_details
                        adapterInvestigation?.setData(investigationList)
                        investigationUUIDS =
                            responseBody.body()?.discharge_result!!.investigation.investigation_details[0].uuid

                        investigationAPIList = investigationList as ArrayList<InvestigationDetails>
                    }

                    if (!responseBody.body()?.discharge_result!!.allergy.allergy_details.isNullOrEmpty()) {
                        allergyList =
                            responseBody.body()?.discharge_result!!.allergy.allergy_details
                        adapterAllergy?.setData(allergyList)
                        allergyUuid =
                            responseBody.body()?.discharge_result!!.allergy.allergy_details[0].uuid

                        allergyAPIList = allergyList as ArrayList<AllergyDetail>

                    }

                    try {
                        if (!responseBody.body()?.discharge_result!!.prescription.prescription_details.isNullOrEmpty()) {
                            prescriptionList =
                                responseBody.body()?.discharge_result!!.prescription.prescription_details
                            adapterPrescription?.setData(prescriptionList)
                            prescriptionUUIDS =
                                responseBody.body()?.discharge_result!!.prescription.prescription_details[0].prescription_details[0].item_master_uuid
                            prescriptionUUID =
                                responseBody.body()?.discharge_result!!.prescription.prescription_details[0].prescription_details[0].prescription_uuid
                            prescriptionAPIList =
                                prescriptionList as ArrayList<PrescriptionDetail>

                        }


                    } catch (e: Exception) {

                    }



                    if (!responseBody.body()?.discharge_result!!.cheif_complaints.cheif_complaints_details.isNullOrEmpty()) {
                        chiefComplaintList =
                            responseBody.body()?.discharge_result!!.cheif_complaints.cheif_complaints_details
                        adapterChiefComplaint?.setData(chiefComplaintList)
                        chiefComplaintsUUIDS =
                            responseBody.body()?.discharge_result!!.cheif_complaints.cheif_complaints_details[0].patient_cheif_complaint_uuid

                        chiefComplaintAPIList =
                            chiefComplaintList as ArrayList<ChiefComplaintsDetails>
                    }

                    dischargeResult.let { dischargeResult ->
                        allergy = dischargeResult?.allergy!!
                        diagnosis = dischargeResult.diagnosis
                        investigation = dischargeResult.investigation
                        lab = dischargeResult.lab
                        prescription = dischargeResult.prescription
                        radiology = dischargeResult.radiology
                        vitals = dischargeResult.vitals
                        chiefComplaintsDetails = dischargeResult.cheif_complaints
                    }

                    LogUtils.logE(
                        "List",
                        "size____lab___" + labList.size
                                + "___radiology____" + radioLogyList.size
                                + "___diagnosisList____" + diagnosisList.size
                                + "___prescriptionList____" + prescriptionList.size
                                + "___vitalList____" + vitalList.size
                                + "___investigationList____" + investigationList.size
                                + "___chiefComplaintList____" + chiefComplaintList.size
                    )

                    if (!allergyList.isNullOrEmpty()) {
                        deleteAllergyView(true)
                    } else {
                        deleteAllergyView(false)
                    }

                    if (!diagnosisList.isNullOrEmpty()) {
                        deleteDiagnosisView(true)
                    } else {
                        deleteDiagnosisView(false)
                    }

                    if (!prescriptionList.isNullOrEmpty()) {
                        deletePrescriptionView(true)
                    } else {
                        deletePrescriptionView(false)
                    }

                    if (!labList.isNullOrEmpty()) {
                        deleteLabView(true)
                    } else {
                        deleteLabView(false)
                    }

                    if (!radioLogyList.isNullOrEmpty()) {
                        deleteRadioLogyView(true)
                    } else {
                        deleteRadioLogyView(false)
                    }

                    if (!vitalList.isNullOrEmpty()) {
                        deleteVitalView(true)
                    } else {
                        deleteVitalView(false)
                    }

                    if (!investigationList.isNullOrEmpty()) {
                        deleteInvestigationView(true)
                    } else {
                        deleteInvestigationView(false)
                    }

                    if (!chiefComplaintList.isNullOrEmpty()) {
                        deleteChiefComplaintView(true)
                    } else {
                        deleteChiefComplaintView(false)
                    }

                    //NOT needed but keep it
                    when {
                        allergyList.isNotEmpty() -> hideAndShowAllergyView(true)
                        diagnosisList.isNotEmpty() -> hideAndShowDiagnosisView(true)
                        prescriptionList.isNotEmpty() -> hideAndShowPrescriptionView(true)
                        labList.isNotEmpty() -> hideAndShowLabView(true)
                        radioLogyList.isNotEmpty() -> hideAndShowRadioLogyView(true)
                        vitalList.isNotEmpty() -> hideAndShowVitalView(true)
                        investigationList.isNotEmpty() -> hideAndShowInvestigationView(true)
                        chiefComplaintList.isNotEmpty() -> hideAndShowInvestigationView(true)
                    }
                }
            }

            override fun onBadRequest(errorBody: Response<DischargeSummaryListResponseModel>?) {
            }

            override fun onServerError(response: Response<*>) {
                toast(getString(R.string.something_went_wrong))
            }

            override fun onUnAuthorized() {
                toast(getString(R.string.unauthorized))
            }

            override fun onForbidden() {
                toast(getString(R.string.something_went_wrong))
            }

            override fun onFailure(failure: String) {
                toast(failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }

        }

    private val callbackGetDSTypeRetrofit = object : RetrofitCallback<ResDischargeType> {
        override fun onSuccessfulResponse(resDSType: Response<ResDischargeType>?) {
            val dischargeTypeList =
                DischargeTypeList(uuid = 0, code = "", name = getString(R.string.discharge_type))
            resDSType?.body()?.discharge_types?.also { it ->
                if (!it.discharge_types.isNullOrEmpty()) {
                    listDSTypeItems.add(0, dischargeTypeList)
                    it.discharge_types?.forEach { dischargeTypeList ->
                        listDSTypeItems.add(dischargeTypeList)
                    }
                    setDischargeTypeSpinner(listDSTypeItems)
                }
            }

        }

        override fun onBadRequest(errorBody: Response<ResDischargeType>?) {
        }

        override fun onServerError(response: Response<*>) {
            toast(getString(R.string.something_went_wrong))
        }

        override fun onUnAuthorized() {
            toast(getString(R.string.unauthorized))
        }

        override fun onForbidden() {
            toast(getString(R.string.something_went_wrong))
        }

        override fun onFailure(failure: String) {
            toast(failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }

    }

    private val callbackGetDSDeathTypeRetrofit = object : RetrofitCallback<ResDischargeDeathType> {

        override fun onSuccessfulResponse(resDSDeathType: Response<ResDischargeDeathType>?) {
            val dischargeDeathTypeList =
                DischargeDeathTypeList(uuid = 0, code = "", name = getString(R.string.death_type))
            resDSDeathType?.body()?.discharge_death_types?.also { it ->
                if (!it.discharge_death_types_list.isNullOrEmpty()) {
                    listDSDeathTypeItems.add(0, dischargeDeathTypeList)
                    it.discharge_death_types_list?.forEach { dischargeDeathList ->
                        listDSDeathTypeItems.add(dischargeDeathList)
                    }
                    setDischargeDeathTypeSpinner(listDSDeathTypeItems)
                }
            }
        }

        override fun onBadRequest(errorBody: Response<ResDischargeDeathType>?) {
        }

        override fun onServerError(response: Response<*>) {
            toast(getString(R.string.something_went_wrong))
        }

        override fun onUnAuthorized() {
            toast(getString(R.string.unauthorized))
        }

        override fun onForbidden() {
            toast(getString(R.string.something_went_wrong))
        }

        override fun onFailure(failure: String) {
            toast(failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    private val callbackGetDSPrevDataRetrofit =
        object : RetrofitCallback<DischargeSummaryPreviousResponseModel> {

            override fun onSuccessfulResponse(responseBody: Response<DischargeSummaryPreviousResponseModel>?) {
                if (responseBody?.body()?.responseContents != null)
                    responseBody.body()?.also { it ->
                        it.responseContents.let { resData ->
                            if (!resData.ds_list.isNullOrEmpty()) {
                                dischargeSummaryPreviousAdapter?.setData(resData.ds_list)
                            } else {
                                // NOTHING TODO
                            }
                        }
                    }

            }

            override fun onBadRequest(errorBody: Response<DischargeSummaryPreviousResponseModel>?) {
            }

            override fun onServerError(response: Response<*>) {
                toast(getString(R.string.something_went_wrong))
            }

            override fun onUnAuthorized() {
                toast(getString(R.string.unauthorized))
            }

            override fun onForbidden() {
                toast(getString(R.string.something_went_wrong))
            }

            override fun onFailure(failure: String) {
                toast(failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private val callbackGetDSNoteTemplate = object : RetrofitCallback<ResDischargeSummaryTemplate> {
        override fun onSuccessfulResponse(responseBody: Response<ResDischargeSummaryTemplate>?) {
            val resDischargeSummaryNoteTemplate =
                ResDischargeSummaryNoteTemplate(
                    nt_uuid = 0,
                    nt_code = "",
                    nt_name = getString(R.string.template_label)
                )
            if (!responseBody?.body()?.responseContents.isNullOrEmpty()) {
                listDSNoteTemplateItems.add(0, resDischargeSummaryNoteTemplate)
                responseBody?.body()?.responseContents?.forEach {
                    listDSNoteTemplateItems.add(it)
                }
                setDischargeNoteTemplateSpinner(listDSNoteTemplateItems)
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    resDSNoteTemplateMap.values.toMutableList()
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding?.spTemplate!!.adapter = adapter
            }
        }

        override fun onBadRequest(errorBody: Response<ResDischargeSummaryTemplate>?) {
            //TODO Nothing
        }

        override fun onServerError(response: Response<*>) {
            toast(getString(R.string.something_went_wrong))
        }

        override fun onUnAuthorized() {
            toast(getString(R.string.unauthorized))
        }

        override fun onForbidden() {
            toast(getString(R.string.something_went_wrong))
        }

        override fun onFailure(failure: String) {
            toast(failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }

    }


    private val callbackGetDSDefaultTemplate =
        object : RetrofitCallback<ResDischargeSummaryDefaultTemplate> {

            override fun onSuccessfulResponse(responseBody: Response<ResDischargeSummaryDefaultTemplate>?) {
            }

            override fun onBadRequest(errorBody: Response<ResDischargeSummaryDefaultTemplate>?) {
            }

            override fun onServerError(response: Response<*>) {
                toast(getString(R.string.something_went_wrong))
            }

            override fun onUnAuthorized() {
                toast(getString(R.string.unauthorized))
            }

            override fun onForbidden() {
                toast(getString(R.string.something_went_wrong))
            }

            override fun onFailure(failure: String) {
                toast(failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private val fetchEncounterRetrofitCallBack =
        object : RetrofitCallback<EncounterResponseModel> {
            override fun onSuccessfulResponse(response: Response<EncounterResponseModel>) {
                if (!response.body()?.responseContents?.isNullOrEmpty()!!) {
                    encounterResponseContent = response.body()?.responseContents!!
                    encounterDoctorUUID =
                        encounterResponseContent[0].encounter_doctors[0].uuid
                    encounterUUID = encounterResponseContent[0].uuid

                    //Call When set default checked
                    viewModel?.getDischargeSummaryPreviousData(
                        patientId, encounterUUID ?: 0,
                        callbackGetDSPrevDataRetrofit
                    )

                    viewModel?.getDischargeSummaryDashBoardList(
                        facilityId, patientId, encounterUUID ?: 0,
                        callbackGetDischargeSummaryRetrofit
                    )
                }
            }

            override fun onBadRequest(response: Response<EncounterResponseModel>) {
                //TODO Nothing
            }

            override fun onServerError(response: Response<*>) {
                toast(getString(R.string.something_went_wrong))
            }

            override fun onUnAuthorized() {
                toast(getString(R.string.unauthorized))
            }

            override fun onForbidden() {
                toast(getString(R.string.something_went_wrong))
            }

            override fun onFailure(failure: String) {
                toast(failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }


    private fun pdfDownload() {
        val pdfRequestModel = DischargePDFRequestModel()
        pdfRequestModel.patient_uuid = patientId
        pdfRequestModel.encounter_uuid = encounterUUID ?: 0
        pdfRequestModel.lab_uuids = listOf(labUUIDS!!, labUUID!!)
        pdfRequestModel.radiology_uuids = listOf(radiologyUUIDS!!)
        pdfRequestModel.allergy_uuids = listOf(allergyUuid!!)
        pdfRequestModel.prescription_uuids = listOf(prescriptionUUIDS!!, prescriptionUUID!!)
        pdfRequestModel.investigation_uuids = listOf(investigationUUIDS!!)
        pdfRequestModel.vitals_uuids = listOf(vitalsUUIDS!!)
        pdfRequestModel.chief_complaint_uuids = listOf()
        val bundle = Bundle()
        bundle.putParcelable(AppConstants.RESPONSECONTENT, pdfRequestModel)
        bundle.putInt(AppConstants.RESPONSENEXT, 10)
        bundle.putString("From", "")

        val labtemplatedialog = DialogDischargePDFViewerActivity()

        labtemplatedialog.arguments = bundle

        (activity as HomeActivity).replaceFragment(labtemplatedialog)
    }

    private fun clearData() {
        binding?.also {
            it.spDischargeSummaryDoctorName.setSelection(0)
            it.spDischargeType.setSelection(0)
            it.spTemplate.setSelection(0)
            it.spDeathType.setSelection(0)
            it.etDischargeSummaryDate.setText("")
            it.etDischargeSummaryDeathPlace.setText("")
            it.etTemplateNote.setText("")
        }

    }


    private fun saveData(isSaveAPI: Boolean) {
        if (isValidData()) {
            allergyAPIList.also { allegeryData ->
                if (!allegeryData.isNullOrEmpty()) {
                    allegeryData.forEach {
                        val details = Detail(
                            context_uuid = allergy.allergy_headers.context_uuid,
                            activity_uuid = allergy.allergy_headers.activity_uuid,
                            context_activity_map_uuid = allergy.allergy_headers.context_activity_map_uuid,
                            transaction_uuid = it.uuid,
                            display_order = allergy.allergy_headers.display_order
                        )
                        detailsList.add(details)
                    }

                }
            }

            diagnosisAPIList.also { diagnosisData ->
                if (!diagnosisData.isNullOrEmpty()) {
                    diagnosisData.forEach {
                        val details = Detail(
                            context_uuid = diagnosis.diagnosis_headers.context_uuid,
                            activity_uuid = diagnosis.diagnosis_headers.activity_uuid,
                            context_activity_map_uuid = diagnosis.diagnosis_headers.context_activity_map_uuid,
                            transaction_uuid = it.patient_diagnosis_uuid,
                            display_order = diagnosis.diagnosis_headers.display_order
                        )
                        detailsList.add(details)
                    }

                }
            }

            radioLogyAPIList.also { radiologyData ->
                if (!radiologyData.isNullOrEmpty()) {
                    radiologyData[0].patient_order_test_details.forEach {
                        val details = Detail(
                            context_uuid = radiology.radiology_headers.context_uuid,
                            activity_uuid = radiology.radiology_headers.activity_uuid,
                            context_activity_map_uuid = radiology.radiology_headers.context_activity_map_uuid,
                            transaction_uuid = it.uuid,
                            display_order = radiology.radiology_headers.display_order
                        )
                        detailsList.add(details)
                    }

                }
            }

            prescriptionAPIList.also { presDetail ->
                if (!presDetail.isNullOrEmpty()) {
                    presDetail.forEach {
                        val details = Detail(
                            context_uuid = prescription.prescription_headers.context_uuid,
                            activity_uuid = prescription.prescription_headers.activity_uuid,
                            context_activity_map_uuid = prescription.prescription_headers.context_activity_map_uuid,
                            transaction_uuid = it.uuid,
                            display_order = prescription.prescription_headers.display_order
                        )
                        if (!detailsList.contains(details))
                            detailsList.add(details)
                    }

                }
            }

            vitalAPIList.also { vitalDetails ->
                if (!vitalDetails.isNullOrEmpty()) {
                    vitalDetails[0].PV_list.forEach {
                        val details = Detail(
                            context_uuid = vitals.vital_headers.context_uuid,
                            activity_uuid = vitals.vital_headers.activity_uuid,
                            context_activity_map_uuid = vitals.vital_headers.context_activity_map_uuid,
                            transaction_uuid = it.patient_vital_uuid,
                            display_order = vitals.vital_headers.display_order
                        )
                        if (!detailsList.contains(details))
                            detailsList.add(details)
                    }

                }
            }

            investigationAPIList.also { investigationList ->
                if (!investigationList.isNullOrEmpty()) {
                    investigationList[0].patient_order_test_details.forEach {
                        val details = Detail(
                            context_uuid = investigation.investigation_headers.context_uuid,
                            activity_uuid = investigation.investigation_headers.activity_uuid,
                            context_activity_map_uuid = investigation.investigation_headers.context_activity_map_uuid,
                            transaction_uuid = it.uuid!!,
                            display_order = investigation.investigation_headers.display_order
                        )
                        if (!detailsList.contains(details))
                            detailsList.add(details)
                    }
                }
            }

            labAPIList.also { labDetail ->
                if (!labDetail.isNullOrEmpty()) {
                    labDetail[0].patient_order_test_details.forEach {
                        val details = Detail(
                            context_uuid = lab.lab_headers.context_uuid,
                            activity_uuid = lab.lab_headers.activity_uuid,
                            context_activity_map_uuid = lab.lab_headers.context_activity_map_uuid,
                            transaction_uuid = it.uuid,
                            display_order = lab.lab_headers.display_order
                        )
                        if (!detailsList.contains(details))
                            detailsList.add(details)
                    }
                }
            }



            chiefComplaintAPIList.also { chiefComplaiint ->
                if (!chiefComplaiint.isNullOrEmpty()) {
                    chiefComplaiint[0].chief_complaint_details.forEach {
                        val details = Detail(
                            context_uuid = chiefComplaintsDetails.cheif_complaints_headers?.context_uuid
                                ?: 0,
                            activity_uuid = chiefComplaintsDetails.cheif_complaints_headers?.activity_uuid
                                ?: 0,
                            context_activity_map_uuid = chiefComplaintsDetails.cheif_complaints_headers?.context_activity_map_uuid
                                ?: 0,
                            transaction_uuid = it.patient_cheif_complaint_uuid ?: 0,
                            display_order = chiefComplaintsDetails.cheif_complaints_headers?.display_order
                                ?: 0
                        )
                        if (!detailsList.contains(details))
                            detailsList.add(details)
                    }
                }
            }

            dischargeResult?.let {
                admissionRequestUUID = it.admission_request_uuid
                admissionStatusUUID = it.admission_status_uuid
                admissionUUID = it.admission_uuid
                encounterTypeUUID = it.encounter_type_uuid
            }


            header?.facility_uuid = facilityId.toString()
            header?.department_uuid = departmentId.toString()
            header?.patient_uuid = patientId
            header?.doctor_uuid = doctorID.toString()
            header?.encounter_uuid = this.encounterUUID!!
            header?.encounter_type_uuid = encounterTypeUUID
            header?.admission_request_uuid = admissionRequestUUID
            header?.admission_uuid = admissionUUID
            if (isSaveAPI) {
                header?.admission_status_uuid = 13
            } else {
                header?.admission_status_uuid = 6
            }
            header?.discharge_type_uuid = dischargeTypeUUID.toString()
            header?.death_type_uuid = deathTypeUUID
            header?.death_place = binding?.etDischargeSummaryDeathPlace?.text.toString()
            header?.discharge_date = dischargeAPIDate
            header?.generated_by = doctorID!!
            header?.is_alive = if (isAlive!!) true else false
            header?.generated_date = generateAPIDate
            if (noteTemplateUUID != 0)
                header?.note_template_uuid = noteTemplateUUID.toString()
            header?.data_template = templateText.toString()
            header?.comments = "say something about"
            header?.note_template_is_default = if (binding?.cbSaveAsDefault?.isChecked!!) 1 else 0

            val saveRequestModel = SaveRequestModel()
            saveRequestModel.headers = this.header!!
            saveRequestModel.details = this.detailsList

            if (isSaveAPI) {
                isApproval = false
                viewModel?.dischargeSave(facilityId, saveRequestModel, emrpostRetrofitCallback)
            } else {
                if (isSaved) {
                    isApproval = true
                    viewModel?.dischargeApprove(
                        facilityId,
                        saveRequestModel,
                        emrpostRetrofitCallback
                    )
                } else {
                    alertDialog("Before Approval save the discharge summary !!!")
                }
            }

            trackDischargeSummarySaveStart(utils?.getEncounterType())
        } else {
            alertDialog("Please fill all the field to save the data...")
        }
    }

    private fun isValidData(): Boolean {
        var isValid = true
        binding?.also {
            if (!it.swDischargeSummary.isChecked) {
                if (it.spDeathType.selectedItemPosition == 0)
                    isValid = false
                if (it.etDischargeSummaryDeathPlace.text.toString().isEmpty())
                    isValid = false
            } else {
                if (it.spDischargeType.selectedItemPosition == 0)
                    isValid = false
            }
            if (binding?.spDischargeSummaryDoctorName!!.selectedItemPosition == 0)
                isValid = false
            if (it.etDischargeSummaryDate.text.toString().isEmpty())
                isValid = false

            if (it.etTemplateNote.text.toString().isEmpty())
                isValid = false
            if (it.spTemplate.selectedItemPosition == 0)
                isValid = false

        }
        return isValid
    }


    private fun hideAndShowAllergyView(isVisible: Boolean) {
        binding?.layoutAllergy?.rvAllergy?.isVisible = isVisible
    }

    private fun hideAndShowDiagnosisView(isVisible: Boolean) {
        binding?.layoutDiagonosis?.rvDiagonosis?.isVisible = isVisible
    }

    private fun hideAndShowPrescriptionView(isVisible: Boolean) {
        binding?.layoutPrescription?.rvPrescription?.isVisible = isVisible
    }

    private fun hideAndShowLabView(isVisible: Boolean) {
        binding?.layoutLab?.rvLab?.isVisible = isVisible
    }

    private fun hideAndShowRadioLogyView(isVisible: Boolean) {
        binding?.layoutRadioLogy?.rvRadiology?.isVisible = isVisible
    }

    private fun hideAndShowVitalView(isVisible: Boolean) {
        binding?.layoutVital?.rvVitalMain?.isVisible = isVisible
    }

    private fun hideAndShowInvestigationView(isVisible: Boolean) {
        binding?.layoutInvestigation?.rvInvestigationMain?.isVisible = isVisible
    }


    private val emrpostRetrofitCallback = object : RetrofitCallback<SaveResponseModel> {
        override fun onSuccessfulResponse(responseBody: Response<SaveResponseModel>?) {
            if (responseBody?.body() != null) {
                isSaved = true
                if (isApproval) {
                    isApproval = false
                }
                alertDialog(responseBody.body()?.message.toString())
                trackDischargeSummarySaveComplete(utils?.getEncounterType(), "success", "")
            }
        }

        override fun onBadRequest(response: Response<SaveResponseModel>?) {
            if (isApproval && response?.message() != null)
                alertDialog("Admission not updated , Something wrong try again !!!")
            trackDischargeSummarySaveComplete(
                utils?.getEncounterType(),
                "failure",
                response?.body()?.message
            )
        }

        override fun onServerError(response: Response<*>) {
            trackDischargeSummarySaveComplete(
                utils?.getEncounterType(),
                "failure",
                getString(R.string.something_went_wrong)
            )
            toast(getString(R.string.something_went_wrong))
        }

        override fun onUnAuthorized() {
            trackDischargeSummarySaveComplete(
                utils?.getEncounterType(),
                "failure",
                getString(R.string.unauthorized)
            )
            toast(getString(R.string.unauthorized))
        }

        override fun onForbidden() {
            trackDischargeSummarySaveComplete(
                utils?.getEncounterType(),
                "failure",
                getString(R.string.something_went_wrong)
            )
            toast(getString(R.string.something_went_wrong))
        }

        override fun onFailure(failure: String) {
            isSaved = false
            isApproval = false
            trackDischargeSummarySaveComplete(utils?.getEncounterType(), "failure", failure)
            toast(failure)
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }


    }

    private val callbackGetDSDoctorNameRetrofit =
        object : RetrofitCallback<DischargeSummaryDoctorNameResponseModel> {

            override fun onSuccessfulResponse(resDoctorRes: Response<DischargeSummaryDoctorNameResponseModel>?) {
                if (!resDoctorRes?.body()?.responseContents.isNullOrEmpty()) {
                    val dischargeSummaryDoctorRes =
                        DischargeSummaryDoctorRes(
                            uuid = 0,
                            user_name = getString(R.string.doctor_name_label)
                        )
                    resDoctorRes?.body()?.responseContents?.also { it ->
                        if (!it.isNullOrEmpty()) {
                            listDSDoctorNameItems.add(0, dischargeSummaryDoctorRes)
                            it.forEach { dischargeSummaryDoctorList ->
                                listDSDoctorNameItems.add(dischargeSummaryDoctorList)
                            }
                            setDischargeTypeSpinner(listDSTypeItems)
                        }
                    }
                    setDischargeDoctorNameTypeSpinner(listDSDoctorNameItems)
                }
            }

            override fun onBadRequest(errorBody: Response<DischargeSummaryDoctorNameResponseModel>?) {
            }

            override fun onServerError(response: Response<*>) {
                toast(getString(R.string.something_went_wrong))
            }

            override fun onUnAuthorized() {
                toast(getString(R.string.unauthorized))
            }

            override fun onForbidden() {
                toast(getString(R.string.something_went_wrong))
            }

            override fun onFailure(failure: String) {
                toast(failure)
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }


    private fun trackDischargeSummaryVisit(type: String?) {
        AnalyticsManager.getAnalyticsManager().trackDischargeSummaryVisit(type)
    }

    private fun trackDischargeSummarySaveStart(type: String?) {
        AnalyticsManager.getAnalyticsManager().trackDischargeSummarySaveStart(type)
    }

    private fun trackDischargeSummarySaveComplete(
        type: String?,
        status: String?,
        message: String?
    ) {
        AnalyticsManager.getAnalyticsManager()
            .trackDischargeSummarySaveComplete(type, status, message)
    }

    private fun trackDischargeSummaryPreviousDischargeSummary(type: String?) {
        AnalyticsManager.getAnalyticsManager().trackDischargeSummaryPreviousDischargeSummary(type)
    }

    private fun toast(msg: String) {
        try {
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun alertDialog(msg: String) {
        val builder = AlertDialog.Builder(
            requireContext(),
            R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
        )
        builder.setTitle(getString(R.string.app_name))
        builder.setMessage(msg)
        builder.setPositiveButton("OK") { d, _ -> d?.dismiss() }
        val alert: AlertDialog = builder.create()
        alert.setOnShowListener {
            val btnPositive: Button = alert.getButton(Dialog.BUTTON_POSITIVE)
            btnPositive.textSize = 16F
            btnPositive.typeface = ResourcesCompat.getFont(requireContext(), R.font.poppins)
        }
        alert.show()
    }

    private fun alertDialogDelete(type: String) {
        val builder = AlertDialog.Builder(
            requireContext(),
            R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
        )
        builder.setTitle(getString(R.string.app_name))
        builder.setMessage("Are you sure you want to delete $type ?")
        builder.setPositiveButton("YES") { d, _ ->
            when (type) {
                getString(R.string.allergy_label) -> {
//                    allergyAPIList = ArrayList()
                    hideAndShowAllergyView(false)
                }
                getString(R.string.diagnosis) -> {
//                    diagnosisAPIList = ArrayList()
                    hideAndShowDiagnosisView(false)
                }
                getString(R.string.prescription) -> {
//                    prescriptionAPIList = ArrayList()
                    hideAndShowPrescriptionView(false)
                }
                getString(R.string.lab_label) -> {
//                    labAPIList = ArrayList()
                    hideAndShowLabView(false)
                }
                getString(R.string.investigation) -> {
//                    investigationAPIList = ArrayList()
                    hideAndShowInvestigationView(false)
                }
                getString(R.string.radiology) -> {
//                    radioLogyAPIList = ArrayList()
                    hideAndShowRadioLogyView(false)
                }
                getString(R.string.vitals) -> {
//                    vitalAPIList = ArrayList()
                    hideAndShowVitalView(false)
                }
            }
            d?.dismiss()
            //TODO RESTORE AGAIN
        }
        builder.setNegativeButton("NO") { d, _ -> d?.dismiss() }
        val alert: AlertDialog = builder.create()
        alert.setOnShowListener {
            val btnPositive: Button = alert.getButton(Dialog.BUTTON_POSITIVE)
            val btnNegative: Button = alert.getButton(Dialog.BUTTON_NEGATIVE)
            btnPositive.textSize = 16F
            btnPositive.typeface = ResourcesCompat.getFont(requireContext(), R.font.poppins)
            btnNegative.textSize = 16F
            btnNegative.typeface = ResourcesCompat.getFont(requireContext(), R.font.poppins)
        }
        alert.show()
    }


}
