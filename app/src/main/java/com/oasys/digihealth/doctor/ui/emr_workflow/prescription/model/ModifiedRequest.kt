package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model

data class ModifiedRequest(
    var details: List<Updatedetail>? = null,
    var header: Updateheader? = null
)