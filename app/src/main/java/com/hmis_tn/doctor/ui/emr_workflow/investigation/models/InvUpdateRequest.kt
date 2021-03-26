package com.hmis_tn.doctor.ui.emr_workflow.investigation.models

data class InvUpdateRequest(
    var existing_details: ArrayList<InvExistingDetail>? = null,
    var new_details: ArrayList<InvNewDetail>? = null,
    var removed_details: ArrayList<InvRemovedDetail>? = null
)