package com.hmis_tn.doctor.ui.emr_workflow.prescription.model

data class ZeroStockResponseModel(
    val message: String = "",
    val responseContents: List<ZeroStockResponseContent> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)