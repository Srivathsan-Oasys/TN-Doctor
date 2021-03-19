package com.oasys.digihealth.doctor.ui.emr_workflow.discharge_summary.model

import com.google.gson.annotations.SerializedName

data class ResChiefComplaintsData(
    @SerializedName("cheif_complaints_headers")
    var cheif_complaints_headers: ChiefComplaintsHeaders? = null,
    @SerializedName("cheif_complaints_details")
    var cheif_complaints_details: List<ChiefComplaintsDetails> = listOf()
)

data class ChiefComplaintsHeaders(
    var context_uuid: Int? = 0,
    var context_activity_map_uuid: Int? = 0,
    var activity_uuid: Int? = 0,
    var display_order: Int? = 0
)

data class ChiefComplaintsDetails(
    var patient_cheif_complaint_uuid: Int? = 0,
    var created_date: String? = "",
    var patient_uuid: Int? = 0,
    var institution_uuid: Int? = 0,
    var institution: String? = "",
    var department_uuid: Int? = 0,
    var department: String? = "",
    var encounter_uuid: Int? = 0,
    var encounter_type_uuid: Int? = 0,
    var encounter_type_code: String? = "",
    var encounter_type_name: String? = "",
    var consultation_uuid: Int? = 0,
    var performed_by_title: String? = "",
    var performed_by_first_name: String? = "",
    var performed_by_middle_name: String? = "",
    var performed_by_last_name: String? = "",
    @SerializedName("chief_complaint_details")
    var chief_complaint_details: List<ChiefComplaintDetails>
)

data class ChiefComplaintDetails(
    var cheif_complaint_uuid: Int? = 0,
    var patient_cheif_complaint_uuid: Int? = 0,
    var cheif_complaint_code: String? = "",
    var cheif_complaint_name: String? = "",
    var cheif_complaint_desc: String? = "",
    var cheif_complaint_performed_date: String? = "",
    var chief_complaint_duration: Int? = 0,
    var chief_complaint_duration_period_uuid: Int? = 0,
    var chief_complaint_duration_period_code: String? = "",
    var chief_complaint_duration_period_name: String? = ""
)