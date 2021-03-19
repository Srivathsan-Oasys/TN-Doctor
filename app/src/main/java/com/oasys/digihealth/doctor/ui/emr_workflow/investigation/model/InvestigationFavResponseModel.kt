package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model

data class InvestigationFavResponseModel(
    val code: Int = 0,
    val message: String = "",
    val responseContentLength: Int = 0,
    val responseContents: List<Any> = listOf()
)