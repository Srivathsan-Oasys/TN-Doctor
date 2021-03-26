package com.hmis_tn.doctor.data.networking.api.req

import com.google.gson.annotations.SerializedName

data class ReqAvailability(
    @SerializedName("doctor_uuid")
    var doctor_id: String? = null,
    @SerializedName("appointment_session_uuid")
    var appointment_session_uuid: String? = null,
    @SerializedName("doctor_ID")
    var doctor_ID: String? = null,
    @SerializedName("start_date")
    var start_date: String? = null,
    @SerializedName("end_date")
    var end_date: String? = null,
    @SerializedName("start_time")
    var start_time: String? = null,
    @SerializedName("end_time")
    var end_time: String? = null,
    @SerializedName("max_slot_per_day")
    var max_slot_per_day: Int? = null,
    @SerializedName("appointment_date")
    var appointment_date: String? = null
)