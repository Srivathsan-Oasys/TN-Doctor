package com.oasys.digihealth.doctor.ui.emr_workflow.history.familyhistory.model

data class FamilyHistoryResponseContent(
    val consultation_uuid: Int = 0,
    val created_by: Int = 0,
    val created_date: String = "",
    val disease_description: Any = Any(),
    val disease_name: String = "",
    val duration: Int = 0,
    val encounter_uuid: Int = 0,
    val family_relation_type: FamilyRelationType = FamilyRelationType(),
    val identified_date: String = "",
    val is_active: Boolean = false,
    val modified_by: Int = 0,
    val modified_date: String = "",
    val patient_uuid: Int = 0,
    val period_uuid: Int = 0,
    val periods: Periods = Periods(),
    val relation_type_uuid: Int = 0,
    val revision: Int = 0,
    val status: Boolean = false,
    val uuid: Int = 0
)