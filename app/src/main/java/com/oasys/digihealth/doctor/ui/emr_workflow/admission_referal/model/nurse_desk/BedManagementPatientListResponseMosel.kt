package com.oasys.digihealth.doctor.ui.emr_workflow.admission_referal.model.nurse_desk

data class BedManagementPatientListResponseMosel(
    val message: String = "",
    val responseContents: List<BedManagementPatientListResponseContent> = listOf(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)