package com.hmis_tn.doctor.data.networking.api.req

import com.google.gson.annotations.SerializedName

data class ReqDoctorBody(
    @SerializedName("user_type_code")
    var user_type_code: String? = null,
    @SerializedName("istelemed")
    var istelemed: Boolean? = null,
    @SerializedName("facility_uuid")
    var facility_uuid: Int? = null
)