package com.hmis_tn.doctor.ui.dashboard.model

data class ChangePasswordOTPResponseModel(
    val msg: String? = "",
    val responseContents: Otp? = Otp(),
    val status: String? = ""
)