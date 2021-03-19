package com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.response

data class LabTestSpinnerresponseContent(
    val code: String? = "",
    val confidential_uuid: Int? = 0,
    val department_uuid: Int? = 0,
    val is_active: Boolean? = false,
    val is_approval_requried: Boolean? = false,
    val is_confidential: Boolean? = false,
    val name: String? = "Test Name",
    val sample_type_code: String? = "",
    val sample_type_name: String? = "",
    val sample_type_uuid: Int? = 0,
    val status: Boolean? = false,
    val sub_department_uuid: Int? = 0,
    val test_disease_name: String? = "",
    val test_disease_uuid: Int? = 0,
    val test_diseases_uuid: Int? = 0,
    val type: String? = "",
    val type_of_method_code: String? = "",
    val type_of_method_name: String? = "",
    val type_of_method_uuid: Int? = 0,
    val uuid: Int? = 0
)