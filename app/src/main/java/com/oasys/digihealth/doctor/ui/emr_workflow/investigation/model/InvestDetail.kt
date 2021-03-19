package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model

data class InvestDetail(
    val lab_code: String = "",
    val lab_name: String = "",
    val lab_test_description: String = "",
    val lab_test_is_active: Boolean = false,
    val lab_test_status: Boolean = false,
    var ischeck: Boolean = false,
    val lab_test_uuid: Int = 0,
    val lab_type_uuid: Int = 0,
    val profile_test_active: Any = Any(),
    val profile_test_code: Any = Any(),
    val profile_test_description: Any = Any(),
    val profile_test_name: Any = Any(),
    val profile_test_status: Any = Any(),
    val profile_test_uuid: Any = Any(),
    val template_details_displayorder: Int = 0,
    val template_details_uuid: Int = 0
)