package com.hmis_tn.doctor.ui.emr_workflow.investigation.models

data class Headers(
    val department_uuid: String = "",
    val display_order: String = "",
    val facility_uuid: String = "",
    val favourite_type_uuid: Int = 0,
    val is_active: Boolean = false,
    val is_public: Boolean = false,
    val revision: Boolean = false,
    val user_uuid: String = ""
)