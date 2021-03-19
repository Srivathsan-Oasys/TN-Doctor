package com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.model.response

data class Encounter(
    val encounter_type: EncounterType? = EncounterType(),
    val encounter_type_uuid: Int? = 0,
    val uuid: Int? = 0
)