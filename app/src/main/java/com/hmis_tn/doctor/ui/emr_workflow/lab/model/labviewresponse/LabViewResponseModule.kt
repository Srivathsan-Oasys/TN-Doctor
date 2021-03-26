package com.hmis_tn.doctor.ui.emr_workflow.lab.model.labviewresponse

data class LabViewResponseModule(
    var req: String? = "",
    var responseContents: List<ResponseContentsLabView?>? = listOf(),
    var statusCode: Int? = 0
)