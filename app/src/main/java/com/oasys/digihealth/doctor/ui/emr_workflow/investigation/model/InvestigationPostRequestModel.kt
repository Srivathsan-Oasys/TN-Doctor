package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model

data class InvestigationPostRequestModel(
    var details: List<DetailXX> = listOf(),
    var header: Header = Header()
)