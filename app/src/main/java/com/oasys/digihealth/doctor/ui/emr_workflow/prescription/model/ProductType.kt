package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model

data class ProductType(
    val code: String? = "",
    val created_by: Int? = 0,
    val created_date: String? = "",
    val description: String? = "",
    val is_active: Boolean? = false,
    val item_category_uuid: Int? = 0,
    val item_sub_category_uuid: Int? = 0,
    val modified_by: Int? = 0,
    val modified_date: String? = "",
    val name: String? = "",
    val revision: Int? = 0,
    val status: Boolean? = false,
    val uuid: Int? = 0
)