package com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.model.response

data class AllergyAllGetResponseModel(
    val code: Int? = 0,
    val responseContent: List<AllergyAll?>? = listOf()
)