package com.hmis_tn.doctor.ui.emr_workflow.diet.model.template_department

data class GetAllDepartmentResp(
    var req: String? = "",
    var responseContents: List<ResponseContent>? = listOf(),
    var status: String? = "",
    var statusCode: Int? = 0,
    var totalRecords: Int? = 0
)