package com.hmis_tn.doctor.ui.emr_workflow.history.allergy.model.response

data class EncounterAllergyTypeResponse(
    val code: Int? = 0,
    val message: String? = "",
    val responseContents: List<Type?>? = listOf()
)