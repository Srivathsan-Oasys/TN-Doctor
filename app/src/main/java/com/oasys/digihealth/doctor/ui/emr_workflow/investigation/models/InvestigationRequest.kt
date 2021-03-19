package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.models

data class InvestigationRequest(
    val details: List<Detail> = listOf(),
    val headers: Headers = Headers()
)