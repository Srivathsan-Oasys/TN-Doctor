package com.oasys.digihealth.doctor.ui.emr_workflow.model.fetch_encounters_response

data class FectchEncounterResponseModel(
    val code: Int? = null,
    val message: String? = null,
    val responseContents: List<FetchEncounterResponseContent?>? = null
)