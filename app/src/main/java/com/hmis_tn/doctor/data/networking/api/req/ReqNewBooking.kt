package com.hmis_tn.doctor.data.networking.api.req

import com.google.gson.annotations.SerializedName

data class ReqNewBooking(
    @SerializedName("doctor_uuid")
    var doctor_uuid: Int? = null,
    @SerializedName("appointment_slots_uuid")
    var appointment_slots_uuid: Int? = null,
    @SerializedName("appointment_date")
    var appointment_date: String? = null,
    @SerializedName("patient_uuid")
    var patient_uuid: Int? = null,
    @SerializedName("facility_uuid")
    var facility_uuid: Int? = null,
    @SerializedName("department_uuid")
    var department_uuid: Int? = null,
    @SerializedName("appointment_session_uuid")
    var appointment_session_uuid: Int? = null,
    @SerializedName("start_time")
    var start_time: String? = null,
    @SerializedName("end_time")
    var end_time: String? = "0",
    @SerializedName("encounter_uuid")
    var encounter_uuid: Int? = 0,
    @SerializedName("encounter_date")
    var encounter_date: String? = "",
    @SerializedName("cancelled_date")
    var cancelled_date: String? = "",
    @SerializedName("rating")
    var rating: Int? = 0,
    @SerializedName("comments")
    var comments: String? = "",
    @SerializedName("is_reschedule")
    var is_reschedule: Boolean? = false,
    @SerializedName("previous_appointment_uuid")
    var previous_appointment_uuid: Int? = null
)