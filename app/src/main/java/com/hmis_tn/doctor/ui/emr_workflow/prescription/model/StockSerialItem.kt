package com.hmis_tn.doctor.ui.emr_workflow.prescription.model

data class StockSerialItem(
    var batch_id: String? = "",
    var expiry_date: String? = "",
    var quantity: Int? = 0,
    var uuid: Int? = 0
)