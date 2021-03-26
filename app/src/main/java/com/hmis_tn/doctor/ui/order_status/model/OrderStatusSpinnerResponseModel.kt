package com.hmis_tn.doctor.ui.order_status.model

data class OrderStatusSpinnerResponseModel(
    val responseContents: List<OrderStatusSpinnerresponseContent?>? = listOf(),
    val req: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)