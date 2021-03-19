package com.oasys.digihealth.doctor.ui.emr_workflow.radiology_result.model

data class RadilogyResultResponseContent(
    val created_date: String = "",
    val encounter_uuid: Int = 0,
    val image_url: List<Any> = listOf(),
    val patient_order: PatientOrder = PatientOrder(),
    val patient_order_test_detail_uuid: Int = 0,
    val patient_order_uuid: Int = 0,
    val patient_uuid: Int = 0,
    val patient_work_order_uuid: Int = 0,
    val result_value: Any = Any(),
    val test_master: TestMaster = TestMaster(),
    val test_master_uuid: Int = 0,
    val uuid: Int = 0,
    val work_order_attachment_uuid: String? = null,
    val work_order_status_uuid: Int = 0
)