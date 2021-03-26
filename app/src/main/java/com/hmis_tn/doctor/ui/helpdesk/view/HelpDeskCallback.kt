package com.hmis_tn.doctor.ui.helpdesk.view

import com.hmis_tn.doctor.ui.helpdesk.model.TicketListResponseContent

interface HelpDeskCallback {

    fun OnDeleteClick(model: TicketListResponseContent?)
    fun OnViewClick(model: TicketListResponseContent?)
}