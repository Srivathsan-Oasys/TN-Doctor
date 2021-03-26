package com.hmis_tn.doctor.ui.emr_workflow.blood_request.model


import com.google.gson.annotations.SerializedName

data class GetAllBloodGroupResp(
    @SerializedName("req")
    val req: String?,
    @SerializedName("responseContents")
    val responseContents: List<ResponseContent>?,
    @SerializedName("statusCode")
    val statusCode: Int?,
    @SerializedName("totalRecords")
    val totalRecords: Int?
)