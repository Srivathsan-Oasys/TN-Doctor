package com.hmis_tn.doctor.ui.emr_workflow.admission_referal.model.response

data class AdmissionDischargeTypeResponseContent(
    val code: String = "",
    val created_by: Int = 0,
    val created_date: Any = Any(),
    val display_order: Int = 0,
    val is_active: Boolean = false,
    val modified_by: Int = 0,
    val modified_date: Any = Any(),
    val name: String = "",
    val revision: Boolean = false,
    val status: Boolean = false,
    val uuid: Int = 0
)