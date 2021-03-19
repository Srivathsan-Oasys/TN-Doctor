package com.oasys.digihealth.doctor.ui.emr_workflow.blood_request.model


import com.google.gson.annotations.SerializedName

data class BloodRequestDetail(
    @SerializedName("blood_component")
    val bloodComponent: BloodComponent?,
    @SerializedName("blood_component_uuid")
    val bloodComponentUuid: Int?,
    @SerializedName("blood_request_uuid")
    val bloodRequestUuid: Int?,
    @SerializedName("quantity_required")
    val quantityRequired: Int?,
    @SerializedName("uuid")
    val uuid: Int?
)