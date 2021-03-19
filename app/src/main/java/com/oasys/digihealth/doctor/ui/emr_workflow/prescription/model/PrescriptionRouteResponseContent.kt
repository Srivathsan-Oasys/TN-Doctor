package com.oasys.digihealth.doctor.ui.emr_workflow.prescription.model

data class PrescriptionRouteResponseContent(
    val Is_default: Boolean = false,
    val color: Any = Any(),
    val created_by: Int = 0,
    val created_date: String = "",
    val display_order: Int = 0,
    val is_active: Boolean = false,
    val language: Int = 0,
    val modified_by: Int = 0,
    val modified_date: String = "",
    val name: String = "Select Route",
    val revision: Int = 0,
    val status: Boolean = false,
    val uuid: Int = 0
)