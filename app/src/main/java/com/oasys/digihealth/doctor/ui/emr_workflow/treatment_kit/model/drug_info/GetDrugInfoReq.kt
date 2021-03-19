package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.drug_info

data class GetDrugInfoReq(
    var is_expiry_batch: Boolean = false,
    var item_master_uuid: Int = 0,
    var store_master_uuid: Int = 0,
    var store_type_uuid: Int = 0
)