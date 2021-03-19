package com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.model.response

data class Name(
    val allergey_code: String? = "",
    val allergy_description: String? = "",
    val allergy_name: String? = "Select",
    val allergy_severity: Any? = Any(),
    val allergy_severity_uuid: Int? = 0,
    val allergy_source: Any? = Any(),
    val allergy_source_uuid: Int? = 0,
    val allergy_type_uuid: Int? = 0,
    val created_by: Int? = 0,
    val created_date: String? = "",
    val is_active: Boolean? = false,
    val modified_by: Int? = 0,
    val modified_date: String? = "",
    val revision: Int? = 0,
    val uuid: Int? = 0
)