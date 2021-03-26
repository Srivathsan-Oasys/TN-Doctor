package com.hmis_tn.doctor.ui.emr_workflow.history.prescription.model

data class Req(
    val encounter_type_uuid: Int = 0,
    val patient_uuid: String = ""
)