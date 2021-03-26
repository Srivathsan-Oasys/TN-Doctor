package com.hmis_tn.doctor.ui.helpdesk.model.ActivityModel

data class TicketActivityListReponse(
    var responseContents: TicketActivityList? = null,
    var req: String? = null,
    var statusCode: Int? = null
)