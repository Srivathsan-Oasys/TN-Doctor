package com.hmis_tn.doctor.ui.order_status.model

data class OrderStatusRequestModel(
    var pageNo: Int = 0,
    var paginationSize: Int = 0,
    var search: String = "",
    var sortField: String = "",
    var sortOrder: String = "",
    val test_uuid: String = "",
    var uhid: String = ""
)