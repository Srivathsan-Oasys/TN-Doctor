package com.hmis_tn.doctor.ui.emr_workflow.history.immunization.model

data class ImmunizationInstitutionResponseModel(
    val responseContents: List<ImmunizationInstitutionresponseContent?>? = listOf(),
    val req: String? = "",
    val status: String? = "",
    val statusCode: Int? = 0
)