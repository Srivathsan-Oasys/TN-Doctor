package com.oasys.digihealth.doctor.ui.emr_workflow.lmis_neworder.model

data class DoctorNameResponseModel(
    val responseContents: ArrayList<DoctorNameResponseContent?> = ArrayList(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)