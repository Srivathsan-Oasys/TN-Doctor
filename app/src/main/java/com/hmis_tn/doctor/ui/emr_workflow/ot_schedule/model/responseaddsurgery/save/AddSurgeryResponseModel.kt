package com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery.save

data class AddSurgeryResponseModel(
    val msg: String? = "",
    val responseContents: ResponseContents? = ResponseContents(),
    val status: String? = "",
    val statusCode: Int? = 0
)