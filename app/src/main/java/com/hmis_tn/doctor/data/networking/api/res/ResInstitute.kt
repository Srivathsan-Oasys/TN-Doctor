package com.hmis_tn.doctor.data.networking.api.res

import com.google.gson.annotations.SerializedName

data class ResInstitute(
    @SerializedName("status")
    var status: String? = "",
    @SerializedName("statusCode")
    var statusCode: Int? = 0,
    @SerializedName("totalRecords")
    var totalRecords: Int? = 0,
    @SerializedName("responseContents")
    var responseContents: List<ResInstituteList>?=null
)

data class ResInstituteList(
    @SerializedName("uuid")
    var uuid: Int? = null,
    @SerializedName("name")
    var name: String? = null

)