package com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.model

data class LabResultGetByDataResponseContent(
    val analyte_uom_code: String = "",
    val order_request_date: OrderRequestDateX = OrderRequestDateX(),
    val test_or_analyte: String = "",
    val test_or_analyte_ref_max: Int = 0,
    val test_or_analyte_ref_min: Int = 0
)