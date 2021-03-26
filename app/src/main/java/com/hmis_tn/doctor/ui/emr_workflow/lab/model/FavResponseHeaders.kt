package com.hmis_tn.doctor.ui.emr_workflow.lab.model

data class FavResponseHeaders(
    val active_from: String = "",
    val created_by: String = "",
    val created_date: String = "",
    val department_uuid: String = "",
    val display_order: String = "",
    val facility_uuid: String = "",
    val favourite_type_uuid: Int = 0,
    val is_active: Int = 0,
    val is_public: Boolean = false,
    val modified_by: String = "",
    val modified_date: String = "",
    val revision: Int = 0,
    val status: Int = 0,
    val user_uuid: String = "",
    val uuid: Int = 0
)