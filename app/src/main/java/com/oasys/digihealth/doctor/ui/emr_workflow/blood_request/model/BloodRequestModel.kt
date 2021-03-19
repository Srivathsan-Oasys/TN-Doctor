package com.oasys.digihealth.doctor.ui.emr_workflow.blood_request.model

data class BloodRequestModel(
    var uuid: Int,
    var isChecked: Boolean,
    var hint: String
) {
    var text: String = ""
}