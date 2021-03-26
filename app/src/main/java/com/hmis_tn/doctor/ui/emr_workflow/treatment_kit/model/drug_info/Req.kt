package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.drug_info

data class Req(
    var is_expiry_batch: Boolean? = false,
    var item_master_uuid: Int? = 0,
    var store_master_uuid: Int? = 0,
    var store_type_uuid: Int? = 0
)