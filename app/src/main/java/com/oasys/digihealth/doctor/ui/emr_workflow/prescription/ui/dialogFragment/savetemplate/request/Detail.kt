package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.ui.dialogFragment.savetemplate.request

data class Detail(
    var chief_complaint_uuid: Int = 0,
    var drug_frequency_uuid: Int = 0,
    var drug_instruction_uuid: Int = 0,
    var drug_route_uuid: Int = 0,
    var duration: Any = Any(),
    var duration_period_uuid: Int = 0,
    var is_active: Boolean = true,
    var is_public: Boolean = true,
    var item_master_uuid: Int = 0,
    var quantity: Int = 0,
    var revision: Boolean = true,
    var test_master_uuid: Int = 0,
    var vital_master_uuid: Int = 0
)
