package com.hmis_tn.doctor.ui.dashboard.model

data class CovidPatientCategoryResponseModel(
    val responseContents: List<PatientCategoryresponseContent?>? = listOf(),
    val statusCode: Int? = 0
)