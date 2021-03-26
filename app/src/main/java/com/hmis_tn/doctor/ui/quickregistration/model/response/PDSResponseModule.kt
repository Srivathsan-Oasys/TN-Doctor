package com.hmis_tn.doctor.ui.quickregistration.model.response

data class PDSResponseModule(
    var message: String? = "",
    var pdsResponseContents: List<PdsResponseContent?>? = listOf(),
    var statusCode: Int? = 0,
    var totalRecords: Int? = 0
)