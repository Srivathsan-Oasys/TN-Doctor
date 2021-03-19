package com.oasys.digihealth.doctor.ui.helpdesk.model


data class TicketInstitutionResponseModel(
    val responseContents: TicketInstitutionResponseContent? = null,
    val msg: String? = "",
    val statusCode: Int? = 0
)