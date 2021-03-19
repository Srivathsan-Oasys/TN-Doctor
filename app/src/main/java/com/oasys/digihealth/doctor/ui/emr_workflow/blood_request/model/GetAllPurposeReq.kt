package com.oasys.digihealth.doctor.ui.emr_workflow.blood_request.model


import com.google.gson.annotations.SerializedName

data class GetAllPurposeReq(
    @SerializedName("sortField")
    val sortField: String?,
    @SerializedName("table_name")
    val tableName: String?
)