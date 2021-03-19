package com.oasys.digihealth.doctor.ui.quick_reg.model.labtest.response

import com.oasys.digihealth.doctor.ui.sample_dispatch.model.SampledispatchModel

data class SimpleResponseModel(
  //  var responseContents: ArrayList<SampledispatchModel> = ArrayList(),
    var msg: String = "",
    var message: String = "",
    var statusCode: Int = 0
)