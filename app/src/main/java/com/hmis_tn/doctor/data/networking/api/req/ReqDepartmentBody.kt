package com.hmis_tn.doctor.data.networking.api.req

import com.google.gson.annotations.SerializedName

data class ReqDepartmentBody(
    @SerializedName("Id")
    var Id: String? = null,
    @SerializedName("facility_uuid")
    var facility_uuid: String? = null
)