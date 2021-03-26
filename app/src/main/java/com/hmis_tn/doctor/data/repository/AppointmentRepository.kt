package com.hmis_tn.doctor.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.makkalnalam.data.networking.api.req.ReqUpdateBooking
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.application.HmisApplication
import com.hmis_tn.doctor.data.event.NetworkEvent
import com.hmis_tn.doctor.data.networking.api.AppointmentAPI
import com.hmis_tn.doctor.data.networking.api.common.ApiException
import com.hmis_tn.doctor.data.networking.api.req.*
import com.hmis_tn.doctor.data.networking.api.res.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppointmentRepositoryImpl(private val api: AppointmentAPI) :
    AppointmentRepository {

    private val _networkEvents = MutableLiveData<NetworkEvent>()
    override val networkEvents: LiveData<NetworkEvent> = _networkEvents

    override suspend fun getAppointmentBooked(
        authorization: String,
        user_uuid: Int,
        reqAppointmentSession: ReqAppointmentSession?
    ): LiveData<ResAppointmentBooked> {
        return liveData(Dispatchers.IO) {
            if (HmisApplication.isConnected()) {
                try {
                    _networkEvents.postValue(NetworkEvent.Loading)
                    val response =
                        api.getAppointmentBooked(
                            authorization,
                            user_uuid,
                            1,
                            reqAppointmentSession
                        )
                    if (response.isSuccessful && response.code() == 200) {
                        withContext(Dispatchers.Main) {
                            _networkEvents.postValue(NetworkEvent.Success)
                        }
                        val data = response.body() ?: throw IllegalStateException("Body is null")
                        emit(data)
                    } else {
                        throw ApiException(response.code())
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        _networkEvents.postValue(
                            NetworkEvent.Failure(R.string.error_like_unlike_failed)
                        )
                    }
                } catch (e: ApiException) {
                    withContext(Dispatchers.Main) {
                        _networkEvents.postValue(NetworkEvent.Failure(R.string.error_like_unlike_failed))
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    _networkEvents.postValue(NetworkEvent.Failure(R.string.no_connection))
                }
            }
        }
    }

    override suspend fun getAppointmentSession(
        authorization: String,
        user_uuid: Int,
        facilityUUID: Int,
        reqAppointmentSession: ReqAppointmentSession?
    ): LiveData<ResAppointmentSession> {
        return liveData(Dispatchers.IO) {
            if (HmisApplication.isConnected()) {
                try {
                    _networkEvents.postValue(NetworkEvent.Loading)
                    val response =
                        api.getAppointmentSession(
                            authorization,
                            user_uuid,
                            facilityUUID,
                            reqAppointmentSession
                        )
                    if (response.isSuccessful && response.code() == 200) {
                        withContext(Dispatchers.Main) {
                            _networkEvents.postValue(NetworkEvent.Success)
                        }
                        val data = response.body() ?: throw IllegalStateException("Body is null")
                        emit(data)
                    } else {
                        throw ApiException(response.code())
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        _networkEvents.postValue(
                            NetworkEvent.Failure(R.string.error_like_unlike_failed)
                        )
                    }
                } catch (e: ApiException) {
                    withContext(Dispatchers.Main) {
                        _networkEvents.postValue(NetworkEvent.Failure(R.string.error_like_unlike_failed))
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    _networkEvents.postValue(NetworkEvent.Failure(R.string.no_connection))
                }
            }
        }
    }

    override suspend fun patientSearch(
        authorization: String,
        user_uuid: Int,
        facility_uuid: Int,
        reqPatientSearch: ReqPatientSearch
    ): LiveData<ResPatientSearch> {
        return liveData(Dispatchers.IO) {
            if (HmisApplication.isConnected()) {
                try {
                    _networkEvents.postValue(NetworkEvent.Loading)
                    val response = api.patientSearchAPI(
                        authorization,
                        user_uuid,
                        facility_uuid,
                        "en",
                        reqPatientSearch
                    )
                    if (response.isSuccessful && response.code() == 200) {
                        withContext(Dispatchers.Main) {
                            _networkEvents.postValue(NetworkEvent.Success)
                        }
                        val data = response.body() ?: throw IllegalStateException("Body is null")
                        emit(data)
                    } else {
                        throw ApiException(response.code())
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        _networkEvents.postValue(
                            NetworkEvent.Failure(R.string.error_like_unlike_failed)
                        )
                    }
                } catch (e: ApiException) {
                    withContext(Dispatchers.Main) {
                        _networkEvents.postValue(NetworkEvent.Failure(R.string.error_like_unlike_failed))
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    _networkEvents.postValue(NetworkEvent.Failure(R.string.no_connection))
                }
            }
        }
    }

    override suspend fun getDoctorListAPI(
        authorization: String,
        user_uuid: Int,
        reqDoctorListBody: ReqDoctorListBody
    ): LiveData<ResDoctorList> {
        return liveData(Dispatchers.IO) {
            if (HmisApplication.isConnected()) {
                try {
                    _networkEvents.postValue(NetworkEvent.Loading)
                    val response = api.getDoctorListAPI(authorization, user_uuid, reqDoctorListBody)
                    if (response.isSuccessful && response.code() == 200) {
                        withContext(Dispatchers.Main) {
                            _networkEvents.postValue(NetworkEvent.Success)
                        }
                        val data = response.body() ?: throw IllegalStateException("Body is null")
                        emit(data)
                    } else {
                        throw ApiException(response.code())
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        _networkEvents.postValue(
                            NetworkEvent.Failure(R.string.error_like_unlike_failed)
                        )
                    }
                } catch (e: ApiException) {
                    withContext(Dispatchers.Main) {
                        _networkEvents.postValue(NetworkEvent.Failure(R.string.error_like_unlike_failed))
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    _networkEvents.postValue(NetworkEvent.Failure(R.string.no_connection))
                }
            }
        }
    }

    override suspend fun getAvailableSlotAPI(
        authorization: String,
        user_uuid: Int,
        reqAvailability: ReqAvailability?
    ): LiveData<ResDoctorSlot> {
        return liveData(Dispatchers.IO) {
            if (HmisApplication.isConnected()) {
                try {
                    _networkEvents.postValue(NetworkEvent.Loading)
                    val response =
                        api.getAvailability(
                            authorization,
                            user_uuid,
                            reqAvailability
                        )
                    if (response.isSuccessful && response.code() == 200) {
                        withContext(Dispatchers.Main) {
                            _networkEvents.postValue(NetworkEvent.Success)
                        }
                        val data = response.body() ?: throw IllegalStateException("Body is null")
                        emit(data)
                    } else {
                        throw ApiException(response.code())
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        _networkEvents.postValue(
                            NetworkEvent.Failure(R.string.error_like_unlike_failed)
                        )
                    }
                } catch (e: ApiException) {
                    withContext(Dispatchers.Main) {
                        _networkEvents.postValue(NetworkEvent.Failure(R.string.error_like_unlike_failed))
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    _networkEvents.postValue(NetworkEvent.Failure(R.string.no_connection))
                }
            }
        }
    }

    override suspend fun bookAppointmentAPI(
        authorization: String,
        user_uuid: Int,
        reqNewBooking: ReqNewBooking?
    ): LiveData<ResBookAppointment> {
        return liveData(Dispatchers.IO) {
            try {
                _networkEvents.postValue(NetworkEvent.Loading)
                val response =
                    api.bookAppointment(authorization, user_uuid, reqNewBooking)
                if (response.isSuccessful && response.code() == 200) {
                    withContext(Dispatchers.Main) {
                        _networkEvents.postValue(NetworkEvent.Success)
                    }
                    val data = response.body() ?: throw IllegalStateException("Body is null")
                    emit(data)
                } else {
                    throw ApiException(response.code())
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _networkEvents.postValue(
                        NetworkEvent.Failure(R.string.error_like_unlike_failed)
                    )
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    _networkEvents.postValue(NetworkEvent.Failure(R.string.error_like_unlike_failed))
                }
            }
        }
    }

    override suspend fun bookAppointmentNewAPI(
        authorization: String,
        user_uuid: Int,
        reqUpdateBooking: ReqUpdateBooking?
    ): LiveData<ResBookAppointment> {
        return liveData(Dispatchers.IO) {
            try {
                _networkEvents.postValue(NetworkEvent.Loading)
                val response =
                    api.bookAppointmentNew(authorization, user_uuid, reqUpdateBooking)
                if (response.isSuccessful && response.code() == 200) {
                    withContext(Dispatchers.Main) {
                        _networkEvents.postValue(NetworkEvent.Success)
                    }
                    val data = response.body() ?: throw IllegalStateException("Body is null")
                    emit(data)
                } else {
                    throw ApiException(response.code())
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _networkEvents.postValue(
                        NetworkEvent.Failure(R.string.error_like_unlike_failed)
                    )
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    _networkEvents.postValue(NetworkEvent.Failure(R.string.error_like_unlike_failed))
                }
            }
        }
    }

    override suspend fun updateSlotAPI(
        authorization: String,
        user_uuid: Int,
        reqUpdateBooking: ReqUpdateBooking?
    ): ResUpdateSlot? {
        _networkEvents.postValue(NetworkEvent.Loading)
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getUpdateSlot(authorization, user_uuid, reqUpdateBooking)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) { _networkEvents.postValue(NetworkEvent.Success) }
                    return@withContext response.body()
                        ?: throw IllegalStateException("Body is null")
                } else {
                    throw ApiException(response.code())
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    if (e.httpCode == 401)
                        _networkEvents.postValue(NetworkEvent.Failure(R.string.error_invalid_credential))
                    else
                        _networkEvents.postValue(NetworkEvent.Failure(R.string.error_like_unlike_failed))
                }
                null
            }
        }
    }

    override suspend fun getInstituteList(
        authorization: String,
        user_uuid: Int,
        facilityUUID: Int
    ): ResInstitute? {
        _networkEvents.postValue(NetworkEvent.Loading)
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getInstituteList(authorization, user_uuid, facilityUUID)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) { _networkEvents.postValue(NetworkEvent.Success) }
                    return@withContext response.body()
                        ?: throw IllegalStateException("Body is null")
                } else {
                    throw ApiException(response.code())
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    if (e.httpCode == 401)
                        _networkEvents.postValue(NetworkEvent.Failure(R.string.error_invalid_credential))
                    else
                        _networkEvents.postValue(NetworkEvent.Failure(R.string.error_like_unlike_failed))
                }
                null
            }
        }
    }

    override suspend fun searchAPI(
        authorization: String,
        user_uuid: Int,
        reqDoctorBody: ReqDoctorBody
    ): LiveData<ResDoctor> {
        return liveData(Dispatchers.IO) {
            if (HmisApplication.isConnected()) {
                try {
                    _networkEvents.postValue(NetworkEvent.Loading)
                    val response = api.searchAPI(authorization, user_uuid, reqDoctorBody)
                    if (response.isSuccessful && response.code() == 200) {
                        withContext(Dispatchers.Main) {
                            _networkEvents.postValue(NetworkEvent.Success)
                        }
                        val data = response.body() ?: throw IllegalStateException("Body is null")
                        emit(data)
                    } else {
                        throw ApiException(response.code())
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        _networkEvents.postValue(
                            NetworkEvent.Failure(R.string.error_like_unlike_failed)
                        )
                    }
                } catch (e: ApiException) {
                    withContext(Dispatchers.Main) {
                        _networkEvents.postValue(NetworkEvent.Failure(R.string.error_like_unlike_failed))
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    _networkEvents.postValue(NetworkEvent.Failure(R.string.no_connection))
                }
            }
        }
    }

    override suspend fun departmentListAPI(
        authorization: String,
        user_uuid: Int,
        reqDepartmentBody: ReqDepartmentBody?
    ): LiveData<ResDepartmentList> {
        return liveData(Dispatchers.IO) {
            if (HmisApplication.isConnected()) {
                try {
                    _networkEvents.postValue(NetworkEvent.Loading)
                    val response =
                        api.departmentListAPI(authorization, user_uuid, reqDepartmentBody)
                    if (response.isSuccessful && response.code() == 200) {
                        withContext(Dispatchers.Main) {
                            _networkEvents.postValue(NetworkEvent.Success)
                        }
                        val data = response.body() ?: throw IllegalStateException("Body is null")
                        emit(data)
                    } else {
                        throw ApiException(response.code())
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        _networkEvents.postValue(
                            NetworkEvent.Failure(R.string.error_like_unlike_failed)
                        )
                    }
                } catch (e: ApiException) {
                    withContext(Dispatchers.Main) {
                        _networkEvents.postValue(NetworkEvent.Failure(R.string.error_like_unlike_failed))
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    _networkEvents.postValue(NetworkEvent.Failure(R.string.no_connection))
                }
            }
        }
    }

    override suspend fun getUserByUserTypeIds(
        authorization: String,
        user_uuid: Int,
        facilityUUID: Int,
        reqTypeIdBody: ReqTypeIdBody?
    ): LiveData<ResUserTypeIds> {
        return liveData(Dispatchers.IO) {
            try {
                _networkEvents.postValue(NetworkEvent.Loading)
                val response =
                    api.getUserByUserTypeId(
                        authorization,
                        user_uuid,
                        facilityUUID,
                        "en",
                        reqTypeIdBody
                    )
                if (response.isSuccessful && response.code() == 200) {
                    withContext(Dispatchers.Main) {
                        _networkEvents.postValue(NetworkEvent.Success)
                    }
                    val data = response.body() ?: throw IllegalStateException("Body is null")
                    emit(data)
                } else {
                    throw ApiException(response.code())
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _networkEvents.postValue(
                        NetworkEvent.Failure(R.string.error_like_unlike_failed)
                    )
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    _networkEvents.postValue(NetworkEvent.Failure(R.string.error_like_unlike_failed))
                }
            }
        }
    }

    override suspend fun updateSession(
        authorization: String,
        user_uuid: Int,
        reqUpdateSession: ReqUpdateSession?
    ): ResUpdateSession? {
        _networkEvents.postValue(NetworkEvent.Loading)
        return withContext(Dispatchers.IO) {
            try {
                val response = api.updateSession(authorization, user_uuid, reqUpdateSession)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) { _networkEvents.postValue(NetworkEvent.Success) }
                    return@withContext response.body()
                        ?: throw IllegalStateException("Body is null")
                } else {
                    throw ApiException(response.code())
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    if (e.httpCode == 401)
                        _networkEvents.postValue(NetworkEvent.Failure(R.string.error_invalid_credential))
                    else
                        _networkEvents.postValue(NetworkEvent.Failure(R.string.error_like_unlike_failed))
                }
                null
            }
        }
    }

    override suspend fun deleteAppointmentList(
        authorization: String,
        user_uuid: Int,
        reqDeleteSession: ReqDeleteSession?
    ): ResUpdateSession? {
        _networkEvents.postValue(NetworkEvent.Loading)
        return withContext(Dispatchers.IO) {
            try {
                val response = api.deleteAppointmentList(authorization, user_uuid, reqDeleteSession)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) { _networkEvents.postValue(NetworkEvent.Success) }
                    return@withContext response.body()
                        ?: throw IllegalStateException("Body is null")
                } else {
                    throw ApiException(response.code())
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    if (e.httpCode == 401)
                        _networkEvents.postValue(NetworkEvent.Failure(R.string.error_invalid_credential))
                    else
                        _networkEvents.postValue(NetworkEvent.Failure(R.string.error_like_unlike_failed))
                }
                null
            }
        }
    }

    override suspend fun viewAppointmentSession(
        authorization: String,
        user_uuid: Int,
        reqViewSession: ReqViewSession?
    ): ResAppointmentSessionView? {
        _networkEvents.postValue(NetworkEvent.Loading)
        return withContext(Dispatchers.IO) {
            try {
                val response = api.viewAppointmentSession(authorization, user_uuid, reqViewSession)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) { _networkEvents.postValue(NetworkEvent.Success) }
                    return@withContext response.body()
                        ?: throw IllegalStateException("Body is null")
                } else {
                    throw ApiException(response.code())
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    if (e.httpCode == 401)
                        _networkEvents.postValue(NetworkEvent.Failure(R.string.error_invalid_credential))
                    else
                        _networkEvents.postValue(NetworkEvent.Failure(R.string.error_like_unlike_failed))
                }
                null
            }
        }
    }

    override suspend fun bookAppointmentFilter(
        authorization: String,
        user_uuid: Int,
        facility_uuid: Int,
        reqAppointmentSession: ReqAppointmentSession?
    ): LiveData<ResAppointmentBooked> {
        return liveData(Dispatchers.IO) {
            if (HmisApplication.isConnected()) {
                try {
                    _networkEvents.postValue(NetworkEvent.Loading)
                    val response = api.bookAppointmentFilter(
                        authorization,
                        user_uuid,
                        facility_uuid,
                        "en",
                        reqAppointmentSession
                    )
                    if (response.isSuccessful && response.code() == 200) {
                        withContext(Dispatchers.Main) {
                            _networkEvents.postValue(NetworkEvent.Success)
                        }
                        val data = response.body() ?: throw IllegalStateException("Body is null")
                        emit(data)
                    } else {
                        throw ApiException(response.code())
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        _networkEvents.postValue(
                            NetworkEvent.Failure(R.string.error_like_unlike_failed)
                        )
                    }
                } catch (e: ApiException) {
                    withContext(Dispatchers.Main) {
                        _networkEvents.postValue(NetworkEvent.Failure(R.string.error_like_unlike_failed))
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    _networkEvents.postValue(NetworkEvent.Failure(R.string.no_connection))
                }
            }
        }
    }


    override suspend fun cancelAppointmentBooked(
        authorization: String,
        user_uuid: Int,
        reqAppointmentCancel: ReqAppointmentCancel?
    ): ResAppointmentCancel? {
        _networkEvents.postValue(NetworkEvent.Loading)
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    api.cancelBookAppointment(authorization, user_uuid, reqAppointmentCancel)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) { _networkEvents.postValue(NetworkEvent.Success) }
                    return@withContext response.body()
                        ?: throw IllegalStateException("Body is null")
                } else {
                    throw ApiException(response.code())
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    if (e.httpCode == 401)
                        _networkEvents.postValue(NetworkEvent.Failure(R.string.error_invalid_credential))
                    else
                        _networkEvents.postValue(NetworkEvent.Failure(R.string.error_like_unlike_failed))
                }
                null
            }
        }
    }

    override suspend fun checkInAppointmentBooked(
        authorization: String,
        user_uuid: Int,
        reqAppointmentCheckIn: ReqAppointmentCheckIn?
    ): ResAppointmentCheckIn? {
        _networkEvents.postValue(NetworkEvent.Loading)
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    api.checkInBookAppointment(authorization, user_uuid, reqAppointmentCheckIn)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) { _networkEvents.postValue(NetworkEvent.Success) }
                    return@withContext response.body()
                        ?: throw IllegalStateException("Body is null")
                } else {
                    throw ApiException(response.code())
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    if (e.httpCode == 401)
                        _networkEvents.postValue(NetworkEvent.Failure(R.string.error_invalid_credential))
                    else
                        _networkEvents.postValue(NetworkEvent.Failure(R.string.error_like_unlike_failed))
                }
                null
            }
        }
    }
}

interface AppointmentRepository {

    val networkEvents: LiveData<NetworkEvent>

    suspend fun getAppointmentBooked(
        authorization: String,
        user_uuid: Int,
        reqAppointmentSession: ReqAppointmentSession?
    ): LiveData<ResAppointmentBooked>

    suspend fun getAppointmentSession(
        authorization: String,
        user_uuid: Int,
        facilityUUID: Int,
        reqAppointmentSession: ReqAppointmentSession?
    ): LiveData<ResAppointmentSession>

    suspend fun getDoctorListAPI(
        authorization: String,
        user_uuid: Int,
        reqDoctorListBody: ReqDoctorListBody
    ): LiveData<ResDoctorList>

    suspend fun patientSearch(
        authorization: String,
        user_uuid: Int,
        facility_uuid: Int,
        reqPatientSearch: ReqPatientSearch
    ): LiveData<ResPatientSearch>

    suspend fun getAvailableSlotAPI(
        authorization: String,
        user_uuid: Int,
        reqAvailability: ReqAvailability?
    ): LiveData<ResDoctorSlot>

    suspend fun bookAppointmentAPI(
        authorization: String,
        user_uuid: Int,
        reqNewBooking: ReqNewBooking?
    ): LiveData<ResBookAppointment>

    suspend fun bookAppointmentNewAPI(
        authorization: String,
        user_uuid: Int,
        reqUpdateBooking: ReqUpdateBooking?
    ): LiveData<ResBookAppointment>

    suspend fun updateSlotAPI(
        authorization: String,
        user_uuid: Int,
        reqUpdateBooking: ReqUpdateBooking?
    ): ResUpdateSlot?

    suspend fun getInstituteList(
        authorization: String,
        user_uuid: Int,
        facilityUUID: Int
    ): ResInstitute?

    suspend fun searchAPI(
        authorization: String,
        user_uuid: Int,
        reqDoctorBody: ReqDoctorBody
    ): LiveData<ResDoctor>

    suspend fun departmentListAPI(
        authorization: String,
        user_uuid: Int,
        reqDepartmentBody: ReqDepartmentBody?
    ): LiveData<ResDepartmentList>

    suspend fun getUserByUserTypeIds(
        authorization: String,
        user_uuid: Int,
        facilityUUID: Int,
        reqTypeIdBody: ReqTypeIdBody?
    ): LiveData<ResUserTypeIds>

    suspend fun updateSession(
        authorization: String,
        user_uuid: Int,
        reqUpdateSession: ReqUpdateSession?
    ): ResUpdateSession?

    suspend fun deleteAppointmentList(
        authorization: String,
        user_uuid: Int,
        reqDeleteSession: ReqDeleteSession?
    ): ResUpdateSession?

    suspend fun viewAppointmentSession(
        authorization: String,
        user_uuid: Int,
        reqViewSession: ReqViewSession?
    ): ResAppointmentSessionView?

    suspend fun bookAppointmentFilter(
        authorization: String,
        user_uuid: Int,
        facility_uuid: Int,
        reqAppointmentSession: ReqAppointmentSession?
    ): LiveData<ResAppointmentBooked>

    suspend fun cancelAppointmentBooked(
        authorization: String,
        user_uuid: Int,
        reqAppointmentCancel: ReqAppointmentCancel?
    ): ResAppointmentCancel?

    suspend fun checkInAppointmentBooked(
        authorization: String,
        user_uuid: Int,
        reqAppointmentCheckIn: ReqAppointmentCheckIn?
    ): ResAppointmentCheckIn?

    companion object {

        fun create(api: AppointmentAPI): AppointmentRepository {
            return AppointmentRepositoryImpl(api)
        }
    }
}