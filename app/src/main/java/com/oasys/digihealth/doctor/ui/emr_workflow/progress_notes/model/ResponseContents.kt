package com.oasys.digihealth.doctor.ui.emr_workflow.progress_notes.model


import com.google.gson.annotations.SerializedName

data class ResponseContents(
    @SerializedName("captured_by")
    var capturedBy: String?,
    @SerializedName("captured_on")
    var capturedOn: String?,
    @SerializedName("created_by")
    var createdBy: String?,
    @SerializedName("created_date")
    var createdDate: String?,
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
    @SerializedName("is_active")
    var isActive: Boolean?,
    @SerializedName("is_phr")
    var isPhr: Boolean?,
    @SerializedName("modified_by")
    var modifiedBy: String?,
    @SerializedName("modified_date")
    var modifiedDate: String?,
    @SerializedName("note_status_uuid")
    var noteStatusUuid: Int?,
    @SerializedName("patient_uuid")
    var patientUuid: String?,
    @SerializedName("progressnote_type_uuid")
    var progressnoteTypeUuid: Int?,
    @SerializedName("revision")
    var revision: Int?,
    @SerializedName("special_note")
    var specialNote: String?,
    @SerializedName("status")
    var status: Boolean?,
    @SerializedName("uuid")
    var uuid: Int?
)