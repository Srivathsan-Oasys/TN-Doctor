package com.hmis_tn.doctor.ui.quick_reg.model

data class BlockZoneResponseModel(
    var responseContents: List<BlockZone> = listOf(),
    var count: Int = 0,
    var statusCode: Int = 0
)