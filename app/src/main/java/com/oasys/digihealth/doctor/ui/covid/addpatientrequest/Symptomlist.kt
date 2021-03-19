package com.oasys.digihealth.doctor.ui.covid.addpatientrequest

data class Symptomlist(
    var created_by: Int = 0,
    var created_date: String = "",
    var duration: String ="",
    var is_active: Boolean = false,
    var modified_by: Int = 0,
    var modified_date: String = "",
    var patient_uuid: Int = 0,
    var status: Boolean = false,
    var symptom_uuid: Int = 0,
    var uuid: Int = 0
)