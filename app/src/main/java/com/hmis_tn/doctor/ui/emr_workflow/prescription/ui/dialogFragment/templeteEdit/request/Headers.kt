package com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.templeteEdit.request

data class Headers(
    var description: String = "",
    var diagnosis_uuid: Int = 0,
    var display_order: Int = 0,
    var facility_uuid: String = "1",
    var is_active: Boolean = true,
    var is_public: String = "true",
    var name: String = "",
    var revision: Boolean = true,
    var template_id: Int = 0,
    var template_type_uuid: Int = 0
)