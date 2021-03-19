package com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.response

data class DeptsRespModel(
    val responseContents: List<DeptsresponseContent>? = listOf(),
    val msg: String? = "",
    val statusCode: Int? = 0
)