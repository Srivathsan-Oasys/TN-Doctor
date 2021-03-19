package com.oasys.digihealth.doctor.ui.emr_workflow.investigation.model

data class InvestigationTemplateResponse(
    val message: String = "",
    val responseContents: InvestigationTemplateResponseContents = InvestigationTemplateResponseContents(),
    val statusCode: Int = 0
)