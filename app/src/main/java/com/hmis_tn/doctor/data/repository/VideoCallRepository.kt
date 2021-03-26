package com.hmis_tn.doctor.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.google.gson.JsonObject
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.application.HmisApplication
import com.hmis_tn.doctor.data.event.NetworkEvent
import com.hmis_tn.doctor.data.networking.api.VideoCallAPI
import com.hmis_tn.doctor.data.networking.api.common.ApiException
import com.hmis_tn.doctor.data.networking.api.req.ReqEConsult
import com.hmis_tn.doctor.data.networking.api.req.ReqGetPatientById
import com.hmis_tn.doctor.data.networking.api.req.ReqGetPatientDetail
import com.hmis_tn.doctor.data.networking.api.req.ReqUpdateRoomBody
import com.hmis_tn.doctor.data.networking.api.res.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private class VideoCallRepositoryImpl(val api: VideoCallAPI) : VideoCallRepository {
    private val _networkEvents = MutableLiveData<NetworkEvent>()
    override val networkEvents: LiveData<NetworkEvent> = _networkEvents

    override suspend fun checkMultiDoctorLogin(
        authorization: String,
        user_uuid: Int,
        doctorId: String
    ): ResAppointmentCancel? {
        _networkEvents.postValue(NetworkEvent.Loading)
        return withContext(Dispatchers.IO) {
            try {
                val doctorBody = JsonObject().apply {
                    this.addProperty("doctor_uuid", doctorId)
                }
                val response =
                    api.checkMultiDoctorLogin(authorization, user_uuid, doctorBody)
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

    override suspend fun postEConsult(
        authorization: String,
        user_uuid: Int,
        reqEConsult: ReqEConsult?
    ): ResPostEConsult? {
        _networkEvents.postValue(NetworkEvent.Loading)
        return withContext(Dispatchers.IO) {
            try {
                val response = api.postEConsult(authorization, user_uuid, reqEConsult)
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

    override suspend fun updateEConsult(
        authorization: String,
        user_uuid: Int,
        reqEConsult: ReqEConsult?
    ): ResUpdateEConsult? {
        _networkEvents.postValue(NetworkEvent.Loading)
        return withContext(Dispatchers.IO) {
            try {
                val response = api.updateEConsult(authorization, user_uuid, reqEConsult)
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

    override suspend fun getPatientById(
        authorization: String,
        user_uuid: Int,
        reqGetPatientById: ReqGetPatientById?
    ): ResGetPatientById? {
        _networkEvents.postValue(NetworkEvent.Loading)
        return withContext(Dispatchers.IO) {
            try {
                val response = api.patientByID(authorization, user_uuid, reqGetPatientById)
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

    override suspend fun getPatientDetail(
        authorization: String,
        user_uuid: Int,
        reqGetPatientDetail: ReqGetPatientDetail?
    ): ResGetPatientDetail? {
        _networkEvents.postValue(NetworkEvent.Loading)
        return withContext(Dispatchers.IO) {
            try {
                val response = api.patientDetail(authorization, user_uuid,"en", reqGetPatientDetail)
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

    override suspend fun updateEConsultRoomCount(
        authorization: String,
        user_uuid: Int,
        reqUpdateRoomBody: ReqUpdateRoomBody?
    ): LiveData<ResPatientUpdate> {
        return liveData(Dispatchers.IO) {
            if (HmisApplication.isConnected()) {
                try {
                    _networkEvents.postValue(NetworkEvent.Loading)
                    val response =
                        api.updateEConsultRoomCount(authorization, user_uuid, reqUpdateRoomBody)
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

    override suspend fun updateEConsultPatientIsConnectAPI(
        authorization: String,
        user_uuid: Int,
        reqUpdateRoomBody: ReqUpdateRoomBody?
    ): LiveData<ResPatientUpdate> {
        return liveData(Dispatchers.IO) {
            if (HmisApplication.isConnected()) {
                try {
                    _networkEvents.postValue(NetworkEvent.Loading)
                    val response =
                        api.updateEConsultPatientIsConnect(authorization, user_uuid, reqUpdateRoomBody)
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
}

interface VideoCallRepository {

    val networkEvents: LiveData<NetworkEvent>

    suspend fun checkMultiDoctorLogin(
        authorization: String,
        user_uuid: Int,
        doctorId : String
    ):ResAppointmentCancel?

    suspend fun postEConsult(
        authorization: String,
        user_uuid: Int,
        reqEConsult: ReqEConsult?
    ): ResPostEConsult?

    suspend fun updateEConsult(
        authorization: String,
        user_uuid: Int,
        reqEConsult: ReqEConsult?
    ): ResUpdateEConsult?

    suspend fun getPatientById(
        authorization: String,
        user_uuid: Int,
        reqGetPatientById: ReqGetPatientById?
    ): ResGetPatientById?

    suspend fun getPatientDetail(
        authorization: String,
        user_uuid: Int,
        reqGetPatientDetail: ReqGetPatientDetail?
    ):ResGetPatientDetail?

    suspend fun updateEConsultRoomCount(
        authorization: String,
        user_uuid: Int,
        reqUpdateRoomBody: ReqUpdateRoomBody?
    ): LiveData<ResPatientUpdate>

    suspend fun updateEConsultPatientIsConnectAPI(
        authorization: String,
        user_uuid: Int,
        reqUpdateRoomBody: ReqUpdateRoomBody?
    ): LiveData<ResPatientUpdate>

    companion object {
        fun create(api: VideoCallAPI): VideoCallRepository {
            return VideoCallRepositoryImpl(api)
        }
    }

}