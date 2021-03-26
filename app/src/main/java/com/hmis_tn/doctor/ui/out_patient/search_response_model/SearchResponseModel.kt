package com.hmis_tn.doctor.ui.out_patient.search_response_model

data class SearchResponseModel(
    var message: String? = null,
    var responseContents: List<ResponseContent?>? = null,
    var statusCode: Int? = null,
    var totalRecords: Int? = null
)