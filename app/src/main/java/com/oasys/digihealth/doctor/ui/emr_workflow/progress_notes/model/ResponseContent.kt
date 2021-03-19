package com.oasys.digihealth.doctor.ui.emr_workflow.progress_notes.model


import com.google.gson.annotations.SerializedName

data class ResponseContent(
    @SerializedName("d_is_active")
    var dIsActive: Boolean?,
    @SerializedName("d_name")
    var dName: String?,
    @SerializedName("d_status")
    var dStatus: Boolean?,
    @SerializedName("d_uuid")
    var dUuid: Int?,
    @SerializedName("f_is_active")
    var fIsActive: Boolean?,
    @SerializedName("f_name")
    var fName: String?,
    @SerializedName("f_status")
    var fStatus: Boolean?,
    @SerializedName("f_uuid")
    var fUuid: Int?,
    @SerializedName("p_captured_by")
    var pCapturedBy: Int?,
    @SerializedName("p_captured_on")
    var pCapturedOn: String?,
    @SerializedName("p_created_by")
    var pCreatedBy: Int?,
    @SerializedName("p_created_date")
    var pCreatedDate: String?,
    @SerializedName("p_daily_note")
    var pDailyNote: String?,
    @SerializedName("p_department_uuid")
    var pDepartmentUuid: Int?,
    @SerializedName("p_encounter_type_uuid")
    var pEncounterTypeUuid: Int?,
    @SerializedName("p_encounter_uuid")
    var pEncounterUuid: Int?,
    @SerializedName("p_facility_uuid")
    var pFacilityUuid: Int?,
    @SerializedName("p_is_active")
    var pIsActive: Boolean?,
    @SerializedName("p_is_phr")
    var pIsPhr: Boolean?,
    @SerializedName("p_modified_by")
    var pModifiedBy: Int?,
    @SerializedName("p_modified_date")
    var pModifiedDate: String?,
    @SerializedName("p_note_status_uuid")
    var pNoteStatusUuid: Int?,
    @SerializedName("p_patient_uuid")
    var pPatientUuid: Int?,
    @SerializedName("p_progressnote_type_uuid")
    var pProgressnoteTypeUuid: Int?,
    @SerializedName("p_revision")
    var pRevision: Int?,
    @SerializedName("p_special_note")
    var pSpecialNote: String?,
    @SerializedName("p_status")
    var pStatus: Boolean?,
    @SerializedName("p_uuid")
    var pUuid: Int?,
    @SerializedName("u_first_name")
    var uFirstName: String?,
    @SerializedName("u_image_url")
    var uImageUrl: String?,
    @SerializedName("u_is_active")
    var uIsActive: Boolean?,
    @SerializedName("u_last_name")
    var uLastName: Any?,
    @SerializedName("u_middle_name")
    var uMiddleName: Any?,
    @SerializedName("u_status")
    var uStatus: Boolean?,
    @SerializedName("u_user_img_url")
    var uUserImgUrl: String?,
    @SerializedName("u_uuid")
    var uUuid: Int?
)