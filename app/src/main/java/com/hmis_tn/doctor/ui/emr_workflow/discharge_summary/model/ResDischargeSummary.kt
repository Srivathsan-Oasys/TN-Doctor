package com.hmis_tn.doctor.ui.emr_workflow.discharge_summary.model

import com.google.gson.annotations.SerializedName

data class ResDischargeSummary(
    val statusCode: Int? = 0,
    val status: String? = "",
    val message: String? = "",
    @SerializedName("discharge_result")
    val dischargeResult: DischargeResult? = null
)

data class DischargeResult(
    var admission_uuid: Int? = 0,
    var admission_request_uuid: Int? = 0,
    var encounter_uuid: Int? = 0,
    var encounter_type_uuid: Int? = 0,
    var department_uuid: Int? = 0,
    var admission_status_uuid: Int? = 0,
    var doctor_uuid: Int? = 0,
    @SerializedName("patient_info")
    var PatientInfo: PatientInfo? = null,
    @SerializedName("allergy")
    var allergy: ResAllergyData? = null,
    @SerializedName("diagnosis")
    var diagnosis: ResDiagnosisData? = null,
    @SerializedName("prescription")
    var prescription: ResPrescriptionData? = null,
    @SerializedName("lab")
    var lab: ResLabData? = null,
    @SerializedName("radiology")
    var radiology: ResRadiologyData? = null,
    @SerializedName("investigation")
    var investigation: ResInvestigationData? = null,
    @SerializedName("vitals")
    var vitals: ResVitalsData? = null,
    @SerializedName("cheif_complaints")
    var cheif_complaints: ResChiefComplaintsData? = null
)

data class PatientInfo(
    var uuid: Int? = 0,
    var uhid: Long? = 0,
    var title_name: String? = "",
    var first_name: String? = "",
    var middle_name: String? = "",
    var last_name: String? = "",
    var age: Int? = 0,
    var mobile: Long? = null,
    var gender: String? = ""
)
