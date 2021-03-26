package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model

import com.google.gson.annotations.SerializedName

data class ResDiagnosisData(
    @SerializedName("diagnosis_headers")
    var diagnosis_headers: DiagnosisHeaders? = null,
    @SerializedName("diagnosis_details")
    var diagnosis_details: List<DiagnosisDetails>? = null
)

data class DiagnosisHeaders(
    var context_uuid: Int? = 0,
    var context_activity_map_uuid: Int? = 0,
    var activity_uuid: Int? = 0,
    var display_order: Int? = 0
)


data class DiagnosisDetails(
    var patient_diagnosis_uuid: Int? = 0,
    var facility_uuid: Int? = 0,
    var department_uuid: Int? = 0,
    var patient_uuid: Int? = 0,
    var encounter_uuid: Int? = 0,
    var encounter_type_uuid: Int? = 0,
    var encounter_type_code: String? = "",
    var encounter_type_name: String? = "",
    var diagnosis_uuid: Int? = 0,
    var performed_by: Int? = 0,
    var performed_date: String? = "",
    var diagnosis_type: String? = "",
    var diagnosis_code: String? = "",
    var diagnosis_name: String? = "",
    var diagnosis_desc: String? = ""
)