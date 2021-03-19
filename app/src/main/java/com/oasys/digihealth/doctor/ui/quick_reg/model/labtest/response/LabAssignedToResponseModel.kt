package com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.response

data class LabAssignedToResponseModel(
    val responseContents: List<LabAssignedToresponseContent?>? = listOf(),
    val req: String? = "",
    val status: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)