package com.hmis_tn.doctor.ui.emr_workflow.investigation.model

data class InvestigationGetTemplateDetailsResponse(
    val req: String = "",
    val responseContent: InvestigationGetTemplateDetailsResponseContent = InvestigationGetTemplateDetailsResponseContent(),
    val statusCode: Int = 0
)