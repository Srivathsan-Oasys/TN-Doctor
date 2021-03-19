package com.oasys.digihealth.doctor.ui.emr_workflow.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.callbacks.FragmentBackClick
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_ALLEGERY_CODE
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_CODE_ADMISSION
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_CODE_ANESTHESIA_NOTES
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_CODE_BLOOD_REQUEST
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_CODE_CERTIFICATE
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_CODE_CHIEF_COMPLAINTS
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_CODE_DIAGNOSIS
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_CODE_DISCHARGEMEDICATION
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_CODE_DOCUMENT
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_CODE_HISTORY
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_CODE_INVESTIGATION
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_CODE_INVESTIGATION_RESULT
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_CODE_IP_CASE_SHEET
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_CODE_LAB
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_CODE_LAB_RESULT
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_CODE_MRD
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_CODE_OP_NOTES
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_CODE_OT_NOTES
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_CODE_OT_SCHEDULE
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_CODE_PRESCRIPTION
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_CODE_PROGRESS_NOTES
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_CODE_RADIOLOGY
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_CODE_RADIOLOGY_RESULT
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_CODE_SPECIALITY_SKETCH
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_CODE_TREATMENT_KIT
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_CODE_VITALS
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_ID_CERTIFICATE
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_ID_DISCHARGEMEDICATION
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_ID_IP_CASE_SHEET
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_ID_MRD
import com.oasys.digihealth.doctor.config.AppConstants.ACTIVITY_ID_OT_SCHEDULE
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.ActivityEmrWorkflowBinding
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.ui.AdmissionFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.anesthesia_notes.ui.AnesthesiaNotesFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.blood_request.ui.BloodRequestFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.certificate.ui.CertificateFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.ChiefComplaintsFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.DocumentFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.LabFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.ui.CriticalCareChartFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.view.DiagnosisFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.ui.DietFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.ui.DischargeSummaryFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.ui.AllergyFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.history.ui.HistoryFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.ui.InvestigationFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation_result.ui.InvestigationResultFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.ip_case_sheet.ui.IpCaseSheetFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.ui.LabResultFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.model.fetch_encounters_response.FetchEncounterResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.mrd.ui.MRDFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.ui.OpNotesFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.ui.OtNotesFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.ui.OtSechduleFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.PrescriptionFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.progress_notes.view.ProgressNotesFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.ui.RadiologyFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology_result.ui.RadiologyResultFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.speciality_sketch.ui.SpecialitySketchChildFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.ui.TreatmentKitFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.view_model.EmrViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.view_model.EmrViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.ui.VitalsFragment
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response


class EmrWorkFlowActivity : Fragment() {

    private var encounter_uuid: Int? = 0
    private var encounter_doctor_uuid: Int? = 0
    private var viewpageradapter: ViewPagerAdapter? = null
    private var tabsArrayList: List<ResponseContent?>? = null
    private var binding: ActivityEmrWorkflowBinding? = null
    private var viewModel: EmrViewModel? = null
    private var utils: Utils? = null
    private var selectedFragment: Fragment? = null
    lateinit var encounterResponseContent: List<FetchEncounterResponseContent?>
    private lateinit var getStoreMasterId: List<StoreMasterresponseContent?>
    private var patientUuid: Int = 0
    private var encounterType: Int = 0
    private var facility_id: Int = 0
    private var department_uuid: Int = 0
    private var store_master_uuid: Int? = null
    private var fragmentBackClick: FragmentBackClick? = null
    var appPreferences: AppPreferences? = null


    val flow: String?
        get() = if (arguments == null) null else requireArguments().getSerializable(
            EmrWorkFlowActivity.ARG_NAME
        ) as String


    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.activity_emr_workflow,
                container,
                false
            )

        viewModel = EmrViewModelFactory(
            requireActivity().application
        )
            .create(EmrViewModel::class.java)
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        utils = Utils(requireContext())

        if (activity !is FragmentBackClick) {
//            throw ClassCastException("Hosting activity must implement BackHandlerInterface")
        } else {
            fragmentBackClick = activity as FragmentBackClick?
        }
        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)

        patientUuid = appPreferences?.getInt(AppConstants.PATIENT_UUID)!!

        encounterType = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)!!

        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)!!

        viewModel!!.errorText.observe(viewLifecycleOwner,
            Observer { toastMessage ->
                utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
            })

        binding?.contentLinearLayout?.visibility = View.INVISIBLE
        binding?.noDataFoundTextView?.visibility = View.INVISIBLE

        callEmrWorkFlow()

        if (flow.equals(AppConstants.OUT_PATIENT)) {
            setOpBackGround()
        } else {
            setIpBackGroung()
        }


        // viewModel?.getEncounter(facility_id,patientUuid, encounterType, fetchEncounterRetrofitCallBack)
        viewModel?.getStoreMaster(facility_id, department_uuid, getStoreMasterRetrofitCallback)
        viewModel?.getPatientLatestRecord(
            facility_id,
            patientUuid,
            encounterType,
            getPatientLatestEncCallback
        )
        viewModel?.getPatientById(facility_id, patientUuid, encounterType, getPatientByIdCallback)
        binding?.llSubMainLayout?.setOnClickListener {
            if (binding?.llConsultingView?.isVisible!!) {
                binding?.llConsultingView?.visibility = View.GONE
            } else {
                binding?.llConsultingView?.visibility = View.VISIBLE

            }
        }

        binding?.rlBack?.setOnClickListener {
            requireActivity().onBackPressed()
        }
        return binding!!.root
    }


    private fun setupViewPager(tabsArrayList: List<ResponseContent?>) {
        viewpageradapter = ViewPagerAdapter(activity?.supportFragmentManager!!)
        for (i in tabsArrayList.indices) {
            when {
                tabsArrayList[i]?.activity_code == ACTIVITY_CODE_CHIEF_COMPLAINTS -> {
                    viewpageradapter!!.addFragment(ChiefComplaintsFragment(), "Chief Complaints")
                }
                tabsArrayList[i]?.activity_code == ACTIVITY_CODE_LAB -> {
                    viewpageradapter!!.addFragment(LabFragment(), "Lab")
                }
                tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_RADIOLOGY -> {
                    viewpageradapter!!.addFragment(RadiologyFragment(), "Radiology")
                }
                tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_INVESTIGATION -> {
                    viewpageradapter!!.addFragment(InvestigationFragment(), "Investigation")
                }
                tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_DIAGNOSIS -> {
                    viewpageradapter!!.addFragment(DiagnosisFragment(), "Diagnosis")
                }
                tabsArrayList[i]!!.activity_code == ACTIVITY_ALLEGERY_CODE -> {
                    viewpageradapter!!.addFragment(AllergyFragment(), "Diagnosis")
                }
                tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_HISTORY -> {
                    viewpageradapter!!.addFragment(HistoryFragment(), "History")
                }
                tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_PRESCRIPTION -> {

                    val prescription = PrescriptionFragment()
                    val bundle = Bundle()
                    bundle.putInt(AppConstants.RESPONSETYPE, 0)
                    prescription.arguments = bundle


                    viewpageradapter!!.addFragment(prescription, "Prescription")
                }
                tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_VITALS -> {
                    viewpageradapter!!.addFragment(VitalsFragment(), "Vitals")
                }
                tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_LAB_RESULT -> {
                    viewpageradapter!!.addFragment(LabResultFragment(), "Lab Result")
                }
                tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_RADIOLOGY_RESULT -> {
                    viewpageradapter!!.addFragment(RadiologyResultFragment(), "Radiology Result")
                }
                tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_TREATMENT_KIT -> {
                    viewpageradapter!!.addFragment(TreatmentKitFragment(), "Treatment Kit")
                }
                tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_OP_NOTES -> {
                    viewpageradapter!!.addFragment(OpNotesFragment(), "Op Notes")
                }
                tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_DOCUMENT -> {
                    viewpageradapter!!.addFragment(DocumentFragment(), "Documents")
                }
                tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_INVESTIGATION_RESULT -> {
                    viewpageradapter!!.addFragment(
                        InvestigationResultFragment(),
                        "Investigation Result"
                    )
                }
                tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_ADMISSION -> {
                    val bundle = Bundle()
                    bundle.putString("flow", flow)
                    val admissionFragment = AdmissionFragment()
                    admissionFragment.arguments = bundle
                    viewpageradapter!!.addFragment(admissionFragment, "Admission/Referral")
                }
                tabsArrayList[i]!!.activity_route_url.equals("bloodrequest") -> {                                    //Sri activity code is null
                    viewpageradapter!!.addFragment(BloodRequestFragment(), "Blood Request")
                }
                tabsArrayList[i]!!.activity_route_url.equals("diet") -> {
                    viewpageradapter!!.addFragment(DietFragment(), "Diet")
                }
                tabsArrayList[i]!!.activity_route_url.equals("mrd") -> {
                    viewpageradapter!!.addFragment(MRDFragment(), "MRD Summary")
                }
                tabsArrayList[i]!!.activity_route_url.equals("progress") -> {
                    viewpageradapter!!.addFragment(ProgressNotesFragment(), "Progress Notes")
                }
                tabsArrayList[i]!!.activity_route_url.equals("discharge") -> {
                    viewpageradapter!!.addFragment(DischargeSummaryFragment(), "Discharge Summary")
                }
                tabsArrayList[i]!!.activity_route_url.equals("casesheet") -> {                                  //Sri activity code is null
                    viewpageradapter!!.addFragment(IpCaseSheetFragment(), "IP Case Sheet")
                }
                tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_OP_NOTES -> {
                    viewpageradapter!!.addFragment(OpNotesFragment(), "Op Notes")
                }
                tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_OT_NOTES -> {
                    viewpageradapter!!.addFragment(OtNotesFragment(), "Ot Notes")
                }
                tabsArrayList[i]!!.activity_code == ACTIVITY_CODE_ANESTHESIA_NOTES -> {
                    viewpageradapter!!.addFragment(AnesthesiaNotesFragment(), "Anesthesia Notes")
                }
                tabsArrayList[i]!!.activity_route_url.equals("criticalcare") -> {
                    viewpageradapter!!.addFragment(
                        CriticalCareChartFragment(),
                        "Critical Care Chart"
                    )
                }
                tabsArrayList[i]!!.activity_route_url.equals("medication") -> {

                    val DischargeMedication = PrescriptionFragment()

                    val bundle = Bundle()
                    bundle.putInt(AppConstants.RESPONSETYPE, 1)
                    DischargeMedication.arguments = bundle

                    viewpageradapter!!.addFragment(DischargeMedication, "Discharge medication")


                }
                tabsArrayList[i]!!.activity_route_url.equals("sketch") -> {
                    viewpageradapter!!.addFragment(
                        SpecialitySketchChildFragment(),
                        "Specality Sketch"
                    )
                }
                tabsArrayList[i]!!.activity_route_url.equals("schedule") -> {

                    viewpageradapter!!.addFragment(OtSechduleFragment(), "Ot Sechdule")

                }
                else -> {
                    viewpageradapter!!.addFragment(CertificateFragment(), "Certificate")
                }
            }
        }
        binding?.viewPager?.adapter = viewpageradapter
        viewpageradapter?.notifyDataSetChanged()
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

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }

        fun getCurrentFragment(position: Int): Fragment? {
            return mFragmentList[position]
        }


    }

    private val emrWorkFlowRetrofitCallBack = object : RetrofitCallback<EmrWorkFlowResponseModel> {
        override fun onSuccessfulResponse(response: Response<EmrWorkFlowResponseModel>) {
            if (response.body()?.responseContents?.isNotEmpty()!!) {
                binding?.contentLinearLayout?.visibility = View.VISIBLE
                binding?.noDataFoundTextView?.visibility = View.INVISIBLE
                tabsArrayList = response.body()?.responseContents!!
                setupViewPager(tabsArrayList!!)
                binding?.viewPager!!.setOffscreenPageLimit(2)
                binding?.tabLayout!!.setupWithViewPager(binding?.viewPager!!)
                for (i in tabsArrayList!!.indices) {
                    val layoutInflater: View? =
                        LayoutInflater.from(requireContext())
                            .inflate(R.layout.treatment_custom_tab_row, null, false)
                    val tabImageView = layoutInflater?.findViewById(R.id.tabImageView) as ImageView
                    val tabTextView = layoutInflater.findViewById(R.id.tabTextView) as TextView

                    when {
                        tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_CHIEF_COMPLAINTS -> {
                            tabImageView.setImageResource(R.drawable.ic_chief_complaint)
                        }
                        tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_VITALS -> {
                            tabImageView.setImageResource(R.drawable.ic_vitals_icon)
                        }
                        tabsArrayList!![i]!!.activity_route_url.equals("discharge") -> {
                            tabImageView.setImageResource(R.drawable.ic_discharge_summary)
                        }
                        tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_LAB -> {
                            tabImageView.setImageResource(R.drawable.ic_widget_lab)
                        }
                        tabsArrayList!![i]!!.activity_code == ACTIVITY_ALLEGERY_CODE -> {
                            tabImageView.setImageResource(R.drawable.ic_widget_lab)
                        }
                        tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_RADIOLOGY -> {
                            tabImageView.setImageResource(R.drawable.ic_widget_radiology)
                        }
                        tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_PRESCRIPTION -> {
                            tabImageView.setImageResource(R.drawable.ic_widget_prescription)
                        }
                        tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_INVESTIGATION -> {
                            tabImageView.setImageResource(R.drawable.ic_widget_investigation)
                        }
                        tabsArrayList!![i]!!.activity_code == AppConstants.ACTIVITY_CODE_OP_NOTES -> {
                            tabImageView.setImageResource(R.drawable.ic_widget_notes)
                        }
                        tabsArrayList!![i]!!.activity_code == AppConstants.ACTIVITY_CODE_DIET -> {
                            tabImageView.setImageResource(R.drawable.ic_widget_diet_order)
                        }
                        tabsArrayList!![i]!!.activity_route_url.equals("criticalcare") -> {
                            tabImageView.setImageResource(R.drawable.ic_widget_critical_case_sheet)
                        }
                        tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_TREATMENT_KIT -> {
                            tabImageView.setImageResource(R.drawable.ic_treatment_kit)
                        }
                        tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_HISTORY -> {
                            tabImageView.setImageResource(R.drawable.ic_history)
                        }
                        tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_DIAGNOSIS -> {
                            tabImageView.setImageResource(R.drawable.ic_diagnosis)
                        }
                        tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_ADMISSION -> {
                            tabImageView.setImageResource(R.drawable.ic_admission_referral)
                        }
                        tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_LAB_RESULT -> {
                            tabImageView.setImageResource(R.drawable.ic_lab_result)
                        }
                        tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_RADIOLOGY_RESULT -> {
                            tabImageView.setImageResource(R.drawable.ic_radiology_result)
                        }
                        tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_INVESTIGATION_RESULT -> {
                            tabImageView.setImageResource(R.drawable.ic_investigation_result)
                        }
                        tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_DOCUMENT -> {
                            tabImageView.setImageResource(R.drawable.ic_documents)
                        }
                        tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_CERTIFICATE ||
                                tabsArrayList!![i]!!.activity_id == ACTIVITY_ID_CERTIFICATE -> {
                            tabImageView.setImageResource(R.drawable.ic_certificate)
                        }
                        tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_BLOOD_REQUEST -> {
                            tabImageView.setImageResource(R.drawable.ic_blood_request)
                        }
                        tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_SPECIALITY_SKETCH -> {
                            tabImageView.setImageResource(R.drawable.ic_speciality_sketch)
                        }
                        tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_OT_NOTES -> {
                            tabImageView.setImageResource(R.drawable.ic_ot_notes)
                        }
                        tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_MRD ||
                                tabsArrayList!![i]!!.activity_id == ACTIVITY_ID_MRD -> {
                            tabImageView.setImageResource(R.drawable.ic_consultation_summary)
                        }
                        tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_IP_CASE_SHEET ||
                                tabsArrayList!![i]!!.activity_id == ACTIVITY_ID_IP_CASE_SHEET -> {
                            tabImageView.setImageResource(R.drawable.ic_ip_case_sheet)
                        }
                        tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_DISCHARGEMEDICATION ||
                                tabsArrayList!![i]!!.activity_id == ACTIVITY_ID_DISCHARGEMEDICATION -> {
                            tabImageView.setImageResource(R.drawable.ic_discharge_medication)
                        }
                        tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_ANESTHESIA_NOTES -> {
                            tabImageView.setImageResource(R.drawable.ic_anesthesia)
                        }
                        tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_OT_SCHEDULE ||
                                tabsArrayList!![i]!!.activity_id == ACTIVITY_ID_OT_SCHEDULE -> {
                            tabImageView.setImageResource(R.drawable.ic_ot_schedule)
                        }
                        tabsArrayList!![i]!!.activity_code == ACTIVITY_CODE_PROGRESS_NOTES -> {
                            tabImageView.setImageResource(R.drawable.ic_progress_notes)
                        }
                    }
                    tabTextView.text = tabsArrayList!![i]?.activity_name
                    binding?.tabLayout?.getTabAt(i)!!.customView = layoutInflater

                }


                binding?.viewPager?.addOnPageChangeListener(object :
                    ViewPager.OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) {
                    }

                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                    }

                    override fun onPageSelected(position: Int) {
                        when (tabsArrayList!![position]!!.activity_code) {
                            ACTIVITY_CODE_PRESCRIPTION ->
                                (viewpageradapter?.getCurrentFragment(position) as PrescriptionFragment).refreshPage()

                            ACTIVITY_CODE_DIAGNOSIS ->
                                (viewpageradapter?.getCurrentFragment(position) as DiagnosisFragment).refreshPage()


                            /*    AppConstants.ACTIVITY_CODE_NURSE_DISCHARGE_SUMMARY ->
                                    (viewpageradapter?.getCurrentFragment(position) as ParentNurseDichargeSummaryFragment).refreshPage()
                                "Dis05" ->
                                    (viewpageradapter?.getCurrentFragment(position) as ParentNurseDichargeSummaryFragment).refreshPage()*/
                        }
                    }
                })
            } else {
                binding?.contentLinearLayout?.visibility = View.INVISIBLE
                binding?.noDataFoundTextView?.visibility = View.VISIBLE
            }
        }

        override fun onBadRequest(response: Response<EmrWorkFlowResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: EmrWorkFlowResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
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
/*
    private val fetchEncounterRetrofitCallBack =
        object : RetrofitCallback<FectchEncounterResponseModel> {
            override fun onSuccessfulResponse(response: Response<FectchEncounterResponseModel>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    encounterResponseContent = response.body()?.responseContents!!
                    encounter_doctor_uuid = encounterResponseContent.get(0)?.encounter_doctors?.get(0)?.uuid
                    encounter_uuid = encounterResponseContent.get(0)?.uuid
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_DOCTOR_UUID,encounter_doctor_uuid!!)
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_UUID,encounter_uuid!!)
                 callEmrWorkFlow()

                } else {
                    viewModel?.createEncounter(
                        patientUuid,
                        encounterType,
                        createEncounterRetrofitCallback
                    )
                } }
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

            encounter_doctor_uuid = response?.body()?.responseContents?.encounterDoctor?.uuid!!.toInt()
            encounter_uuid = response?.body()?.responseContents?.encounter?.uuid!!.toInt()
            patientUuid= response?.body()?.responseContents?.encounterDoctor?.patient_uuid!!.toInt()
            appPreferences?.saveInt(AppConstants.ENCOUNTER_DOCTOR_UUID,encounter_doctor_uuid!!)
            appPreferences?.saveInt(AppConstants.ENCOUNTER_UUID,encounter_uuid!!)
            appPreferences?.saveInt(AppConstants.PATIENT_UUID,patientUuid!!)
            callEmrWorkFlow()
        }

        override fun onBadRequest(response: Response<CreateEncounterResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: CreateEncounterResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    CreateEncounterResponseModel::class.java
                )
                */
/*   utils?.showToast(
                       R.color.negativeToast,
                       binding?.mainLayout!!,
                       responseModel.message!!
                   )*//*

            } catch (e: Exception) {
                */
/*   utils?.showToast(
                       R.color.negativeToast,
                       binding?.mainLayout!!,
                       getString(R.string.something_went_wrong)
                   )*//*

                e.printStackTrace()
            }
        }

        override fun onServerError(response: Response<*>) {
            */
/* utils?.showToast(
                 R.color.negativeToast,
                 binding?.mainLayout!!,
                 getString(R.string.something_went_wrong)
             )*//*

        }
        override fun onUnAuthorized() {
            */
/*    utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.unauthorized)
                )*//*

        }

        override fun onForbidden() {
            */
/*   utils?.showToast(
                   R.color.negativeToast,
                   binding?.mainLayout!!,
                   getString(R.string.something_went_wrong)
               )*//*

        }

        override fun onFailure(failure: String) {
//            utils?.showToast(R.color.negativeToast, binding?.mainLayout!!, failure)
        }

        override fun onEverytime() {
            viewModel!!.progressBar.value = 8
        }
    }
*/

    val getPatientByIdCallback = object : RetrofitCallback<PatientDetailResponse> {
        override fun onSuccessfulResponse(response: Response<PatientDetailResponse>) {
            val data = response.body()?.responseContent
            if (data != null) {
                if (data.salutation_details?.name != null) {
                    binding?.tvPatientName?.text =
                        " " + data.salutation_details.name + data.first_name
                    binding?.tvPatientNameNew?.text =
                        " " + data.salutation_details.name + data.first_name + " / " +
                                data.age.toString() + "Y" + " / " + data.gender_details?.name

                } else {
                    binding?.tvPatientName?.text = data.first_name
                    binding?.tvPatientNameNew?.text = " " + data.first_name + " / " +
                            data.age.toString() + "Y" + " / " + data.gender_details?.name

                }
                binding?.tvAgeGender?.text =
                    " / " + data.age.toString() + " Year(s)" + " / " + data.gender_details?.name
                binding?.tvAgeGenderNew?.text =
                    data.uhid.toString() + " / " + data.patient_detail?.mobile




                if (data.gender_details?.name == "Male") {

                    binding?.patientImageNew?.setImageResource(R.drawable.ic_man_placeholder)
                } else {

                    binding?.patientImageNew?.setImageResource(R.drawable.ic_female_palceholder)

                }

            }


        }

        override fun onBadRequest(response: Response<PatientDetailResponse>) {
            val gson = GsonBuilder().create()
            val responseModel: GetStoreMasterResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    GetStoreMasterResponseModel::class.java
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

    val getPatientLatestEncCallback = object : RetrofitCallback<PatientLatestRecordResponse> {
        override fun onSuccessfulResponse(response: Response<PatientLatestRecordResponse>) {
            val data = response.body()?.responseContents
            var formatedDate: String? = ""
            if (data?.createdDate != null) {
                formatedDate =
                    Utils.parseDate(data.createdDate, "yyyy-MM-dd HH:mm", "dd-MMM-yyyy hh:mm a")
            }
            if (data != null)
                binding?.tvConsultentView?.text =
                    data.doctorFirstName + " / " + data.departmentName + " / " + formatedDate
        }

        override fun onBadRequest(response: Response<PatientLatestRecordResponse>) {
            val gson = GsonBuilder().create()
            val responseModel: GetStoreMasterResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    GetStoreMasterResponseModel::class.java
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

    val getStoreMasterRetrofitCallback = object : RetrofitCallback<GetStoreMasterResponseModel> {
        override fun onSuccessfulResponse(response: Response<GetStoreMasterResponseModel>) {

            if (response.body()?.responseContents?.isNotEmpty()!!) {
                val listData = response.body()?.responseContents
                getStoreMasterId = response.body()?.responseContents!!


                for (i in getStoreMasterId.size - 1 downTo 0) {

                    if (getStoreMasterId[i]?.store_type_uuid == 2) {

                        store_master_uuid = getStoreMasterId[i]?.store_master_uuid!!.toInt()
                        appPreferences?.saveInt(AppConstants.STOREMASTER_UUID, store_master_uuid!!)

                        var store_name = getStoreMasterId[i]?.store_master!!.store_name

                        appPreferences?.saveString(AppConstants.STOREMASTER_NAME, store_name)


                        break
                    }

                }

                /*  store_master_uuid = getStoreMasterId[getStoreMasterId.size - 1]?.store_master_uuid!!.toInt()
                  appPreferences?.saveInt(AppConstants.STOREMASTER_UUID,store_master_uuid!!)

                  var store_name = getStoreMasterId[getStoreMasterId.size - 1]?.store_master!!.store_name


                  appPreferences?.saveString(AppConstants.STOREMASTER_NAME,store_name)*/
            } else {


                appPreferences?.saveInt(AppConstants.STOREMASTER_UUID, 0)

                appPreferences?.saveString(AppConstants.STOREMASTER_NAME, "")
            }
        }

        override fun onBadRequest(response: Response<GetStoreMasterResponseModel>) {
            val gson = GsonBuilder().create()
            val responseModel: GetStoreMasterResponseModel
            try {
                responseModel = gson.fromJson(
                    response.errorBody()!!.string(),
                    GetStoreMasterResponseModel::class.java
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

    companion object {
        const val ARG_NAME = "flow"


        fun newInstance(from: String): EmrWorkFlowActivity {
            val fragment = EmrWorkFlowActivity()

            val bundle = Bundle().apply {
                putSerializable(ARG_NAME, from)
            }

            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onStart() {
        super.onStart()
        fragmentBackClick?.setSelectedFragment(this)
    }

    fun callEmrWorkFlow() {
        if (flow.equals(AppConstants.OUT_PATIENT)) {
            viewModel?.getEmrWorkFlow(emrWorkFlowRetrofitCallBack, 2)
        } else {
            viewModel?.getEmrWorkFlow(emrWorkFlowRetrofitCallBack, 3)
        }
    }

    fun setOpBackGround() {
        binding?.llMainLayout?.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.ip_bg_color
            )
        )
        binding?.llSubMainLayout?.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.ip_border_color)

    }

    fun setIpBackGroung() {
        binding?.llMainLayout?.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.op_bg_color
            )
        )
        binding?.llSubMainLayout?.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.op_border_color)

    }


}