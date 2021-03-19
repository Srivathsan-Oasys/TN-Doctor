package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model

data class Headers(
    var department_uuid: String? = "",
    var display_order: String? = "",
    var facility_uuid: String? = "",
    var favourite_type_uuid: Int? = 0,
    var is_active: Boolean? = false,
    var is_public: Boolean? = false,
    var revision: Boolean? = false,
    var user_uuid: String? = ""
)