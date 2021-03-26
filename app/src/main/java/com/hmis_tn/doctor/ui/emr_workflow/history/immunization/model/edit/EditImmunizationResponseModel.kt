package com.hmis_tn.doctor.ui.emr_workflow.history.immunization.model.edit

data class EditImmunizationResponseModel(
    val responseContent: EditImmunizationresponseContent? = EditImmunizationresponseContent(),
    val code: Int? = 0
)