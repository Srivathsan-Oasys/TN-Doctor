package com.oasys.digihealth.doctor.ui.dashboard.model.registration

data class OutComeResponseModel(
    var responseContents: ArrayList<OutCome> = ArrayList(),
    var statusCode: Int = 0
)