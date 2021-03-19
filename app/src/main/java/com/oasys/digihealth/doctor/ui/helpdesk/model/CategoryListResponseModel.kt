package com.oasys.digihealth.doctor.ui.helpdesk.model


data class CategoryListResponseModel(
    val responseContents: List<CategoryResponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0
)