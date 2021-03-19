package com.oasys.digihealth.doctor.ui.helpdesk.model


data class VendorListResponseModel(
    val responseContents: List<VendorResponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0
)