package com.hmis_tn.doctor.ui.emr_workflow.mrd.models

data class ToLocation(
    val description: String = "",
    val lab_master_type_uuid: Int = 0,
    val location_code: String = "",
    val location_name: String = "",
    val uuid: Int = 0
)