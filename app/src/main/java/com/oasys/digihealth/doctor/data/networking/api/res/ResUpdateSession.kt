package com.oasys.digihealth.doctor.data.networking.api.res

import com.google.gson.annotations.SerializedName

data class ResUpdateSession(
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("statusCode")
    var statusCode: Int? = 0
)