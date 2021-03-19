package com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.response

data class DepartmentAutoCompleteResponseModel(
    val req: String? = "",
    val responseContents: List<ResponseContent>? = listOf(),
    val status: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)