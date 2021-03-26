package com.hmis_tn.doctor.ui.emr_workflow.blood_request.model


import com.google.gson.annotations.SerializedName

data class GetBloodComponentsResp(
    @SerializedName("req")
    var req: String?,
    @SerializedName("responseContents")
    var responseContents: List<ResponseContentXXX>?,
    @SerializedName("statusCode")
    var statusCode: Int?,
    @SerializedName("totalRecords")
    var totalRecords: Int?
)