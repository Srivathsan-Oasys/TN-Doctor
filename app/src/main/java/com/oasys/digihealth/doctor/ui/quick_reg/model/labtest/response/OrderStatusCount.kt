package com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.response

data class OrderStatusCount(
    val order_count: Int? = 0,
    val order_status_name: String? = "",
    val order_status_uuid: Int? = 0
)