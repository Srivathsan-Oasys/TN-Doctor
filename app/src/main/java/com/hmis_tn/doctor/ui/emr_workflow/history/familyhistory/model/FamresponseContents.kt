package com.hmis_tn.doctor.ui.emr_workflow.history.familyhistory.model

data class responseContents(
    val consultation_uuid: Int? = 0,
    val created_by: String? = "",
    val created_date: String? = "",
    val disease_name: String? = "",
    val duration: String? = "",
    val encounter_uuid: Int? = 0,
    val identified_date: String? = "",
    val is_active: Boolean? = false,
    val modified_by: String? = "",
    val modified_date: String? = "",
    val patient_uuid: String? = "",
    val period_uuid: String? = "",
    val relation_type_uuid: String? = "",
    val revision: Int? = 0,
    val status: Boolean? = false
)