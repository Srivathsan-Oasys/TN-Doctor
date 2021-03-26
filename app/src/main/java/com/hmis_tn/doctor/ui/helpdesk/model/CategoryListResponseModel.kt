package com.hmis_tn.doctor.ui.helpdesk.model


data class CategoryListResponseModel(
    val responseContents: List<CategoryResponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0
)