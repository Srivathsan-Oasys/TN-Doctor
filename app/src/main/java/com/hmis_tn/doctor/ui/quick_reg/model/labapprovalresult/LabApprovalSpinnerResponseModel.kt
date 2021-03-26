package com.hmis_tn.doctor.ui.quick_reg.model.labapprovalresult

data class LabApprovalSpinnerResponseModel(
    val req: String = "",
    val responseContents: List<LabApprovalSpinnerResponseContent> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)