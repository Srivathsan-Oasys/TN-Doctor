package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.dialogFragment.savetemplate.request

data class Headers(
    var department_uuid: String = "",
    var description: String = "",
    var diagnosis_uuid: Int = 0,
    var display_order: String = "",
    var facility_uuid: String = "",
    var is_active: Boolean = true,
    var is_public: String = "true",
    var name: String = "",
    var revision: Boolean = true,
    var template_type_uuid: Int = 1,
    var user_uuid: String = ""
)

