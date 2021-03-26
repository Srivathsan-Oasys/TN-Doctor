package com.hmis_tn.doctor.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.hmis_tn.doctor.data.networking.api.req.ReqEConsult
import com.hmis_tn.doctor.data.networking.api.req.ReqGetPatientById
import com.hmis_tn.doctor.data.networking.api.req.ReqGetPatientDetail
import com.hmis_tn.doctor.data.networking.api.req.ReqUpdateRoomBody
import com.hmis_tn.doctor.data.networking.api.res.*
import com.hmis_tn.doctor.data.repository.VideoCallRepository
import kotlinx.coroutines.launch

class VideoCallViewModel(
    private val repository: VideoCallRepository
) : ViewModel() {
    val networkEvent = repository.networkEvents

    fun checkMultiDoctorLogin(
        authorization: String,
        user_uuid: Int,
        doctorId: String,
        success: (ResAppointmentCancel?) -> Unit
    ) {
        viewModelScope.launch {
            val res = repository.checkMultiDoctorLogin(authorization, user_uuid, doctorId)
            if (res != null) {
                if (res.statusCode == 200 || res.statusCode == 201)
                    success(res)
                else
                    success(res)
            } else
                success(null)
        }
    }

    fun postEConsult(
        tokenBearer: String,
        userUUID: Int,
        reqEConsult: ReqEConsult?,
        success: (ResPostEConsult?) -> Unit
    ) {
        viewModelScope.launch {
            val res = repository.postEConsult(tokenBearer, userUUID, reqEConsult)
            if (res != null) {
                if (res.statusCode == 200 || res.statusCode == 201)
                    success(res)
                else
                    success(res)
            } else
                success(null)
        }
    }

    fun updateEConsult(
        tokenBearer: String,
        userUUID: Int,
        reqEConsult: ReqEConsult?,
        success: (ResUpdateEConsult?) -> Unit
    ) {
        viewModelScope.launch {
            val res = repository.updateEConsult(tokenBearer, userUUID, reqEConsult)
            if (res != null) {
                if (res.statusCode == 200 || res.statusCode == 201)
                    success(res)
                else
                    success(res)
            } else
                success(null)
        }
    }

    fun getPatientById(
        tokenBearer: String,
        userUUID: Int,
        reqGetPatientById: ReqGetPatientById?,
        success: (ResGetPatientById?) -> Unit
    ) {
        viewModelScope.launch {
            val res = repository.getPatientById(tokenBearer, userUUID, reqGetPatientById)
            if (res != null) {
                if (res.statusCode == 200 || res.statusCode == 201)
                    success(res)
                else
                    success(res)
            } else
                success(null)
        }
    }

    fun getPatientDetail(
        tokenBearer: String,
        userUUID: Int,
        reqGetPatientDetail: ReqGetPatientDetail?,
        success: (ResGetPatientDetail?) -> Unit
    ) {
        viewModelScope.launch {
            val res = repository.getPatientDetail(tokenBearer, userUUID, reqGetPatientDetail)
            if (res != null) {
                if (res.statusCode == 200 || res.statusCode == 201)
                    success(res)
                else
                    success(res)
            } else
                success(null)
        }
    }

    fun updateEConsultRoomCount(
        tokenBearer: String,
        userUUID: Int,
        reqUpdateRoomBody: ReqUpdateRoomBody?
    ): LiveData<ResPatientUpdate> {
        return liveData {
            emitSource(
                repository.updateEConsultRoomCount(
                    tokenBearer,
                    userUUID,
                    reqUpdateRoomBody
                )
            )
        }
    }

    fun updateEConsultPatientIsConnectAPIAPI(
        tokenBearer: String,
        userUUID: Int,
        reqUpdateRoomBody: ReqUpdateRoomBody?
    ): LiveData<ResPatientUpdate> {
        return liveData {
            emitSource(
                repository.updateEConsultPatientIsConnectAPI(
                    tokenBearer,
                    userUUID,
                    reqUpdateRoomBody
                )
            )
        }
    }

}