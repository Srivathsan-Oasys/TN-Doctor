package com.oasys.digihealth.doctor.ui.quick_reg.model.request

data class SaveOrderRequestModel(
    var details: ArrayList<Detail> = ArrayList(),
    var header: Header = Header()
)