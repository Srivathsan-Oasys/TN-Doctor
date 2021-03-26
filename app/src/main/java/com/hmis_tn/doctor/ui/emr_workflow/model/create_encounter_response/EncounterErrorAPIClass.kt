package com.hmis_tn.doctor.ui.emr_workflow.model.create_encounter_response


data class EncounterErrorAPIClass(
    var code: Int? = 0,
    var message: String? = "",
    val existingDetails: ExisitingData = ExisitingData()
)