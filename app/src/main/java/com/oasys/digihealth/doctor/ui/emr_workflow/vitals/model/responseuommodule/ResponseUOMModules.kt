package com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.responseuommodule

data class ResponseUOMModules(
    var req: String? = "",
    var responseContents: List<ResponseContentsUOM?>? = listOf(),
    var statusCode: Int? = 0,
    var totalRecords: Int? = 0
)