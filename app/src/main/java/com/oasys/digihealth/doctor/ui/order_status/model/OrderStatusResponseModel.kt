package com.oasys.digihealth.doctor.ui.order_status.model

data class OrderStatusResponseModel(
    val completedCount: Int = 0,
    val message: String = "",
    val pendingCount: Int = 0,
    val responseContents: List<OrderStatusResponseContent> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)