package com.hmis_tn.doctor.ui.emr_workflow.view.lab.model

import com.hmis_tn.doctor.ui.emr_workflow.lab.model.PrevLabResponseContent

data class PrevLabResponseModel(
    val message: String = "",
    val responseContents: List<PrevLabResponseContent> = listOf(),
    val statusCode: Int = 0
)