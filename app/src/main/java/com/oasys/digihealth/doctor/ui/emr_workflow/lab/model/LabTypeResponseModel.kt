package com.oasys.digihealth.doctor.ui.emr_workflow.lab.model

data class LabTypeResponseModel(
    var req: String? = "",
    var responseContents: List<LabTypeResponseContent?>? = listOf(),
    var statusCode: Int? = 0,
    var totalRecords: Int? = 0
)