package com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model

data class AdmissionResponseContent(
    val clinical_type_uuid: Int = 0,
    val code: String = "",
    val created_by: Int = 0,
    val created_date: String = "",
    val department_type_uuid: Any = Any(),
    val end_age: Int = 0,
    val is_active: Boolean = false,
    val is_gender_based: Any = Any(),
    val modified_by: Int = 0,
    val modified_date: String = "",
    val name: String = "",
    val revision: Int = 0,
    val short_code: String = "",
    val start_age: Int = 0,
    val status: Boolean = false,
    val uuid: Int = 0
)