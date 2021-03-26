package com.hmis_tn.doctor.ui.login.model

data class ChangePasswordOTPResponseModel(
    val msg: String? = "",
    val responseContents: Otp? = Otp(),
    val status: String? = ""
)