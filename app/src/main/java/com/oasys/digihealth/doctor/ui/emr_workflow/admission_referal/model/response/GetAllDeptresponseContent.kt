package com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.response

data class GetAllDeptresponseContent(
    val clinical_type_uuid: Int? = 0,
    val code: String? = "",
    val created_by: Int? = 0,
    val created_date: String? = "",
    val department_type_uuid: Any? = Any(),
    val end_age: Any? = Any(),
    val is_active: Boolean? = false,
    val is_gender_based: Any? = Any(),
    val is_speciality: Any? = Any(),
    val modified_by: Int? = 0,
    val modified_date: String? = "",
    val name: String? = "",
    val parent_department_uuid: Int? = 0,
    val revision: Int? = 0,
    val short_code: String? = "",
    val start_age: Any? = Any(),
    val status: Boolean? = false,
    val uuid: Int? = 0
)