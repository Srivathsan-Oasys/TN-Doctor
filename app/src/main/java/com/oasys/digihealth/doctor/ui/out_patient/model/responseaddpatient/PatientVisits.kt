package com.oasys.digihealth.doctor.ui.out_patient.model.responseaddpatient

data class PatientVisits(
    var created_by: String? = "",
    var created_date: String? = "",
    var department_uuid: String? = "",
    var facility_uuid: String? = "",
    var is_mlc: Int? = 0,
    var modified_by: String? = "",
    var modified_date: String? = "",
    var patient_type_uuid: Int? = 0,
    var registered_date: String? = "",
    var seqNum: String? = "",
    var session_uuid: Int? = 0,
    var speciality_department_uuid: Int? = 0,
    var unit_uuid: Int? = 0
)