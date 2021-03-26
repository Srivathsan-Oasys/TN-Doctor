package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model.nurse_desk.save_model

data class Detail(
    var activity_uuid: Int = 0,
    var context_activity_map_uuid: Int = 0,
    var context_uuid: Int = 0,
    var display_order: Int = 0,
    var transaction_uuid: Int = 0
)