package com.hmis_tn.doctor.ui.emr_workflow.history.immunization.model

data class ImmunizationNameResponseModel(
    val responseContents: List<ImmunizationnameresponseContent?>? = listOf(),
    val message: String? = "",
    val req: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)