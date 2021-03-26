package com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.response

data class DepartmentResponseModel(
    val responseContents: List<DepartmentresponseContent>? = listOf(),
    val req: String? = "",
    val status: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)