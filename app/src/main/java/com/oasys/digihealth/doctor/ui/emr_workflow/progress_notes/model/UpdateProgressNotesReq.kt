package com.oasys.digihealth.doctor.ui.emr_workflow.progress_notes.model


import com.google.gson.annotations.SerializedName

data class UpdateProgressNotesReq(
    @SerializedName("captured_by")
    var capturedBy: String?,
    @SerializedName("captured_on")
    var capturedOn: String?,
    @SerializedName("daily_note")
    var dailyNote: String?,
    @SerializedName("department_uuid")
    var departmentUuid: String?,
    @SerializedName("encounter_type_uuid")
    var encounterTypeUuid: Int?,
    @SerializedName("encounter_uuid")
    var encounterUuid: Int?,
    @SerializedName("facility_uuid")
    var facilityUuid: String?,
    @SerializedName("is_phr")
    var isPhr: Boolean?,
    @SerializedName("note_status_uuid")
    var noteStatusUuid: Int?,
    @SerializedName("patient_uuid")
    var patientUuid: String?,
    @SerializedName("progressnote_type_uuid")
    var progressnoteTypeUuid: Int?,
    @SerializedName("special_note")
    var specialNote: String?
)