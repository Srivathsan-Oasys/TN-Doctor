package com.oasys.digihealth.doctor.ui.helpdesk.model


data class TicketCountResponseModel(
    val responseContents: List<TicketCountResponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)