package com.hmis_tn.doctor.ui.emr_workflow.history.allergy.model.Request

data class EditPatientAllergies(

    var allergy_master_uuid: String? = "",
    var allergy_severity_uuid: String? = "",
    var allergy_source_uuid: String? = "",
    var allergy_type_uuid: String? = "",
    var consultation_uuid: Int? = 0,
    var duration: String? = "",
    var encounter_uuid: Int? = 0,
    var patient_uuid: String? = "",
    var period_uuid: String? = ""

)