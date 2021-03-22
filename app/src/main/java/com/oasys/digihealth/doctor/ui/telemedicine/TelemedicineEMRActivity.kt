package com.oasys.digihealth.doctor.ui.telemedicine

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.callbacks.FragmentBackClick
import com.oasys.digihealth.doctor.component.extention.toast
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.data.event.NetworkEvent
import com.oasys.digihealth.doctor.databinding.ActivityTelemedicineEmrBinding
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.ui.AdmissionFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.ChiefComplaintsFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.LabFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.view.DiagnosisFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.history.ui.HistoryFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.investigation.ui.InvestigationFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.fetch_encounters_response.FetchEncounterResponseContent
import com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.PrescriptionFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.radiology.ui.RadiologyFragment
import com.oasys.digihealth.doctor.ui.emr_workflow.view_model.EmrViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.view_model.EmrViewModelFactory
import com.oasys.digihealth.doctor.ui.emr_workflow.vitals.ui.VitalsFragment
import com.oasys.digihealth.doctor.ui.telemedicine.pageradapter.EMRPagerAdapter
import com.oasys.digihealth.doctor.ui.viewmodel.AppointmentViewModel
import com.oasys.digihealth.doctor.utils.Utils
import retrofit2.Response

class TelemedicineEMRActivity : AppCompatActivity() {
    private var binding: ActivityTelemedicineEmrBinding? = null

    private var encounter_uuid: Int? = 0
    private var encounter_doctor_uuid: Int? = 0
    private var viewpageradapter: EMRPagerAdapter? = null
    private var tabsArrayList: List<ResponseContent?>? = null
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


    private val viewModelAppointment by viewModel<AppointmentViewModel>()
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null

    private var tokenBearer = ""
    private var userUUID: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_telemedicine_emr)
        viewModel = EmrViewModelFactory(application).create(EmrViewModel::class.java)
        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        appPreferences = AppPreferences.getInstance(this, AppConstants.SHARE_PREFERENCE_NAME)

        patientUuid = appPreferences?.getInt(AppConstants.PATIENT_UUID)!!

        encounterType = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)!!

        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!
        department_uuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)!!

        viewModel!!.errorText.observe(this, Observer { toastMessage ->
            utils!!.showToast(R.color.negativeToast, binding?.mainLayout!!, toastMessage)
        })

        binding?.contentLinearLayout?.visibility = View.INVISIBLE
        binding?.noDataFoundTextView?.visibility = View.INVISIBLE

        viewModel?.getEncounter(
            facility_id,
            patientUuid,
            encounterType,
            fetchEncounterRetrofitCallBack
        )
        patientUuid = 435

        viewModel?.getStoreMaster(facility_id, department_uuid, getStoreMasterRetrofitCallback)
        viewModel?.getPatientLatestRecord(
            facility_id,
            patientUuid,
            encounterType,
            getPatientLatestEncCallback
        )
        viewModel?.getPatientById(facility_id, patientUuid, encounterType, getPatientByIdCallback)
        /*binding?.ivConsultingView?.setOnClickListener {
            if (binding?.llConsultingView?.isVisible!!) {
                binding?.llConsultingView?.visibility = View.GONE
            } else {
                binding?.llConsultingView?.visibility = View.VISIBLE

            }
        }*/

        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        val userDataStoreBean = userDetailsRoomRepository?.getUserDetails()

        tokenBearer = AppConstants.BEARER_AUTH + userDataStoreBean?.access_token
        userUUID = userDataStoreBean?.uuid!!
        initToolBar()
        initUI()
        subscribeToNetworkEvents()
    }

    private fun initToolBar() {
        binding?.tbEmr?.run {
            this.setNavigationOnClickListener {
                finish()
            }
        }
    }

    private fun initUI() {

    }


    private fun subscribeToNetworkEvents() {
        viewModelAppointment.networkEvent.observe(this) {
            when (it) {
                NetworkEvent.Loading -> showLoading(true)
                NetworkEvent.Success -> showLoading(false)
                is NetworkEvent.ApiMessage -> {
                    showLoading(false)
                    it.getContentIfNotHandled()?.run {
                        toast(it.msg)
                    }
                }
                is NetworkEvent.Failure -> {
                    showLoading(false)
                    it.getContentIfNotHandled()?.run {
                        toast(it.res)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
    }

    private fun setupViewPager(tabsArrayList: List<ResponseContent?>) {
        viewpageradapter = EMRPagerAdapter(supportFragmentManager)
        for (i in tabsArrayList.indices) {
            if (tabsArrayList[i]?.activity_code == AppConstants.ACTIVITY_CODE_CHIEF_COMPLAINTS) {
                viewpageradapter!!.addFragment(ChiefComplaintsFragment(), "Chief Complaints")
            } else if (tabsArrayList[i]?.activity_code == AppConstants.ACTIVITY_CODE_LAB) {
                viewpageradapter!!.addFragment(LabFragment(), "Lab")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_RADIOLOGY) {
                viewpageradapter!!.addFragment(RadiologyFragment(), "Radiology")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_INVESTIGATION) {
                viewpageradapter!!.addFragment(InvestigationFragment(), "Investigation")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_DIAGNOSIS) {
                viewpageradapter!!.addFragment(DiagnosisFragment(), "Diagnosis")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_HISTORY) {
                viewpageradapter!!.addFragment(HistoryFragment(), "History")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_PRESCRIPTION) {

                val prescription = PrescriptionFragment()
                val bundle = Bundle()
                bundle.putInt(AppConstants.RESPONSETYPE, 0)
                prescription.setArguments(bundle)


                viewpageradapter!!.addFragment(prescription, "Prescription")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_VITALS) {
                viewpageradapter!!.addFragment(VitalsFragment(), "Vitals")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_LAB_RESULT) {
//                viewpageradapter!!.addFragment(LabResultFragment(), "Lab Result")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_RADIOLOGY_RESULT) {
//                viewpageradapter!!.addFragment(RadiologyResultFragment(), "Radiology Result")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_TREATMENT_KIT) {
//                viewpageradapter!!.addFragment(TreatmentKitFragment(), "Treatment Kit")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_OP_NOTES) {
//                viewpageradapter!!.addFragment(OpNotesFragment(), "Op Notes")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_DOCUMENT) {
//                viewpageradapter!!.addFragment(DocumentFragment(), "Documents")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_INVESTIGATION_RESULT) {
//                viewpageradapter!!.addFragment(InvestigationResultFragment(), "Investigation Result")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_ADMISSION) {
                val bundle = Bundle()
//                bundle.putString("flow",flow)
                bundle.putString("flow", AppConstants.OUT_PATIENT)
                val admissionFragment = AdmissionFragment()
                admissionFragment.arguments = bundle
                viewpageradapter!!.addFragment(admissionFragment, "Admission/Referral")
            } else if (tabsArrayList[i]!!.activity_route_url.equals("bloodrequest")) {                                    //Sri activity code is null
//                viewpageradapter!!.addFragment(BloodRequestFragment(), "Blood Request")
            } else if (tabsArrayList[i]!!.activity_route_url.equals("diet")) {
//                viewpageradapter!!.addFragment(DietFragment(),"Diet")
            } else if (tabsArrayList[i]!!.activity_route_url.equals("mrd")) {
//                viewpageradapter!!.addFragment(MRDFragment(), "MRD")
            } else if (tabsArrayList[i]!!.activity_route_url.equals("progress")) {
//                viewpageradapter!!.addFragment(ProgressNotesFragment(),"Progress Notes")
            } else if (tabsArrayList[i]!!.activity_route_url.equals("discharge")) {
//                viewpageradapter!!.addFragment(DischargeSummaryFragment(),"Discharge Summary")
            } else if (tabsArrayList[i]!!.activity_route_url.equals("casesheet")) {                                  //Sri activity code is null
//                viewpageradapter!!.addFragment(IpCaseSheetFragment(),"IP Case Sheet")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_OP_NOTES) {
//                viewpageradapter!!.addFragment(OpNotesFragment(),"Op Notes")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_OT_NOTES) {
//                viewpageradapter!!.addFragment(OtNotesFragment(),"Ot Notes")
            } else if (tabsArrayList[i]!!.activity_code == AppConstants.ACTIVITY_CODE_ANESTHESIA_NOTES) {
//                viewpageradapter!!.addFragment(AnesthesiaNotesFragment(),"Anesthesia Notes")
            } else if (tabsArrayList[i]!!.activity_route_url.equals("criticalcare")) {
//                viewpageradapter!!.addFragment(CriticalCareChartFragment(),"Critical Care Chart")
            } else if (tabsArrayList[i]!!.activity_route_url.equals("medication")) {

                val DischargeMedication = PrescriptionFragment()

                val bundle = Bundle()
                bundle.putInt(AppConstants.RESPONSETYPE, 1)
                DischargeMedication.setArguments(bundle)

//                viewpageradapter!!.addFragment(DischargeMedication,"Discharge medication")


            } else if (tabsArrayList[i]!!.activity_route_url.equals("sketch")) {
//                viewpageradapter!!.addFragment(SpecialitySketchChildFragment(),"Specality Sketch")
            } else if (tabsArrayList[i]!!.activity_route_url.equals("schedule")) {
//                viewpageradapter!!.addFragment(OtSechduleFragment(),"Ot Sechdule")
            } else {
//                viewpageradapter!!.addFragment(CertificateFragment(), "Certificate")
            }
        }
        binding?.viewPager?.adapter = viewpageradapter
        viewpageradapter?.notifyDataSetChanged()
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
                Log.e("tabsArrayList", "_______" + tabsArrayList?.size)
                for (i in tabsArrayList!!.indices) {
                    val layoutInflater: View? =
                        LayoutInflater.from(this@TelemedicineEMRActivity)
                            .inflate(R.layout.treatment_custom_tab_row, null, false)
                    val tabImageView = layoutInflater?.findViewById(R.id.tabImageView) as ImageView
                    val tabTextView = layoutInflater.findViewById(R.id.tabTextView) as TextView
                    tabTextView.text = tabsArrayList!![i]?.activity_name
                    binding?.tabLayout?.getTabAt(i)?.customView = layoutInflater
                }
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
    private val fetchEncounterRetrofitCallBack =
        object : RetrofitCallback<FectchEncounterResponseModel> {
            override fun onSuccessfulResponse(response: Response<FectchEncounterResponseModel>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    encounterResponseContent = response.body()?.responseContents!!
                    encounter_doctor_uuid =
                        encounterResponseContent.get(0)?.encounter_doctors?.get(0)?.uuid
                    encounter_uuid = encounterResponseContent.get(0)?.uuid
                    appPreferences?.saveInt(
                        AppConstants.ENCOUNTER_DOCTOR_UUID,
                        encounter_doctor_uuid!!
                    )
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_UUID, encounter_uuid!!)
                    callEmrWorkFlow()

                } else {
                    viewModel?.createEncounter(
                        patientUuid,
                        encounterType,
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

            encounter_doctor_uuid =
                response?.body()?.responseContents?.encounterDoctor?.uuid!!.toInt()
            encounter_uuid = response?.body()?.responseContents?.encounter?.uuid!!.toInt()
            patientUuid =
                response?.body()?.responseContents?.encounterDoctor?.patient_uuid!!.toInt()
            appPreferences?.saveInt(AppConstants.ENCOUNTER_DOCTOR_UUID, encounter_doctor_uuid!!)
            appPreferences?.saveInt(AppConstants.ENCOUNTER_UUID, encounter_uuid!!)
            appPreferences?.saveInt(AppConstants.PATIENT_UUID, patientUuid!!)
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

    private val getPatientByIdCallback = object : RetrofitCallback<PatientDetailResponse> {
        override fun onSuccessfulResponse(response: Response<PatientDetailResponse>) {
            val data = response.body()?.responseContent
            if (data != null) {
                /*if (data.salutation_details?.name != null) {
                    binding?.tvPatientName?.text =
                        " " + data.salutation_details?.name + data.first_name

                } else {
                    binding?.tvPatientName?.text = " " + data.first_name
                }*/

                binding?.tvPatientName?.text = " " + data.first_name
                binding?.tvAgeGender?.text =
                    " / " + data.age.toString() + " Year(s)" + " / " + data.gender_details?.name
            }else{
                binding?.tvPatientName?.text = "-"
                binding?.tvAgeGender?.text = ""
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

    private val getPatientLatestEncCallback =
        object : RetrofitCallback<PatientLatestRecordResponse> {
            override fun onSuccessfulResponse(response: Response<PatientLatestRecordResponse>) {
                val data = response.body()?.responseContents
//                if (data != null)
                /*binding?.tvConsultentView?.text =
                    data?.doctorFirstName + " / " + data?.departmentName + " / " + data?.createdDate*/
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

    private val getStoreMasterRetrofitCallback =
        object : RetrofitCallback<GetStoreMasterResponseModel> {
            override fun onSuccessfulResponse(response: Response<GetStoreMasterResponseModel>) {

                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    val listData = response.body()?.responseContents
                    getStoreMasterId = response.body()?.responseContents!!


                    for (i in getStoreMasterId.size - 1 downTo 0) {

                        if (getStoreMasterId[i]?.store_type_uuid == 2) {

                            store_master_uuid = getStoreMasterId[i]?.store_master_uuid!!.toInt()
                            appPreferences?.saveInt(
                                AppConstants.STOREMASTER_UUID,
                                store_master_uuid!!
                            )

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

    fun callEmrWorkFlow() {
        viewModel?.getEmrWorkFlow(emrWorkFlowRetrofitCallBack, 2)
        /*if (flow.equals(AppConstants.OUT_PATIENT)) {
            viewModel?.getEmrWorkFlow(emrWorkFlowRetrofitCallBack, 2)
        } else {
            viewModel?.getEmrWorkFlow(emrWorkFlowRetrofitCallBack, 3)
        }*/
    }
}