package com.hmis_tn.doctor.ui.quick_reg.model.labtest.request

data class RejectRequestModel(
    var Id: ArrayList<Int> = ArrayList(),
    var reject_category_uuid: String = "",
    var reject_reason: String = ""
)