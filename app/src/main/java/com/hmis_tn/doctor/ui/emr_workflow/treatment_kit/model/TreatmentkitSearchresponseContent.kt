package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model

data class TreatmentkitSearchresponseContent(
    val code: String? = "",
    val department_uuid: Int? = 0,
    val is_active: Boolean? = false,
    val name: String? = "",
    val status: Boolean? = false,
    val sub_department_uuid: Int? = 0,
    val type: String? = "",
    val uuid: Int? = 0
)