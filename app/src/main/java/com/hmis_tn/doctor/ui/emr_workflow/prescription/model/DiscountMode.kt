package com.hmis_tn.doctor.ui.emr_workflow.prescription.model

data class DiscountMode(
    var Is_default: Boolean? = false,
    var code: String? = "",
    var color: Any? = Any(),
    var created_by: Int? = 0,
    var created_date: String? = "",
    var display_order: Int? = 0,
    var is_active: Boolean? = false,
    var language: Int? = 0,
    var modified_by: Int? = 0,
    var modified_date: String? = "",
    var name: String? = "",
    var revision: Int? = 0,
    var status: Boolean? = false,
    var uuid: Int? = 0
)