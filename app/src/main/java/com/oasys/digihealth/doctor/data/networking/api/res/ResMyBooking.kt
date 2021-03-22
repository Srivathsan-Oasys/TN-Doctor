package com.oasys.digihealth.doctor.data.networking.api.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResMyBooking(
    @SerializedName("statusCode")
    val statusCode: Int? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("responseContents")
    val responseContents: List<ResMyBookingData>? = listOf()
) : Parcelable

@Parcelize
data class ResMyBookingData(
    @SerializedName("as_uuid")
    var as_uuid: Int? = 0,
    @SerializedName("facility_uuid")
    var facility_uuid: Int? = 0,
    @SerializedName("facility_name")
    var facility_name: String? = null,
    @SerializedName("facility_code")
    var facility_code: String? = null,
    @SerializedName("department_uuid")
    var department_uuid: Int? = null,
    @SerializedName("department_name")
    var department_name: String? = null,
    @SerializedName("department_code")
    var department_code: String? = null,
    @SerializedName("doctor_uuid")
    var doctor_uuid: Int? = null,
    @SerializedName("doc_username")
    var doc_username: String? = null,
    @SerializedName("doc_firstname")
    var doc_firstname: String? = null,
    @SerializedName("doc_middlename")
    var doc_middlename: String? = null,
    @SerializedName("doc_lastname")
    var doc_lastname: String? = null,
    @SerializedName("lab_uuid")
    var lab_uuid: String? = null,
    @SerializedName("lab_username")
    var lab_username: String? = null,
    @SerializedName("lab_firstname")
    var lab_firstname: String? = null,
    @SerializedName("lab_middlename")
    var lab_middlename: String? = null,
    @SerializedName("lab_lastname")
    var lab_lastname: String? = null,
    @SerializedName("rad_uuid")
    var rad_uuid: Int? = null,
    @SerializedName("rad_username")
    var rad_username: String? = null,
    @SerializedName("rad_firstname")
    var rad_firstname: String? = null,
    @SerializedName("rad_middlename")
    var rad_middlename: String? = null,
    @SerializedName("rad_lastname")
    var rad_lastname: String? = null,
    @SerializedName("patient_uuid")
    var patient_uuid: Int? = null,
    @SerializedName("patient_mobile")
    var patient_mobile: String? = null,
    @SerializedName("patient_name")
    var patient_name: String? = null,
    @SerializedName("patient_age")
    var patient_age: Int? = null,
    @SerializedName("patient_gender")
    var patient_gender: Int? = null,
    @SerializedName("gender")
    var gender: Int? = null,
    @SerializedName("gender_name")
    var gender_name: String? = null,
    @SerializedName("gender_code")
    var gender_code: String? = null,
    @SerializedName("start_time")
    var start_time: String? = null,
    @SerializedName("end_time")
    var end_time: String? = null,
    @SerializedName("appointment_status_name")
    var appointment_status_name: String? = null,
    @SerializedName("appointment_status_code")
    var appointment_status_code: String? = null,
    @SerializedName("appointment_no")
    var appointment_no: String? = null,
    @SerializedName("appointment_request_uuid")
    var appointment_request_uuid: Int? = null,
    @SerializedName("appointment_category_uuid")
    var appointment_category_uuid: Int? = null,
    @SerializedName("appointment_type_uuid")
    var appointment_type_uuid: Int? = null,
    @SerializedName("appointment_priority_uuid")
    var appointment_priority_uuid: Int? = null,
    @SerializedName("appointment_date")
    var appointment_date: String? = null,
    @SerializedName("appointment_status_uuid")
    var appointment_status_uuid: Int? = null,
    @SerializedName("is_force_booking")
    var is_force_booking: Boolean? = null,
    @SerializedName("appointment_session_uuid")
    var appointment_session_uuid: Int? = null,
    @SerializedName("referral_type_uuid")
    var referral_type_uuid: Int? = null,
    @SerializedName("referral_uuid")
    var referral_uuid: Int? = null,
    @SerializedName("is_assigned_to_user")
    var is_assigned_to_user: Boolean? = null,
    @SerializedName("is_assigned_to_group")
    var is_assigned_to_group: Boolean? = null,
    @SerializedName("is_mrd_file_request")
    var is_mrd_file_request: Boolean? = null,
    @SerializedName("assigned_user_uuid")
    var assigned_user_uuid: Int? = null,
    @SerializedName("assigned_group_uuid")
    var assigned_group_uuid: Int? = null,
    @SerializedName("doc_token")
    var doc_token: String? = null,
    @SerializedName("doc_session_uuid")
    var doc_session_uuid: String? = null,
    @SerializedName("rating")
    var rating: Int? = null,
    @SerializedName("encounter_uuid")
    var encounter_uuid: Int? = null,
    @SerializedName("encounter_type_uuid")
    var encounter_type_uuid: Int? = null,
    @SerializedName("encounter_date")
    var encounter_date: String? = null,
    @SerializedName("max_slot_per_day")
    var max_slot_per_day: Int? = null,
    @SerializedName("start_date")
    var start_date: String? = null,
    @SerializedName("end_date")
    var end_date: String? = null,
    @SerializedName("cancelled_date")
    var cancelled_date: String? = null,
    @SerializedName("cancelled_by")
    var cancelled_by: Int? = null,
    @SerializedName("as_created_by")
    var as_created_by: Int? = null,
    @SerializedName("as_modified_by")
    var as_modified_by: Int? = null,
    @SerializedName("as_created_date")
    var as_created_date: String? = null,
    @SerializedName("modified_date")
    var modified_date: String? = null
) : Parcelable