package com.hmis_tn.doctor.data.networking.api.res

import com.google.gson.annotations.SerializedName

data class ResUpdateEConsult(
    @SerializedName("statusCode")
    var statusCode: Int? = null,
    @SerializedName("msg")
    var msg: String? = null,
    @SerializedName("responseContents")
    var responseContents: List<Int>? = null
)

