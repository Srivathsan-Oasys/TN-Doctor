package com.oasys.digihealth.doctor.ui.emr_workflow.lab.model

data class FavAddAllDepatResponseModel(
    val req: String = "",
    val responseContents: List<FavAddAllDepatResponseContent> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)