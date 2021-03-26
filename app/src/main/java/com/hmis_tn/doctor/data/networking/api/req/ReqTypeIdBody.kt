package com.hmis_tn.doctor.data.networking.api.req

import com.google.gson.annotations.SerializedName

data class ReqTypeIdBody(
    @SerializedName("Id")
    var id: ArrayList<Int>? = null
)