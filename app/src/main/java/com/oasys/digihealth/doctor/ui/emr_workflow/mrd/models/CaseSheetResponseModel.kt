package com.oasys.digihealth.doctor.ui.emr_workflow.mrd.models

data class CaseSheetResponseModel(
    val msg: String = "",
    val req: Req = Req(),
    val responseContents: List<CaseSheetResponseContent> = listOf(),
    val status: String = "",
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)