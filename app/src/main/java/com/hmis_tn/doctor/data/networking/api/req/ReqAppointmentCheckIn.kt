package com.hmis_tn.doctor.data.networking.api.req

import com.google.gson.annotations.SerializedName

data class ReqAppointmentCheckIn(
    @SerializedName("Id")
    var Id: Int? = null
)