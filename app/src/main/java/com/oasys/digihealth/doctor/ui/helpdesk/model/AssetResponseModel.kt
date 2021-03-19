package com.oasys.digihealth.doctor.ui.helpdesk.model


data class AssetResponseModel(
    val responseContents: List<AssetResponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0
)