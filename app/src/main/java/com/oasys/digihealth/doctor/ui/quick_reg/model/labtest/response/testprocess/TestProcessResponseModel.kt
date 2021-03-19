package com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.response.testprocess

data class TestProcessResponseModel(
    val responseContents: List<TestPrecessresponseContent?>? = listOf(),
    val disease_result_data: List<DiseaseResultData?>? = listOf(),
    val message: String? = "",
    val order_status_count: List<OrderStatusCount?>? = listOf(),
    val req: Req? = Req(),
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)