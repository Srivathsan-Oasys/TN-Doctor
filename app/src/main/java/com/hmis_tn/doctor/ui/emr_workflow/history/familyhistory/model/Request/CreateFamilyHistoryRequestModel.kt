package com.hmis_tn.doctor.ui.emr_workflow.history.familyhistory.model.Request

data class CreateFamilyHistoryRequestModel(
    var consultation_uuid: Int? = 0,
    var disease_name: String? = "",
    var duration: String? = "",
    var encounter_uuid: Int? = 0,
    var identified_date: String? = "",
    var patient_uuid: String? = "",
    var period_uuid: String? = "",
    var relation_type_uuid: String? = ""
)