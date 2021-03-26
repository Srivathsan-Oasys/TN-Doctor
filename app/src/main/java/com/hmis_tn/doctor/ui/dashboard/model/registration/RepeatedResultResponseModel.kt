package com.hmis_tn.doctor.ui.dashboard.model.registration

data class RepeatedResultResponseModel(
    val responseContents: List<RepeatedResultresponseContent?>? = listOf(),
    val statusCode: Int? = 0
)