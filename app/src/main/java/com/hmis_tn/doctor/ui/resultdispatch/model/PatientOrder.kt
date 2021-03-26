package com.hmis_tn.doctor.ui.resultdispatch.model

data class PatientOrder(
    var order_number: Int? = 0,
    var order_request_date: String? = "",
    var order_status_uuid: Int? = 0,
    var uuid: Int? = 0
)