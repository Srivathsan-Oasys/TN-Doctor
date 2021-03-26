package com.hmis_tn.doctor.data.networking.api.res

import com.google.gson.annotations.SerializedName

data class ResBookAppointment(
    @SerializedName("statusCode")
    var statusCode: Int? = null,
    @SerializedName("status")
    var status: String? = null,
    @SerializedName("msg")
    var msg: String? = null
)