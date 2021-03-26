package com.hmis_tn.doctor.ui.emr_workflow.history.model.response

data class HistoryResponce(
    val code: Int? = 0,
    val responseContents: List<History?>? = listOf(),
    val message: String? = ""
)