package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.previous_model

data class Ds(
    val ds_details: DsDetails = DsDetails(),
    val dsd_details: List<DsdDetail> = listOf()
)