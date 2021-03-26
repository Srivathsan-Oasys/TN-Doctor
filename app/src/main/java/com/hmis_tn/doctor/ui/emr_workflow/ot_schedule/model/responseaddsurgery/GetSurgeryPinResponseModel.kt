package com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery

data class GetSurgeryPinResponseModel(
    val responseContents: List<GetSurgeryPinresponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)