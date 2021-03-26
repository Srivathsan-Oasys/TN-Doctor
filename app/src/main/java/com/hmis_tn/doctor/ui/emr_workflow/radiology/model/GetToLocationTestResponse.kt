package com.hmis_tn.doctor.ui.emr_workflow.radiology.model

data class GetToLocationTestResponse(
    var responseContents: ToLocation = ToLocation(),
    var message: String = "",
    var statusCode: Int = 0,
    var totalRecords: Int = 0
)