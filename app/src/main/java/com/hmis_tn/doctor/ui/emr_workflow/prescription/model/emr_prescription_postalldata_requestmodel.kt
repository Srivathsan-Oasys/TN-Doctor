package com.hmis_tn.doctor.ui.emr_workflow.prescription.model

data class emr_prescription_postalldata_requestmodel(
    var details: List<Detail?>? = listOf(),
    var header: Header? = Header()
)