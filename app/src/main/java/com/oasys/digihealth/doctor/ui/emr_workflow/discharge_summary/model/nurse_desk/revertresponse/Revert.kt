package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.nurse_desk.revertresponse

data class Revert(
    var current_user_type: String = "",
    var generated_type_code: String = "",
    var message: String = "",
    var status: Boolean = false
)