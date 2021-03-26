package com.hmis_tn.doctor.ui.emr_workflow.vitals.model.previous_vitals

data class PV(
    val patient_vital_uuid: Int? = 0,
    val salutaion_name: String? = "",
    val uom_code: Any? = Any(),
    val uom_name: String? = "",
    val uom_uuid: Int? = 0,
    val vital_master_uuid: Int? = 0,
    val vital_name: String? = "",
    val vital_performed_date: String? = "",
    val vital_type_uuid: Int? = 0,
    val vital_value: String? = "",
    val vital_value_type_uuid: Int? = 0
)