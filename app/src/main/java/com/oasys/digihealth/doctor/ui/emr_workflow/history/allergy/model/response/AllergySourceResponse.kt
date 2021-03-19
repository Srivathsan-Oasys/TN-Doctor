package com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.model.response

data class AllergySourceResponse(
    val req: String? = "",
    val responseContents: List<Source?>? = listOf(),
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)