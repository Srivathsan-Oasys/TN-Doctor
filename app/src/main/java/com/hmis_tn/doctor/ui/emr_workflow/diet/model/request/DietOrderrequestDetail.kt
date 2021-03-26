package com.hmis_tn.doctor.ui.emr_workflow.diet.model.request

data class DietOrderrequestDetail(
    var diet_frequency_uuid: Int = 0,
    var diet_master_uuid: Int = 0,
    var diet_order_category_uuid: Int = 0,
    var quantity: Int = 0
)
