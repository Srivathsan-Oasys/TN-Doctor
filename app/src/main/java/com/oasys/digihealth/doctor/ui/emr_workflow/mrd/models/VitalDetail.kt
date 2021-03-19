package com.oasys.digihealth.doctor.ui.emr_workflow.mrd.models

import com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.discharge_model.PV

data class VitalDetail(
    val PV_list: List<PV> = listOf(),
    val created_date: String = "",
    val department_name: String = "",
    val doctor_firstname: String = "",
    val doctor_lastlename: Any = Any(),
    val doctor_middlename: Any = Any(),
    val doctor_title: String = "",
    val doctor_uuid: Int = 0,
    val encounter_type_code: String = "",
    val encounter_type_name: String = "",
    val encounter_type_uuid: Int = 0,
    val encounter_uuid: Int = 0,
    val institution_name: String = "",
    val institution_uuid: Int = 0,
    val patient_uuid: Int = 0,
    val patient_vital_uuid: Int = 0
)