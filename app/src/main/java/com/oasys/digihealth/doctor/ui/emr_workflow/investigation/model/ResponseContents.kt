package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model

data class ResponseContents(
    val created_by: Int = 0,
    val created_date: String = "",
    val facility_uuid: Int = 0,
    val from_department_uuid: Int = 0,
    val is_active: Boolean = false,
    val modified_by: Int = 0,
    val modified_date: String = "",
    val profile_uuid: Any = Any(),
    val revision: Any = Any(),
    val status: Boolean = false,
    val test_master_uuid: Int = 0,
    val to_department_uuid: Int = 0,
    val to_location_uuid: Int = 0,
    val to_subdepartment_uuid: Int = 0,
    val uuid: Int = 0,
    val ward_uuid: Any = Any()
)