package com.hmis_tn.doctor.ui.dashboard.model

data class CovidPeriodResponseModel(
    val responseContents: List<PeriodresponseContent?>? = listOf(),
    val req: String? = "",
    val statusCode: Int? = 0
)