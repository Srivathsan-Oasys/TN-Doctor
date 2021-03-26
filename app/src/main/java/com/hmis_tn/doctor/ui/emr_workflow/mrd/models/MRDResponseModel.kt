package com.hmis_tn.doctor.ui.emr_workflow.mrd.models

data class MRDResponseModel(
    val discharge_result: DischargeResult = DischargeResult(),
    val message: String = "",
    val statusCode: Int = 0
)