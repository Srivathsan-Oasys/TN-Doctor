package com.hmis_tn.doctor.ui.emr_workflow.history.familyhistory.model

data class FamilyTypeSpinnerResponseModel(
    val responseContents: List<FamilyTyperesponseContent?>? = listOf(),
    val req: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)