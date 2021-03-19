package com.oasys.digihealth.doctor.ui.out_patient.model.search_request_model

data class InpatientRequestModel(
    val admission_status_uuid: String = "",
    val pageNo: Int = 0,
    val paginationSize: Int = 0,
    val search: String = "",
    val sortField: String = "",
    val sortOrder: String = ""
)