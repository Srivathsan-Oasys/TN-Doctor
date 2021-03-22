package com.oasys.digihealth.doctor.ui.emr_workflow.blood_request.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.component.extention.isTablet
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.FragmentBloodRequestChildBinding
import com.oasys.digihealth.doctor.fire_base_analytics.AnalyticsManager
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.ui.emr_workflow.blood_request.model.*
import com.oasys.digihealth.doctor.ui.emr_workflow.blood_request.view_model.BloodRequestViewModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.create_encounter_response.CreateEncounterResponseModel
import com.oasys.digihealth.doctor.ui.emr_workflow.model.fetch_encounters_response.FectchEncounterResponseModel
import com.oasys.digihealth.doctor.utils.Utils
import com.oasys.digihealth.doctor.utils.custom_views.CustomProgressDialog
import retrofit2.Response

class BloodRequestChildFragment : Fragment() {

    private val TAG = BloodRequestChildFragment::class.java.simpleName

    private var viewModel: BloodRequestViewModel? = null
    private var binding: FragmentBloodRequestChildBinding? = null
    private var utils: Utils? = null
    private var customProgressDialog: CustomProgressDialog? = null

    private var appPreferences: AppPreferences? = null
    private var facilityId: Int? = null
    private var departmentUuid: Int? = null
    private var encounterType: Int? = null

    private val bloodGroupList = ArrayList<ResponseContent>()
    private val bloodGroupNameList = ArrayList<String>()
    private val purposeList = ArrayList<ResponseContentX>()
    private val purposeNameList = ArrayList<String>()

    private val bloodRequestList = ArrayList<ResponseContentXXX>()
    private var bloodRequestAdapter: BloodRequestAdapter? = null

    private val prevBloodRequestList = ArrayList<ResponseContentXX>()
    private var prevBloodRequestAdapter: PrevBloodRequestAdapter? = null

    private var previousBloodRequest: ResponseContentXX? = null

    private var patientUuid: Int = 0
    private var encounter_uuid: Int = 0
    private var doctor_en_uuid: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_blood_request_child,
            container,
            false
        )
        utils = Utils(requireActivity())
        customProgressDialog = CustomProgressDialog(activity)

        viewModel = ViewModelProvider(this)[BloodRequestViewModel::class.java]

        appPreferences =
            AppPreferences.getInstance(requireContext(), AppConstants.SHARE_PREFERENCE_NAME)
        facilityId = appPreferences?.getInt(AppConstants.FACILITY_UUID)
//        patientId = appPreferences?.getInt(AppConstants.PATIENT_UUID)
        patientUuid = appPreferences?.getInt(AppConstants.PATIENT_UUID) ?: 0
        departmentUuid = appPreferences?.getInt(AppConstants.DEPARTMENT_UUID)
        encounterType = appPreferences?.getInt(AppConstants.ENCOUNTER_TYPE)
//        encounterUuid = appPreferences?.getInt(AppConstants.ENCOUNTER_UUID)
//        encounterDoctorUuid = appPreferences?.getInt(AppConstants.ENCOUNTER_DOCTOR_UUID)

        trackBloodRequestVisit(utils?.getEncounterType())

        initViews()
        listeners()
        attachObservables()

        getAllBloodGroup()
        getAllPurpose()
        getBloodComponents()
        getPreviousBloodRequest()

        return binding?.root
    }

    private fun initViews() {
        binding?.recyclerViewBloodRequest?.layoutManager = if (isTablet(requireContext())) {
            GridLayoutManager(requireActivity(), 2)
        } else {
            LinearLayoutManager(requireActivity())
        }
        bloodRequestAdapter = BloodRequestAdapter(bloodRequestList)
        binding?.recyclerViewBloodRequest?.adapter = bloodRequestAdapter

        binding?.recyclerViewPrevBloodRequest?.layoutManager =
            LinearLayoutManager(requireActivity())
        prevBloodRequestAdapter = PrevBloodRequestAdapter(
            list = prevBloodRequestList,
            compressAllOthers = {
                previousBloodRequest = it
                prevBloodRequestList.forEach { responseContentXX: ResponseContentXX ->
                    responseContentXX.isExpanded = (responseContentXX == it)
                }
            })
        binding?.recyclerViewPrevBloodRequest?.adapter = prevBloodRequestAdapter
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun listeners() {
        binding?.favouriteDrawerCardView?.setOnClickListener {
            binding?.drawerLayout?.openDrawer(GravityCompat.END)
            getPreviousBloodRequest()
        }

        binding?.saveCardView?.setOnClickListener {
            if (validate()) {
                trackBloodRequestSaveStart(utils?.getEncounterType())
                getEncounter()
            } else {
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.enter_mandatory_fields)
                )
            }
        }

        binding?.clearCardView?.setOnClickListener {
            clearAllFields()
        }

        binding?.cvAddNew?.setOnClickListener {
            binding?.drawerLayout?.closeDrawer(GravityCompat.END)
            clearAllFields()
        }

        binding?.cvRepeat?.setOnClickListener {
            repeatPreviousRequest()
        }

        binding?.drawerLayout?.addDrawerListener(object : ActionBarDrawerToggle(
            activity,
            binding?.drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                trackBloodRequestPreviousBloodRequest(utils?.getEncounterType())
            }
        })
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

    private fun getEncounter() {
        viewModel?.getEncounter(
            facilityId!!,
            patientUuid,
            encounterType!!,
            fetchEncounterRetrofitCallBack
        )
    }

    private fun clearAllFields() {
        binding?.spinnerBloodGroup?.setSelection(0)
        binding?.etBloodHB?.text?.clear()
        binding?.spinnerPurpose?.setSelection(0)
        binding?.etPreviousBagNo?.text?.clear()
        binding?.switchPrevTransfusion?.isChecked = false
        binding?.switchTransfusionReaction?.isChecked = false
        binding?.switchPregnant?.isChecked = false
        clearRecyclerView()
    }

    private fun clearRecyclerView() {
        bloodRequestList.forEach {
            it.isChecked = false
            it.text = ""
        }
        bloodRequestAdapter?.notifyDataSetChanged()
    }

    private fun validate(): Boolean {
        return when {
            binding?.spinnerBloodGroup?.selectedItemPosition == 0 -> false
            binding?.etBloodHB?.text?.isEmpty() == true -> false
            binding?.spinnerPurpose?.selectedItemPosition == 0 -> false
            else -> true
        }
    }

    private fun repeatPreviousRequest() {
        previousBloodRequest?.let { prevBloodRequest ->
            var bloodGroupIndex = 0

            val adapterBloodGrp =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    bloodGroupNameList
                )
            adapterBloodGrp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding?.spinnerBloodGroup?.adapter = adapterBloodGrp
            for (i in bloodGroupList.indices) {
                if (bloodGroupList[i].uuid == prevBloodRequest.bloodGroup?.uuid) {
                    bloodGroupIndex = i
                    break
                }
            }

            val adapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    purposeNameList
                )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding?.spinnerPurpose?.adapter = adapter
            binding?.spinnerBloodGroup?.setSelection(bloodGroupIndex)
            binding?.etBloodHB?.setText(prevBloodRequest.bloodHb)
            var purposeIndex = 0
            for (i in purposeList.indices) {
                if (purposeList[i].uuid == prevBloodRequest.bloodRequestPurpose?.uuid) {
                    purposeIndex = i
                    break
                }
            }
            binding?.spinnerPurpose?.setSelection(purposeIndex)
            binding?.switchTransfusionReaction?.isChecked =
                prevBloodRequest.transfusionReaction == true
            binding?.switchPrevTransfusion?.isChecked =
                prevBloodRequest.isPreviousTransfusion == true
            binding?.switchPregnant?.isChecked = prevBloodRequest.isPregnant == true
            for (i in bloodRequestList.indices) {
                for (j in prevBloodRequest.bloodRequestDetails?.indices!!) {
                    if (bloodRequestList[i].code == prevBloodRequest.bloodRequestDetails[j].bloodComponent?.code) {
                        bloodRequestList[i].isChecked = true
                        bloodRequestList[i].text =
                            prevBloodRequest.bloodRequestDetails[j].quantityRequired?.toString()
                                ?: ""
                        break
                    } else {
                        bloodRequestList[i].isChecked = false
                        bloodRequestList[i].text = ""
                    }
                }
                if (prevBloodRequest.bloodRequestDetails.isNullOrEmpty()) {
                    bloodRequestList[i].isChecked = false
                    bloodRequestList[i].text = ""
                }
            }
            bloodRequestAdapter?.notifyDataSetChanged()
            binding?.recyclerViewBloodRequest?.adapter?.notifyDataSetChanged()
            bloodRequestAdapter = BloodRequestAdapter(bloodRequestList)
            binding?.recyclerViewBloodRequest?.adapter = bloodRequestAdapter
            binding?.drawerLayout?.closeDrawer(GravityCompat.END)
        }
    }

    private fun getAllBloodGroup() {
        val body = GetAllBloodGroupReq("blood_groups")
        viewModel?.getAllBloodGroup(facilityId!!, body, allBloodGroupRetrofitCallback)
    }

    private fun getAllPurpose() {
        val body = GetAllPurposeReq("name", "blood_request_purpose")
        viewModel?.getAllPurpose(facilityId!!, body, allPurposeRetrofitCallback)
    }

    private fun getBloodComponents() {
        val body = GetBloodComponentsReq("blood_components")
        viewModel?.getBloodComponent(facilityId!!, body, getBloodComponentsRespCallback)
    }

    private fun getPreviousBloodRequest() {
        val body = GetPreviousBloodReq(patientUuid.toString())
        viewModel?.getPreviousBloodRequest(
            facilityId!!,
            body,
            getPreviousBloodRespRetrofitCallback
        )
    }

    private fun bloodRequestSave(
        bloodGroupUuid: String,
        bloodHb: String,
        purposeUuid: String,
        prevBloodBagNo: String,
        previousTransfusion: Boolean,
        transfusionReaction: Boolean,
        pregnant: Boolean
    ) {
        val header = Header(
            bloodGroupUuid = bloodGroupUuid,
            bloodHb = bloodHb,
            patientUuid = patientUuid.toString(),
            doctorUuid = doctor_en_uuid.toString(),
            encounterTypeUuid = encounterType,
            encounterUuid = encounter_uuid,
            bloodRequestPurposeUuid = purposeUuid,
            isPregnant = pregnant,
            isPreviousTransfusion = previousTransfusion,
            transfusionReaction = transfusionReaction
        )
        val detailList = ArrayList<Detail>()
        bloodRequestList.forEach {
            if (it.isChecked) {
                val detail = Detail(
                    bloodComponentUuid = it.uuid,
                    quantityRequired = it.text
                )
                detailList.add(detail)
            }
        }
        val body = BloodRequestSaveReq(
            header = header,
            details = detailList
        )
        viewModel?.bloodRequestSave(
            facilityId!!,
            body,
            bloodRequestSaveRespRetrofitCallback
        )
    }

    private val allBloodGroupRetrofitCallback = object : RetrofitCallback<GetAllBloodGroupResp> {
        override fun onSuccessfulResponse(responseBody: Response<GetAllBloodGroupResp>?) {
            responseBody?.body()?.let { getAllBloodGroupResp ->
//                bloodGroupList.add(getString(R.string.blood_group))

                bloodGroupNameList.add("Select Blood Group")

                getAllBloodGroupResp.responseContents?.forEach { responseContent: ResponseContent ->
                    bloodGroupNameList.add(responseContent.name ?: "")
                    bloodGroupList.add(responseContent)
                }

                val adapter =
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        bloodGroupNameList
                    )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding?.spinnerBloodGroup?.adapter = adapter
            }
        }

        override fun onBadRequest(errorBody: Response<GetAllBloodGroupResp>?) {
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

    private val allPurposeRetrofitCallback = object : RetrofitCallback<GetAllPurposeResp> {
        override fun onSuccessfulResponse(responseBody: Response<GetAllPurposeResp>?) {
            responseBody?.body()?.let { getAllBloodGroupResp ->
                purposeNameList.add("Select Purpose")

                getAllBloodGroupResp.responseContents?.forEach { responseContent ->
                    purposeNameList.add(responseContent.name ?: "")
                    purposeList.add(responseContent)
                }

                val adapter =
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        purposeNameList
                    )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding?.spinnerPurpose?.adapter = adapter
            }
        }

        override fun onBadRequest(errorBody: Response<GetAllPurposeResp>?) {
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

    private val getBloodComponentsRespCallback = object : RetrofitCallback<GetBloodComponentsResp> {
        override fun onSuccessfulResponse(responseBody: Response<GetBloodComponentsResp>?) {
            responseBody?.body()?.let { getBloodComponentsResp ->
                bloodRequestList.clear()
                getBloodComponentsResp.responseContents?.let { bloodRequestList.addAll(it) }
                bloodRequestAdapter?.notifyDataSetChanged()
            }
        }

        override fun onBadRequest(errorBody: Response<GetBloodComponentsResp>?) {
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

    private val getPreviousBloodRespRetrofitCallback =
        object : RetrofitCallback<GetPreviousBloodResp> {
            override fun onSuccessfulResponse(responseBody: Response<GetPreviousBloodResp>?) {
                responseBody?.body()?.let { getPreviousBloodResp ->
                    if (getPreviousBloodResp.statusCode == 200) {
                        prevBloodRequestList.clear()
                        getPreviousBloodResp.responseContents?.let { list ->
                            prevBloodRequestList.addAll(list)
                            if (prevBloodRequestList.size > 0) {
                                prevBloodRequestList[0].isExpanded = true
                                previousBloodRequest = prevBloodRequestList[0]
                            }
                            prevBloodRequestAdapter?.notifyDataSetChanged()
                        }
                    }
                }
            }

            override fun onBadRequest(errorBody: Response<GetPreviousBloodResp>?) {
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

    private val bloodRequestSaveRespRetrofitCallback =
        object : RetrofitCallback<BloodRequestSaveResp> {
            override fun onSuccessfulResponse(responseBody: Response<BloodRequestSaveResp>?) {
                if (responseBody?.isSuccessful == true) {
                    trackBloodRequestSaveComplete(utils?.getEncounterType(), "success", "")
                    utils?.showToast(
                        R.color.positiveToast,
                        binding?.mainLayout!!,
                        "Blood Request Saved Successfully"
                    )
                    clearAllFields()
                }
            }

            override fun onBadRequest(errorBody: Response<BloodRequestSaveResp>?) {
                trackBloodRequestSaveComplete(
                    utils?.getEncounterType(),
                    "failure",
                    errorBody?.message()
                )
                utils?.showToast(
                    R.color.negativeToast,
                    binding?.mainLayout!!,
                    getString(R.string.something_went_wrong)
                )
            }

            override fun onServerError(response: Response<*>?) {
                trackBloodRequestSaveComplete(
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
                trackBloodRequestSaveComplete(
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
                trackBloodRequestSaveComplete(
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
                trackBloodRequestSaveComplete(utils?.getEncounterType(), "failure", s)
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

    private val fetchEncounterRetrofitCallBack =
        object : RetrofitCallback<FectchEncounterResponseModel> {
            override fun onSuccessfulResponse(response: Response<FectchEncounterResponseModel>) {
                if (response.body()?.responseContents?.isNotEmpty()!!) {
                    val encounterResponseContent = response.body()?.responseContents!!
                    doctor_en_uuid =
                        encounterResponseContent.get(0)?.encounter_doctors?.get(0)?.uuid!!
                    encounter_uuid = encounterResponseContent.get(0)?.uuid!!
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_DOCTOR_UUID, doctor_en_uuid)
                    appPreferences?.saveInt(AppConstants.ENCOUNTER_UUID, encounter_uuid)

                    var bloodGroupUuid = ""
                    bloodGroupList.forEach { responseContent: ResponseContent ->
                        if (binding?.spinnerBloodGroup?.selectedItem == responseContent.name) {
                            bloodGroupUuid = responseContent.uuid.toString()
                            return@forEach
                        }
                    }
                    var purposeUuid = ""
                    purposeList.forEach { responseContentX: ResponseContentX ->
                        if (binding?.spinnerPurpose?.selectedItem == responseContentX.name) {
                            purposeUuid = responseContentX.uuid.toString()
                            return@forEach
                        }
                    }
                    bloodRequestSave(
                        bloodGroupUuid,
                        binding?.etBloodHB?.text?.toString() ?: "",
                        purposeUuid,
                        binding?.etPreviousBagNo?.text?.toString() ?: "",
                        binding?.switchPrevTransfusion?.isChecked ?: false,
                        binding?.switchTransfusionReaction?.isChecked ?: false,
                        binding?.switchPregnant?.isChecked ?: false
                    )

                } else {
                    viewModel?.createEncounter(
                        patientUuid,
                        encounterType!!,
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

    val createEncounterRetrofitCallback = object : RetrofitCallback<CreateEncounterResponseModel> {
        override fun onSuccessfulResponse(response: Response<CreateEncounterResponseModel>) {


            doctor_en_uuid = response.body()?.responseContents?.encounterDoctor?.uuid!!.toInt()
            encounter_uuid = response.body()?.responseContents?.encounter?.uuid!!.toInt()
            patientUuid = response.body()?.responseContents?.encounterDoctor?.patient_uuid!!.toInt()

            var bloodGroupUuid = ""
            bloodGroupList.forEach { responseContent: ResponseContent ->
                if (binding?.spinnerBloodGroup?.selectedItem == responseContent.name) {
                    bloodGroupUuid = responseContent.uuid.toString()
                    return@forEach
                }
            }
            var purposeUuid = ""
            purposeList.forEach { responseContentX: ResponseContentX ->
                if (binding?.spinnerPurpose?.selectedItem == responseContentX.name) {
                    purposeUuid = responseContentX.uuid.toString()
                    return@forEach
                }
            }
            bloodRequestSave(
                bloodGroupUuid,
                binding?.etBloodHB?.text?.toString() ?: "",
                purposeUuid,
                binding?.etPreviousBagNo?.text?.toString() ?: "",
                binding?.switchPrevTransfusion?.isChecked ?: false,
                binding?.switchTransfusionReaction?.isChecked ?: false,
                binding?.switchPregnant?.isChecked ?: false
            )
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

    private fun trackBloodRequestVisit(type: String?) {
        AnalyticsManager.getAnalyticsManager().trackBloodRequestVisit(type)
    }

    private fun trackBloodRequestSaveStart(type: String?) {
        AnalyticsManager.getAnalyticsManager().trackBloodRequestSaveStart(type)
    }

    private fun trackBloodRequestSaveComplete(type: String?, status: String?, message: String?) {
        AnalyticsManager.getAnalyticsManager().trackBloodRequestSaveComplete(type, status, message)
    }

    private fun trackBloodRequestPreviousBloodRequest(type: String?) {
        AnalyticsManager.getAnalyticsManager().trackBloodRequestPreviousBloodRequest(type)
    }

}
