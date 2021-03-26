package com.hmis_tn.doctor.ui.emr_workflow.prescription.model

data class GstMaster(
    var child_gst_uuid: Int? = 0,
    var gst_code: String? = "",
    var gst_name: String? = "",
    var gst_percentage: String? = "",
    var parent_gst_uuid: Int? = 0,
    var uuid: Int? = 0
)