package com.hmis_tn.doctor.ui.emr_workflow.prescription.model

data class ResponsePresContents(
    var details: List<DetailX> = listOf(),
    var headers: Headers? = Headers()
)