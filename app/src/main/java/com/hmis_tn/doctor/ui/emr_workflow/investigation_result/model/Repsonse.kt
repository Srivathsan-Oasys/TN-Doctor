package com.hmis_tn.doctor.ui.emr_workflow.investigation_result.model

import android.graphics.Bitmap

data class Repsonse(
    val analyte_uom: String = "",
    val patient_order_test_detail_uuid: Int = 0,
    val qualifier_value: String = "",
    val result_value: String = "",
    val test_or_analyte: String = "",
    val test_or_analyte_ref_max: String = "",
    val test_or_analyte_ref_min: String = "",
    val uuid: Int = 0,
    val work_order_attachment_attachment_name: Any = Any(),
    val work_order_attachment_comments: Any = Any(),
    val work_order_attachment_file_path: String? = null,
    var bytestream: Bitmap? = null,
    val work_order_attachments_uuid: Any = Any()
)