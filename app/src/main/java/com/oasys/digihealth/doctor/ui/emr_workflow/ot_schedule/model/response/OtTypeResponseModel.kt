package com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.response

data class OtTypeResponseModel(
    val responseContents: List<OtTyperesponseContent?>? = listOf(),
    val req: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)