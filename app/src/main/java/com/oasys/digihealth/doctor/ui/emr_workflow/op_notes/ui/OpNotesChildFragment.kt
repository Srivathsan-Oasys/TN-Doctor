package com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentOpNotesChildBinding
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
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.add_consultations.OpNotesAddConsultationsReq
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.add_consultations.OpNotesAddConsultationsResp
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.get_default.GetOpNotesDefaultResp
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.prev_records.GetOpNotesPreviousRecordsResp
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.prev_records.observed_values.GetOpNotesObservedValuesResp
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.set_default.SetOpNotesDefaultReq
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.set_default.SetOpNotesDefaultResp
import com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.view_model.OpNotesViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.PrescriptionFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.ui.RadiologyFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology_result.ui.RadiologyResultFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.speciality_sketch.ui.SpecialitySketchFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.ui.TreatmentKitFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.ui.VitalsFragment
import com.oasys.digihealth.doctor.utils.CustomProgressDialog
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response

class OpNotesChildFragment : Fragment() {

    private var binding: FragmentOpNotesChildBinding? = null
    private var viewModel: OpNotesViewModel? = null
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

    private val prevOpNotesList =
        ArrayList<com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.prev_records.ResponseContent>()
    private val observedValuesList =
        ArrayList<com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.prev_records.observed_values.ResponseContent>()

    val headingList = ArrayList<ProfileSection>()

    private var selectedItemPosition: Int? = null

    private val saveOpNotesDetailsReqItemList = ArrayList<SaveOpNotesDetailsReqItem>()

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
                R.layout.fragment_op_notes_child,
                container,
                false
            )
        utils = Utils(requireContext())
        customProgressDialog = CustomProgressDialog(activity)

        viewModel = ViewModelProvider(this)[OpNotesViewModel::class.java]

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilityId = appPreferences?.getInt(AppConstants.FACILITY_UUID)
        patientUuid = appPreferences?.getInt(AppConstants.PATIENT_UUID) ?: 0
        departmentUuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
//        encounterUuid = appPreferences?.getInt(AppConstants.ENCOUNTER_UUID)
        encounterType = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)
//        encounterDoctorUuid = appPreferences?.getInt(AppConstants.ENCOUNTER_DOCTOR_UUID)

        userDetailsRoomRepository = activity?.application?.let { UserDetailsRoomRepository(it) }

        trackOpNotesVisit(utils?.getEncounterType())

        initViews()
        listeners()
        attachObservables()

        getAllProfileTypes()
        getEmrWorkflow()

        return binding?.root
    }

    private fun initViews() {
        binding?.rvPrevOpNotes?.layoutManager = LinearLayoutManager(activity)
        binding?.rvPrevOpNotes?.adapter = PrevOpNotesAdapter(list = prevOpNotesList,
            view = { responseContent ->
                binding?.drawerLayout?.closeDrawer(GravityCompat.END)
                selectProfile(responseContent.profile_uuid)
                getObservedValues(responseContent)
            })
    }

    private fun populateHeading() {
        binding?.rvHeading?.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding?.rvHeading?.adapter = OpNotesHeadingAdapter(
            list = headingList,
            emrWorkflowList = emrWorkflowList,
            select = { profileSection ->
                loadFragment(profileSection)
                headingList.forEach { ps: ProfileSection ->
                    ps.isSelected = (profileSection == ps)
                }
            }
        )
    }

    private fun loadFragment(
        profileSection: ProfileSection,
        observedValuesList: ArrayList<com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.prev_records.observed_values.ResponseContent>? = null
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
            fragment = OpNotesHeadingFragment(this, profileSection, observedValuesList)
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
        binding?.spinnerProfileTypes?.setOnTouchListener { _, _ ->
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
                            getOpNotes(profileTypesResp.uuid ?: 0)
                        }
                    }
                }
            }

        binding?.chkSetDefault?.setOnCheckedChangeListener { _, isChecked ->
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

                trackOpNotesPreviousOpNotes(utils?.getEncounterType())
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
            trackOpNotesSaveStart(utils?.getEncounterType())
//            getEncounter()
            saveAnswers()
        }

        binding?.clearCardView?.setOnClickListener {
            clearAllFields()
        }

        binding?.hsvTable?.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding?.drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN)
//                    view.parent.requestDisallowInterceptTouchEvent(true)
                }
                MotionEvent.ACTION_UP -> {
                    binding?.drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
//                    view.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            view.onTouchEvent(motionEvent)
            true
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
        saveOpNotesDetailsReqItemList.clear()

        //remove all the UI and populate the fresh UI
        selectedItemPosition?.let { itemPosition ->
            val selectedItem = binding?.spinnerProfileTypes?.getItemAtPosition(itemPosition)
            for (i in profileTypesRespList.indices) {
                val profileTypesResp = profileTypesRespList[i]
                if (profileTypesResp.profile_name == selectedItem) {
                    getOpNotes(profileTypesResp.uuid ?: 0)
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
            profileType = AppConstants.PROFILE_TYPE_OP_NOTES,
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

    private fun getOpNotes(profileUuid: Int) {
        viewModel?.getOpNotes(facilityId!!, profileUuid, getOpNotesRestRetrofitCallback)
    }

    private fun getEncounter() {
        viewModel?.getEncounter(
            facilityId!!,
            patientUuid,
            departmentUuid!!,
            encounterType!!,
            getOpNotesEncounterByDocAndPatientIdRespCallback
        )
    }

    private fun saveAnswers() {
        val body = SaveOpNotesDetailsReq()
        body.addAll(saveOpNotesDetailsReqItemList)

        viewModel?.saveAnswers(facilityId!!, body, saveOpNotesDetailsRespCallback)
    }

    private fun getDefault() {
        viewModel?.getDefault(
            facilityId!!,
            AppConstants.PROFILE_TYPE_OP_NOTES,
            getOpNotesDefaultRespCallback
        )
    }

    private fun setDefault(profileTypeUuid: Int, profileUuid: Int) {
        val body = SetOpNotesDefaultReq(profileTypeUuid, profileUuid)
        viewModel?.setDefault(facilityId!!, body, setOpNotesDefaultRespCallback)
    }

    private fun getPreviousRecords() {
        viewModel?.getPreviousRecords(
            facilityId!!,
            patientUuid,
            1,
            getOpNotesPreviousRecordsRespCallback
        )
    }

    private fun getObservedValues(
        responseContent: com.oasys.digihealth.doctor.ui.emr_workflow.op_notes.model.prev_records.ResponseContent
    ) {
        viewModel?.getObservedValues(
            facilityId!!,
            patientUuid,
            getOpNotesObservedValuesRespCallback
        )
    }

    private fun addConsultation() {
        val body = OpNotesAddConsultationsReq(
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
            opNotesAddConsultationsRespCallback
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
        saveOpNotesDetailsReqItemList.forEach { item ->
            /*update ans*/
            if (item.profile_section_category_concept_value_uuid == profileSectionCategoryConceptValue.uuid) {
                val saveOpNotesDetailsReqItem =
                    saveOpNotesDetailsReqItemList[saveOpNotesDetailsReqItemList.indexOf(item)]

                item.term_key = answer

                saveOpNotesDetailsReqItemList.remove(item)
                if (!toRemove)
                    saveOpNotesDetailsReqItemList.add(saveOpNotesDetailsReqItem)

                return
            }
        }

        //sri some values are hardcoded based on api dev team suggestion
        val saveOpNotesDetailsReqItem = SaveOpNotesDetailsReqItem(
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
            profile_type_uuid = AppConstants.PROFILE_TYPE_OP_NOTES,
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

        saveOpNotesDetailsReqItemList.add(saveOpNotesDetailsReqItem)
    }

    private val getAllProfileTypesRespRetrofitCallback =
        object : RetrofitCallback<GetOpNotesAllProfileTypesResp> {
            override fun onSuccessfulResponse(responseBody: Response<GetOpNotesAllProfileTypesResp>?) {
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

            override fun onBadRequest(errorBody: Response<GetOpNotesAllProfileTypesResp>?) {
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

    private val getOpNotesRestRetrofitCallback =
        object : RetrofitCallback<GetOpNotesDetailResp> {
            override fun onSuccessfulResponse(responseBody: Response<GetOpNotesDetailResp>?) {
                if (responseBody?.isSuccessful == true) {
                    responseBody.body()?.let { getOpNotesResp ->
                        getOpNotesResp.responseContents?.forEach { responseContent: ResponseContentX? ->
                            headingList.clear()
                            responseContent?.profile_sections?.forEach { profileSection: ProfileSection? ->
                                headingList.add(profileSection!!)
                            }
                            setupHeadingRecyclerView()
                        }
                        if (getOpNotesResp.responseContents?.isNullOrEmpty()!!)
                            removeAllHeading()
                    }
                }
            }

            override fun onBadRequest(errorBody: Response<GetOpNotesDetailResp>?) {
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

    private val getOpNotesEncounterByDocAndPatientIdRespCallback =
        object : RetrofitCallback<GetOpNotesEncounterByDocAndPatientIdResp> {
            override fun onSuccessfulResponse(responseBody: Response<GetOpNotesEncounterByDocAndPatientIdResp>?) {
                responseBody?.body()?.let { getOpNotesEncounterByDocAndPatientIdResp ->
                    if (getOpNotesEncounterByDocAndPatientIdResp.responseContents?.isNotEmpty() == true) {
                        getOpNotesEncounterByDocAndPatientIdResp.responseContents?.get(0)
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

            override fun onBadRequest(errorBody: Response<GetOpNotesEncounterByDocAndPatientIdResp>?) {
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
                /*val gson = GsonBuilder().create()
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
                }*/
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

    private val saveOpNotesDetailsRespCallback =
        object : RetrofitCallback<SaveOpNotesDetailsResp> {
            override fun onSuccessfulResponse(responseBody: Response<SaveOpNotesDetailsResp>?) {
                if (responseBody?.isSuccessful == true) {
                    trackOpNotesSaveComplete(utils?.getEncounterType(), "success", "")
                    utils?.showToast(
                        R.color.positiveToast,
                        binding?.mainLayout!!,
                        getString(R.string.data_save)
                    )
                    saveOpNotesDetailsReqItemList.clear()
                    clearAllFields()
                }
            }

            override fun onBadRequest(errorBody: Response<SaveOpNotesDetailsResp>?) {
                trackOpNotesSaveComplete(
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
                trackOpNotesSaveComplete(
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
                trackOpNotesSaveComplete(
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
                trackOpNotesSaveComplete(
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
                if (s != null) {
                    trackOpNotesSaveComplete(utils?.getEncounterType(), "failure", s)
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

    private val getOpNotesDefaultRespCallback = object : RetrofitCallback<GetOpNotesDefaultResp> {
        override fun onSuccessfulResponse(responseBody: Response<GetOpNotesDefaultResp>?) {
            if (responseBody?.isSuccessful == true) {
                responseBody.body()?.let { getOpNotesDefaultResp ->
                    getOpNotesDefaultResp.responseContent?.let { responseContent ->
                        selectProfile(responseContent.profile_uuid)
                    }
                }
            }
        }

        override fun onBadRequest(errorBody: Response<GetOpNotesDefaultResp>?) {
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

    private val setOpNotesDefaultRespCallback = object : RetrofitCallback<SetOpNotesDefaultResp> {
        override fun onSuccessfulResponse(responseBody: Response<SetOpNotesDefaultResp>?) {
            if (responseBody?.isSuccessful == true) {
                utils?.showToast(
                    R.color.positiveToast,
                    binding?.mainLayout!!,
                    getString(R.string.data_save)
                )
            }
        }

        override fun onBadRequest(errorBody: Response<SetOpNotesDefaultResp>?) {
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

    private val getOpNotesPreviousRecordsRespCallback =
        object : RetrofitCallback<GetOpNotesPreviousRecordsResp> {
            override fun onSuccessfulResponse(responseBody: Response<GetOpNotesPreviousRecordsResp>?) {
                if (responseBody?.isSuccessful == true) {
                    responseBody.body()?.let { getOpNotesPreviousRecordsResp ->
                        getOpNotesPreviousRecordsResp.responseContents?.let { list ->
                            prevOpNotesList.clear()
                            prevOpNotesList.addAll(list)
                            binding?.rvPrevOpNotes?.adapter?.notifyDataSetChanged()
                        }
                    }
                }
            }

            override fun onBadRequest(errorBody: Response<GetOpNotesPreviousRecordsResp>?) {
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

    private val getOpNotesObservedValuesRespCallback =
        object : RetrofitCallback<GetOpNotesObservedValuesResp> {
            override fun onSuccessfulResponse(responseBody: Response<GetOpNotesObservedValuesResp>?) {
                if (responseBody?.isSuccessful == true) {
                    responseBody.body()?.let { getOpNotesObservedValuesResp ->
                        getOpNotesObservedValuesResp.responseContent?.let { list ->
                            observedValuesList.addAll(list)
                        }
                    }
                }
            }

            override fun onBadRequest(errorBody: Response<GetOpNotesObservedValuesResp>?) {
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

    private val opNotesAddConsultationsRespCallback =
        object : RetrofitCallback<OpNotesAddConsultationsResp> {
            override fun onSuccessfulResponse(responseBody: Response<OpNotesAddConsultationsResp>?) {
                if (responseBody?.body()?.code == 200) {
                    consultationUuid = responseBody.body()?.responseContents?.uuid
                }
            }

            override fun onBadRequest(errorBody: Response<OpNotesAddConsultationsResp>?) {
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

    private fun trackOpNotesVisit(type: String?) {
        AnalyticsManager.getAnalyticsManager().trackOpNotesVisit(type)
    }

    private fun trackOpNotesSaveStart(type: String?) {
        AnalyticsManager.getAnalyticsManager().trackOpNotesSaveStart(type)
    }

    private fun trackOpNotesSaveComplete(type: String?, status: String?, message: String?) {
        AnalyticsManager.getAnalyticsManager().trackOpNotesSaveComplete(type, status, message)
    }

    private fun trackOpNotesPreviousOpNotes(type: String?) {
        AnalyticsManager.getAnalyticsManager().trackOpNotesPreviousOpNotes(type)
    }

}
