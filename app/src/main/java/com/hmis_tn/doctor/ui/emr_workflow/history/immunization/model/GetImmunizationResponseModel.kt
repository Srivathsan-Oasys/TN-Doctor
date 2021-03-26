package com.hmis_tn.doctor.ui.emr_workflow.history.immunization.model

data class GetImmunizationResponseModel(
    val responseContents: List<GetImmunizationresponseContent?>? = listOf(),
    val code: Int? = 0,
    val message: String? = ""
)