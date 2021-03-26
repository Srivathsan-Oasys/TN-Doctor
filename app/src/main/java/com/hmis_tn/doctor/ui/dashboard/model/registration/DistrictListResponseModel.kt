package com.hmis_tn.doctor.ui.dashboard.model.registration

data class DistrictListResponseModel(
    var responseContents: ArrayList<District> = ArrayList(),
    var req: String = "",
    var statusCode: Int = 0
)