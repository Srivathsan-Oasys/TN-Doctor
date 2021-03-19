package com.oasys.digihealth.doctor.ui.helpdesk.model


data class TicketCountResponseContent(
    val count: Int? = 0,
    val ticket_status_name: String? = "",
    val ticketstatus_uuid: Int? = 0
)