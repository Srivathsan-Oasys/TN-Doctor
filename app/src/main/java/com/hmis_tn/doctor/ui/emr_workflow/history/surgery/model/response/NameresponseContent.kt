package com.hmis_tn.doctor.ui.emr_workflow.history.surgery.model.response

data class NameresponseContent(
    val anaesthesia_type_uuid: Int? = 0,
    val body_site_uuid: Int? = 0,
    val code: String? = "",
    val created_by: Int? = 0,
    val created_date: String? = "",
    val description: String? = "",
    val duration: Int? = 0,
    val equipment_uuid: Int? = 0,
    val is_active: Boolean? = false,
    val modified_by: Int? = 0,
    val modified_date: String? = "",
    val name: String? = "Select",
    val operation_type_uuid: Int? = 0,
    val procedure_category_uuid: Int? = 0,
    val procedure_region_uuid: Int? = 0,
    val procedure_scheme_uuid: Int? = 0,
    val procedure_sub_category_uuid: Int? = 0,
    val procedure_technique_uuid: Int? = 0,
    val procedure_type_uuid: Int? = 0,
    val procedure_version_uuid: Int? = 0,
    val revision: Int? = 0,
    val speciality_uuid: Int? = 0,
    val status: Boolean? = false,
    val uuid: Int? = 0
)