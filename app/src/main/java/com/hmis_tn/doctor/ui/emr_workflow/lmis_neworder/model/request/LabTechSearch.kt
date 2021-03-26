package com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.model.request

data class LabTechSearch(
    var is_default: Boolean = false,
    var lab_uuid: String? = null,
    var other_department_uuids: List<Int> = listOf(),
    var search: String = "",
    var sortField: String = "",
    var sortOrder: String = ""
)