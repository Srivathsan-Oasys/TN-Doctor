package com.oasys.digihealth.doctor.ui.emr_workflow.view.lab.model

import com.oasys.digihealth.doctor.ui.emr_workflow.lab.model.PrevLabResponseContent

data class PrevLabResponseModel(
    val message: String = "",
    val responseContents: List<PrevLabResponseContent> = listOf(),
    val statusCode: Int = 0
)