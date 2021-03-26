package com.hmis_tn.doctor.ui.emr_workflow.lmis_neworder.model

data class DoctorNameResponseModel(
    val responseContents: ArrayList<DoctorNameResponseContent?> = ArrayList(),
    val statusCode: Int = 0,
    val totalRecords: Int = 0
)