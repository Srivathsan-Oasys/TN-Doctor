package com.oasys.digihealth.doctor.ui.helpdesk.model


data class AssetResponseContent(
    val asset_name: String? = "",
    val asset_code: String? = "",
    val uuid: Int? = 0,
    val asset_class_name: String? = "",
    val asset_class_name_uuid: Int? = 0,
    val department_uuid: Int? = 0,
    val facility_uuid: Int? = 0,
    val asset_description: String? = "",
    val asset_category_uuid: Int? = 0,
    val asset_type_uuid: Int? = 0,
    val created_by: String? = "",
    val created_date: String? = "",
    val modified_by: String? = "",
    val modified_date: String? = "",
    val revision: String? = "",
    val is_active: Boolean? = false,
    val status: Boolean? = false,
    val AssetDetails: List<AssetDetailResponseContent?>? = listOf()
)