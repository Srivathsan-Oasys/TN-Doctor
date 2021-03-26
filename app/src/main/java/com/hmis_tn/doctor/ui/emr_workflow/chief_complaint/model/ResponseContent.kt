package com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.model

data class ResponseContent(
    val chiefComplaintCode: String = "",
    val chiefComplaintDuration: Int = 0,
    val chiefComplaintDurationName: String = "",
    val chiefComplaintName: String = "",
    val patientCCId: Int = 0
)