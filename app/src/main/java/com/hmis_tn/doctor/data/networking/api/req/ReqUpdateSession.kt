package com.hmis_tn.doctor.data.networking.api.req

import com.google.gson.annotations.SerializedName

data class ReqUpdateSession(
    @SerializedName("Id")
    var Id: Int? = null,
    @SerializedName("facility_uuid")
    var facility_uuid: Int? = null,
    @SerializedName("department_uuid")
    var department_uuid: Int? = null,
    @SerializedName("doctor_uuid")
    var doctor_uuid: Int? = null,
    @SerializedName("lab_uuid")
    var lab_uuid: Int? = null,
    @SerializedName("radiology_uuid")
    var radiology_uuid: Int? = null,
    @SerializedName("appointment_session_type_uuid")
    var appointment_session_type_uuid: Int? = null,
    @SerializedName("session_type_uuid")
    var session_type_uuid: Int? = 0,
    @SerializedName("session_name")
    var session_name: String? = null,
    @SerializedName("is_order_mandatory")
    var is_order_mandatory: Int? = 0,
    @SerializedName("can_allow_force_booking")
    var can_allow_force_booking: Int? = 0,
    @SerializedName("start_date")
    var start_date: String? = null,
    @SerializedName("end_date")
    var end_date: String? = null,
    @SerializedName("is_monday")
    var is_monday: Boolean? = false,
    @SerializedName("is_tuesday")
    var is_tuesday: Boolean? = false,
    @SerializedName("is_wednesday")
    var is_wednesday: Boolean? = false,
    @SerializedName("is_thursday")
    var is_thursday: Boolean? = false,
    @SerializedName("is_friday")
    var is_friday: Boolean? = false,
    @SerializedName("is_saturday")
    var is_saturday: Boolean? = false,
    @SerializedName("is_sunday")
    var is_sunday: Boolean? = false,
    @SerializedName("is_all")
    var is_all: Boolean? = false,
    @SerializedName("is_e_consultation")
    var is_e_consultation: Boolean? = false,
    @SerializedName("is_regular_condultation")
    var is_regular_condultation: Boolean? = false,
    @SerializedName("appointment_slot_type_uuid")
    var appointment_slot_type_uuid: Int? = 0,
    @SerializedName("slot_duration")
    var slot_duration: Int? = 0,
    @SerializedName("slot_duration_type_uuid")
    var slot_duration_type_uuid: Int? = 0,
    @SerializedName("start_time")
    var start_time: String? = null,
    @SerializedName("end_time")
    var end_time: String? = null,
    @SerializedName("break_from")
    var break_from: String? = "",
    @SerializedName("break_to")
    var break_to: String? = "",
    @SerializedName("max_slot_per_day")
    var max_slot_per_day: Int? = 0,
    @SerializedName("holiday_from")
    var holiday_from: String? = "",
    @SerializedName("holiday_to")
    var holiday_to: String? = "",
    @SerializedName("holiday_to")
    var econsult_charges: Int? = 0
)

data class ReqViewSession(
    @SerializedName("uuid")
    var uuid: Int? = null
)

data class ReqDeleteSession(
    @SerializedName("Id")
    var Id: Int? = null
)