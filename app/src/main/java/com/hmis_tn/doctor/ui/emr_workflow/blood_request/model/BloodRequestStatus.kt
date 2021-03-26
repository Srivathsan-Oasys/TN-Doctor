package com.hmis_tn.doctor.ui.emr_workflow.blood_request.model


import com.google.gson.annotations.SerializedName

data class BloodRequestStatus(
    @SerializedName("code")
    val code: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("uuid")
    val uuid: Int?
)