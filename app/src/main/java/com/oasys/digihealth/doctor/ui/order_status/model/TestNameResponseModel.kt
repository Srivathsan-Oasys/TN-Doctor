package com.oasys.digihealth.doctor.ui.order_status.model

data class TestNameResponseModel(
    val responseContents: List<TestNameResponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)