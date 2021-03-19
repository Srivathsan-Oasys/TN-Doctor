package com.oasys.digihealth.doctor.ui.quick_reg.model.reports.response


data class LabTestWiseReportResponseModel(
    val responseContents: List<LabTestWiseReportResponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)