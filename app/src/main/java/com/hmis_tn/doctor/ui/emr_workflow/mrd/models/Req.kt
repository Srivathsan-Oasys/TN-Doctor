package com.hmis_tn.doctor.ui.emr_workflow.mrd.models

data class Req(
    val is_emr: Boolean = false,
    val pageNo: Int = 0,
    val paginationSize: Int = 0,
    val patient_mobile: String = "",
    val patient_pin: String = "",
    val searchKey: String = "",
    val sortField: String = "",
    val sortOrder: String = "",
    val visit_type: String = ""
)