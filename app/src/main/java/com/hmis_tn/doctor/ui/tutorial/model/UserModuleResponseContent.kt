package com.hmis_tn.doctor.ui.tutorial.model

data class UserModuleResponseContent(
    val uuid: Int? = 0,
    val module_uuid: Int? = 0,
    val activity_uuid: Int? = 0,
    val tutorial_type_uuid: Int? = 0,
    val title_uuid: String? = "",
    val tutorial_url: String? = "",
    val thumbnail_url: String? = "",
    val display_order: Int? = 0,
    val view_count: Int? = 0,
    val download_count: Int? = 0,
    val is_active: Boolean? = true,
    val revision: Boolean? = true,
    val status: Boolean? = true,
    val created_by: Int? = 0,
    val modified_by: Int? = 0,
    val created_date: String? = "",
    val activity: Activity? = null,
    val modified_date: String? = ""

)


data class Activity(
    val name: String? = null,
    val uuid: Int? = null

)