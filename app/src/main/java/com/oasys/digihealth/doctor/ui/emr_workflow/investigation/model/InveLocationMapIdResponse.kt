package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model

data class InveLocationMapIdResponse(
    val message: String = "",
    val responseContents: ResponseContents = ResponseContents(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)