package com.hmis_tn.doctor.ui.login.model

data class Req(
    val otp: String? = "",
    val password: String? = "",
    val username: String? = ""
)