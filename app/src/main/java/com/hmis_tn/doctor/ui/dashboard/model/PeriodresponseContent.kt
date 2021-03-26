package com.hmis_tn.doctor.ui.dashboard.model

data class PeriodresponseContent(
    val code: String? = "",
    val created_by: Int? = 0,
    val created_date: String? = "",
    val is_active: Boolean? = false,
    val modified_by: Int? = 0,
    val modified_date: String? = "",
    var name: String? = "",
    var uuid: Int? = 0
)