package com.hmis_tn.doctor.ui.emr_workflow.diet.model.request

data class GetPreviousDietOrderReq(
    var no_of_records: Int?,
    var patient_uuid: String?
)