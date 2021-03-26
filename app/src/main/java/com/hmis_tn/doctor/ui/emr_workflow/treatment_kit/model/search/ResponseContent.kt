package com.hmis_tn.doctor.ui.emr_workflow.treatment_kit.model.search

data class ResponseContent(
    val treatment_code: String?,
    val treatment_kit_id: Int?,
    val treatment_name: String?,
    val treatment_type_id: Int?
) {
    override fun toString(): String {
        return treatment_name ?: ""
    }
}