package com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model.templaterequest

data class RequestTemplateModule(
    var lab_uuid: String? = "",
    var other_department_uuids: List<Int?>? = listOf(),
    var search: String? = "",
    var sortField: String? = "",
    var sortOrder: String? = ""
)