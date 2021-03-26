package com.hmis_tn.doctor.ui.emr_workflow.prescription.ui.dialogFragment.savetemplate.request

data class IsBatchMandatory(
    var `data`: List<Int> = listOf(),
    var type: String = ""
)