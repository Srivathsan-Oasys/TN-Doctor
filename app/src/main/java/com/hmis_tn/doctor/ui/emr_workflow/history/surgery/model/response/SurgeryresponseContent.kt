package com.hmis_tn.doctor.ui.emr_workflow.history.surgery.model.response

data class SurgeryresponseContent(
    val institution_name: String? = "",
    val institution_uuid: Int? = 0,
    val procedure_name: String? = "",
    val ps_comments: String? = "",
    val ps_patient_uuid: Int? = 0,
    val ps_performed_date: String? = "",
    val ps_uuid: Int? = 0
)