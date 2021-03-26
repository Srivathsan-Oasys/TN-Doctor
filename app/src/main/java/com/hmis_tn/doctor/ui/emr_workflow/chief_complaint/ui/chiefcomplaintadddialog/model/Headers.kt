package com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.ui.chiefcomplaintadddialog.model

data class Headers(
    var active_from: String = "",
    var created_by: String = "",
    var created_date: String = "",
    var department_uuid: String = "",
    var display_order: String = "",
    var facility_uuid: String = "",
    var favourite_type_uuid: Int = 0,
    var is_active: Int = 0,
    var is_public: Boolean = false,
    var modified_by: String = "",
    var modified_date: String = "",
    var revision: Int = 0,
    var status: Int = 0,
    var ticksheet_type_uuid: Int = 0,
    var user_uuid: String = "",
    var uuid: Int = 0
)