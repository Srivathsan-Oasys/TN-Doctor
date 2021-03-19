package com.oasys.digihealth.doctor.ui.emr_workflow.mrd.models

data class PatientOrderTestDetailXX(
    val is_nurse_collected: Boolean = false,
    val order_priority: OrderPriorityXX = OrderPriorityXX(),
    val order_priority_uuid: Int = 0,
    val order_status: OrderStatusXX = OrderStatusXX(),
    val order_status_uuid: Int = 0,
    val patient_order_uuid: Int = 0,
    val patient_uuid: Int = 0,
    val sample_collection_date: Any = Any(),
    val test_master: TestMasterXX = TestMasterXX(),
    val test_master_uuid: Int = 0,
    val uuid: Int = 0
)