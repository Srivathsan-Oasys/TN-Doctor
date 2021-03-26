package com.hmis_tn.doctor.ui.emr_workflow.investigation.model

data class InvestigationTypeResponseContent(
    var code: String = "",
    var created_by: Int = 0,
    var created_date: String = "",
    var is_active: Boolean = false,
    var modified_by: Int = 0,
    var modified_date: String = "",
    var name: String = "",
    var revision: Int = 0,
    var status: Boolean = false,
    var uuid: Int = 0
)