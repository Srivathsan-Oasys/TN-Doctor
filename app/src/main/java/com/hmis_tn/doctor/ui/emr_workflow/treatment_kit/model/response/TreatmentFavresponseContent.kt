package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.response

data class TreatmentFavresponseContent(
    val favourite_active: Boolean? = false,
    val favourite_code: String? = "",
    val favourite_display_order: Int? = 0,
    val favourite_id: Int? = 0,
    val favourite_name: String? = "",
    val favourite_type_id: Int? = 0,
    val treatment_kit_id: Int? = 0,
    val treatment_kit_type_id: Int? = 0,
    var position: Int? = 0,
    var isSelected: Boolean = false
)