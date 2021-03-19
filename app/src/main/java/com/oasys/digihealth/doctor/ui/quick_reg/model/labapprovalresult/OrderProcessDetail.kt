package com.oasys.digihealth.doctor.ui.quick_reg.model.labapprovalresult

data class OrderProcessDetail(
    var Id: Int = 0,
    var auth_status_uuid: Int? = null,
    var order_status_uuid: Int = 0,
    var to_location_uuid: Int = 0
)