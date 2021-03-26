package com.hmis_tn.doctor.ui.quick_reg.model.reports.response


data class LabWiseReportResponseModel(
    val responseContents: List<LabWiseReportResponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)