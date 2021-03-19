package com.oasys.digihealth.doctor.ui.emr_workflow.documents.model

data class AttachmentData(
    val attached_date: String = "",
    val attachment_name: String = "",
    val attachment_type_uuid: String = "",
    val comments: String = "",
    val consultation_uuid: String = "",
    val created_by: String = "",
    val created_date: String = "",
    val encounter_uuid: String = "",
    val file_path: String = "",
    val folder_name: String = "",
    val is_active: Boolean = false,
    val modified_by: String = "",
    val modified_date: String = "",
    val patient_uuid: String = "",
    val revision: Int = 0,
    val status: Boolean = false
)