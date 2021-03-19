package com.oasys.digihealth.doctor.ui.quick_reg.model

data class MapListResponseModel(
    var responseContents: MapList = MapList(),
    var message: String = "",
    var statusCode: Int = 0,
    var totalRecords: Int = 0
)