package com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.previous_vitals

data class GetPrevPatientVitalResp(
    val code: Int? = 0,
    val message: String? = "",
    val responseContents: ResponseContents? = ResponseContents()
)