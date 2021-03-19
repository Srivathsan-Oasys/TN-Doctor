package com.oasys.digihealth.doctor.ui.emr_workflow.blood_request.model


import com.google.gson.annotations.SerializedName

data class ResponseContentXX(
    @SerializedName("blood_group")
    val bloodGroup: BloodGroup?,
    @SerializedName("blood_group_uuid")
    val bloodGroupUuid: Int?,
    @SerializedName("blood_hb")
    val bloodHb: String?,
    @SerializedName("blood_request_details")
    val bloodRequestDetails: List<BloodRequestDetail>?,
    @SerializedName("blood_request_purpose")
    val bloodRequestPurpose: BloodRequestPurpose?,
    @SerializedName("blood_request_purpose_uuid")
    val bloodRequestPurposeUuid: Int?,
    @SerializedName("blood_request_status")
    val bloodRequestStatus: BloodRequestStatus?,
    @SerializedName("blood_request_status_uuid")
    val bloodRequestStatusUuid: Int?,
    @SerializedName("blood_requested_by")
    val bloodRequestedBy: Int?,
    @SerializedName("blood_requested_by_name")
    val bloodRequestedByName: String?,
    @SerializedName("created_by")
    val createdBy: Int?,
    @SerializedName("created_date")
    val createdDate: String?,
    @SerializedName("department_uuid")
    val departmentUuid: Int?,
    @SerializedName("is_pregnant")
    val isPregnant: Boolean?,
    @SerializedName("is_previous_transfusion")
    val isPreviousTransfusion: Boolean?,
    @SerializedName("modified_by")
    val modifiedBy: Int?,
    @SerializedName("modified_date")
    val modifiedDate: String?,
    @SerializedName("patient_uuid")
    val patientUuid: Int?,
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("transfusion_reaction")
    val transfusionReaction: Boolean?,
    @SerializedName("uuid")
    val uuid: Int?
) {
    var isExpanded: Boolean = false
}