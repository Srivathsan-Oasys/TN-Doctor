package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.response

data class TreatmentNameresponseContent(
    val treatment_code: String? = "",
    val treatment_kit_id: Int? = 0,
    val treatment_name: String? = "",
    val treatment_type_id: Int? = 0
)