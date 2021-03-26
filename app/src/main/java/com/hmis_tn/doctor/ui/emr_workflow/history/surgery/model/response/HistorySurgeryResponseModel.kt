package com.hmis_tn.doctor.ui.emr_workflow.history.surgery.model.response

data class HistorySurgeryResponseModel(
    val code: Int? = 0,
    val responseContent: List<SurgeryresponseContent?>? = listOf(),
    val message: String? = ""
)