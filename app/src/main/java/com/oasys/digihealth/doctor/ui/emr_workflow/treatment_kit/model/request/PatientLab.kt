package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.request

data class PatientLab(
    var details: List<Detail?>? = listOf(),
    var header: Header? = Header()
)