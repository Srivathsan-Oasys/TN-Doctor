package com.oasys.digihealth.doctor.ui.emr_workflow.treatment_kit.model.response

data class TreatmentPrescRouteSpinnerresponseContent(
    val Is_default: Boolean? = false,
    val color: String? = "",
    val created_by: Int? = 0,
    val created_date: String? = "",
    val display_order: Int? = 0,
    val is_active: Boolean? = false,
    val language: Int? = 0,
    val modified_by: Int? = 0,
    val modified_date: String? = "",
    val name: String = "",
    val revision: Int? = 0,
    val status: Boolean? = false,
    val uuid: Int? = 0
)