package com.hmis_tn.doctor.data.networking.api

import com.makkalnalam.data.networking.api.req.ReqUpdateBooking
import com.hmis_tn.doctor.BuildConfig.BASE_DOMAIN
import com.hmis_tn.doctor.data.networking.api.req.*
import com.hmis_tn.doctor.data.networking.api.res.*
import com.hmis_tn.doctor.data.utility.API.API_APPOINTMENT_BOOKED
import com.hmis_tn.doctor.data.utility.API.API_APPOINTMENT_BOOKED_CANCEL
import com.hmis_tn.doctor.data.utility.API.API_APPOINTMENT_BOOKED_CHECK_IN
import com.hmis_tn.doctor.data.utility.API.API_APPOINTMENT_INSTITUTE_LIST
import com.hmis_tn.doctor.data.utility.API.API_APPOINTMENT_SESSION_GET
import com.hmis_tn.doctor.data.utility.API.API_APPOINTMENT_SESSION_UPDATE
import com.hmis_tn.doctor.data.utility.API.API_APPOINTMENT_USER_TYPE_IDS
import com.hmis_tn.doctor.data.utility.API.API_AVAILABILITY_SLOT
import com.hmis_tn.doctor.data.utility.API.API_BOOK_APPOINTMENT
import com.hmis_tn.doctor.data.utility.API.API_BOOK_APPOINTMENT_FILTER
import com.hmis_tn.doctor.data.utility.API.API_BOOK_SLOT_APPOINTMENT
import com.hmis_tn.doctor.data.utility.API.API_DELETE_APPOINTMENT
import com.hmis_tn.doctor.data.utility.API.API_DEPARTMENT_LIST
import com.hmis_tn.doctor.data.utility.API.API_DOCTOR_LIST
import com.hmis_tn.doctor.data.utility.API.API_PATIENT_SEARCH
import com.hmis_tn.doctor.data.utility.API.API_SEARCH
import com.hmis_tn.doctor.data.utility.API.API_UPDATE_SLOT
import com.hmis_tn.doctor.data.utility.API.API_VIEW_APPOINTMENT
import com.hmis_tn.doctor.data.utility.API.APP_MASTER
import com.hmis_tn.doctor.data.utility.API.APP_REGISTRATION
import com.hmis_tn.doctor.data.utility.API.APP_TELEMEDICINE
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface AppointmentAPI {

    @POST(BASE_DOMAIN + APP_TELEMEDICINE + API_APPOINTMENT_BOOKED)
    suspend fun getAppointmentBooked(
        @Header("Authorization") authorization: String,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body reqAppointmentSession: ReqAppointmentSession?
    ): retrofit2.Response<ResAppointmentBooked>

    @POST(BASE_DOMAIN + APP_TELEMEDICINE + API_APPOINTMENT_SESSION_GET)
    suspend fun getAppointmentSession(
        @Header("Authorization") authorization: String,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Body reqAppointmentSession: ReqAppointmentSession?
    ): retrofit2.Response<ResAppointmentSession>

    @POST(BASE_DOMAIN + APP_REGISTRATION + API_PATIENT_SEARCH)
    suspend fun patientSearchAPI(
        @Header("Authorization") authorization: String,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("Accept-Language") acceptLanguage: String,
        @Body reqPatientSearch: ReqPatientSearch?
    ): retrofit2.Response<ResPatientSearch>

    @POST(BASE_DOMAIN + APP_TELEMEDICINE + API_DOCTOR_LIST)
    suspend fun getDoctorListAPI(
        @Header("Authorization") authorization: String,
        @Header("user_uuid") user_uuid: Int,
        @Body reqDoctorListBody: ReqDoctorListBody?
    ): retrofit2.Response<ResDoctorList>

    @POST(BASE_DOMAIN + APP_TELEMEDICINE + API_AVAILABILITY_SLOT)
    suspend fun getAvailability(
        @Header("Authorization") authorization: String,
        @Header("user_uuid") user_uuid: Int,
        @Body reqAvailability: ReqAvailability?
    ): retrofit2.Response<ResDoctorSlot>

    @POST(BASE_DOMAIN + APP_TELEMEDICINE + API_BOOK_APPOINTMENT)
    suspend fun bookAppointment(
        @Header("Authorization") authorization: String,
        @Header("user_uuid") user_uuid: Int,
        @Body reqNewBooking: ReqNewBooking?
    ): retrofit2.Response<ResBookAppointment>

    @POST(BASE_DOMAIN + APP_TELEMEDICINE + API_BOOK_SLOT_APPOINTMENT)
    suspend fun bookAppointmentNew(
        @Header("Authorization") authorization: String,
        @Header("user_uuid") user_uuid: Int,
        @Body reqUpdateBooking: ReqUpdateBooking?
    ): retrofit2.Response<ResBookAppointment>

    @POST(BASE_DOMAIN + APP_TELEMEDICINE + API_UPDATE_SLOT)
    suspend fun getUpdateSlot(
        @Header("Authorization") authorization: String,
        @Header("user_uuid") user_uuid: Int,
        @Body reqUpdateBooking: ReqUpdateBooking?
    ): retrofit2.Response<ResUpdateSlot>

    @POST(BASE_DOMAIN + APP_MASTER + API_APPOINTMENT_INSTITUTE_LIST)
    suspend fun getInstituteList(
        @Header("Authorization") authorization: String,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int
    ): retrofit2.Response<ResInstitute>

    @POST(BASE_DOMAIN + APP_MASTER + API_SEARCH)
    suspend fun searchAPI(
        @Header("Authorization") authorization: String,
        @Header("user_uuid") user_uuid: Int,
        @Body reqDoctorBody: ReqDoctorBody?
    ): retrofit2.Response<ResDoctor>

    @POST(BASE_DOMAIN + APP_MASTER + API_DEPARTMENT_LIST)
    suspend fun departmentListAPI(
        @Header("Authorization") authorization: String,
        @Header("user_uuid") user_uuid: Int,
        @Body reqDepartmentBody: ReqDepartmentBody?
    ): retrofit2.Response<ResDepartmentList>

    @POST(BASE_DOMAIN + APP_MASTER + API_APPOINTMENT_USER_TYPE_IDS)
    suspend fun getUserByUserTypeId(
        @Header("Authorization") authorization: String,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("Accept-Language") acceptLanguage: String,
        @Body reqTypeIdBody: ReqTypeIdBody?
    ): retrofit2.Response<ResUserTypeIds>

    @POST(BASE_DOMAIN + APP_TELEMEDICINE + API_APPOINTMENT_SESSION_UPDATE)
    suspend fun updateSession(
        @Header("Authorization") authorization: String,
        @Header("user_uuid") user_uuid: Int,
        @Body reqUpdateSession: ReqUpdateSession?
    ): retrofit2.Response<ResUpdateSession>


    @POST(BASE_DOMAIN + APP_TELEMEDICINE + API_BOOK_APPOINTMENT_FILTER)
    suspend fun bookAppointmentFilter(
        @Header("Authorization") authorization: String,
        @Header("user_uuid") user_uuid: Int,
        @Header("facility_uuid") facility_uuid: Int,
        @Header("Accept-Language") acceptLanguage: String,
        @Body reqAppointmentSession: ReqAppointmentSession?
    ): retrofit2.Response<ResAppointmentBooked>

    @POST(BASE_DOMAIN + APP_TELEMEDICINE + API_DELETE_APPOINTMENT)
    suspend fun deleteAppointmentList(
        @Header("Authorization") authorization: String,
        @Header("user_uuid") user_uuid: Int,
        @Body reqDeleteSession: ReqDeleteSession?
    ): retrofit2.Response<ResUpdateSession>

    @POST(BASE_DOMAIN + APP_TELEMEDICINE + API_VIEW_APPOINTMENT)
    suspend fun viewAppointmentSession(
        @Header("Authorization") authorization: String,
        @Header("user_uuid") user_uuid: Int,
        @Body reqViewSession: ReqViewSession?
    ): retrofit2.Response<ResAppointmentSessionView>

    @POST(BASE_DOMAIN + APP_TELEMEDICINE + API_APPOINTMENT_BOOKED_CANCEL)
    suspend fun cancelBookAppointment(
        @Header("Authorization") authorization: String,
        @Header("user_uuid") user_uuid: Int,
        @Body reqAppointmentCancel: ReqAppointmentCancel?
    ):retrofit2.Response<ResAppointmentCancel>

    @POST(BASE_DOMAIN + APP_TELEMEDICINE + API_APPOINTMENT_BOOKED_CHECK_IN)
    suspend fun checkInBookAppointment(
        @Header("Authorization") authorization: String,
        @Header("user_uuid") user_uuid: Int,
        @Body reqAppointmentCheckIn: ReqAppointmentCheckIn?
    ):retrofit2.Response<ResAppointmentCheckIn>
}