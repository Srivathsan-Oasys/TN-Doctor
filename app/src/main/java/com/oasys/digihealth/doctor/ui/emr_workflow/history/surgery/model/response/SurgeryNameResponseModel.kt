package com.oasys.digihealth.doctor.ui.emr_workflow.history.surgery.model.response

data class SurgeryNameResponseModel(
    val responseContents: List<NameresponseContent?>? = listOf(),
    val req: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)