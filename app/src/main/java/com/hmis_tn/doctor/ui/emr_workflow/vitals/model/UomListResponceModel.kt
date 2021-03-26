package com.hmis_tn.doctor.ui.emr_workflow.vitals.model

data class UomListResponceModel(
    val responseContents: List<Uom> = listOf(),
    val message: String = "",
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)