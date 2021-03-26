package com.hmis_tn.doctor.ui.emr_workflow.prescription.model

data class PrescriptionUpdateRequest(
    var header: PresUpdateheader? = null,
    var details: List<PresUpdatedetail>? = null
)