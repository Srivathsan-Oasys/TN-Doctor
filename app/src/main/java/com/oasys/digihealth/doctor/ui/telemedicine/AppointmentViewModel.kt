package com.oasys.digihealth.doctor.ui.telemedicine

import androidx.lifecycle.*
import com.makkalnalam.data.networking.api.req.ReqUpdateBooking
import com.oasys.digihealth.doctor.data.networking.api.res.*
import com.oasys.digihealth.doctor.data.networking.api.req.*
import com.oasys.digihealth.doctor.data.repository.AppointmentRepository
import kotlinx.coroutines.launch

class AppointmentViewModel(private val repository: AppointmentRepository) : ViewModel() {
    val networkEvent = repository.networkEvents

    private var patientSelected = MutableLiveData<ResPatientListSearch>()

    fun getAppointmentBooked(
        tokenBearer: String,
        userUUID: Int,
        reqAppointmentSession: ReqAppointmentSession?
    ): LiveData<ResAppointmentBooked> {
        return liveData {
            emitSource(
                repository.getAppointmentBooked(
                    tokenBearer,
                    userUUID,
                    reqAppointmentSession
                )
            )
        }
    }

    fun getAppointmentSession(
        tokenBearer: String,
        userUUID: Int,
        facilityUUID: Int,
        reqAppointmentSession: ReqAppointmentSession?
    ): LiveData<ResAppointmentSession> {
        return liveData {
            emitSource(
                repository.getAppointmentSession(
                    tokenBearer,
                    userUUID,
                    facilityUUID,
                    reqAppointmentSession
                )
            )
        }
    }

    fun patientSearchAPI(
        tokenBearer: String,
        userUUID: Int,
        facility_uuid: Int,
        reqPatientSearch: ReqPatientSearch
    ): LiveData<ResPatientSearch> {
        return liveData {
            emitSource(
                repository.patientSearch(
                    tokenBearer,
                    userUUID,
                    facility_uuid,
                    reqPatientSearch
                )
            )
        }
    }

    fun getDoctorList(
        tokenBearer: String,
        userUUID: Int,
        reqDoctorListBody: ReqDoctorListBody
    ): LiveData<ResDoctorList> {
        return liveData {
            emitSource(repository.getDoctorListAPI(tokenBearer, userUUID, reqDoctorListBody))
        }
    }

    fun getAvailableSlotAPI(
        tokenBearer: String,
        userUUID: Int,
        reqAvailability: ReqAvailability?
    ): LiveData<ResDoctorSlot> {
        return liveData {
            emitSource(repository.getAvailableSlotAPI(tokenBearer, userUUID, reqAvailability))
        }
    }

    fun bookAppointment(
        tokenBearer: String,
        userUUID: Int,
        reqNewBooking: ReqNewBooking?
    ): LiveData<ResBookAppointment> {
        return liveData {
            emitSource(repository.bookAppointmentAPI(tokenBearer, userUUID, reqNewBooking))
        }
    }

    fun bookAppointmentNew(
        tokenBearer: String,
        userUUID: Int,
        reqUpdateBooking: ReqUpdateBooking?
    ): LiveData<ResBookAppointment> {
        return liveData {
            emitSource(repository.bookAppointmentNewAPI(tokenBearer, userUUID, reqUpdateBooking))
        }
    }

    fun updateSlot(
        tokenBearer: String,
        userUUID: Int,
        reqUpdateBooking: ReqUpdateBooking?,
        success: (ResUpdateSlot?) -> Unit
    ) {
        viewModelScope.launch {
            val res = repository.updateSlotAPI(tokenBearer, userUUID, reqUpdateBooking)
            if (res?.statusCode == 200 || res?.statusCode == 201)
                success(res)
            else
                success(res)
        }
    }

    fun getInstituteList(
        tokenBearer: String,
        userUUID: Int,
        facilityUUID: Int,
        success: (ResInstitute?) -> Unit
    ) {
        viewModelScope.launch {
            val res = repository.getInstituteList(tokenBearer, userUUID, facilityUUID)
            if (res != null) {
                if (res.statusCode == 200 || res.statusCode == 201)
                    success(res)
                else
                    success(res)
            } else
                success(null)
        }
    }

    fun getUserByUserTypeIds(
        tokenBearer: String,
        userUUID: Int,
        facilityUUID: Int,
        reqTypeIdBody: ReqTypeIdBody?
    ): LiveData<ResUserTypeIds> {
        return liveData {
            emitSource(
                repository.getUserByUserTypeIds(
                    tokenBearer,
                    userUUID,
                    facilityUUID,
                    reqTypeIdBody
                )
            )
        }
    }

    fun searchAPI(
        tokenBearer: String,
        userUUID: Int,
        reqDoctorBody: ReqDoctorBody
    ): LiveData<ResDoctor> {
        return liveData {
            emitSource(repository.searchAPI(tokenBearer, userUUID, reqDoctorBody))
        }
    }

    fun departmentListAPI(
        tokenBearer: String,
        userUUID: Int,
        reqDepartmentBody: ReqDepartmentBody
    ): LiveData<ResDepartmentList> {
        return liveData {
            emitSource(repository.departmentListAPI(tokenBearer, userUUID, reqDepartmentBody))
        }
    }

    fun bookAppointmentFilter(
        authorization: String,
        user_uuid: Int,
        facility_uuid: Int,
        reqAppointmentSession: ReqAppointmentSession?
    ): LiveData<ResAppointmentBooked> {
        return liveData {
            emitSource(
                repository.bookAppointmentFilter(
                    authorization,
                    user_uuid,
                    facility_uuid,
                    reqAppointmentSession
                )
            )
        }
    }

    fun deleteAppointmentList(
        authorization: String,
        user_uuid: Int,
        reqDeleteSession: ReqDeleteSession?,
        success: (ResUpdateSession?) -> Unit
    ) {
        viewModelScope.launch {
            val res = repository.deleteAppointmentList(authorization, user_uuid, reqDeleteSession)
            if (res != null) {
                if (res.statusCode == 200 || res.statusCode == 201)
                    success(res)
                else
                    success(res)
            } else
                success(null)
        }
    }

    fun viewAppointmentSession(
        authorization: String,
        user_uuid: Int,
        reqViewSession: ReqViewSession?,
        success: (ResAppointmentSessionView?) -> Unit
    ) {
        viewModelScope.launch {
            val res = repository.viewAppointmentSession(authorization, user_uuid, reqViewSession)
            if (res != null) {
                if (res.statusCode == 200 || res.statusCode == 201)
                    success(res)
                else
                    success(res)
            } else
                success(null)
        }
    }

    fun cancelAppointmentList(
        authorization: String,
        user_uuid: Int,
        reqAppointmentCancel: ReqAppointmentCancel?,
        success: (ResAppointmentCancel?) -> Unit
    ) {
        viewModelScope.launch {
            val res =
                repository.cancelAppointmentBooked(authorization, user_uuid, reqAppointmentCancel)
            if (res != null) {
                if (res.statusCode == 200 || res.statusCode == 201)
                    success(res)
                else
                    success(res)
            } else
                success(null)
        }
    }


    fun checkInAppointment(
        authorization: String,
        user_uuid: Int,
        reqAppointmentCheckIn: ReqAppointmentCheckIn?,
        success: (ResAppointmentCheckIn?) -> Unit
    ) {
        viewModelScope.launch {
            val res =
                repository.checkInAppointmentBooked(authorization, user_uuid, reqAppointmentCheckIn)
            if (res != null) {
                if (res.statusCode == 200 || res.statusCode == 201)
                    success(res)
                else
                    success(res)
            } else
                success(null)
        }
    }

    fun selectedPatient(patientSelected: ResPatientListSearch) {
        this.patientSelected.postValue(patientSelected)
    }

    fun getSelectedPatient(): ResPatientListSearch? {
        return patientSelected.value
    }
}