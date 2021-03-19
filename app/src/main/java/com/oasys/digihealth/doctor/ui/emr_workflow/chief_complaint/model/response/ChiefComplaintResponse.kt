package com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.model.response

data class ChiefComplaintResponse(
    val responseContents: List<ChiefComplaintResponseContent> = listOf(),
    val code: Int = 0,
    val message: String = ""
)