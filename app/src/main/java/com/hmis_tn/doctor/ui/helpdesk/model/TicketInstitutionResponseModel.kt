package com.hmis_tn.doctor.ui.helpdesk.model


data class TicketInstitutionResponseModel(
    val responseContents: TicketInstitutionResponseContent? = null,
    val msg: String? = "",
    val statusCode: Int? = 0
)