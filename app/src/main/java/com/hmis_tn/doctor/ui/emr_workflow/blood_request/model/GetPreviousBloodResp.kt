package com.hmis_tn.doctor.ui.emr_workflow.blood_request.model


import com.google.gson.annotations.SerializedName

data class GetPreviousBloodResp(
    @SerializedName("message")
    val message: String?,
    @SerializedName("req")
    val req: String?,
    @SerializedName("responseContents")
    val responseContents: List<ResponseContentXX>?,
    @SerializedName("statusCode")
    val statusCode: Int?
)