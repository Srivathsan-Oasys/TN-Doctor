package com.hmis_tn.doctor.ui.dashboard.model

data class SalutationresponseContent(
    val Is_default: Boolean? = false,
    val code: String? = "",
    val color: Any? = Any(),
    val created_by: Int? = 0,
    val created_date: String? = "",
    val display_order: Any? = Any(),
    val is_active: Boolean? = false,
    val language_uuid: Int? = 0,
    val modified_by: Int? = 0,
    val modified_date: String? = "",
    var name: String? = "",
    val revision: Int? = 0,
    val type_code: String? = "",
    var uuid: Int? = 0
)