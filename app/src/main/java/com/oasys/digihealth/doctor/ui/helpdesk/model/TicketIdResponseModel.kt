package com.oasys.digihealth.doctor.ui.helpdesk.model


data class TicketIdResponseModel(
    val responseContents: TicketListResponseContent? = null,
    val message: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)