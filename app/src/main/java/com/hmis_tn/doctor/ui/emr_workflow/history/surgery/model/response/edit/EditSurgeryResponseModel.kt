package com.hmis_tn.doctor.ui.emr_workflow.history.surgery.model.response.edit

data class EditSurgeryResponseModel(
    val responseContent: EditSurgeryresponseContent? = EditSurgeryresponseContent(),
    val code: Int? = 0
)