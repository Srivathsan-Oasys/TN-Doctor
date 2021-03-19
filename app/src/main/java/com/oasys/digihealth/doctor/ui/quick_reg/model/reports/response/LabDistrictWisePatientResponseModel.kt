package com.oasys.digihealth.doctor.ui.quick_reg.model.reports.response

class LabDistrictWisePatientResponseModel (
    val responseContents: List<LabDistrictWisePatientResponseContent?>? = listOf(),
    val message: String? = "",
    val statusCode: Int? = 0,
    val totalRecords: Int? = 0
)