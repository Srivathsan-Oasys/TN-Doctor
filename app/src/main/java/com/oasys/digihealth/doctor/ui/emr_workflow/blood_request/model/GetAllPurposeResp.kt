package com.oasys.digihealth.doctor.ui.emr_workflow.blood_request.model


import com.google.gson.annotations.SerializedName

data class GetAllPurposeResp(
    @SerializedName("req")
    val req: String?,
    @SerializedName("responseContents")
    val responseContents: List<ResponseContentX>?,
    @SerializedName("statusCode")
    val statusCode: Int?,
    @SerializedName("totalRecords")
    val totalRecords: Int?
)