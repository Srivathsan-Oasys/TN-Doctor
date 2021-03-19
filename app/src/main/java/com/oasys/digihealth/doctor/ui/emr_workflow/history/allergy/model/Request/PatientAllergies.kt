package com.oasys.digihealth.doctor.ui.emr_workflow.history.allergy.model.Request

data class PatientAllergies(
    var allergy_master_uuid: String? = "",
    var allergy_severity_uuid: String? = "",
    var allergy_source_uuid: String? = "",
    var allergy_type_uuid: String? = "",
    var consultation_uuid: Int? = 0,
    var duration: String? = "",
    var encounter_uuid: Int? = 0,
    var patient_allergy_status_uuid: Int? = 0,
    var patient_uuid: String? = "",
    var performed_date: String? = "",
    var encounter_type_uuid: String? = "1",
    var ward_master_uuid: Int? = 0,
    var period_uuid: String? = ""

)