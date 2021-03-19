package com.oasys.digihealth.doctor.ui.emr_workflow.diagnosis.view.snomed_dialog.response

data class SnomedParentDataResponseModel(
    var Snomed_Parent_data: ArrayList<SnomedData> = ArrayList(),
    var code: Int = 0,
    var message: String = ""
)