package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.response

data class TreatmentKitCreateResponseModel(
    val code: Int? = 0,
    val message: String? = "",
    val reqContents: ReqContents? = ReqContents()
)