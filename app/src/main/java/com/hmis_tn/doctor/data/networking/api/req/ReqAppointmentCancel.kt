package com.hmis_tn.doctor.data.networking.api.req

import com.google.gson.annotations.SerializedName

data class ReqAppointmentCancel(
    @SerializedName("previous_appointment_uuid")
    var previous_appointment_uuid :Int?=null,
    @SerializedName("appointment_uuid")
    var appointment_uuid :Int?=null,
    @SerializedName("is_reschedule")
    var is_reschedule :Int?=null,
    @SerializedName("appointment_slots_uuid")
    var appointment_slots_uuid :Int?=null,
    @SerializedName("appointment_status_uuid")
    var appointment_status_uuid :Int?=null,
    @SerializedName("comments")
    var comments :String?=null,
    @SerializedName("cancelled_date")
    var cancelled_date :String?=null
)