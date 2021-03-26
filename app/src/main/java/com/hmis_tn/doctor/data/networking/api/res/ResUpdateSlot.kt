package com.hmis_tn.doctor.data.networking.api.res

import com.google.gson.annotations.SerializedName

data class ResUpdateSlot(
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("statusCode")
    var statusCode: Int? = 0
)