package com.hmis_tn.doctor.ui.emr_workflow.radiology.model

data class RemovedDetail(
    var uuid: Int? = null,
    var patient_orders_uuid: Int? = null,
    var test_master_uuid: Int? = null
)