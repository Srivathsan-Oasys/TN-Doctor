package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.discharge_model

data class TestMasterX(
    val code: String = "",
    val department_uuid: Int = 0,
    val description: String = "",
    val is_active: Boolean = false,
    val name: String = "",
    val sample_type_uuid: Int? = null,
    val sub_department_uuid: Int = 0,
    val uuid: Int = 0,
    val value_type_uuid: Int = 0
)