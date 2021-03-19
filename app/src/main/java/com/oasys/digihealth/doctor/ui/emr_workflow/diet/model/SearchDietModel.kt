package com.oasys.digihealth.doctor.ui.emr_workflow.diet.model

data class SearchDietResponse(
    val message: String? = null,
    val responseContents: ArrayList<DietResponseResponseContent?>? = null,
    val statusCode: Int? = null,
    val totalRecords: Int? = null
)

data class DietResponseResponseContent(
    val code: String? = null,
    val created_by: Int? = null,
    val created_date: String? = null,
    val diet_category_uuid: Int? = null,
    val diet_frequency_uuid: Int? = null,
    val diet_type_uuid: Int? = null,
    val is_active: Boolean? = null,
    val modified_by: Int? = null,
    val modified_date: String? = null,
    val name: String? = null,
    val regional_name: String? = null,
    val revision: Int? = null,
    val status: Boolean? = null,
    val uuid: Int? = null
)


