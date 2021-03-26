package com.hmis_tn.doctor.ui.helpdesk.model


data class TicketIdResponseModel(
    val responseContents: TicketListResponseContent? = null,
    val message: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)