package com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.model

data class PrevLabLmisResponseModel(
    val message: String = "",
    val responseContents: List<PrevLabLmisResponseContent> = listOf(),
    val statusCode: Int = 0
)