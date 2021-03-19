package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model

data class PrescriptionSearchResponseModel(
    var responseContents: List<PrescriptionSearchResponseContent?>? = listOf(),
    var message: String? = "",
    var req: Req? = Req(),
    var status: Int? = 0,
    var statusCode: Int? = 0,
    var totalRecords: Int? = 0
)