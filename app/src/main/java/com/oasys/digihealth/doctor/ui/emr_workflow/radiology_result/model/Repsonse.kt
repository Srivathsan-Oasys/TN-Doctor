package com.oasys.digihealth.doctor.ui.emr_workflow.radiology_result.model

import android.graphics.Bitmap

data class Repsonse(
    val analyte_uom: Any = Any(),
    val patient_order_test_detail_uuid: Int = 0,
    val qualifier_value: Any = Any(),
    val result_value: String = "",
    val test_or_analyte: String = "",
    val work_order_attachment_file_path: String = "",
    val test_or_analyte_ref_max: Any = Any(),
    val test_or_analyte_ref_min: Any = Any(),
    var bytestream: Bitmap? = null,
    val uuid: Int = 0
)