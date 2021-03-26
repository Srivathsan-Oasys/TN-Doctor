package com.hmis_tn.doctor.ui.helpdesk.model


data class DepartmentResponseContent(
    val facility_uuid: Int? = 0,
    val department_uuid: Int? = 0,
    val uuid: Int? = 0,
    val revision: String? = "",
    val status: Boolean? = false,
    val is_active: Boolean? = false,
    val created_by: String? = "",
    val created_date: String? = "",
    val modified_by: String? = "",
    val modified_date: String? = "",
    val department: TicketInstitutionResponseContent? = TicketInstitutionResponseContent()
)