package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.discharge_model

data class ItemMaster(
    val code: String = "",
    val facility_uuid: Int = 0,
    val is_emar: Boolean = false,
    val name: String = "",
    val strength: String = "",
    val uuid: Int = 0
)