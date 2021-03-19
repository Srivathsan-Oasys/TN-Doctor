package com.oasys.digihealth.doctor.ui.helpdesk.model


data class CategoryResponseContent(
    val code: String? = "",
    val name: String? = "",
    val uuid: Int? = 0,
    val status: Boolean? = false,
    val display_order: Int? = 0,
    val is_active: Boolean? = false,
    val color: String? = "",
    val is_default: String? = "",
    val revision: Boolean? = false,
    val created_date: String? = "",
    val modified_date: String? = "",
    val created_by: Int? = 0,
    val modified_by: Int? = 0
)