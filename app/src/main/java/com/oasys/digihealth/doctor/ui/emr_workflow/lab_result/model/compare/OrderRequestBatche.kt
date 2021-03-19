package com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.model.compare

data class OrderRequestBatche(
    var analyte_uom_code: String? = "",
    var analyte_uom_name: String? = "",
    var analyte_uom_uuid: Int? = 0,
    var order_request_date: String? = "",
    var patient_order_details_uuid: Int? = 0,
    var patient_order_test_detail_uuid: Int? = 0,
    var patient_work_order_details_uuid: Int? = 0,
    var result_value: String? = "",
    var test_or_analyte: String? = "",
    var test_or_analyte_ref_max: String? = "",
    var test_or_analyte_ref_min: String? = ""
)