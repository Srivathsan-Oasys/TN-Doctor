package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model

data class HeadersX(
    var department_uuid: String = "",
    var display_order: String = "",
    var facility_uuid: String = "",
    var favourite_type_uuid: Int = 0,
    var is_active: Boolean = false,
    var is_public: Boolean = false,
    var revision: Boolean = false,
    var user_uuid: String = ""
)