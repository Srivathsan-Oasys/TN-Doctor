package com.oasys.digihealth.doctor.ui.emr_workflow.blood_request.model


import com.google.gson.annotations.SerializedName

data class GetPreviousBloodReq(
    @SerializedName("patient_uuid")
    val patientUuid: String?
)