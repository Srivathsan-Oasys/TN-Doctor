package com.hmis_tn.doctor.ui.quick_reg.model.labtest.request.orderRequest

data class OrderProcessDetails(
    var count: Int = 0,
    var rows: ArrayList<Header> = ArrayList()
)