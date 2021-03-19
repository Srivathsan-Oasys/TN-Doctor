package com.oasys.digihealth.doctor.ui.helpdesk.model


data class DepartmentResponseModel(
    val responseContents: List<DepartmentResponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0
)