package com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.favouriteEdit.requestparamter

data class Headers(
    var department_uuid: String? = "",
    var display_order: String? = "",
    var facility_uuid: String? = "",
    var favourite_type_uuid: Int? = 0,
    var is_active: String? = "",
    var is_public: Boolean? = false,
    var revision: Boolean? = false,
    var ticksheet_type_uuid: Int? = 0,
    var user_uuid: String? = ""
)