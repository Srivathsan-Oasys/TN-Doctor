package com.hmis_tn.doctor.ui.sample_dispatch.model

data class OrderByResponseModel(
    val responseContents: List<OrderByResponseContent> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)