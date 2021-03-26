package com.hmis_tn.doctor.ui.dashboard.model

data class CovidMobileBelongsToResponseModel(
    val responseContents: List<MobilebelongstoresponseContent?>? = listOf(),
    val statusCode: Int? = 0
)