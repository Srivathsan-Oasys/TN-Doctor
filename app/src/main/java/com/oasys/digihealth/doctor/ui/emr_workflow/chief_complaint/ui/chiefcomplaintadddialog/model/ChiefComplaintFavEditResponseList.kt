package com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.chiefcomplaintadddialog.model

data class ChiefComplaintFavEditResponseList(
    var chief_complaint_id: Int = 0,
    var favourite_display_order: String = "",
    var favourite_id: Int = 0,
    var is_active: Boolean = false
)