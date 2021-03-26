package com.hmis_tn.doctor.ui.emr_workflow.radiology.model

data class RadiologyRequestModel(
    var existing_details: List<ExistingDetail> = listOf(),
    var new_details: List<NewDetail> = listOf(),
    var removed_details: List<RemovedDetail> = listOf()
)