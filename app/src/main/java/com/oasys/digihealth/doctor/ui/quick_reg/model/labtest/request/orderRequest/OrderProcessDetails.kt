package com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.request.orderRequest

data class OrderProcessDetails(
    var count: Int = 0,
    var rows: ArrayList<Header> = ArrayList()
)