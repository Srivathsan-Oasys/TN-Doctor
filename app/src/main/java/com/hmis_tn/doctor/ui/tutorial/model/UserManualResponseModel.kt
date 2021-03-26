package com.hmis_tn.doctor.ui.tutorial.model


data class UserManualResponseModel(
    val responseContents: List<UserManualResponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0
)