package com.oasys.digihealth.doctor.ui.dashboard.model

data class CovidGenderResponseModel(
    val responseContents: List<GenderresponseContent?>? = listOf(),
    val req: String? = "",
    val statusCode: Int? = 0
)