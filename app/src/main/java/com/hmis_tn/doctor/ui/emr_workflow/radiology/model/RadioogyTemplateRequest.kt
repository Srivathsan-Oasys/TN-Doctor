package com.hmis_tn.doctor.ui.emr_workflow.radiology.model

data class RadioogyTemplateRequest(
    var details: List<DetailXX> = listOf(),
    var headers: HeadersXX = HeadersXX()
)