package com.hmis_tn.doctor.ui.helpdesk.model


data class VendorListResponseModel(
    val responseContents: List<VendorResponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0
)