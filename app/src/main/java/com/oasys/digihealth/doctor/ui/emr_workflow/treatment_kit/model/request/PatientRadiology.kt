package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.request

data class PatientRadiology(
    var details: List<DetailXX?>? = listOf(),
    var header: HeaderXX? = HeaderXX()
)