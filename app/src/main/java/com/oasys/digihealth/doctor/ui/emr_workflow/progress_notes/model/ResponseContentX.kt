package com.oasys.digihealth.doctor.ui.emr_workflow.progress_notes.model


import com.google.gson.annotations.SerializedName

data class ResponseContentX(
    @SerializedName("admission_request_uuid")
    var admissionRequestUuid: Int?,
    @SerializedName("admission_uuid")
    var admissionUuid: Int?,
    @SerializedName("appointment_uuid")
    var appointmentUuid: Int?,
    @SerializedName("created_by")
    var createdBy: Int?,
    @SerializedName("created_date")
    var createdDate: String?,
    @SerializedName("department_uuid")
    var departmentUuid: Int?,
    @SerializedName("discharge_date")
    var dischargeDate: Any?,
    @SerializedName("discharge_type_uuid")
    var dischargeTypeUuid: Int?,
    @SerializedName("encounter_date")
    var encounterDate: String?,
    @SerializedName("encounter_doctors")
    var encounterDoctors: List<EncounterDoctor>?,
    @SerializedName("encounter_identifier")
    var encounterIdentifier: Int?,
    @SerializedName("encounter_priority_uuid")
    var encounterPriorityUuid: Int?,
    @SerializedName("encounter_status_uuid")
    var encounterStatusUuid: Int?,
    @SerializedName("encounter_type_uuid")
    var encounterTypeUuid: Int?,
    @SerializedName("facility_uuid")
    var facilityUuid: Int?,
    @SerializedName("is_active")
    var isActive: Boolean?,
    @SerializedName("is_active_encounter")
    var isActiveEncounter: Boolean?,
    @SerializedName("is_followup")
    var isFollowup: Boolean?,
    @SerializedName("is_group_casuality")
    var isGroupCasuality: Boolean?,
    @SerializedName("is_mrd_request")
    var isMrdRequest: Boolean?,
    @SerializedName("modified_by")
    var modifiedBy: Int?,
    @SerializedName("modified_date")
    var modifiedDate: String?,
    @SerializedName("patient_uuid")
    var patientUuid: Int?,
    @SerializedName("revision")
    var revision: Boolean?,
    @SerializedName("status")
    var status: Boolean?,
    @SerializedName("uuid")
    var uuid: Int?
)