package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model.save_model

data class Detail(
    var activity_uuid: Int = 0,
    var context_activity_map_uuid: Int = 0,
    var context_uuid: Int = 0,
    var display_order: Int = 0,
    var transaction_uuid: Int = 0
)