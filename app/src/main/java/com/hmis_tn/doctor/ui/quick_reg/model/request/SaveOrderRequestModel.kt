package com.hmis_tn.doctor.ui.quick_reg.model.request

data class SaveOrderRequestModel(
    var details: ArrayList<Detail> = ArrayList(),
    var header: Header = Header()
)