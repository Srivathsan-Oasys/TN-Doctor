package com.hmis_tn.doctor.data.networking.api

import com.google.gson.JsonObject
import com.hmis_tn.doctor.BuildConfig.BASE_DOMAIN
import com.hmis_tn.doctor.data.networking.api.req.ReqEConsult
import com.hmis_tn.doctor.data.networking.api.req.ReqGetPatientById
import com.hmis_tn.doctor.data.networking.api.req.ReqGetPatientDetail
import com.hmis_tn.doctor.data.networking.api.req.ReqUpdateRoomBody
import com.hmis_tn.doctor.data.networking.api.res.*
import com.hmis_tn.doctor.data.utility.API.API_ECONSULT_UPDATE_PATIENT_CONNECT
import com.hmis_tn.doctor.data.utility.API.API_ECONSULT_UPDATE_ROOM_COUNT
import com.hmis_tn.doctor.data.utility.API.API_POST_E_CONSULT
import com.hmis_tn.doctor.data.utility.API.API_UPDATE_E_CONSULT
import com.hmis_tn.doctor.data.utility.API.APP_ECONSULTATION
import com.hmis_tn.doctor.data.utility.API.GET_PATIENT_BY_ID
import com.hmis_tn.doctor.data.utility.API.GET_PATIENT_DETAIL
import com.hmis_tn.doctor.data.utility.API.VIDEO_CALL_CHECK_MULTI_LOGIN
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface VideoCallAPI {

    @POST(BASE_DOMAIN + APP_ECONSULTATION + VIDEO_CALL_CHECK_MULTI_LOGIN)
    suspend fun checkMultiDoctorLogin(
        @Header("Authorization") authorization: String,
        @Header("user_uuid") user_uuid: Int,
        @Body body: JsonObject
    ): Response<ResAppointmentCancel>

    @POST(BASE_DOMAIN + APP_ECONSULTATION + API_POST_E_CONSULT)
    suspend fun postEConsult(
        @Header("Authorization") authorization: String,
        @Header("user_uuid") user_uuid: Int,
        @Body reqEConsult: ReqEConsult?
    ): Response<ResPostEConsult>

    @POST(BASE_DOMAIN + APP_ECONSULTATION + API_UPDATE_E_CONSULT)
    suspend fun updateEConsult(
        @Header("Authorization") authorization: String,
        @Header("user_uuid") user_uuid: Int,
        @Body reqEConsult: ReqEConsult?
    ): Response<ResUpdateEConsult>

    @POST(BASE_DOMAIN + APP_ECONSULTATION + GET_PATIENT_BY_ID)
    suspend fun patientByID(
        @Header("Authorization") authorization: String,
        @Header("user_uuid") user_uuid: Int,
        @Body reqGetPatientById: ReqGetPatientById?
    ): Response<ResGetPatientById>

    @POST(BASE_DOMAIN + GET_PATIENT_DETAIL)
    suspend fun patientDetail(
        @Header("Authorization") authorization: String,
        @Header("user_uuid") user_uuid: Int,
        @Header("Accept-Language") lanugage: String,
        @Body reqGetPatientDetail: ReqGetPatientDetail?
    ): Response<ResGetPatientDetail>

    @POST(BASE_DOMAIN + APP_ECONSULTATION + API_ECONSULT_UPDATE_ROOM_COUNT)
    suspend fun updateEConsultRoomCount(
        @Header("Authorization") authorization: String,
        @Header("user_uuid") user_uuid: Int,
        @Body reqUpdateRoomBody: ReqUpdateRoomBody?
    ): Response<ResPatientUpdate>

    @POST(BASE_DOMAIN + APP_ECONSULTATION + API_ECONSULT_UPDATE_PATIENT_CONNECT)
    suspend fun updateEConsultPatientIsConnect(
        @Header("Authorization") authorization: String,
        @Header("user_uuid") user_uuid: Int,
        @Body reqUpdateRoomBody: ReqUpdateRoomBody?
    ): retrofit2.Response<ResPatientUpdate>


}