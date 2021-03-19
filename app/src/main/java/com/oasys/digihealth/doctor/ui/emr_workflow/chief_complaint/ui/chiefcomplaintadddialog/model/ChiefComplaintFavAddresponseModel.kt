package com.oasys.digihealth.doctor.ui.emr_workflow.chief_complaint.ui.chiefcomplaintadddialog.model

data class ChiefComplaintFavAddresponseModel(
    val responseContents: ChiefComplaintFavAddList = ChiefComplaintFavAddList(),
    val code: Int = 0,
    val message: String = ""
)