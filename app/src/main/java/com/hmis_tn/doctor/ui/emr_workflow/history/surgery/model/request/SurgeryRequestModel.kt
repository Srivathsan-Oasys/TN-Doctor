package com.hmis_tn.doctor.ui.emr_workflow.history.surgery.model.request

data class SurgeryRequestModel(
    val comments: String? = "",
    val consultation_uuid: Int? = 0,
    val encounter_uuid: Int? = 0,
    val facility_uuid: String? = "",
    val patient_uuid: Int? = 0,
    val performed_date: String? = "",
    val procedure_uuid: String? = "",
    val surgery_description: String? = "",
    val surgery_name: String? = ""
)