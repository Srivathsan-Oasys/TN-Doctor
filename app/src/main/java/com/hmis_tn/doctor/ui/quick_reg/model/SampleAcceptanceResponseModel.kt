package com.hmis_tn.doctor.ui.quick_reg.model

data class SampleAcceptanceResponseModel(
    val responseContents: List<SampleAcceptanceresponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)