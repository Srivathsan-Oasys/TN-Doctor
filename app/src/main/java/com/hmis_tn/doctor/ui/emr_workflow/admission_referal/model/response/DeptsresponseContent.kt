package com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.response

data class DeptsresponseContent(
    val code: String? = "",
    val department_settings: List<DepartmentSetting>? = listOf(),
    val name: String? = "",
    val uuid: Int? = 0
)