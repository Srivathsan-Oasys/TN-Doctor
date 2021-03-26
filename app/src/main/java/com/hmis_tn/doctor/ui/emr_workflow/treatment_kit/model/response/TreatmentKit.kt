package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.response

data class TreatmentKit(
    val activefrom: String? = "",
    val code: String? = "",
    val created_by: String? = "",
    val created_date: String? = "",
    val department_uuid: String? = "",
    val facility_uuid: String? = "",
    val is_active: Boolean? = false,
    val is_public: String? = "",
    val modified_by: String? = "",
    val modified_date: String? = "",
    val name: String? = "",
    val revision: Int? = 0,
    val status: Int? = 0,
    val treatment_kit_type_uuid: Int? = 0,
    val user_uuid: String? = ""
)