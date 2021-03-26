package com.hmis_tn.doctor.ui.emr_workflow.radiology.model

data class ManageRadiologyTemplateResponseContent(
    var details: List<DetailX> = listOf(),
    var headers: HeadersX = HeadersX()
)