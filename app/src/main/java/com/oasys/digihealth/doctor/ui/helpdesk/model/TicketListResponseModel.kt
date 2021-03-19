package com.oasys.digihealth.doctor.ui.helpdesk.model


data class TicketListResponseModel(
    val responseContents: List<TicketListResponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)