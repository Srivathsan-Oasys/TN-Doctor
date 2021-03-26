package com.hmis_tn.doctor.ui.emr_workflow.chief_complaint.model.duration

data class DurationResponseModel(
    val code: Int? = null,
    val message: String? = null,
    val responseContents: ArrayList<DurationResponseContent?>? = null
)