package com.hmis_tn.doctor.data.networking.api.res

import com.google.gson.annotations.SerializedName

data class ResAppointmentBooked(
    @SerializedName("statusCode")
    var statusCode: Int? = null,
    @SerializedName("totalRecords")
    var totalRecords: Int? = null,
    @SerializedName("status")
    var status: String? = null,
    @SerializedName("msg")
    var msg: String? = null,
    @SerializedName("responseContents")
    var responseContents: List<ResAppointmentBookedList>? = null
)

data class ResAppointmentBookedList(
    @SerializedName("as_uuid")
    var as_uuid: Int? = null,
    @SerializedName("previous_appointment_uuid")
    var previous_appointment_uuid: Int? = null,
    @SerializedName("appointment_slots_uuid")
    var appointment_slots_uuid: Int? = null,
    @SerializedName("appointment_status_uuid")
    var appointment_status_uuid: Int? = null,
    @SerializedName("facility_uuid")
    var facility_uuid: Int? = null,
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
    @SerializedName("doc_uuid")
    var doc_uuid: Int? = null,
    @SerializedName("doc_username")
    var doc_username: String? = null,
    @SerializedName("doc_firstname")
    var doc_firstname: String? = null,
    @SerializedName("doc_middlename")
    var doc_middlename: String? = null,
    @SerializedName("doc_lastname")
    var doc_lastname: String? = null,
    @SerializedName("lab_uuid")
    var lab_uuid: Int? = null,
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
    @SerializedName("session_name")
    var session_name: String? = null,
    @SerializedName("start_date")
    var start_date: String? = null,
    @SerializedName("end_date")
    var end_date: String? = null,
    @SerializedName("holiday_from")
    var holiday_from: String? = null,
    @SerializedName("holiday_to")
    var holiday_to: String? = null,
    @SerializedName("econsult_charges")
    var econsult_charges: Int? = null,
    @SerializedName("is_e_consultation")
    var is_e_consultation: Boolean? = null,
    @SerializedName("is_regular_consultation")
    var is_regular_consultation: Boolean? = null,
    @SerializedName("slot_duration")
    var slot_duration: Int? = null,
    @SerializedName("max_slot_per_day")
    var max_slot_per_day: Int? = null,
    @SerializedName("is_active")
    var is_active: Boolean? = null,
    @SerializedName("status")
    var status: Boolean? = null,
    @SerializedName("as_created_by")
    var as_created_by: Int? = null,
    @SerializedName("as_created_date")
    var as_created_date: String? = null,
    @SerializedName("as_modified_by")
    var as_modified_by: Int? = null,
    @SerializedName("as_start_time")
    var as_start_time: String? = null,
    @SerializedName("as_end_time")
    var as_end_time: String? = null,
    @SerializedName("as_image_url")
    var as_image_url: String? = null,
    @SerializedName("modified_date")
    var modified_date: String? = null,
    @SerializedName("created_username")
    var created_username: String? = null,
    @SerializedName("created_firstname")
    var created_firstname: String? = null,
    @SerializedName("created_middlename")
    var created_middlename: String? = null,
    @SerializedName("modified_username")
    var modified_username: String? = null,
    @SerializedName("modified_firstname")
    var modified_firstname: String? = null,
    @SerializedName("modified_lastname")
    var modified_lastname: String? = null,
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
    @SerializedName("patient_pin")
    var patient_pin: String? = null,
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
    @SerializedName("is_reschedule")
    var is_reschedule: Boolean? = null
)

