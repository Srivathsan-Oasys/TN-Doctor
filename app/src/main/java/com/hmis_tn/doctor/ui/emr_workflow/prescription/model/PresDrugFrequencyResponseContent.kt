package com.hmis_tn.doctor.ui.emr_workflow.prescription.model

data class PresDrugFrequencyResponseContent(
    val code: String = "",
    val comments: Any = Any(),
    val created_by: Int = 0,
    val created_date: String = "",
    val description: Any = Any(),
    val display: String = "",
    val is_active: Boolean = false,
    val modified_by: Int = 0,
    val modified_date: String = "",
    val name: String = "Select Frequency",
    val nooftimes: Int = 0,
    val perdayquantity: String = "",
    val product_type_uuid: Int = 0,
    val revision: Int = 0,
    val status: Boolean = false,
    val uuid: Int = 0
)