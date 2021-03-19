package com.oasys.digihealth.doctor.ui.emr_workflow.blood_request.model


import com.google.gson.annotations.SerializedName

data class ResponseContents(
    @SerializedName("blood_group_uuid")
    var bloodGroupUuid: String?,
    @SerializedName("blood_hb")
    var bloodHb: String?,
    @SerializedName("blood_request_date")
    var bloodRequestDate: String?,
    @SerializedName("blood_request_no")
    var bloodRequestNo: Int?,
    @SerializedName("blood_request_priority_uuid")
    var bloodRequestPriorityUuid: Int?,
    @SerializedName("blood_request_purpose_uuid")
    var bloodRequestPurposeUuid: String?,
    @SerializedName("blood_request_status_uuid")
    var bloodRequestStatusUuid: Int?,
    @SerializedName("blood_request_type_uuid")
    var bloodRequestTypeUuid: Int?,
    @SerializedName("blood_requested_by")
    var bloodRequestedBy: Int?,
    @SerializedName("created_by")
    var createdBy: String?,
    @SerializedName("created_date")
    var createdDate: String?,
    @SerializedName("department_uuid")
    var departmentUuid: Int?,
    @SerializedName("doctor_uuid")
    var doctorUuid: String?,
    @SerializedName("encounter_type_uuid")
    var encounterTypeUuid: Int?,
    @SerializedName("encounter_uuid")
    var encounterUuid: Int?,
    @SerializedName("facility_uuid")
    var facilityUuid: String?,
    @SerializedName("is_pregnant")
    var isPregnant: Boolean?,
    @SerializedName("is_previous_transfusion")
    var isPreviousTransfusion: Boolean?,
    @SerializedName("medical_officer_uuid")
    var medicalOfficerUuid: Int?,
    @SerializedName("modified_by")
    var modifiedBy: Int?,
    @SerializedName("modified_date")
    var modifiedDate: String?,
    @SerializedName("no_of_units")
    var noOfUnits: Int?,
    @SerializedName("patient_uuid")
    var patientUuid: String?,
    @SerializedName("status")
    var status: Boolean?,
    @SerializedName("transfusion_date")
    var transfusionDate: String?,
    @SerializedName("transfusion_reaction")
    var transfusionReaction: Boolean?,
    @SerializedName("uuid")
    var uuid: Int?,
    @SerializedName("ward_uuid")
    var wardUuid: Int?
)