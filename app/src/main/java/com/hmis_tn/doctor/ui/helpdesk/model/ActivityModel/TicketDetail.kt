package com.hmis_tn.doctor.ui.helpdesk.model.ActivityModel

data class TicketDetail(
    var assignto: String? = null,
    var attachment: Any? = null,
    var close_comments: Any? = null,
    var comments: String? = "",
    var created_by: Int? = null,
    var created_date: String? = null,
    var creator_name: String? = "",
    var is_open: Boolean? = null,
    var login_type: Any? = null,
    var modified_by: Int? = null,
    var modified_date: String? = null,
    var priority: Any? = null,
    var priority_uuid: Int? = null,
    var rating_uuid: Int? = null,
    var revision: Any? = null,
    var status: Boolean? = null,
    var ticketmanagment_uuid: Int? = null,
    var ticketstatus_uuid: Int? = null,
    var uuid: Int? = null
)