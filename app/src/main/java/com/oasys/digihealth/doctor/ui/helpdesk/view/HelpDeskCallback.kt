package com.oasys.digihealth.doctor.ui.helpdesk.view

import com.oasys.digihealth.doctor.ui.helpdesk.model.TicketListResponseContent

interface HelpDeskCallback {

    fun OnDeleteClick(model: TicketListResponseContent?)
    fun OnViewClick(model: TicketListResponseContent?)
}