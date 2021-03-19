package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model

import com.google.gson.annotations.SerializedName

data class ResDischargeDeathType(
    var statusCode: Int? = 0,
    @SerializedName("responseContent")
    var discharge_death_types: DischargeDeathTypes? = null,
    var message: String? = ""
)

data class DischargeDeathTypes(
    @SerializedName("Discharge_Types")
    var discharge_death_types_list: List<DischargeDeathTypeList>? = null
)

data class DischargeDeathTypeList(
    var uuid: Int? = 0,
    var code: String? = "",
    var name: String? = "",
    var display_order: Int? = 0,
    var is_active: Boolean? = false,
    var status: Boolean? = false,
    var revision: Boolean? = false,
    var created_by: Int? = 0,
    var modified_by: Int? = 0,
    var created_date: String? = "",
    var modified_date: String? = ""
)