package com.hmis_tn.doctor.ui.emr_workflow.history.allergy.model.response

data class Severity(
    val code: String? = "",
    val created_by: Int? = 0,
    val created_date: String? = "",
    val is_active: Boolean? = false,
    val modified_by: Int? = 0,
    val modified_date: String? = "",
    val name: String? = "Select",
    val revision: Int? = 0,
    val status: Boolean? = false,
    val uuid: Int? = 0
)