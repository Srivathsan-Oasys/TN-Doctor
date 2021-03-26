package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.request

data class TreatmentKit(
    var activefrom: String? = "",
    var code: String? = "",
    var department_uuid: String? = "",
    var facility_uuid: String? = "",
    var is_active: String? = "",
    var is_public: String? = "",
    var name: String? = "",
    var treatment_kit_type_uuid: Int? = 0,
    var user_uuid: String? = ""
)