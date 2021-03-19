package com.oasys.digihealth.doctor.ui.emr_workflow.blood_request.model


import com.google.gson.annotations.SerializedName

data class BloodRequestSaveResp(
    @SerializedName("message")
    var message: String?,
    @SerializedName("responseContents")
    var responseContents: ResponseContents?,
    @SerializedName("statusCode")
    var statusCode: Int?
)