package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.request

data class PatientPrescription(
    var details: List<DetailX?>? = listOf(),
    var header: HeaderX? = HeaderX()
)