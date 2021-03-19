package com.oasys.digihealth.doctor.ui.emr_workflow.vitals.model.response

data class VitalResponse(
    var comments: String = "",
    var consultation_uuid: Int = 0,
    var created_by: String = "",
    var created_date: String = "",
    var department_uuid: String = "",
    var doctor_uuid: String = "",
    var encounter_type_uuid: Int = 0,
    var encounter_uuid: Int = 0,
    var facility_uuid: String = "",
    var is_active: Boolean = false,
    var loinc_code: String = "",
    var mnemonic_code: String = "",
    var modified_by: String = "",
    var modified_date: String = "",
    var patient_uuid: String = "",
    var patient_vital_status_uuid: Int = 0,
    var performed_date: String = "",
    var reference_range_from: String = "",
    var reference_range_to: String = "",
    var revision: Int = 0,
    var status: Boolean = false,
    var tat_end_time: String = "",
    var tat_start_time: String = "",
    var uuid: Int = 0,
    var vital_group_uuid: Int = 0,
    var vital_master_uuid: Int = 0,
    var vital_qualifier_uuid: Int = 0,
    var vital_type_uuid: Int = 0,
    var vital_uom_uuid: Int = 0,
    var vital_value: String = "",
    var vital_value_type_uuid: Int = 0
)