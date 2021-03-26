package com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery

data class OtDiagnosisSearchResponseModel(
    val responseContents: List<OtDiagnosisresponseContent?>? = listOf(),
    val code: Int? = 0,
    val message: String? = "",
    val totalRecords: Int? = 0
)