package com.hmis_tn.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog.response

data class SnomedChildDataResponseModel(
    var Snomed_Children_data: ArrayList<SnomedData> = ArrayList(),
    var code: Int = 0,
    var message: String = ""
)