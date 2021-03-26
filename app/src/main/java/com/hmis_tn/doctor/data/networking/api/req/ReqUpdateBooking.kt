package com.makkalnalam.data.networking.api.req

import com.google.gson.annotations.SerializedName

data class ReqUpdateBooking(
    @SerializedName("Id")
    var id: Int? = null,
    @SerializedName("appointment_date")
    var appointment_date: String? = null,
    @SerializedName("start_time")
    var start_time: String? = null,
    @SerializedName("doctor_uuid")
    var doctor_uuid: Int? = null

)