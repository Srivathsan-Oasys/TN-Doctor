package com.hmis_tn.doctor.ui.institute.model.Phermisiun

data class StoreListResponseModel(
    var responseContents: List<Store> = listOf(),
    var message: String = "",
    var status: Int = 0,
    var statusCode: Int = 0,
    var totalRecords: Int = 0
)