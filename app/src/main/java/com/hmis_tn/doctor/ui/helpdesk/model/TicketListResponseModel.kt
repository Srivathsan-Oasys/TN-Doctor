package com.hmis_tn.doctor.ui.helpdesk.model


data class TicketListResponseModel(
    val responseContents: List<TicketListResponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)