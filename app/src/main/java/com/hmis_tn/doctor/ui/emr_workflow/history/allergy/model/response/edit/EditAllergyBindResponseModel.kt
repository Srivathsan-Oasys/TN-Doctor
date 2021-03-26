package com.hmis_tn.doctor.ui.emr_workflow.history.allergy.model.response.edit

data class EditAllergyBindResponseModel(
    val responseContent: EditAllerdyBindresponseContent? = EditAllerdyBindresponseContent(),
    val code: Int? = 0
)