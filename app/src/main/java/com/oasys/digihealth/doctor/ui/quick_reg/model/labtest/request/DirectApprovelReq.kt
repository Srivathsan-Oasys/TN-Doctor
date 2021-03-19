package com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.request

import com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.request.orderRequest.Header

data class DirectApprovelReq(
    var details: ArrayList<Header> = ArrayList()
)