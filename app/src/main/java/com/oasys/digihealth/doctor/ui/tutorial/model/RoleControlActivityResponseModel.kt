package com.oasys.digihealth.doctor.ui.tutorial.model


data class RoleControlActivityResponseModel(
    val responseContents: List<RoleControlActivityResponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0
)