package com.hmis_tn.doctor.ui.emr_workflow.history.surgery.model.response

data class CreateSurgeryResponseModel(
    val code: Int? = 0,
    val message: String? = "",
    val responseContents: SurgeryCreateresponseContents? = SurgeryCreateresponseContents()
)