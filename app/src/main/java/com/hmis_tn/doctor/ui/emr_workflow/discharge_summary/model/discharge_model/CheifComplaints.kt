package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.discharge_model

data class CheifComplaints(
    val cheif_complaints_details: List<CheifComplaintsDetail> = listOf(),
    val cheif_complaints_headers: CheifComplaintsHeaders = CheifComplaintsHeaders()
)