package com.oasys.digihealth.doctor.ui.quick_reg.model

data class QuickSearchResponseModel(
    val responseContents: List<QuickSearchresponseContent?>? = listOf(),
    val responseContent: QuickSearchresponseContent?=null,
    val message: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)