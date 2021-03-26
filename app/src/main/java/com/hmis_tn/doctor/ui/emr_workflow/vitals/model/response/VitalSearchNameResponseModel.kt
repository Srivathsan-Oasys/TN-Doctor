package com.hmis_tn.doctor.ui.emr_workflow.vitals.model.response

data class VitalSearchNameResponseModel(
    val responseContents: List<VitalSearchNameresponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0
)