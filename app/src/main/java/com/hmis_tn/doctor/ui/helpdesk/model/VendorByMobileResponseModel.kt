package com.hmis_tn.doctor.ui.helpdesk.model


data class VendorByMobileResponseModel(
    val responseContents: VendorResponseContent? = null,
    val message: String? = "",
    val msg: String? = "",
    val statusCode: Int? = 0
)