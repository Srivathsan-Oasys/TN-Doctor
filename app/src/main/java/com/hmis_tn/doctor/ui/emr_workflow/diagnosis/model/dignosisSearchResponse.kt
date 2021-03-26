package com.hmis_tn.doctor.ui.emr_workflow.diagnosis.model

data class dignosisSearchResponse(
    val code: Int = 0,
    val responseContents: ArrayList<DignosisSearch> = ArrayList(),
    val message: String = ""
)