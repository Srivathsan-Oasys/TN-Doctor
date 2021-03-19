package com.oasys.digihealth.doctor.ui.emr_workflow.mrd.models

data class CaseSheetRequestModel(
    var is_emr: Boolean = false,
    var pageNo: Int = 0,
    var paginationSize: Int = 0,
    var patient_mobile: String = "",
    var patient_pin: String = "",
    val searchKey: String = "",
    var sortField: String = "",
    var sortOrder: String = "",
    var visit_type: String = ""
)