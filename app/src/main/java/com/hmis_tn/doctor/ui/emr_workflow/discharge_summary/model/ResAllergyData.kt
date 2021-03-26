package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model

import com.google.gson.annotations.SerializedName

class ResAllergyData(
    @SerializedName("allergy_headers")
    var allergy_headers: AllergyHeaders? = null,
    @SerializedName("allergy_details")
    var allergy_details: List<AllergyDetailsList> = listOf()
)

data class AllergyHeaders(
    var context_uuid: Int? = -1,
    var context_activity_map_uuid: Int? = -1,
    var activity_uuid: Int? = -1,
    var display_order: Int? = -1
)

data class AllergyDetailsList(
    var uuid: Int? = 0,
    var patient_uuid: Int? = 0,
    var symptom: Int? = 0,
    var date: String? = "0",
    var encounter_type_uuid: Int? = 0,
    var encounter_type_name: String? = "",
    var encounter_type_code: String? = "",
    var allergy_uuid: Int? = 0,
    var allergy_name: String? = "",
    var allergy_severity_uuid: Int? = 0,
    var allergy_severity_name: String? = "",
    var allergy_severity_code: String? = "",
    var allergy_source_uuid: Int? = 0,
    var allergy_source_code: String? = "",
    var allergy_source_name: String? = "",
    var allergy_type_uuid: Int? = 0,
    var allergy_type_code: String? = "",
    var allergy_type_name: String? = "",
    var allergy_duration: Int? = 0,
    var periods_code: String? = "",
    var periods_name: String? = ""
)