package com.hmis_tn.doctor.ui.myprofile.model

data class MyProfileResponseModel(
    val req: String? = "",
    val responseContents: Resc? = Resc(),
    val statusCode: Int? = 0
)

