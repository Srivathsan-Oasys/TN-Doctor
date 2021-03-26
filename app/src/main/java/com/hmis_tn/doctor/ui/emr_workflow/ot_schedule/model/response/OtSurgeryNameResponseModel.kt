package com.hmis_tn.doctor.ui.emr_workflow.ot_schedule.model.response

data class OtSurgeryNameResponseModel(
    val responseContents: List<OtSurgeryNameresponseContent?>? = listOf(),
    val req: ReqX? = ReqX(),
    val status: String? = "",
    val statusCode: Int? = 0
)