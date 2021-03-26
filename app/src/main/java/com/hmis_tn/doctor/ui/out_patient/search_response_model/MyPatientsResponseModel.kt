package com.hmis_tn.doctor.ui.out_patient.search_response_model

data class MyPatientsResponseModel(
    val code: Int = 0,
    val message: String = "",
    val responseContents: List<MypatientResponseContent> = listOf(),
    val totalRecords: Int = 0
)