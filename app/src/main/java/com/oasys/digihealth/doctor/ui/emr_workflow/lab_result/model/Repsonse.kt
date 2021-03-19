package com.oasys.digihealth.doctor.ui.emr_workflow.lab_result.model

data class Repsonse(
    val analyte_uom: String = "",
    val patient_order_test_detail_uuid: Int = 0,
    val qualifier_value: Any = Any(),
    val result_value: Any = Any(),
    val test_or_analyte: String = "",
    val test_or_analyte_ref_max: Int = 0,
    val test_or_analyte_ref_min: Int = 0,
    val uuid: Int = 0
)