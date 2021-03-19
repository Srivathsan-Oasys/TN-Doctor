package com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.model.response.period

data class PeriodsResponseModel(
    val responseContents: List<PeriodresponseContent?>? = listOf(),
    val req: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)