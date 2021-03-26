package com.hmis_tn.doctor.ui.emr_workflow.lab_result.model

data class OrderRequestDateX(
    val analyte_uom_code: String = "",
    val analyte_uom_name: String = "",
    val order_request_date: String = "",
    val patient_order_details_uuid: Int = 0,
    val patient_order_test_detail_uuid: Int = 0,
    val patient_work_order_details_uuid: Int = 0,
    val result_value: Any = Any(),
    val test_or_analyte: String = "",
    val test_or_analyte_ref_max: Int = 0,
    val test_or_analyte_ref_min: Int = 0
)