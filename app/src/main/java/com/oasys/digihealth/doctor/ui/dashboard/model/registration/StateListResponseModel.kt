package com.oasys.digihealth.doctor.ui.dashboard.model.registration

data class StateListResponseModel(
    var req: String = "",
    var responseContents: ArrayList<State> = ArrayList(),
    var statusCode: Int = 0
)