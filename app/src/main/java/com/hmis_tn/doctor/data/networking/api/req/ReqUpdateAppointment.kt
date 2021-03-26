package com.makkalnalam.data.networking.api.req

import com.google.gson.annotations.SerializedName

data class ReqUpdateAppointment(
    @SerializedName("appointment_uuid")
    var appointment_uuid: Int? = null,
    @SerializedName("appointment_slot_uuid")
    var appointment_slot_uuid: Int? = null,
    @SerializedName("appointment_date")
    var appointment_date: String? = null,
    @SerializedName("doctor_uuid")
    var doctor_uuid: Int? = null,
    @SerializedName("start_time")
    var start_time: String? = null
)