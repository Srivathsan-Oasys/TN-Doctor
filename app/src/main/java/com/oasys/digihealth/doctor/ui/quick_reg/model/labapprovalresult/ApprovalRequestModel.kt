package com.oasys.digihealth.doctor.ui.quick_reg.model.labapprovalresult

data class ApprovalRequestModel(
    var Id: List<Int> = listOf(),
    var auth_status_uuid: Int = 0,
    var details: ArrayList<Row> = ArrayList()
)