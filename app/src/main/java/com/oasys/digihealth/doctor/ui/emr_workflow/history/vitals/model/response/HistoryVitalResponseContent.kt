package com.oasys.digihealth.doctor.ui.emr_workflow.history.vitals.model.response

data class HistoryVitalResponseContent(
    val doctor_uuid: Int? = 0,
    val patient_uuid: Int? = 0,
    val patient_vital_uuid: Int? = 0,
    val uom_code: String? = "",
    val uom_name: String? = "",
    val vital_master_uuid: Int? = 0,
    val vital_name: String? = "",
    val vital_performed_date: String? = "",
    val vital_type_uuid: Int? = 0,
    val vital_value: String? = "",
    val vital_value_type_uuid: Int? = 0
)