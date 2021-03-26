package com.hmis_tn.doctor.ui.emr_workflow.prescription.model

data class SaleUom(
    var code: String? = "",
    var created_by: Int? = 0,
    var created_date: String? = "",
    var description: Any? = Any(),
    var is_active: Boolean? = false,
    var modified_by: Int? = 0,
    var modified_date: String? = "",
    var name: String? = "",
    var revision: Int? = 0,
    var status: Boolean? = false,
    var uom_type_uuid: Int? = 0,
    var uuid: Int? = 0
)