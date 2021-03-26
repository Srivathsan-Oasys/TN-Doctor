package com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.model.search_complaint

data class ComplaintSearchResponseModel(
    val code: Int? = null,
    val message: String? = null,
    val responseContents: ArrayList<ResponseContent> = ArrayList()
)