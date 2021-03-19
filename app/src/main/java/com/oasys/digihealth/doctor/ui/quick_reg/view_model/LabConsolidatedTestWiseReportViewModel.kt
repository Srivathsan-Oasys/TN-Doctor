package com.oasys.digihealth.doctor.ui.quick_reg.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.application.HmisApplication
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.retrofitCallbacks.RetrofitCallback
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.block_dropdown.GetBlockDropdownReq
import com.oasys.digihealth.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.block_dropdown.GetBlockDropdownResp
import com.oasys.digihealth.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.department_dropdown.GetDepartmentDropdownReq
import com.oasys.digihealth.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.department_dropdown.GetDepartmentDropdownResp
import com.oasys.digihealth.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.gender_dropdown.GetGenderDropdownReq
import com.oasys.digihealth.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.gender_dropdown.GetGenderDropdownResp
import com.oasys.digihealth.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.health_office_dropdown.GetHealthOfficeDropdownReq
import com.oasys.digihealth.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.health_office_dropdown.GetHealthOfficeDropdownResp
import com.oasys.digihealth.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.hud_dropdown.GetHudDropdownReq
import com.oasys.digihealth.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.hud_dropdown.GetHudDropdownResp
import com.oasys.digihealth.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.institution_category.GetInstitutionCategoryReq
import com.oasys.digihealth.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.institution_category.GetInstitutionCategoryResp
import com.oasys.digihealth.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.institution_dropdown.GetInstitutionDropdownReq
import com.oasys.digihealth.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.institution_dropdown.GetInstitutionDropdownResp
import com.oasys.digihealth.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.institution_type_dropdown.GetInstitutionTypeDropdownReq
import com.oasys.digihealth.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.institution_type_dropdown.GetInstitutionTypeDropdownResp
import com.oasys.digihealth.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.lab_name_dropdown.GetLabNameDropdownReq
import com.oasys.digihealth.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.lab_name_dropdown.GetLabNameDropdownResp
import com.oasys.digihealth.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.order_status_dropdown.GetOrderStatusDropdownReq
import com.oasys.digihealth.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.order_status_dropdown.GetOrderStatusDropdownResp
import com.oasys.digihealth.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.server_time.GetServerTimeResp
import com.oasys.digihealth.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.test_name_dropdown.GetTestNameDropdownReq
import com.oasys.digihealth.doctor.ui.quick_reg.model.lab_consolidated_test_wise_report.test_name_dropdown.GetTestNameDropdownResp
import com.oasys.digihealth.doctor.utils.Utils

class LabConsolidatedTestWiseReportViewModel(
    application: Application
) : AndroidViewModel(
    application
) {

    var errorText = MutableLiveData<String>()
    var progress = MutableLiveData<Int>()
    var appPreferences: AppPreferences? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository =
        UserDetailsRoomRepository(application)

    init {
        userDetailsRoomRepository = UserDetailsRoomRepository(application)
        appPreferences = AppPreferences.getInstance(application, AppConstants.SHARE_PREFERENCE_NAME)
    }

    fun getServerTime(
        facility_uuid: Int,
        getServerTimeRespCallback: RetrofitCallback<GetServerTimeResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getConsolidatedTestWiseServerTime(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid
        )?.enqueue(RetrofitMainCallback(getServerTimeRespCallback))
    }

    fun getInstitutionDropdown(
        facility_uuid: Int,
        getInstitutionDropdownReq: GetInstitutionDropdownReq,
        getInstitutionDropdownReqCallback: RetrofitCallback<GetInstitutionDropdownResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getConsolidatedTestWiseInstitutionDropdown(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            getInstitutionDropdownReq
        )?.enqueue(RetrofitMainCallback(getInstitutionDropdownReqCallback))
    }

    fun getHealthOfficeDropdown(
        facility_uuid: Int,
        getHealthOfficeDropdownReq: GetHealthOfficeDropdownReq,
        getHealthOfficeDropdownRespCallback: RetrofitCallback<GetHealthOfficeDropdownResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getConsolidatedTestWiseHealthOfficeDropdown(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            getHealthOfficeDropdownReq
        )?.enqueue(RetrofitMainCallback(getHealthOfficeDropdownRespCallback))
    }

    fun getInstitutionTypeDropdown(
        facility_uuid: Int,
        getInstitutionTypeDropdownReq: GetInstitutionTypeDropdownReq,
        getInstitutionTypeDropdownRespCallback: RetrofitCallback<GetInstitutionTypeDropdownResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getConsolidatedTestWiseInstitutionTypeDropdown(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            getInstitutionTypeDropdownReq
        )?.enqueue(RetrofitMainCallback(getInstitutionTypeDropdownRespCallback))
    }

    fun getBlockDropdown(
        facility_uuid: Int,
        getBlockDropdownReq: GetBlockDropdownReq,
        getBlockDropdownRespCallback: RetrofitCallback<GetBlockDropdownResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getConsolidatedTestWiseBlockDropdown(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            getBlockDropdownReq
        )?.enqueue(RetrofitMainCallback(getBlockDropdownRespCallback))
    }

    fun getHudDropdown(
        facility_uuid: Int,
        getHudDropdownReq: GetHudDropdownReq,
        getHudDropdownRespCallback: RetrofitCallback<GetHudDropdownResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getConsolidatedTestWiseHudDropdown(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            getHudDropdownReq
        )?.enqueue(RetrofitMainCallback(getHudDropdownRespCallback))
    }

    fun getInstitutionCategory(
        facility_uuid: Int,
        getInstitutionCategoryReq: GetInstitutionCategoryReq,
        getInstitutionCategoryRespCallback: RetrofitCallback<GetInstitutionCategoryResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getConsolidatedTestWiseInstitutionCategory(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            getInstitutionCategoryReq
        )?.enqueue(RetrofitMainCallback(getInstitutionCategoryRespCallback))
    }

    fun getDepartmentDropdown(
        facility_uuid: Int,
        getDepartmentDropdownReq: GetDepartmentDropdownReq,
        getDepartmentDropdownRespCallback: RetrofitCallback<GetDepartmentDropdownResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getConsolidatedTestWiseDepartmentDropdown(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            getDepartmentDropdownReq
        )?.enqueue(RetrofitMainCallback(getDepartmentDropdownRespCallback))
    }

    fun getTestNameDroopdown(
        facility_uuid: Int,
        getTestNameDropdownReq: GetTestNameDropdownReq,
        getTestNameDropdownRespCallback: RetrofitCallback<GetTestNameDropdownResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getConsolidatedTestWiseTestNameDropdown(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            getTestNameDropdownReq
        )?.enqueue(RetrofitMainCallback(getTestNameDropdownRespCallback))
    }

    fun getLabNameDroopdown(
        facility_uuid: Int,
        getLabNameDropdownReq: GetLabNameDropdownReq,
        getLabNameDropdownRespCallback: RetrofitCallback<GetLabNameDropdownResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getConsolidatedTestWiseLabNameDropdown(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            getLabNameDropdownReq
        )?.enqueue(RetrofitMainCallback(getLabNameDropdownRespCallback))
    }

    fun getOrderStatusDroopdown(
        facility_uuid: Int,
        getOrderStatusDropdownReq: GetOrderStatusDropdownReq,
        getOrderStatusDropdownRespCallback: RetrofitCallback<GetOrderStatusDropdownResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getConsolidatedTestWiseOrderStatusDropdown(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            getOrderStatusDropdownReq
        )?.enqueue(RetrofitMainCallback(getOrderStatusDropdownRespCallback))
    }

    fun getGenderDroopdown(
        facility_uuid: Int,
        getGenderDropdownReq: GetGenderDropdownReq,
        getGenderDropdownRespCallback: RetrofitCallback<GetGenderDropdownResp>
    ) {
        if (!Utils.isNetworkConnected(getApplication())) {
            errorText.value = getApplication<Application>().getString(R.string.no_internet)
            return
        }

        progress.value = 0
        val aiiceApplication = HmisApplication.get(getApplication())
        val apiService = aiiceApplication.getRetrofitService()
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        apiService?.getConsolidatedTestWiseGenderDropdown(
            "accept",
            AppConstants.BEARER_AUTH + userDataStoreBean?.access_token,
            userDataStoreBean?.uuid!!,
            facility_uuid,
            getGenderDropdownReq
        )?.enqueue(RetrofitMainCallback(getGenderDropdownRespCallback))
    }
}