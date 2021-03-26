package com.hmis_tn.doctor.data.networking.api.res

import com.google.gson.annotations.SerializedName

data class ResDoctorSlot(
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("statusCode")
    var statusCode: Int? = 0,
    @SerializedName("responseContents")
    val responseContents: List<DoctorSlotList>? = null
)

data class DoctorSlotList(
    @SerializedName("uuid")
    var uuid: Int? = 0,
    @SerializedName("appointment_session_uuid")
    var appointment_session_uuid: Int? = 0,
    @SerializedName("start_time")
    var start_time: String? = null,
    @SerializedName("start_date")
    var start_date: String? = null,
    @SerializedName("appointment_status_uuid")
    var appointment_status_uuid: Int? = null,
    @SerializedName("is_e_consult")
    var is_e_consult: Boolean? = null,
    @SerializedName("doctor_uuid")
    var doctor_uuid: Int? = 0,
    @SerializedName("end_time")
    var end_time: String? = null,
    @SerializedName("appointment_date")
    var appointment_date: String? = null,
    @SerializedName("created_date")
    var created_date: String? = null,
    @SerializedName("modified_date")
    var modified_date: String? = null,
    @SerializedName("is_active")
    var is_active: Boolean? = null,
    @SerializedName("status")
    var status: Boolean? = null,
    @SerializedName("created_by")
    var created_by: Int? = null,
    @SerializedName("modified_by")
    var modified_by: Int? = null,
    @SerializedName("econsult_charges")
    var econsult_charges: Int? = null
)