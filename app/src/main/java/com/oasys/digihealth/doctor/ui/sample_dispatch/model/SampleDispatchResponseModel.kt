package com.oasys.digihealth.doctor.ui.sample_dispatch.model

data class SampleDispatchResponseModel(
    var responseContents: ArrayList<SampledispatchModel> = ArrayList(),
    var message: String = "",
    var statusCode: Int = 0
)