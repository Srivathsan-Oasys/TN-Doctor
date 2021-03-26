package com.hmis_tn.doctor.ui.emr_workflow.vitals.model

data class Uom(
    val code: String = "",
    val created_by: Int = 0,
    val created_date: String = "",
    val is_active: Boolean = false,
    val modified_by: Int = 0,
    val modified_date: String = "",
    val name: String = "",
    val revision: Int = 0,
    val status: Boolean = false,
    val uom_type: UomType = UomType(),
    val uom_type_uuid: Int = 0,
    val uuid: Int = 0
)