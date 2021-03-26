package com.hmis_tn.doctor.ui.emr_workflow.history.allergy.model.response

data class AllergyNameResponse(
    val message: String? = "",
    val responseContents: List<Name?>? = listOf(),
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)