package com.hmis_tn.doctor.ui.emr_workflow.blood_request.model


import com.google.gson.annotations.SerializedName

data class Detail(
    @SerializedName("blood_component_uuid")
    var bloodComponentUuid: Int?,
    @SerializedName("quantity_required")
    var quantityRequired: String?
)