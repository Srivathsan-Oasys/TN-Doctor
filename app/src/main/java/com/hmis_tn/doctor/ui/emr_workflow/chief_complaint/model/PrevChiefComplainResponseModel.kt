package com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.model

import com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.model.search_complaint.PrevChiefComplaintResponseContent

data class PrevChiefComplainResponseModel(
    val code: Int = 0,
    val message: String = "",
    val responseContents: List<PrevChiefComplaintResponseContent> = listOf()
)