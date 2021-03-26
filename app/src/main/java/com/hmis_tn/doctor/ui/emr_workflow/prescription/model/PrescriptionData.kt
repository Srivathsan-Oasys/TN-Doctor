package com.hmis_tn.doctor.ui.emr_workflow.prescription.model

data class PrescriptionData(
    var created_by: String? = "",
    var department_uuid: String? = "",
    var dispense_status_uuid: Int? = 0,
    var doctor_uuid: String? = "",
    var encounter_type_uuid: Int? = 0,
    var encounter_uuid: Int? = 0,
    var facility_uuid: String? = "",
    var modified_by: String? = "",
    var notes: String? = "",
    var patient_uuid: String? = "",
    var prescription_date: String? = "",
    var prescription_no: String? = "",
    var prescription_status_uuid: Int? = 0,
    var store_master_uuid: Int? = 0,
    var treatment_kit_uuid: Int? = 0
)