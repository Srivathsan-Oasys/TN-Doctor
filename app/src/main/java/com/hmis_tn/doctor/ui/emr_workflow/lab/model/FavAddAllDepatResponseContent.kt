package com.hmis_tn.doctor.ui.emr_workflow.lab.model

data class FavAddAllDepatResponseContent(
    val a_uuid: Int = 0,
    val code: String = "",
    val end_age: Int = 0,
    val is_speciality: Any = 0,
    val is_active: Int = 0,
    val is_gender_based: Int = 0,
    var name: String = "",
    val short_code: String = "",
    val start_age: Int = 0,
    val status: Int = 0,
    var uuid: Int = 0
)