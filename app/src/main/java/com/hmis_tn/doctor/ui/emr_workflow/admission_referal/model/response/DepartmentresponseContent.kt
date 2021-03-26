package com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.response

data class DepartmentresponseContent(
    val a_uuid: Int? = 0,
    val code: String? = "",
    val is_active: Int? = 0,
    val is_gender_based: Any? = Any(),
    val name: String? = "",
    val short_code: String? = "",
    val status: Int? = 0,
    val uuid: Int? = 0
)