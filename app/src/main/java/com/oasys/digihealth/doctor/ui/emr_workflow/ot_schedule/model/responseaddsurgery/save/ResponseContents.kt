package com.oasys.digihealth.doctor.ui.emr_workflow.ot_schedule.model.responseaddsurgery.save

data class ResponseContents(
    val msg: String? = "",
    val req: Req? = Req(),
    val responseContents: ResponseContentsX? = ResponseContentsX(),
    val status: String? = "",
    val statusCode: Int? = 0
)