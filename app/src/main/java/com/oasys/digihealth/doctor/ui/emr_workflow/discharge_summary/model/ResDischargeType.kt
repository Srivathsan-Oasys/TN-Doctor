package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model

import com.google.gson.annotations.SerializedName

data class ResDischargeType(
    var statusCode: Int? = 0,
    @SerializedName("responseContent")
    var discharge_types: DischargeTypes? = null,
    var message: String? = ""
)

data class DischargeTypes(
    @SerializedName("Discharge_Types")
    var discharge_types: List<DischargeTypeList>? = null
)

data class DischargeTypeList(
    var uuid: Int? = 0,
    var code: String? = "",
    var name: String? = "",
    var display_order: Int? = 0,
    var is_active: Boolean? = false,
    var status: Boolean? = false,
    var revision: Boolean? = false,
    var created_by: Int? = 0,
    var modified_by: Int? = 0
)