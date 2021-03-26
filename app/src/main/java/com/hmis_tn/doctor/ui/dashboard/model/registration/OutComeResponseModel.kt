package com.hmis_tn.doctor.ui.dashboard.model.registration

data class OutComeResponseModel(
    var responseContents: ArrayList<OutCome> = ArrayList(),
    var statusCode: Int = 0
)