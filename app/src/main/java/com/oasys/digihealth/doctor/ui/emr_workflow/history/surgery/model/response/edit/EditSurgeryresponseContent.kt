package com.oasys.digihealth.doctor.ui.emr_workflow.history.surgery.model.response.edit

data class EditSurgeryresponseContent(
    val comments: String? = "",
    val consultation_uuid: Int? = 0,
    val created_by: Int? = 0,
    val created_date: String? = "",
    val encounter_uuid: Int? = 0,
    val facility_uuid: Int? = 0,
    val is_active: Boolean? = false,
    val modified_by: Int? = 0,
    val modified_date: String? = "",
    val patient_uuid: Int? = 0,
    val performed_by: Int? = 0,
    val performed_date: String? = "",
    val procedure_uuid: Int? = 0,
    val revision: Int? = 0,
    val status: Boolean? = false,
    val surgery_description: String? = "",
    val surgery_name: String? = "",
    val surgery_type_uuid: Int? = 0,
    val uuid: Int? = 0
)