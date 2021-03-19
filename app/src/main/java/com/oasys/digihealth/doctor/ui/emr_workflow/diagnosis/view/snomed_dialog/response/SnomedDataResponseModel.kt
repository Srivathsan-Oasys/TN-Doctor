package com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog.response

data class SnomedDataResponseModel(
    var Snomed_data: ArrayList<SnomedData> = ArrayList(),
    var code: Int = 0,
    var message: String = ""
)