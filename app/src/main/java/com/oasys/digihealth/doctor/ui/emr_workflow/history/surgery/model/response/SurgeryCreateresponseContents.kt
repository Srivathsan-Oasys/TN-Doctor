package com.oasys.digihealth.doctor.ui.emr_workflow.history.surgery.model.response

data class SurgeryCreateresponseContents(
    val comments: String? = "",
    val consultation_uuid: Int? = 0,
    val created_by: String? = "",
    val created_date: String? = "",
    val encounter_uuid: Int? = 0,
    val facility_uuid: String? = "",
    val is_active: Boolean? = false,
    val modified_by: String? = "",
    val modified_date: String? = "",
    val patient_uuid: Int? = 0,
    val performed_by: String? = "",
    val performed_date: String? = "",
    val procedure_uuid: String? = "",
    val revision: Int? = 0,
    val status: Boolean? = false,
    val surgery_description: String? = "",
    val surgery_name: String? = ""
)