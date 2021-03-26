package com.hmis_tn.doctor.ui.login.model.institution_response

data class InstitutionResponseModel(
    val responseContents: List<InstitutionresponseContent?>? = listOf(),
    val msg: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)