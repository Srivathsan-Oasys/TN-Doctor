package com.hmis_tn.doctor.ui.emr_workflow.vitals.model.responseuommodule

data class ResponseContentsUOM(
    var Is_default: Int? = 0,
    var code: Any? = Any(),
    var color: Any? = Any(),
    var created_by: Int? = 0,
    var created_date: String? = "",
    var display_order: Any? = Any(),
    var is_active: Boolean? = false,
    var language: Any? = Any(),
    var modified_by: Int? = 0,
    var modified_date: String? = "",
    var name: String? = "",
    var revision: Revision? = Revision(),
    var status: Boolean? = false,
    var uuid: Int? = 0
)