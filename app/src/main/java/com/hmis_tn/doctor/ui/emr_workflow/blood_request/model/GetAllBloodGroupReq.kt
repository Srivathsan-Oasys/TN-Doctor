package com.hmis_tn.doctor.ui.emr_workflow.blood_request.model


import com.google.gson.annotations.SerializedName

data class GetAllBloodGroupReq(
    @SerializedName("table_name")
    val tableName: String?
)