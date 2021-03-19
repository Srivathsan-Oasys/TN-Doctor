package com.oasys.digihealth.doctor.ui.emr_workflow.history.familyhistory.model

data class FamilyHistoryResponseModel(
    val code: Int = 0,
    val message: String = "",
    val responseContent: List<FamilyHistoryResponseContent> = listOf()
)