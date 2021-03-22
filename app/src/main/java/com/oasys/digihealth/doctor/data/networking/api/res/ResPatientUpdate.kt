package com.oasys.digihealth.doctor.data.networking.api.res

import com.google.gson.annotations.SerializedName

data class ResPatientUpdate(
    @SerializedName("status")
    var status: String? = null,
    @SerializedName("statusCode")
    var statusCode: Int? = null,
    @SerializedName("msg")
    var msg: String? = null
)


