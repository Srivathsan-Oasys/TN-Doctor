package com.oasys.digihealth.doctor.ui.emr_workflow.blood_request.model


import com.google.gson.annotations.SerializedName

data class BloodRequestSaveReq(
    @SerializedName("details")
    var details: List<Detail>?,
    @SerializedName("header")
    var header: Header?
)