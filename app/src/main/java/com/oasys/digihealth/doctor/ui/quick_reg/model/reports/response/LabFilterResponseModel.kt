package com.oasys.digihealth.doctor.ui.quick_reg.model.reports.response

data class LabFilterResponseModel(
    val responseContents: List<LabFilterResponseContent?>? = listOf(),
    val req: String? = "",
    val status: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)