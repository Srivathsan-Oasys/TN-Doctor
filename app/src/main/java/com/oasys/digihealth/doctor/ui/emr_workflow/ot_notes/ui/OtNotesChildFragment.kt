package com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentOtNotesChildBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.fire_base_analytics.AnalyticsManager
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.ui.AdmissionFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.blood_request.ui.BloodRequestFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.certificate.ui.CertificateFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.ChiefComplaintsFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.DocumentFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.LabFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.critical_care_chart.ui.CriticalCareChartFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.view.DiagnosisFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.diet.ui.DietFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.ui.AllergyFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.history.ui.HistoryFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.ui.InvestigationFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation_result.ui.InvestigationResultFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.ui.LabResultFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.model.EmrWorkFlowResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.mrd.ui.MRDFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.ui.OpNotesFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.add_consultations.OtNotesAddConsultationsReq
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.add_consultations.OtNotesAddConsultationsResp
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.get_default.GetOtNotesDefaultResp
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.prev_records.GetOtNotesPreviousRecordsResp
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.prev_records.observed_values.GetOtNotesObservedValuesResp
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.set_default.SetOtNotesDefaultReq
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.set_default.SetOtNotesDefaultResp
import com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.view_model.OtNotesViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.PrescriptionFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.ui.RadiologyFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology_result.ui.RadiologyResultFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.speciality_sketch.ui.SpecialitySketchFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.ui.TreatmentKitFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.ui.VitalsFragment
import com.oasys.digihealth.doctor.utils.custom_views.CustomProgressDialog
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response

class OtNotesChildFragment : Fragment() {

    private var binding: FragmentOtNotesChildBinding? = null
    private var viewModel: OtNotesViewModel? = null
    private var utils: Utils? = null
    private var customProgressDialog: CustomProgressDialog? = null

    private var appPreferences: AppPreferences? = null
    private var facilityId: Int? = null

    //    private var patientId: Int? = null
    private var departmentUuid: Int? = null
    private var encounterType: Int? = null
//    private var encounterUuid: Int? = null

    private var patientUuid: Int = 0
    private var encounterUuid: Int = 0
    private var encounterDoctorUuid: Int = 0
    private var doctorUuid: Int = 0
    private var encounterTypeUuid: Int = 0
    private var profileUuid: Int? = 0
    private var consultationUuid: Int? = 0

    private val profileTypesRespList = ArrayList<ResponseContent>()
    private val profileTypesRespNameList = ArrayList<String>()

    private val prevOtNotesList =
        ArrayList<com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.prev_records.ResponseContent>()
    private val observedValuesList =
        ArrayList<com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.prev_records.observed_values.ResponseContent>()

    val headingList = ArrayList<ProfileSection>()

    private var selectedItemPosition: Int? = null

    private val saveOtNotesDetailsReqItemList = ArrayList<SaveOtNotesDetailsReqItem>()

    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    var mandatoryQuestions = HashMap<Int, Boolean>()

    private val emrWorkflowList =
        ArrayList<com.oasys.digihealth.doctor.ui.emr_workflow.model.ResponseContent>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_ot_notes_child,
                container,
                false
            )
        utils = Utils(requireContext())
        customProgressDialog = CustomProgressDialog(activity)

        viewModel = ViewModelProvider(this)[OtNotesViewModel::class.java]

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilityId = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        patientUuid = appPreferences?.getInt(AppConstants.PATIENT_UUID) ?: 0
        departmentUuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
//        encounterUuid = appPreferences?.getInt(AppConstants.ENCOUNTER_UUID)
        encounterType = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)
//        encounterDoctorUuid = appPreferences?.getInt(AppConstants.ENCOUNTER_DOCTOR_UUID)

        userDetailsRoomRepository = activity?.application?.let { UserDetailsRoomRepository(it) }

        trackOtNotesVisit(utils?.getEncounterType())

        initViews()
        listeners()
        attachObservables()

        getAllProfileTypes()
        getEmrWorkflow()

        return binding?.root
    }

    private fun initViews() {
        binding?.rvPrevOtNotes?.layoutManager = LinearLayoutManager(activity)
        binding?.rvPrevOtNotes?.adapter = PrevOtNotesAdapter(list = prevOtNotesList,
            view = { responseContent ->
                binding?.drawerLayout?.closeDrawer(GravityCompat.END)
                selectProfile(responseContent.profile_uuid)
                getObservedValues(responseContent)
            })
    }

    private fun populateHeading() {
        binding?.rvHeading?.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding?.rvHeading?.adapter = OtNotesHeadingAdapter(
            list = headingList,
            emrWorkflowList = emrWorkflowList,
            select = { profileSection ->
                loadFragment(profileSection)
                headingList.forEach { ps: ProfileSection ->
                    ps.isSelected = (profileSection == ps)
                }
            })
    }

    private fun loadFragment(
        profileSection: ProfileSection,
        observedValuesList: ArrayList<com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.prev_records.observed_values.ResponseContent>? = null
    ) {
//        val bundle = Bundle()
//        bundle.putSerializable("sub_heading", profileSection)
        val fragment: Fragment
//        fragment.arguments = bundle
        if (profileSection.sections == null) {
            fragment = when (profileSection.activity_uuid) {
                42 -> {
                    LabFragment()
                }
                43 -> {
                    RadiologyFragment()
                }
                44 -> {
                    val prescriptionFragment = PrescriptionFragment()
                    val bundle = Bundle()
                    bundle.putInt(AppConstants.RESPONSETYPE, 0)
                    prescriptionFragment.arguments = bundle
                    prescriptionFragment
                }
                45 -> {
                    TreatmentKitFragment()
                }
                46 -> {
                    ChiefComplaintsFragment()
                }
                47 -> {
                    HistoryFragment()
                }
                57 -> {
                    VitalsFragment()
                }
                58 -> {
                    InvestigationFragment()
                }
                59 -> {
                    DiagnosisFragment()
                }
                60 -> {
                    val bundle = Bundle()
                    bundle.putString("flow", utils?.getWorkFlow())
                    val admissionFragment = AdmissionFragment()
                    admissionFragment.arguments = bundle
                    admissionFragment
                }
                182 -> {
                    LabResultFragment()
                }
                183 -> {
                    RadiologyResultFragment()
                }
                184 -> {
                    InvestigationResultFragment()
                }
                210 -> {
                    DocumentFragment()
                }
                211 -> {
                    DietFragment()
                }
                382 -> {
                    CertificateFragment()
                }
                383 -> {
                    MRDFragment()
                }
                219 -> {
                    AllergyFragment()
                }
                1289 -> {
                    OpNotesFragment()
                }
                1315 -> {
                    CriticalCareChartFragment()
                }
                1309 -> {
                    BloodRequestFragment()
                }
                1310 -> {
                    SpecialitySketchFragment()
                }
                else -> {
                    BloodRequestFragment()
                }
            }
        } else {
            fragment = OtNotesHeadingFragment(this, profileSection, observedValuesList)
        }
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flHeadingContainer, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun removeAllHeading() {
        headingList.clear()
        for (fragment in childFragmentManager.fragments) {
            childFragmentManager.beginTransaction().remove(fragment).commit()
        }
        binding?.rvHeading?.adapter?.notifyDataSetChanged()
    }

    private fun setupHeadingRecyclerView() {
        binding?.rvHeading?.adapter?.notifyDataSetChanged()
        if (headingList.size > 0) {
            headingList[0].isSelected = true
            if (observedValuesList.size > 0)
                loadFragment(headingList[0], observedValuesList)
            else
                loadFragment(headingList[0])
        } else {
            removeAllHeading()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun listeners() {
        binding?.spinnerProfileTypes?.setOnTouchListener { v, event ->
            if (binding?.spinnerProfileTypes?.adapter == null) {
                val adapter =
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        profileTypesRespNameList
                    )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding?.spinnerProfileTypes?.adapter = adapter
            }
            false
        }

        binding?.spinnerProfileTypes?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    getEncounter()
                    mandatoryQuestions.clear()
                    selectedItemPosition = position
                    val selectedItem = parent?.getItemAtPosition(position)
                    for (i in profileTypesRespList.indices) {
                        val profileTypesResp = profileTypesRespList[i]
                        if (profileTypesResp.profile_name == selectedItem) {
                            profileUuid = profileTypesResp.uuid
                            getOtNotes(profileTypesResp.uuid ?: 0)
                        }
                    }
                }
            }

        binding?.chkSetDefault?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val selectedItemId = binding?.spinnerProfileTypes?.selectedItemId ?: -1
                val selectedItem = binding?.spinnerProfileTypes?.selectedItem
                if (selectedItemId >= 0) {
                    for (i in profileTypesRespList.indices) {
                        val profileTypeResp = profileTypesRespList[i]
                        if (profileTypeResp.profile_name == selectedItem) {
                            setDefault(
                                profileTypeResp.profile_type_uuid ?: 0,
                                profileTypeResp.uuid ?: 0
                            )
                            break
                        }
                    }
                } else {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        "Please select any dropdown item"
                    )
                    binding?.chkSetDefault?.isChecked = false
                }
            } else {

            }
        }

        binding?.favouriteDrawerCardView?.setOnClickListener {
            binding?.drawerLayout!!.openDrawer(GravityCompat.END)
        }

        binding?.drawerLayout?.addDrawerListener(object : ActionBarDrawerToggle(
            activity,
            binding?.drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                trackOtNotesPreviousOtNotes(utils?.getEncounterType())
                getPreviousRecords()
            }
        })

        binding?.saveCardView?.setOnClickListener {
            if (mandatoryQuestions.size <= 0) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.enter_mandatory_fields)
                )
                return@setOnClickListener
            }
            mandatoryQuestions.values.forEach {
                if (!it) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        getString(R.string.enter_mandatory_fields)
                    )
                    return@setOnClickListener
                }
            }
            trackOtNotesSaveStart(utils?.getEncounterType())
//            getEncounter()
            saveAnswers()
        }

        binding?.clearCardView?.setOnClickListener {
            clearAllFields()
        }
    }

    private fun attachObservables() {
        viewModel?.progress?.observe(requireActivity(), Observer { progress ->
            if (progress == View.VISIBLE) {
                customProgressDialog?.show()
            } else if (progress == View.GONE) {
                customProgressDialog?.dismiss()
            }
        })
    }

    private fun clearAllFields() {
        saveOtNotesDetailsReqItemList.clear()

        //remove all the UI and populate the fresh UI
        selectedItemPosition?.let { itemPosition ->
            val selectedItem = binding?.spinnerProfileTypes?.getItemAtPosition(itemPosition)
            for (i in profileTypesRespList.indices) {
                val profileTypesResp = profileTypesRespList[i]
                if (profileTypesResp.profile_name == selectedItem) {
                    getOtNotes(profileTypesResp.uuid ?: 0)
                }
            }
        }
    }

    private fun selectProfile(profileUuid: Int?) {
        if (binding?.spinnerProfileTypes?.adapter == null) {
            val adapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    profileTypesRespNameList
                )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding?.spinnerProfileTypes?.adapter = adapter
        }
        var position = 0
        for (i in profileTypesRespList.indices) {
            val profileTypeResp = profileTypesRespList[i]
            if (profileTypeResp.uuid == profileUuid) {
                position = i
                break
            }
        }
        binding?.spinnerProfileTypes?.setSelection(position)
    }

    private fun getAllProfileTypes() {
        viewModel?.getAllProfileTypes(
            facility_uuid = facilityId!!,
            profileType = AppConstants.PROFILE_TYPE_OT_NOTES,
            departmentId = departmentUuid!!,
            getAllProfileTypesRespRetrofitCallback = getAllProfileTypesRespRetrofitCallback
        )
    }

    private fun getEmrWorkflow() {
        viewModel?.getEmrWorkflow(
            contextId = 2,
            emrWorkFlowResponseModelCallback = emrWorkFlowResponseModelCallback
        )
    }

    private fun getOtNotes(profileUuid: Int) {
        viewModel?.getOtNotes(facilityId!!, profileUuid, getOtNotesRestRetrofitCallback)
    }

    private fun getEncounter() {
        viewModel?.getEncounter(
            facilityId!!,
            patientUuid,
            departmentUuid!!,
            encounterType!!,
            getOtNotesEncounterByDocAndPatientIdRespCallback
        )
    }

    private fun saveAnswers() {
        val body = SaveOtNotesDetailsReq()
        body.addAll(saveOtNotesDetailsReqItemList)

        viewModel?.saveAnswers(facilityId!!, body, saveOtNotesDetailsRespCallback)
    }

    private fun getDefault() {
        viewModel?.getDefault(
            facilityId!!,
            AppConstants.PROFILE_TYPE_OT_NOTES,
            getOtNotesDefaultRespCallback
        )
    }

    private fun setDefault(profileTypeUuid: Int, profileUuid: Int) {
        val body = SetOtNotesDefaultReq(profileTypeUuid, profileUuid)
        viewModel?.setDefault(facilityId!!, body, setOtNotesDefaultRespCallback)
    }

    private fun getPreviousRecords() {
        viewModel?.getPreviousRecords(
            facilityId!!,
            patientUuid,
            1,
            getOtNotesPreviousRecordsRespCallback
        )
    }

    private fun getObservedValues(
        responseContent: com.oasys.digihealth.doctor.ui.emr_workflow.ot_notes.model.prev_records.ResponseContent
    ) {
        viewModel?.getObservedValues(
            facilityId!!,
            patientUuid,
            getOtNotesObservedValuesRespCallback
        )
    }

    private fun addConsultation() {
        val body = OtNotesAddConsultationsReq(
            approved_by = 0,
            claim_number = "",
            claim_process_uuid = 0,
            department_uuid = departmentUuid?.toString(),
            doctor_uuid = doctorUuid.toString(),
            encounter_doctor_uuid = encounterDoctorUuid,
            encounter_type_uuid = encounterTypeUuid,
            encounter_uuid = encounterUuid,
            entry_status = 1,
            last_consult_by = 0,
            last_consult_date = "",
            ot_register_uuid = 0,
            patient_uuid = patientUuid.toString(),
            profile_type_uuid = 0,
            profile_uuid = profileUuid,
            restrict_reason = "",
            visible_dept_uuid = 0,
            visible_user_uuid = 0,
            visittype_uuid = 0,
            ward_uuid = 0
        )
        viewModel?.addConsultations(
            facilityId!!,
            body,
            otNotesAddConsultationsRespCallback
        )
    }

    fun formReqPayload(
        profileSection: ProfileSection,
        profileSectionCategory: ProfileSectionCategory,
        profileSectionCategoryConcept: ProfileSectionCategoryConcept,
        profileSectionCategoryConceptValue: ProfileSectionCategoryConceptValue,
        answer: String,
        toRemove: Boolean
    ) {
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        saveOtNotesDetailsReqItemList.forEach { item ->
            /*update ans*/
            if (item.profile_section_category_concept_value_uuid == profileSectionCategoryConceptValue.uuid) {
                val saveOtNotesDetailsReqItem =
                    saveOtNotesDetailsReqItemList[saveOtNotesDetailsReqItemList.indexOf(item)]

                item.term_key = answer

                saveOtNotesDetailsReqItemList.remove(item)
                if (!toRemove)
                    saveOtNotesDetailsReqItemList.add(saveOtNotesDetailsReqItem)

                return
            }
        }

        //sri some values are hardcoded based on api dev team suggestion
        val saveOtNotesDetailsReqItem = SaveOtNotesDetailsReqItem(
            activity_uuid = null,
            category_key = profileSectionCategory.categories?.name,
            category_uuid = profileSectionCategory.category_uuid,
            comments = "",
            concept_key = profileSectionCategoryConcept.name,
            concept_uuid = profileSectionCategoryConceptValue.profile_section_category_concept_uuid,
            consultation_uuid = consultationUuid,
            doctor_uuid = doctorUuid,
            encounter_doctor_uuid = encounterDoctorUuid,
            encounter_type_uuid = encounterTypeUuid,
            encounter_uuid = encounterUuid,
            entry_date = utils?.getCurrentDateTime("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
            entry_status = 1,
            is_comment_button_selected = false,
            is_latest = 1,
            patient_uuid = patientUuid.toString(),
            profile_section_category_concept_uuid = profileSectionCategoryConceptValue.profile_section_category_concept_uuid,
            profile_section_category_concept_value_terms_uuid = 0,
            profile_section_category_concept_value_uuid = profileSectionCategoryConceptValue.uuid,
            profile_section_category_uuid = profileSectionCategory.uuid,
            profile_section_uuid = profileSection.uuid,
            profile_type_uuid = AppConstants.PROFILE_TYPE_OT_NOTES,
            profile_uuid = profileSection.profile_uuid,
            result_binary = "",
            result_path = "",
            result_value = 0,
            result_value_json = "",
            result_value_rich_text = "",
            section_key = profileSection.sections?.name,
            section_uuid = profileSection.section_uuid,
            status = 1,
            term_key = answer
        )

        saveOtNotesDetailsReqItemList.add(saveOtNotesDetailsReqItem)
    }

    private val getAllProfileTypesRespRetrofitCallback =
        object : RetrofitCallback<GetOtNotesAllProfileTypesResp> {
            override fun onSuccessfulResponse(responseBody: Response<GetOtNotesAllProfileTypesResp>?) {
                if (responseBody?.isSuccessful == true) {
                    responseBody.body()?.let { getAllProfileTypesResp ->
                        getAllProfileTypesResp.responseContents?.forEach { responseContent: ResponseContent? ->
                            profileTypesRespList.add(responseContent!!)
                            profileTypesRespNameList.add(responseContent.profile_name ?: "")
                        }
                    }
                    getDefault()
                }
            }

            override fun onBadRequest(errorBody: Response<GetOtNotesAllProfileTypesResp>?) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
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

            override fun onFailure(s: String?) {
                if (s != null) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        s
                    )
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private val emrWorkFlowResponseModelCallback =
        object : RetrofitCallback<EmrWorkFlowResponseModel> {
            override fun onSuccessfulResponse(responseBody: Response<EmrWorkFlowResponseModel>?) {
                if (responseBody?.code() == 200) {
                    emrWorkflowList.clear()
                    responseBody.body()?.responseContents?.forEach { responseContent ->
                        responseContent?.let { emrWorkflowList.add(it) }
                    }
//                    binding?.rvHeading?.adapter?.notifyDataSetChanged()
                    populateHeading()
                }
            }

            override fun onBadRequest(errorBody: Response<EmrWorkFlowResponseModel>?) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
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

            override fun onFailure(s: String?) {
                if (s != null) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        s
                    )
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private val getOtNotesRestRetrofitCallback =
        object : RetrofitCallback<GetOtNotesDetailResp> {
            override fun onSuccessfulResponse(responseBody: Response<GetOtNotesDetailResp>?) {
                if (responseBody?.isSuccessful == true) {
                    responseBody.body()?.let { getOtNotesResp ->
                        getOtNotesResp.responseContents?.forEach { responseContent: ResponseContentX? ->
                            headingList.clear()
                            responseContent?.profile_sections?.forEach { profileSection: ProfileSection? ->
                                headingList.add(profileSection!!)
                            }
                            setupHeadingRecyclerView()
                        }
                        if (getOtNotesResp.responseContents?.isNullOrEmpty()!!)
                            removeAllHeading()
                    }
                }
            }

            override fun onBadRequest(errorBody: Response<GetOtNotesDetailResp>?) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
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

            override fun onFailure(s: String?) {
                if (s != null) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        s
                    )
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private val getOtNotesEncounterByDocAndPatientIdRespCallback =
        object : RetrofitCallback<GetOtNotesEncounterByDocAndPatientIdResp> {
            override fun onSuccessfulResponse(responseBody: Response<GetOtNotesEncounterByDocAndPatientIdResp>?) {
                responseBody?.body()?.let { getOtNotesEncounterByDocAndPatientIdResp ->
                    if (getOtNotesEncounterByDocAndPatientIdResp.responseContents?.isNotEmpty() == true) {
                        getOtNotesEncounterByDocAndPatientIdResp.responseContents?.get(0)
                            .let { responseContentXX ->
                                doctorUuid =
                                    responseContentXX?.encounter_doctors?.get(0)?.doctor_uuid ?: 0
                                encounterDoctorUuid =
                                    responseContentXX?.encounter_doctors?.get(0)?.uuid ?: 0
                                encounterTypeUuid = responseContentXX?.encounter_type_uuid ?: 0
                                encounterUuid = responseContentXX?.uuid ?: 0
                                patientUuid = responseContentXX?.patient_uuid ?: 0
                                appPreferences?.saveInt(
                                    AppConstants.ENCOUNTER_DOCTOR_UUID,
                                    encounterDoctorUuid
                                )
                                appPreferences?.saveInt(AppConstants.ENCOUNTER_UUID, encounterUuid)

                                addConsultation()
//                                saveAnswers()
                            }
                    } else {
                        viewModel?.createEncounter(
                            patientUuid,
                            encounterType!!,
                            createEncounterRetrofitCallback
                        )
                    }
                }
            }

            override fun onBadRequest(errorBody: Response<GetOtNotesEncounterByDocAndPatientIdResp>?) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
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

            override fun onFailure(s: String?) {
                if (s != null) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        s
                    )
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private val createEncounterRetrofitCallback =
        object : RetrofitCallback<CreateEncounterResponseModel> {
            override fun onSuccessfulResponse(response: Response<CreateEncounterResponseModel>) {


                encounterDoctorUuid =
                    response.body()?.responseContents?.encounterDoctor?.uuid!!.toInt()
                encounterUuid = response.body()?.responseContents?.encounter?.uuid!!.toInt()
                patientUuid =
                    response.body()?.responseContents?.encounterDoctor?.patient_uuid!!.toInt()

//                saveAnswers()
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
                viewModel!!.progress.value = 8
            }
        }

    private val saveOtNotesDetailsRespCallback =
        object : RetrofitCallback<SaveOtNotesDetailsResp> {
            override fun onSuccessfulResponse(responseBody: Response<SaveOtNotesDetailsResp>?) {
                if (responseBody?.isSuccessful == true) {
                    trackOtNotesSaveComplete(utils?.getEncounterType(), "success", "")
                    utils?.showToast(
                        R.color.positiveToast,
                        binding?.mainLayout!!,
                        getString(R.string.data_save)
                    )
                    saveOtNotesDetailsReqItemList.clear()
                    clearAllFields()
                }
            }

            override fun onBadRequest(errorBody: Response<SaveOtNotesDetailsResp>?) {
                trackOtNotesSaveComplete(
                    utils?.getEncounterType(),
                    "failure",
                    errorBody?.body()?.message
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onServerError(response: Response<*>?) {
                trackOtNotesSaveComplete(
                    utils?.getEncounterType(),
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
                trackOtNotesSaveComplete(
                    utils?.getEncounterType(),
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
                trackOtNotesSaveComplete(
                    utils?.getEncounterType(),
                    "failure",
                    getString(R.string.something_went_wrong)
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onFailure(s: String?) {
                trackOtNotesSaveComplete(utils?.getEncounterType(), "failure", s)
                if (s != null) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        s
                    )
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private val getOtNotesDefaultRespCallback = object : RetrofitCallback<GetOtNotesDefaultResp> {
        override fun onSuccessfulResponse(responseBody: Response<GetOtNotesDefaultResp>?) {
            if (responseBody?.isSuccessful == true) {
                responseBody.body()?.let { getOtNotesDefaultResp ->
                    getOtNotesDefaultResp.responseContent?.let { responseContent ->
                        selectProfile(responseContent.profile_uuid)
                    }
                }
            }
        }

        override fun onBadRequest(errorBody: Response<GetOtNotesDefaultResp>?) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
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

        override fun onFailure(s: String?) {
            if (s != null) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    s
                )
            }
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    private val setOtNotesDefaultRespCallback = object : RetrofitCallback<SetOtNotesDefaultResp> {
        override fun onSuccessfulResponse(responseBody: Response<SetOtNotesDefaultResp>?) {
            if (responseBody?.isSuccessful == true) {
                utils?.showToast(
                    R.color.positiveToast,
                    binding?.mainLayout!!,
                    getString(R.string.data_save)
                )
            }
        }

        override fun onBadRequest(errorBody: Response<SetOtNotesDefaultResp>?) {
            utils?.showToast(
                R.color.negativeToast,
                binding?.mainLayout!!,
                getString(R.string.something_went_wrong)
            )
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

        override fun onFailure(s: String?) {
            if (s != null) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    s
                )
            }
        }

        override fun onEverytime() {
            viewModel!!.progress.value = 8
        }
    }

    private val getOtNotesPreviousRecordsRespCallback =
        object : RetrofitCallback<GetOtNotesPreviousRecordsResp> {
            override fun onSuccessfulResponse(responseBody: Response<GetOtNotesPreviousRecordsResp>?) {
                if (responseBody?.isSuccessful == true) {
                    responseBody.body()?.let { getOtNotesPreviousRecordsResp ->
                        getOtNotesPreviousRecordsResp.responseContents?.let { list ->
                            prevOtNotesList.clear()
                            prevOtNotesList.addAll(list)
                            binding?.rvPrevOtNotes?.adapter?.notifyDataSetChanged()
                        }
                    }
                }
            }

            override fun onBadRequest(errorBody: Response<GetOtNotesPreviousRecordsResp>?) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
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

            override fun onFailure(s: String?) {
                if (s != null) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        s
                    )
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private val getOtNotesObservedValuesRespCallback =
        object : RetrofitCallback<GetOtNotesObservedValuesResp> {
            override fun onSuccessfulResponse(responseBody: Response<GetOtNotesObservedValuesResp>?) {
                if (responseBody?.isSuccessful == true) {
                    responseBody.body()?.let { getOtNotesObservedValuesResp ->
                        getOtNotesObservedValuesResp.responseContent?.let { list ->
                            observedValuesList.addAll(list)
                        }
                    }
                }
            }

            override fun onBadRequest(errorBody: Response<GetOtNotesObservedValuesResp>?) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
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

            override fun onFailure(s: String?) {
                if (s != null) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        s
                    )
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private val otNotesAddConsultationsRespCallback =
        object : RetrofitCallback<OtNotesAddConsultationsResp> {
            override fun onSuccessfulResponse(responseBody: Response<OtNotesAddConsultationsResp>?) {
                if (responseBody?.body()?.code == 200) {
                    consultationUuid = responseBody.body()?.responseContents?.uuid
                }
            }

            override fun onBadRequest(errorBody: Response<OtNotesAddConsultationsResp>?) {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
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

            override fun onFailure(s: String?) {
                if (s != null) {
                    utils?.showToast(
                        R.color.negativeToast,
                        binding?.mainLayout!!,
                        s
                    )
                }
            }

            override fun onEverytime() {
                viewModel!!.progress.value = 8
            }
        }

    private fun trackOtNotesVisit(type: String?) {
        AnalyticsManager.getAnalyticsManager().trackOtNotesVisit(type)
    }

    private fun trackOtNotesSaveStart(type: String?) {
        AnalyticsManager.getAnalyticsManager().trackOtNotesSaveStart(type)
    }

    private fun trackOtNotesSaveComplete(type: String?, status: String?, message: String?) {
        AnalyticsManager.getAnalyticsManager().trackOtNotesSaveComplete(type, status, message)
    }

    private fun trackOtNotesPreviousOtNotes(type: String?) {

    }

}
