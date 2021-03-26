package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.discharge_model

data class PV(
    val patient_vital_uuid: Int = 0,
    val uom_code: String = "",
    val uom_name: String = "",
    val vital_master_uuid: Int = 0,
    val vital_name: String = "",
    val vital_performed_date: String = "",
    val vital_type_uuid: Int = 0,
    val vital_value: String = "",
    val vital_value_type_uuid: Int = 0
)