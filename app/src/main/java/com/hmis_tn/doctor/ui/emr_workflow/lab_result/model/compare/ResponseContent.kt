package com.hmis_tn.doctor.ui.emr_workflow.lab_result.model.compare

data class ResponseContent(
    var analyte_uom_code: String? = "",
    var analyte_uom_name: String? = "",
    var min_value: String? = "",
    var order_request_batches: List<OrderRequestBatche>? = listOf(),
    var order_request_date: String? = "",
    var test_or_analyte: String? = ""
) {
    var date1: String = ""
    var date2: String = ""
}