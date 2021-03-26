package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.investigation_code_search

data class ResponseContent(
    val code: String? = "",
    val confidential_uuid: Any? = Any(),
    val department_uuid: Int? = 0,
    val is_active: Boolean? = false,
    val is_approval_requried: Boolean? = false,
    val is_confidential: Boolean? = false,
    val name: String? = "",
    val sample_type_code: Any? = Any(),
    val sample_type_name: Any? = Any(),
    val sample_type_uuid: Int? = 0,
    val status: Boolean? = false,
    val sub_department_uuid: Int? = 0,
    val test_disease_name: Any? = Any(),
    val test_disease_uuid: Any? = Any(),
    val test_diseases_uuid: Int? = 0,
    val type: String? = "",
    val type_of_method_code: String? = "",
    val type_of_method_name: String? = "",
    val type_of_method_uuid: Int? = 0,
    val uuid: Int? = 0
)